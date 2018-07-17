package io.github.ilyazinkovich.reactive.delivery.redispatch;

import io.github.ilyazinkovich.reactive.delivery.core.Order;
import io.github.ilyazinkovich.reactive.delivery.core.OrderId;
import io.github.ilyazinkovich.reactive.delivery.core.ReDispatch;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ReDispatcher {

  private final Consumer<Order> orderConsumer;
  private final Map<OrderId, AtomicInteger> retriesCount;
  private final Consumer<RetriesExceeded> retriesExceededConsumer;

  public ReDispatcher(final Consumer<Order> orderConsumer,
      final Map<OrderId, AtomicInteger> retriesCount,
      final Consumer<RetriesExceeded> retriesExceededConsumer) {
    this.orderConsumer = orderConsumer;
    this.retriesCount = retriesCount;
    this.retriesExceededConsumer = retriesExceededConsumer;
  }

  public void accept(final ReDispatch reDispatch) {
    final OrderId bookingId = reDispatch.order.id;
    retriesCount.putIfAbsent(bookingId, new AtomicInteger());
    if (retriesCount.get(bookingId).incrementAndGet() < 3) {
      orderConsumer.accept(reDispatch.order);
    } else {
      System.out.printf("Retries count exceeded for order %s%n", bookingId.uid);
      retriesExceededConsumer.accept(new RetriesExceeded(reDispatch.order));
    }
  }
}
