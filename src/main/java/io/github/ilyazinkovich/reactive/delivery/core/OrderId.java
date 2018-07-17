package io.github.ilyazinkovich.reactive.delivery.core;

import java.util.Objects;
import java.util.UUID;

public class OrderId {

  public final String uid;

  public OrderId(final String uid) {
    this.uid = uid;
  }

  public static OrderId next() {
    return new OrderId(UUID.randomUUID().toString());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final OrderId bookingId = (OrderId) o;
    return Objects.equals(uid, bookingId.uid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }
}
