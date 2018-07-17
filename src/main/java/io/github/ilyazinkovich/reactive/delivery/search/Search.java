package io.github.ilyazinkovich.reactive.delivery.search;

import static java.util.Collections.emptySet;

import io.github.ilyazinkovich.reactive.delivery.core.Order;
import io.github.ilyazinkovich.reactive.delivery.core.Courier;
import io.github.ilyazinkovich.reactive.delivery.core.Location;
import io.github.ilyazinkovich.reactive.delivery.core.ReDispatch;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Search {

  private final Consumer<FoundCouriers> foundCouriersConsumer;
  private final Map<Location, Set<Courier>> couriersByLocation;
  private final Consumer<ReDispatch> reDispatchConsumer;

  public Search(final Consumer<FoundCouriers> foundCouriersConsumer,
      final Map<Location, Set<Courier>> couriersByLocation,
      final Consumer<ReDispatch> reDispatchConsumer) {
    this.foundCouriersConsumer = foundCouriersConsumer;
    this.couriersByLocation = couriersByLocation;
    this.reDispatchConsumer = reDispatchConsumer;
  }

  public void accept(final Order booking) {
    final Set<Courier> couriers =
        couriersByLocation.getOrDefault(booking.pickupArea, emptySet());
    if (couriers.isEmpty()) {
      reDispatchConsumer.accept(new ReDispatch(booking));
    } else {
      foundCouriersConsumer.accept(new FoundCouriers(booking, couriers));
    }
  }
}
