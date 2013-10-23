package org.joow.elevator2;

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

    public abstract boolean isFinal();

    public abstract Integer moveCost();

    public final Integer cost(final Integer actualMove) {
        final Integer moves = actualMove - sinceMove;
        return moves * moveCost();
    }
}
