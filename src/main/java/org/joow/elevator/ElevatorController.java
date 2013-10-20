package org.joow.elevator;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.SortedSet;
import java.util.TreeSet;

public class ElevatorController {
    private Cab cab;

    private Direction destination;

    private final SortedSet<Action> nextActions = new TreeSet<>();

    public ElevatorController() {
        this(new Cab(), Direction.UP);
    }

    public ElevatorController(final Cab cab) {
        this(cab, Direction.UP);
    }

    public ElevatorController(final Cab cab, final Direction destination) {
        this.cab = cab;
        this.destination = destination;
    }

    public void callAt(final int atFloor, final Direction to) {
        final Action action = new Action(atFloor, to);

        synchronized (nextActions) {
            nextActions.add(action);
        }
    }

    public boolean isAhead(final int floor, final Direction direction) {
        if (direction == Direction.UP) {
            return floor >= cab.floor();
        } else {
            return floor <= cab.floor();
        }
    }

    public void go(int floorToGo) {
        final Direction to = (floorToGo < cab.floor()) ? Direction.DOWN : Direction.UP;
        callAt(floorToGo, to);
    }

    public void reset() {
        cab = new Cab();
        destination = Direction.UP;
        nextActions.clear();
    }

    public Command nextCommand() {
        final Optional<Action> optionalNextAction = nextAction();

        if (optionalNextAction.isPresent()) {
            final Action nextAction = optionalNextAction.get();

            if (nextAction.floor() == cab.floor()) {
                if (cab.areDoorsOpened()) {
                    return cab.closeDoors();
                } else {
                    synchronized (nextActions) {
                        nextActions.remove(nextAction);
                    }
                    return cab.openDoors();
                }
            }

            if (cab.areDoorsOpened()) {
                return cab.closeDoors();
            }
            return cab.moveTo(nextAction.floor());
        }

        if (cab.areDoorsOpened()) {
            return cab.closeDoors();
        } else if (cab.floor() > 0) {
            return cab.moveTo(0);
        }

        return Command.NOTHING;
    }

    private Optional<Action> nextAction() {
        final ImmutableSet<Action> currentActions = copyOfNextActions();

        if (currentActions.isEmpty()) {
            return Optional.absent();
        }

        SortedSet<Action> filteredActions = Sets.newTreeSet(Collections2.filter(currentActions,
                Predicates.and(new ActionAheadPredicate(destination), new ActionDirectionPredicate(destination))));

        if (filteredActions.isEmpty()) {
            final Direction invertedDestination = Direction.inverseOf(destination);
            filteredActions = Sets.newTreeSet(Collections2.filter(currentActions,
                    new ActionDirectionPredicate(invertedDestination)));

            if (filteredActions.isEmpty()) {
                filteredActions = Sets.newTreeSet(Collections2.filter(currentActions,
                        new ActionDirectionPredicate(destination)));
            }
        }

        final Action nextAction = nextActionByDestination(filteredActions, filteredActions.first().direction());

        if (nextAction.floor() > cab.floor() && destination == Direction.DOWN) {
            destination = Direction.inverseOf(destination);
        } else if (nextAction.floor() < cab.floor() && destination == Direction.UP) {
            destination = Direction.inverseOf(destination);
        }

        return Optional.of(nextAction);
    }

    private ImmutableSet<Action> copyOfNextActions() {
        synchronized (nextActions) {
            return ImmutableSet.copyOf(nextActions);
        }
    }

    private Action nextActionByDestination(final SortedSet<Action> actions, final Direction destination) {
        if (destination == Direction.UP) {
            return actions.first();
        } else {
            return actions.last();
        }
    }

    private class ActionDirectionPredicate implements Predicate<Action> {
        private final Direction direction;

        public ActionDirectionPredicate(final Direction direction) {
            this.direction = direction;
        }

        @Override
        public boolean apply(final Action action) {
            return action.direction() == direction;
        }
    }

    private class ActionAheadPredicate implements Predicate<Action> {
        private final Direction direction;

        public ActionAheadPredicate(final Direction direction) {
            this.direction = direction;
        }

        @Override
        public boolean apply(final Action action) {
            return isAhead(action.floor(), direction);
        }
    }
}
