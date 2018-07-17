package io.github.ilyazinkovich.reactive.delivery.sort;

import io.github.ilyazinkovich.reactive.delivery.core.Order;
import io.github.ilyazinkovich.reactive.delivery.core.CourierId;

public class Assignment {

  public final Order booking;
  public final CourierId courierId;

  Assignment(final Order booking, final CourierId courierId) {
    this.booking = booking;
    this.courierId = courierId;
  }
}
