package com.weareadaptive.interview.java.shared;

public enum OrderSide {
    UNKNOWN('U'), BUY('B'), SELL('S');

    public char asChar() {
        return asChar;
    }

    private final char asChar;

    OrderSide(char asChar) {
        this.asChar = asChar;
    }
}
