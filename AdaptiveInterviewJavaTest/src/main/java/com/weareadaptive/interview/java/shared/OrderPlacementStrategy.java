package com.weareadaptive.interview.java.shared;

public interface OrderPlacementStrategy {
    boolean needToPlaceAnOrder(BidAsk bidAsk, int price) throws IllegalArgumentException;
}
