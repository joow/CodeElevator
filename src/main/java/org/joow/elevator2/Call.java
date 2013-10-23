package org.joow.elevator2;

public class Call extends Action {
    public Call(final Integer floor, final Direction direction) {
        this(floor, direction, 0);
    }

    public Call(final Integer floor, final Direction direction, final Integer sinceMove) {
        super(floor, direction, sinceMove);
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public Integer moveCost() {
        return 1;
    }
}
