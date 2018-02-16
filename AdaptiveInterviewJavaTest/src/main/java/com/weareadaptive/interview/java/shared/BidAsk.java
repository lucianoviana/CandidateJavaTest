package com.weareadaptive.interview.java.shared;

public enum BidAsk {
    UNKNOWN('U'), BID('B'), ASK('A');

    public char asChar() {
        return asChar;
    }

    private final char asChar;

    private BidAsk(char asChar) {
        this.asChar = asChar;
    }
}
