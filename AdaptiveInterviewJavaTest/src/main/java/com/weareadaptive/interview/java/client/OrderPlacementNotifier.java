package com.weareadaptive.interview.java.client;


import com.weareadaptive.interview.java.orders.Order;

public interface OrderPlacementNotifier {
    void onOrderPlacedSuccess(Order o);
    void onOrderPlacedFailed(Order o);
}

