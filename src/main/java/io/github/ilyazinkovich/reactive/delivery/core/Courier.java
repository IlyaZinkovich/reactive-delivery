package io.github.ilyazinkovich.reactive.delivery.core;

import java.util.Comparator;
import java.util.Random;

public class Courier {

  public final CourierId id;

  public Courier(final CourierId id) {
    this.id = id;
  }

  public static Comparator<Courier> etaComparator() {
    final Random random = new Random();
    return (left, right) -> random.nextInt(2) - 1;
  }
}
