package io.github.ilyazinkovich.reactive.delivery.filter;

import io.github.ilyazinkovich.reactive.delivery.core.Order;
import io.github.ilyazinkovich.reactive.delivery.core.Courier;
import java.util.Set;

public class FilteredCouriers {

  public final Order order;
  public final Set<Courier> couriers;

  FilteredCouriers(final Order order, final Set<Courier> couriers) {
    this.order = order;
    this.couriers = couriers;
  }
}
