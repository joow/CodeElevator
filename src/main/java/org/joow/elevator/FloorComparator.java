package org.joow.elevator;

import java.util.Comparator;

public final class FloorComparator implements Comparator<Command> {
    private final Direction direction;

    public FloorComparator(final Direction direction) {
        this.direction = direction;
    }

    @Override
    public int compare(Command command1, Command command2) {
        final int result = Integer.compare(command1.floor(), command2.floor());

        if (direction == Direction.UP) {
            return result;
        } else {
            return -result;
        }
    }
}
