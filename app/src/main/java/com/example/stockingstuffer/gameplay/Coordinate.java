package com.example.stockingstuffer.gameplay;

public class Coordinate {

    /* The coordinate class is used to encapsulate a horizontal and vertical position
       associated with the stocking, the presents, and the coal. */

    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this. y = y;
    }
    public Coordinate changeCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
