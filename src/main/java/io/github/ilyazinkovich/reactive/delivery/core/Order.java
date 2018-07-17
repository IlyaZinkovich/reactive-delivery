package io.github.ilyazinkovich.reactive.delivery.core;

public class Order {

  public final OrderId id;
  public final Location pickupArea;

  public Order(final OrderId id, final Location pickupArea) {
    this.id = id;
    this.pickupArea = pickupArea;
  }
}
