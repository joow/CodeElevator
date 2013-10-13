package org.joow.elevator;

public enum Direction {
    UP, DOWN, NONE;

    public static Direction of(String directionString) {
        for (final Direction direction : values()) {
            if (direction.toString().equalsIgnoreCase(directionString)) {
                return direction;
            }
        }

        throw new IllegalArgumentException(directionString + " is not a known direction.");
    }

    public static Direction inverseOf(Direction direction) {
        if (direction == UP) {
            return DOWN;
        } else if (direction == DOWN) {
            return UP;
        } else {
            return NONE;
        }
    }

    public boolean isAhead(final int commandFloor, final int elevatorFloor) {
        if (this == UP) {
            return commandFloor >= elevatorFloor;
        } else {
            return commandFloor <= elevatorFloor;
        }
    }
}
