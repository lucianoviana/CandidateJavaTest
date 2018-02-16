package com.weareadaptive.interview.java.client;

import com.weareadaptive.interview.java.orders.Order;
import com.weareadaptive.interview.java.shared.OrderPlacementStrategy;

import java.io.IOException;

public interface OrderBooker {
    void start(final String host, final int port);
    void stop();
    void subscribe() throws IOException;
    Long placeOrder(Order order);
    void setTickNotifier(TickNotifier notifier);
    void setOrderPlacementNotifier(OrderPlacementNotifier notifier);
    OrderPlacementStrategy getOrderPlacementStrategy();
}