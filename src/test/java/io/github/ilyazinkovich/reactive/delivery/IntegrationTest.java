package io.github.ilyazinkovich.reactive.delivery;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import io.github.ilyazinkovich.reactive.delivery.core.Courier;
import io.github.ilyazinkovich.reactive.delivery.core.CourierId;
import io.github.ilyazinkovich.reactive.delivery.core.Location;
import io.github.ilyazinkovich.reactive.delivery.core.Order;
import io.github.ilyazinkovich.reactive.delivery.core.OrderId;
import io.github.ilyazinkovich.reactive.delivery.core.ReDispatch;
import io.github.ilyazinkovich.reactive.delivery.filter.Filter;
import io.github.ilyazinkovich.reactive.delivery.filter.FilteredCouriers;
import io.github.ilyazinkovich.reactive.delivery.redispatch.ReDispatcher;
import io.github.ilyazinkovich.reactive.delivery.redispatch.RetriesExceeded;
import io.github.ilyazinkovich.reactive.delivery.search.FoundCouriers;
import io.github.ilyazinkovich.reactive.delivery.search.Search;
import io.github.ilyazinkovich.reactive.delivery.sort.Assignment;
import io.github.ilyazinkovich.reactive.delivery.sort.Sort;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class IntegrationTest {

  private static final Random random = new Random();
  private static final Supplier<Integer> AT_LEAST_ONE = () -> random.nextInt(8) + 1;
  private static final Predicate<Courier> NO_FILTER = courier -> true;
  private static final int TEST_BOOKINGS_COUNT = 10;
  private final PublishSubject<Order> ordersSubject = PublishSubject.create();
  private final PublishSubject<FoundCouriers> foundCouriersSubject = PublishSubject.create();
  private final PublishSubject<FilteredCouriers> filteredCouriersSubject = PublishSubject.create();
  private final PublishSubject<RetriesExceeded> retriesExceededSubject = PublishSubject.create();
  private final PublishSubject<ReDispatch> reDispatchSubject = PublishSubject.create();
  private final PublishSubject<Assignment> assignmentSubject = PublishSubject.create();

  @Test
  void testOptimisticFlow() {
    final List<Location> locations = generateTestLocations();
    final Search search = createSearch(locations, AT_LEAST_ONE);
    final Filter filter = createFilter();
    final Sort sort = createSort();
    final ReDispatcher reDispatcher = createReDispatcher();
    ordersSubject.subscribe(search::accept);
    foundCouriersSubject.subscribe(filter::accept);
    filteredCouriersSubject.subscribe(sort::accept);
    reDispatchSubject.subscribe(reDispatcher::accept);

    final TestSubscriber<Assignment> assignmentTestSubscriber = TestSubscriber.create();
    assignmentSubject.subscribe(assignmentTestSubscriber::onNext);
    final TestSubscriber<RetriesExceeded> retriesExceededTestSubscriber = TestSubscriber.create();
    retriesExceededSubject.subscribe(retriesExceededTestSubscriber::onNext);

    final List<Order> orders = generateTestOrders(locations);
    orders.forEach(ordersSubject::onNext);

    assignmentTestSubscriber.assertValueCount(TEST_BOOKINGS_COUNT);
    retriesExceededTestSubscriber.assertValueCount(0);
  }

  private List<Order> generateTestOrders(final List<Location> locations) {
    return locations.stream().map(location -> new Order(OrderId.next(), location))
        .collect(toList());
  }

  private List<Location> generateTestLocations() {
    return Stream.generate(this::generateLocation)
        .limit(TEST_BOOKINGS_COUNT).collect(toList());
  }

  private ReDispatcher createReDispatcher() {
    return new ReDispatcher(ordersSubject::onNext, new HashMap<>(), retriesExceededSubject::onNext);
  }

  private Sort createSort() {
    return new Sort(assignmentSubject::onNext);
  }

  private Filter createFilter() {
    return new Filter(filteredCouriersSubject::onNext, NO_FILTER, reDispatchSubject::onNext);
  }

  private Search createSearch(final List<Location> locations,
      final Supplier<Integer> couriersCountSupplier) {
    final Map<Location, Set<Courier>> couriersByLocation =
        locations.stream()
            .collect(toMap(
                location -> location,
                location -> generateTestCouriers(couriersCountSupplier)
            ));
    return new Search(foundCouriersSubject::onNext, couriersByLocation, reDispatchSubject::onNext);
  }

  private Set<Courier> generateTestCouriers(final Supplier<Integer> couriersCountSupplier) {
    return Stream.generate(() -> new Courier(CourierId.next()))
        .limit(couriersCountSupplier.get())
        .collect(toSet());
  }

  private Location generateLocation() {
    final double lat = random.nextDouble() * 180 - 90;
    final double lng = random.nextDouble() * 180 - 90;
    return new Location(lat, lng);
  }
}
