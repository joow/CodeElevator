package org.joow.elevator;

public class Cab {
    private int floor;

    private DoorState doorState = DoorState.CLOSED;

    public int floor() {
        return floor;
    }

    public boolean areDoorsOpened() {
        return doorState == DoorState.OPENED;
    }

    public Command moveTo(final int toFloor) {
        if (toFloor == floor) {
            return Command.NOTHING;
        }

        if (areDoorsOpened()) {
            return closeDoors();
        } else if (toFloor > floor) {
            floor++;
            return Command.UP;
        } else {
            floor--;
            return Command.DOWN;
        }
    }

    public Command openDoors() {
        return changeDoorsState(DoorState.OPENED);
    }

    public Command closeDoors() {
        return changeDoorsState(DoorState.CLOSED);
    }

    private Command changeDoorsState(final DoorState newDoorState) {
        if (newDoorState == doorState) {
            return Command.NOTHING;
        }

        doorState = newDoorState;

        if (doorState == DoorState.OPENED) {
            return Command.OPEN;
        } else {
            return Command.CLOSE;
        }
    }

    public static class Builder {
        private Cab cab = new Cab();

        public Builder at(final int floor) {
            cab.floor = floor;
            return this;
        }

        public Builder withDoors(final DoorState doorState) {
            cab.doorState = doorState;
            return this;
        }

        public Cab build() {
            return cab;
        }
    }
}
