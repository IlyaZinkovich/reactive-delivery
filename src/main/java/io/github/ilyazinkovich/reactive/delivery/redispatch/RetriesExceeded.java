package io.github.ilyazinkovich.reactive.delivery.redispatch;

import io.github.ilyazinkovich.reactive.delivery.core.Order;

public class RetriesExceeded {

  private final Order booking;

  public RetriesExceeded(final Order booking) {
    this.booking = booking;
  }
}
