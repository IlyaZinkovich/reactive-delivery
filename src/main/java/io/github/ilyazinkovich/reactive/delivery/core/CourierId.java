package io.github.ilyazinkovich.reactive.delivery.core;

import java.util.Objects;
import java.util.UUID;

public class CourierId {

  final String uid;

  public CourierId(final String uid) {
    this.uid = uid;
  }

  public static CourierId next() {
    return new CourierId(UUID.randomUUID().toString());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final CourierId courierId = (CourierId) o;
    return Objects.equals(uid, courierId.uid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }
}
