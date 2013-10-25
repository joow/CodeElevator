package org.joow.elevator2;

import org.joow.elevator.DoorState;

public class Cabin {
    private final Integer floor;

    private final DoorState doorState;

    private final Integer move;

    public Cabin() {
        this(0);
    }

    public Cabin(final Integer floor) {
        this(floor, DoorState.CLOSED);
    }

    public Cabin(final int floor, final DoorState doorState) {
        this(floor, doorState, 0);
    }

    public Cabin(final int floor, final DoorState doorState, final Integer move) {
        this.floor = floor;
        this.doorState = doorState;
        this.move = move;
    }

    public Integer floor() {
        return floor;
    }

    public Boolean areDoorsOpened() {
        return doorState == DoorState.OPENED;
    }

    public Integer move() {
        return move;
    }

    public Cabin moveTo(final int toFloor) {
        if (toFloor == floor) {
            return this;
        }

        if (areDoorsOpened()) {
            return closeDoors();
        } else if (toFloor > floor) {
            return new Cabin(floor + 1, doorState, move() + 1);
        } else {
            return new Cabin(floor - 1, doorState, move() + 1);
        }
    }

    public Cabin openDoors() {
        return changeDoorsState(DoorState.OPENED);
    }

    public Cabin closeDoors() {
        return changeDoorsState(DoorState.CLOSED);
    }

    private Cabin changeDoorsState(final DoorState newDoorState) {
        if (newDoorState == doorState) {
            return this;
        }

        if (doorState == DoorState.OPENED) {
            return new Cabin(floor, DoorState.CLOSED, move() + 1);
        } else {
            return new Cabin(floor, DoorState.OPENED, move() + 1);
        }
    }
}
