package org.joow.elevator2;

public class Go extends Action {
    public Go(final Integer floor, final Direction direction) {
        this(floor, direction, 0);
    }

    public Go(final Integer floor, final Direction direction, final Integer sinceMove) {
        super(floor, direction, sinceMove);
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public Integer moveCost() {
        return 2;
    }
}
