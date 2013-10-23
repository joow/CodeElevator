package org.joow.elevator2;

import com.google.common.base.Optional;

public class ElevatorController {
    private Cabin cabin;

    private final Actions actions = new Actions();

    public ElevatorController() {
        this(new Cabin());
    }

    public ElevatorController(final Cabin cabin) {
        this.cabin = cabin;
    }

    public void callAt(final Integer atFloor, final Direction to) {
        actions.add(new Call(atFloor, to, cabin.move()));
    }

    public void go(final Integer floorToGo) {
        final Direction to = (floorToGo < cabin.floor()) ? Direction.DOWN : Direction.UP;
        actions.add(new Go(floorToGo, to, cabin.move()));
    }

    public void reset() {
        cabin = new Cabin();
        actions.clear();
    }

    public void userHasEntered() {
    }

    public void userHasExited() {
    }

    public Command nextCommand() {
        final Optional<Action> optionalNextAction = actions.next(cabin);

        if (optionalNextAction.isPresent()) {
            final Action nextAction = optionalNextAction.get();

            if (nextAction.floor() == cabin.floor()) {
                if (cabin.areDoorsOpened()) {
                    cabin = cabin.closeDoors();
                    return Command.CLOSE;
                } else {
                    actions.remove(nextAction);
                    cabin = cabin.openDoors();
                    return Command.OPEN;
                }
            }

            if (cabin.areDoorsOpened()) {
                cabin = cabin.closeDoors();
                return Command.CLOSE;
            }

            final int currentFloor = cabin.floor();
            cabin = cabin.moveTo(nextAction.floor());

            if (nextAction.floor() > currentFloor) {
                return Command.UP;
            } else {
                return Command.DOWN;
            }
        }

        if (cabin.areDoorsOpened()) {
            cabin = cabin.closeDoors();
            return Command.CLOSE;
        } else if (cabin.floor() > 0) {
            cabin = cabin.moveTo(0);
            return Command.DOWN;
        }

        return Command.NOTHING;
    }
}
