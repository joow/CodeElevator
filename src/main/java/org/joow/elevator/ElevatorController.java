package org.joow.elevator;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import java.util.SortedSet;
import java.util.TreeSet;

public class ElevatorController {
    private Cab cab;

    private Direction destination;

    private SortedSet<Action> nextActions = new TreeSet<>();

    private SortedSet<Action> waitingActions = new TreeSet<>();

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

        if (to == destination && isAhead(atFloor, destination)) {
            nextActions.add(action);
        } else {
            waitingActions.add(action);
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
        waitingActions.clear();
    }

    public Command nextCommand() {
        final Optional<Action> optionalNextAction = nextAction();

        if (optionalNextAction.isPresent()) {
            final Action nextAction = optionalNextAction.get();

            if (nextAction.floor() == cab.floor()) {
                if (cab.areDoorsOpened()) {
                    return cab.closeDoors();
                } else {
                    removeAction(nextAction);
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
        }

        return Command.NOTHING;
    }

    private void removeAction(final Action action) {
        nextActions.remove(action);
        waitingActions.remove(action);
    }

    private Optional<Action> nextAction() {
        if (nextActions.isEmpty()) {
            if (waitingActions.isEmpty()) {
                return Optional.absent();
            }

            final Direction invertedDestination = Direction.inverseOf(destination);
            final Action nextAction = nextActionByDestination(waitingActions, invertedDestination);

            if (nextAction.floor() > cab.floor() && destination == Direction.DOWN) {
                destination = invertedDestination;
                nextActions.addAll(Sets.newTreeSet(Collections2.filter(waitingActions,
                        Predicates.and(new ActionDirectionPredicate(invertedDestination),
                                new ActionAheadPredicate(invertedDestination)))));
                waitingActions.removeAll(nextActions);
            } else if (nextAction.floor() < cab.floor() && destination == Direction.UP) {
                destination = invertedDestination;
                nextActions.addAll(Sets.newTreeSet(Collections2.filter(waitingActions,
                        Predicates.and(new ActionDirectionPredicate(invertedDestination),
                                new ActionAheadPredicate(invertedDestination)))));
                waitingActions.removeAll(nextActions);
            }

            return Optional.of(nextAction);
        }

        return Optional.of(nextActionByDestination(nextActions, destination));
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
