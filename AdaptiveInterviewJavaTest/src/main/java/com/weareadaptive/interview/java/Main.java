package com.weareadaptive.interview.java;

import com.weareadaptive.interview.java.client.*;
import com.weareadaptive.interview.java.orders.Order;
import com.weareadaptive.interview.java.orders.OrderFactory;
import com.weareadaptive.interview.java.server.*;
import com.weareadaptive.interview.java.shared.BidAsk;
import com.weareadaptive.interview.java.shared.OrderPlacementStrategy;
import com.weareadaptive.interview.java.shared.OrderSide;
import com.weareadaptive.interview.java.shared.OrderType;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        final OrderBooker booker = OrderBookerImpl.getInstance();
        final Server server = Server.getInstance(900);

        try {

            // ======= INITIALIZATION SECTION - NO NEED TO MODIFY =============
            server.start();
            // Give it some time to initialize
            Thread.sleep(1000);
            booker.start("127.0.0.1", 900);
            // ======= END INITIALIZATION SECTION ==============================




            // THIS IS TO BE INVOKED/IMPLEMENTED BY A CANDIDATE
            //




            // ======= DO NOT MODIFY BELOW THIS LINE =======================
            run();

            server.stop();
            booker.stop();

        } catch (Exception ex) {
            System.out.println(ex);
            run();
        }
    }

    private static void run() {
        try {
            System.in.read();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
