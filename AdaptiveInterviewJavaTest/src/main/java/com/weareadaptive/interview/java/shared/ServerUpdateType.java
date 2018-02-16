package com.weareadaptive.interview.java.shared;

public enum ServerUpdateType {
    UNKNOWN('U'), TICK_UPDATE('T'), ORDER_PLACED('P'), ORDER_FAILED('F'), END_OF_DAY('E');

    public char asChar() {
        return asChar;
    }

    private final char asChar;

    private ServerUpdateType(char asChar) {
        this.asChar = asChar;
    }
}
