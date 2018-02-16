package com.weareadaptive.interview.java.shared;

public enum OrderType {
    UNKNOWN('U'), MARKET('M');

    public char asChar() {
        return asChar;
    }

    private final char asChar;

    private OrderType(char asChar) {
        this.asChar = asChar;
    }
}
