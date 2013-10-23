package org.joow.elevator2;

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
}
