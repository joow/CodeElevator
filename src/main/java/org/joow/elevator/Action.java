package org.joow.elevator;

import java.util.Objects;

public class Action implements Comparable<Action> {
    private final int floor;

    private final Direction direction;

    public Action(final int floor, final Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Action)) {
            return false;
        }

        final Action action = (Action) object;

        return Integer.compare(floor, action.floor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor);
    }

    @Override
    public int compareTo(final Action action) {
        return Integer.compare(floor, action.floor);
    }

    public int floor() {
        return floor;
    }

    public Direction direction() {
        return direction;
    }
}
