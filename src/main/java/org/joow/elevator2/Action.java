package org.joow.elevator2;

import com.google.common.collect.ComparisonChain;

import java.util.Objects;

public abstract class Action {
    private final Integer floor;

    private final Direction direction;

    private final Integer sinceMove;

    public Action(final Integer floor, final Direction direction, final Integer sinceMove) {
        this.floor = floor;
        this.direction = direction;
        this.sinceMove = sinceMove;
    }

    public Integer floor() {
        return floor;
    }

    public Direction direction() {
        return direction;
    }

    public Integer move() {
        return sinceMove;
    }

    public abstract boolean isFinal();

    public abstract Integer moveCost();

    public final Integer cost(final Integer actualMove) {
        final Integer moves = actualMove - sinceMove;
        return moves * moveCost();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Action)) {
            return false;
        }

        final Action action = (Action) obj;

        return ComparisonChain.start().compareFalseFirst(isFinal(), action.isFinal()).compare(floor, action.floor)
                .compare(direction, action.direction).compare(sinceMove, action.sinceMove).result() == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFinal(), floor, direction, sinceMove);
    }
}
