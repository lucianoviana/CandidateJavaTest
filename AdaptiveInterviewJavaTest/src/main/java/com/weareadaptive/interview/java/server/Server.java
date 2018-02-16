package com.weareadaptive.interview.java.server;

import com.weareadaptive.interview.java.orders.Order;
import com.weareadaptive.interview.java.orders.OrderFactory;
import com.weareadaptive.interview.java.shared.BidAsk;
import com.weareadaptive.interview.java.shared.OrderSide;
import com.weareadaptive.interview.java.shared.OrderType;
import com.weareadaptive.interview.java.shared.ServerUpdateType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

public class Server {

    private String host;
    private int port;
    private Thread th;
    private ServerSocket s;
    private static Server instance;

    public static Server getInstance(int port) {
        if (instance == null)
            instance = new Server(port);

        return instance;
    }


    private Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {

        stop();

        final int port = this.port;
        this.s = new ServerSocket(port);
        final ServerSocket sock = this.s;

        th = new Thread(new Runnable() {
            public void run() {
                System.out.println("Server is listening on port " + port);
                try {

                    while(true) {
                        final Socket clientSocket = sock.accept();
                        System.out.println("Client connected on: " + clientSocket.getLocalAddress().toString());
                        final Thread readThread = new Thread(new Runnable() {
                            public void run() {
                                try {
                                    System.out.println("Reader Thread started");
                                    final DataInputStream in =
                                            new DataInputStream(clientSocket.getInputStream());
                                    final DataOutputStream out =
                                            new DataOutputStream(clientSocket.getOutputStream());

                                    while(true) {
                                        final long id = in.readLong();
                                        final char orderType = in.readChar();
                                        final char side = in.readChar();
                                        final char symbol = in.readChar();
                                        final int shares = in.readInt();
                                        final int price = in.readInt();
                                        if (Math.random() > 0.5) {
                                            writeOrderPlaced(id, orderType, side, symbol, shares, price, out);
                                        } else {
                                            writeOrderFailed(id, orderType, side, symbol, shares, price, out);
                                        }
                                    }

                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }
                        });

                        final Thread writeThread = new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Date startTime = new Date();
                                    char stockX = 'X';
                                    char stockY = 'Y';
                                    System.out.println("Writer Thread started");
                                    final DataOutputStream out =
                                            new DataOutputStream(clientSocket.getOutputStream());

                                    boolean endOfDay = false;
                                    while(!endOfDay) {

                                        Thread.sleep(1000);

                                        long diff = (new Date().getTime() - startTime.getTime()) / (60 * 1000);
                                        if (diff >= 2) {
                                            sendEndOfDay(out);
                                            endOfDay = true;
                                        } else {
                                            int nxtSellPrice = generateRandomPrice(98, 102);
                                            int nxtBuyPrice = generateRandomPrice(48, 52);
                                            stockX = (stockX == 'X') ? 'Y' : 'X';
                                            stockY = (stockY == 'Y') ? 'X' : 'Y';
                                            //System.out.println("Stock" + stockA + " - Sell price " + nxtSellPrice + ", Stock" + stockB + " - Buy price " + nxtBuyPrice);
                                            tickUpdate(stockX, BidAsk.BID, nxtSellPrice, out);
                                            tickUpdate(stockY, BidAsk.ASK, nxtBuyPrice, out);
                                        }
                                    }

                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }
                        });

                        readThread.start();
                        writeThread.start();

                        readThread.join();
                        writeThread.join();
                    }

                } catch (InterruptedIOException io) {
                    System.out.println("Server thread exiting...");
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

        th.setDaemon(true);
        th.start();
    }

    public void stop() {
        try {
            if (th != null) {
                if (s != null) {
                    s.close();
                    s = null;
                }

                if (th.isAlive()) {
                    th.interrupt();
                    th.join();
                    System.out.println("Server thread joined");
                }

                th = null;
            }
        } catch (Exception ex) {
            System.out.println("WARN: error stopping server: " + ex);
        }
    }

    private int generateRandomPrice(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void tickUpdate(final char stock, final BidAsk bidAsk, int price, DataOutputStream out) throws IOException {
        out.writeChar(ServerUpdateType.TICK_UPDATE.asChar());
        out.writeChar(stock);
        out.writeChar(bidAsk.asChar());
        out.writeInt(price);
        out.flush();
    }

    private void writeOrderPlacedOfFailed(ServerUpdateType tp, long id, char orderType, char side, char symbol, int shares, int price, DataOutputStream out) throws IOException {
        out.writeChar(tp.asChar());
        out.writeLong(id);
        out.writeChar(orderType);
        out.writeChar(side);
        out.writeChar(symbol);
        out.writeInt(shares);
        out.writeInt(price);
        out.flush();
    }

    private void writeOrderPlaced(long id, char orderType, char side, char symbol, int shares, int price, DataOutputStream out) throws IOException {
        writeOrderPlacedOfFailed(ServerUpdateType.ORDER_PLACED, id, orderType,  side, symbol, shares, price, out);
    }

    private void writeOrderFailed(long id, char orderType, char side, char symbol, int shares, int price, DataOutputStream out) throws IOException {
        writeOrderPlacedOfFailed(ServerUpdateType.ORDER_FAILED, id, orderType,  side, symbol, shares, price, out);
    }

    private void sendEndOfDay(DataOutputStream out) throws IOException {
        out.writeChar(ServerUpdateType.END_OF_DAY.asChar());
        out.flush();
    }
}
