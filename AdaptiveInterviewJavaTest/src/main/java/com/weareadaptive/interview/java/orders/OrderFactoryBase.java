package com.weareadaptive.interview.java.orders;

import com.weareadaptive.interview.java.shared.OrderSide;
import com.weareadaptive.interview.java.shared.OrderType;

public abstract class OrderFactoryBase {
    protected OrderFactoryBase() {

    }

    // TO BE IMPLEMENTED BY CANDIDATE
    protected static OrderFactory instance;

    static OrderFactory getInstance() { return null; }

    public abstract Order createOrder(OrderType orderType, OrderSide side, char sym, int shares, int price);
}
