package io.github.ilyazinkovich.reactive.delivery.filter;

import static java.util.stream.Collectors.toSet;

import io.github.ilyazinkovich.reactive.delivery.core.Courier;
import io.github.ilyazinkovich.reactive.delivery.core.ReDispatch;
import io.github.ilyazinkovich.reactive.delivery.search.FoundCouriers;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Filter {

  private final Consumer<FilteredCouriers> filteredCouriersConsumer;
  private final Predicate<Courier> courierFilter;
  private final Consumer<ReDispatch> reDispatchConsumer;

  public Filter(final Consumer<FilteredCouriers> filteredCouriersConsumer,
      final Predicate<Courier> courierFilter,
      final Consumer<ReDispatch> reDispatchConsumer) {
    this.filteredCouriersConsumer = filteredCouriersConsumer;
    this.courierFilter = courierFilter;
    this.reDispatchConsumer = reDispatchConsumer;
  }

  public void accept(final FoundCouriers foundCouriers) {
    final Set<Courier> couriers = foundCouriers.couriers.stream()
        .filter(courierFilter)
        .collect(toSet());
    if (couriers.isEmpty()) {
      reDispatchConsumer.accept(new ReDispatch(foundCouriers.booking));
    } else {
      final FilteredCouriers filteredCouriers =
          new FilteredCouriers(foundCouriers.booking, couriers);
      filteredCouriersConsumer.accept(filteredCouriers);
    }
  }
}
