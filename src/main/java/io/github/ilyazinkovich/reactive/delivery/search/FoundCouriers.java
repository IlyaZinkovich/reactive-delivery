package io.github.ilyazinkovich.reactive.delivery.search;

import io.github.ilyazinkovich.reactive.delivery.core.Order;
import io.github.ilyazinkovich.reactive.delivery.core.Courier;
import java.util.Set;

public class FoundCouriers {

  public final Order booking;
  public final Set<Courier> couriers;

  FoundCouriers(final Order booking, final Set<Courier> couriers) {
    this.booking = booking;
    this.couriers = couriers;
  }
}
