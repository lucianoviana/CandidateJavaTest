package com.weareadaptive.interview.java.orders;

import com.weareadaptive.interview.java.shared.OrderSide;
import com.weareadaptive.interview.java.shared.OrderType;

public abstract class Order {

    // TO BE IMPLEMENTED BY CANDIDATE
    protected OrderType orderType;
    protected OrderSide side;
    protected int price;
    protected int shares;
    protected char symbol;
    protected long id;

    protected Order(OrderType orderType, char symbol, int shares, int price, OrderSide side) {
        this.id = (int)(Math.random() * 1000000000);
    }

    public abstract char getSymbol();

    public abstract OrderType getOrderType();

    public abstract OrderSide getSide();

    public abstract int getPrice();

    public abstract int getShares();

    public abstract long getId();

    // END TO BE IMPLEMENTED BY CANDIDATE
}
