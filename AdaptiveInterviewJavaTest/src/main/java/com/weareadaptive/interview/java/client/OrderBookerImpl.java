package com.weareadaptive.interview.java.client;

import com.weareadaptive.interview.java.orders.Order;
import com.weareadaptive.interview.java.orders.OrderFactory;
import com.weareadaptive.interview.java.ordplacementstrategies.DefaultOrderPlacementStrategy;
import com.weareadaptive.interview.java.shared.*;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class OrderBookerImpl implements OrderBooker {

    private static OrderBookerImpl instance = new OrderBookerImpl();
    private Socket s;
    private DataOutputStream out;
    private String host;
    private int port;
    private TickNotifier tickNotifier;
    private OrderPlacementNotifier orderPlacementNotifier;
    private OrderPlacementStrategy orderPlacementStrategy = new DefaultOrderPlacementStrategy();

    private OrderBookerImpl() {

    }

    public static OrderBooker getInstance() {
        return instance;
    }

    public void start(final String host, final int port)  {
        stop();
        this.host = host;
        this.port = port;
    }

    public void stop() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            if (s != null) {
                s.close();
                s = null;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void subscribe() throws IOException {
        s = new Socket(host, port);
        out = new DataOutputStream(s.getOutputStream());
        System.out.println("Client subscribed");

        try {
            final TickNotifier notif = this.tickNotifier;
            final Thread readThread = new Thread(new Runnable() {
                public void run() {
                    System.out.println("Client Read Thread started");
                    try {
                        final DataInputStream in =
                                new DataInputStream(s.getInputStream());

                        boolean endOfDay = false;

                        while(!endOfDay) {
                            final char messageType = in.readChar();
                            if (messageType == ServerUpdateType.TICK_UPDATE.asChar()) {
                                final char stock = in.readChar();
                                final char side = in.readChar();
                                final int price = in.readInt();
                                if (notif != null) {
                                    notif.onTickNotification(stock, side == 'A' ? BidAsk.ASK : (side == 'U' ? BidAsk.UNKNOWN : BidAsk.BID), price);
                                }
                            }

                            if (messageType == ServerUpdateType.ORDER_PLACED.asChar() || messageType == ServerUpdateType.ORDER_FAILED.asChar()) {
                                final long id = in.readLong();
                                final OrderType orderType = in.readChar() == 'M' ? OrderType.MARKET : OrderType.UNKNOWN;
                                final OrderSide side = in.readChar() == 'S' ? OrderSide.SELL : OrderSide.BUY;
                                final char symbol = in.readChar();
                                final int shares = in.readInt();
                                final int price = in.readInt();

                                final Order o = OrderFactory.getInstance().createOrder(orderType, side, symbol, shares, price);

                                if (orderPlacementNotifier != null) {
                                    if (messageType == ServerUpdateType.ORDER_PLACED.asChar())
                                        orderPlacementNotifier.onOrderPlacedSuccess(o);
                                    else
                                        orderPlacementNotifier.onOrderPlacedFailed(o);
                                }
                            }

                            if (messageType == ServerUpdateType.END_OF_DAY.asChar()) {

                                endOfDay = true;
                                System.out.println("=========== END OF DAY received =============");

                                if (notif != null) {
                                    Map<String, Long> ret = new HashMap<String, Long>();
                                    notif.onEndOfDay(ret);
                                    for(Map.Entry<String, Long> e : ret.entrySet())
                                        System.out.println(e.getKey() + " - " + e.getKey());
                                }
                            }
                        }

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }


                }
            });

            readThread.start();

            readThread.join();

        } catch (InterruptedException iex) {
            System.out.println(iex);
        }
    }

    public Long placeOrder(Order order) {
        try {
            out.writeLong(order.getId());
            out.writeChar(order.getOrderType().asChar());
            out.writeChar(order.getSide().asChar());
            out.writeChar(order.getSymbol());
            out.writeInt(order.getShares());
            out.writeInt(order.getPrice());
            out.flush();
            return order.getId();
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public OrderPlacementStrategy getOrderPlacementStrategy() {
        return orderPlacementStrategy;
    }

    public void setTickNotifier(TickNotifier notifier) {
        this.tickNotifier = notifier;
    }

    public void setOrderPlacementNotifier(OrderPlacementNotifier notifier) {
        this.orderPlacementNotifier = notifier;
    }
}

