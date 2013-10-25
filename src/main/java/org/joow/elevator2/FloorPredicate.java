package org.joow.elevator2;

import com.google.common.base.Predicate;

public class FloorPredicate implements Predicate<Action> {
    private final Integer floor;

    public FloorPredicate(final Integer floor) {
        this.floor = floor;
    }

    @Override
    public boolean apply(final Action action) {
        return action.floor() == floor;
    }
}