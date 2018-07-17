package io.github.ilyazinkovich.reactive.delivery.sort;

import io.github.ilyazinkovich.reactive.delivery.core.Courier;
import io.github.ilyazinkovich.reactive.delivery.filter.FilteredCouriers;
import java.util.function.Consumer;

public class Sort {

  private final Consumer<Assignment> assignedCouriersConsumer;

  public Sort(final Consumer<Assignment> assignedCouriersConsumer) {
    this.assignedCouriersConsumer = assignedCouriersConsumer;
  }

  public void accept(final FilteredCouriers filteredCouriers) {
    filteredCouriers.couriers.stream()
        .min(Courier.etaComparator())
        .map(courier -> new Assignment(filteredCouriers.order, courier.id))
        .ifPresent(assignedCouriersConsumer::accept);
  }
}
