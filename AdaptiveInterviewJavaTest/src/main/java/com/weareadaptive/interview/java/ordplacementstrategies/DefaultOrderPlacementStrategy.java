package com.weareadaptive.interview.java.ordplacementstrategies;

import com.weareadaptive.interview.java.shared.BidAsk;
import com.weareadaptive.interview.java.shared.OrderPlacementStrategy;

public class DefaultOrderPlacementStrategy implements OrderPlacementStrategy {
    public boolean needToPlaceAnOrder(BidAsk bidAsk, int price) throws IllegalArgumentException {
        if (bidAsk == BidAsk.UNKNOWN) {
            System.out.println("Unknown order type");
            return false;
        }

        if (price <= 0) {
            System.out.println("Invalid price");
            return false;
        }

        return (bidAsk == BidAsk.ASK && price < 50) ||
                (bidAsk == BidAsk.BID && price > 100);
    }
}
