package org.joow.elevator;

public enum Direction {
    UP, DOWN;

    public static Direction of(final String to) {
        for (final Direction direction : values()) {
            if (direction.toString().equalsIgnoreCase(to)) {
                return direction;
            }
        }

        throw new IllegalArgumentException(String.format("%s is not a valid direction !", to));
    }

    public static Direction inverseOf(final Direction direction) {
        if (direction == UP) {
            return DOWN;
        } else {
            return UP;
        }
    }
}
