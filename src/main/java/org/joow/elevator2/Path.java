package org.joow.elevator2;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.*;

public class Path {
    private final List<Action> actions;

    private final Integer cost;

    public Path(final List<Action> actions, final Cabin cabin) {
        this.actions = ImmutableList.copyOf(actions);
        this.cost = computeCost(cabin);
    }

    public Integer cost() {
        return cost;
    }

    private Integer computeCost(final Cabin cabin) {
        if (actions.isEmpty()) {
            return 0;
        }

        final Action nextAction = actions.get(0);

        if (cabin.floor() == nextAction.floor()) {
            final List<Action> actionsServed = Lists.newArrayList(Collections2.filter(actions, new FloorPredicate(cabin.floor())));
            final List<Action> actionsNotServed = Lists.newArrayList(Collections2.filter(actions, Predicates.not(new FloorPredicate(cabin.floor()))));
            final List<Action> remainingActions = replaceServedActions(cabin.floor(), cabin.move());
            //return costOfCurrentMove(actionsNotServed) + Paths.getBestPath(remainingActions, cabin.openDoors()).cost();
            return cost(actionsServed, cabin.move()) + Paths.getBestPath(remainingActions, cabin.openDoors()).cost();
        } else {
            //return costOfCurrentMove(actions) + new Path(actions, cabin.moveTo(nextAction.floor())).cost();
            return new Path(actions, cabin.moveTo(nextAction.floor())).cost();
        }
    }

    private List<Action> replaceServedActions(final Integer floor, final Integer actualMove) {
        final Collection<Action> replacedActions = Collections2.transform(actions, new Function<Action, Action>() {
            @Override
            public Action apply(final Action action) {
                if (action.floor() == floor && !action.isFinal()) {
                    if (action.direction() == Direction.UP) {
                        return new Go(5, Direction.UP, actualMove + 1);
                    } else {
                        return new Go(0, Direction.DOWN, actualMove + 1);
                    }
                } else {
                    return action;
                }
            }
        });

        return Lists.newArrayList(Collections2.filter(replacedActions, Predicates.not(new FloorPredicate(floor))));
    }

    private int cost(final List<? extends Action> actions, final Integer actualMove) {
        if (actions.isEmpty()) {
            return 0;
        } else {
            return actions.get(0).cost(actualMove) + cost(actions.subList(1, actions.size()), actualMove);
        }
    }

    private int costOfCurrentMove(final List<? extends Action> actions) {
        if (actions.isEmpty()) {
            return 0;
        } else {
            return actions.get(0).moveCost() + costOfCurrentMove(actions.subList(1, actions.size()));
        }
    }

    public Action first() {
        return actions.get(0);
    }

    private class FloorPredicate implements Predicate<Action> {
        private final Integer floor;

        public FloorPredicate(final Integer floor) {
            this.floor = floor;
        }

        @Override
        public boolean apply(final Action action) {
            return action.floor() == floor;
        }
    }
}
