package org.joow.elevator2;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PathCalculator extends RecursiveTask<Integer> {
    private final List<Action> actions;

    private final Cabin cabin;

    public PathCalculator(List<Action> actions, Cabin cabin) {
        this.actions = actions;
        this.cabin = cabin;
    }

    @Override
    protected Integer compute() {
        if (actions.isEmpty()) {
            return 0;
        }

        final Action nextAction = actions.get(0);
        final Cabin cabinAtRequestedFloor = moveCabinTo(nextAction.floor(), cabin);

        final List<Action> servedActions = Lists.newArrayList(Collections2.filter(actions, new FloorPredicate(cabinAtRequestedFloor.floor())));
        final List<Action> remainingActions = replaceServedActions(cabinAtRequestedFloor.floor(), cabinAtRequestedFloor.move());
        final Integer costServedActions = cost(servedActions, cabinAtRequestedFloor.move());

        if (nextAction.isFinal() || remainingActions.size() == 1) {
            return costServedActions + new PathCalculator(remainingActions, cabinAtRequestedFloor.openDoors()).compute();
        } else {
            return costServedActions + Paths.bestPath(remainingActions, cabinAtRequestedFloor.openDoors()).cost();
        }
    }

    private Cabin moveCabinTo(final Integer floor, final Cabin cabin) {
        if (cabin.floor() == floor) {
            return cabin;
        } else {
            return moveCabinTo(floor, cabin.moveTo(floor));
        }
    }

    private List<Action> replaceServedActions(final Integer floor, final Integer actualMove) {
        final Collection<Action> replacedActions = Lists.transform(actions, new Function<Action, Action>() {
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

    private int cost(final List<Action> actions, final Integer actualMove) {
        if (actions.isEmpty()) {
            return 0;
        } else {
            return actions.get(0).cost(actualMove) + cost(actions.subList(1, actions.size()), actualMove);
        }
    }
}
