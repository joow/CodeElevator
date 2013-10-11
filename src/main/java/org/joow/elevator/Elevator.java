package org.joow.elevator;

public final class Elevator {
    private final int floor;

    private final Direction direction;

    private final boolean doorsOpened;

    public Elevator(final int floor, final Direction direction, final boolean doorsOpened) {
        this.floor = floor;
        this.direction = direction;
        this.doorsOpened = doorsOpened;
    }

    public int floor() {
        return floor;
    }

    public Direction direction() {
        return direction;
    }

    public boolean doorsAreOpened() {
        return doorsOpened;
    }

    public boolean doorsAreClosed() {
        return !doorsAreOpened();
    }
}
