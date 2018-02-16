package com.weareadaptive.interview.java.client;

import com.weareadaptive.interview.java.shared.BidAsk;

import java.util.Map;

public interface TickNotifier {
    void onTickNotification(final char stock, final BidAsk bidAsk, final int price);
    void onEndOfDay(Map<String, Long> dayStatistics);
}

