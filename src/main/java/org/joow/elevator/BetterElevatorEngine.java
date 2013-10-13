package org.joow.elevator;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.*;

public class BetterElevatorEngine extends ElevatorEngine {
    private int floor;

    private Direction target = Direction.UP;

    private Direction currentDirection = Direction.UP;

    private DoorState doorState = DoorState.CLOSED;

    private final Set<Command> nextCommands = new HashSet<>();

    private final Set<Command> waitingCommands = new HashSet<>();

    public BetterElevatorEngine() {
        this(0, Direction.UP);
    }

    public BetterElevatorEngine(final int floor, final Direction target) {
        this.floor = floor;
        this.target = target;
    }

    public void callAt(final int floorAt, Direction to) {
        if (floorAlreadyInNextCommands(floorAt)) {
            return;
        }

        if (elevatorIsNotAvailable(floorAt, to)) {
            final Command command = new Command(floorAt, to, false);

            if (currentDirection.isAhead(floorAt, floor) || (to == target && target.isAhead(floorAt, floor))) {
                if (target != command.direction()) {
                    target = command.direction();
                    final List<Command> waitingCommandsForTarget = new ArrayList<>();
                    waitingCommandsForTarget.addAll(getCommandsByTarget());

                    for (final Command waitingCommandForTarget : waitingCommandsForTarget) {
                        if (target.isAhead(waitingCommandForTarget.floor(), floor)) {
                            nextCommands.add(waitingCommandForTarget);
                            waitingCommands.remove(waitingCommandForTarget);
                        }
                    }
                }

                nextCommands.add(command);
            } else {
                waitingCommands.add(command);
            }
        }
    }

    private boolean floorAlreadyInNextCommands(final int floorAt) {
        for (Command nextCommand : nextCommands) {
            if (floorAt == nextCommand.floor()) {
                return true;
            }
        }

        return false;
    }

    private boolean elevatorIsNotAvailable(final int floorAt, final Direction to) {
        return !(floorAt == floor && to == target && doorState == DoorState.OPENED);
    }

    public void go(final int toFloor) {
        final Direction to = (toFloor < floor) ? Direction.DOWN : Direction.UP;
        callAt(toFloor, to);
    }

    public void reset() {
        floor = 0;
        target = Direction.UP;
        currentDirection = Direction.UP;
        doorState = DoorState.CLOSED;
        nextCommands.clear();
        waitingCommands.clear();
    }

    public String nextCommand() {
        final Optional<Command> nextCommand = getNextCommand();

        if (nextCommand.isPresent()) {
            final Command command = nextCommand.get();

            if (command.floor() == floor) {
                nextCommands.remove(command);
                if (DoorState.isOpened(doorState)) {
                    doorState = DoorState.CLOSED;
                    return "CLOSE";
                } else {
                    doorState = DoorState.OPENED;
                    return "OPEN";
                }
            }

            if (DoorState.isOpened(doorState)) {
                doorState = DoorState.CLOSED;
                return "CLOSE";
            }

            if (command.floor() > floor) {
                floor++;
                currentDirection = Direction.UP;
                return "UP";
            }

            if (command.floor() < floor) {
                floor--;
                currentDirection = Direction.DOWN;
                return "DOWN";
            }
        } else {
            if (DoorState.isOpened(doorState)) {
                doorState = DoorState.CLOSED;
                return "CLOSE";
            } else if (floor > 0) {
                floor --;
                return "DOWN";
            }

            return "NOTHING";
        }

        throw new IllegalArgumentException("Unable to resolve next command !");
    }

    private Optional<Command> getNextCommand() {
        if (nextCommands.isEmpty()) {
            target = Direction.inverseOf(target);
            nextCommands.addAll(getCommandsByTarget());
            waitingCommands.removeAll(nextCommands);

            if (nextCommands.isEmpty()) {
                return Optional.absent();
            }
        }

        final List<Command> commands = new ArrayList<>(nextCommands.size());
        commands.addAll(nextCommands);
        Collections.sort(commands, new FloorComparator(target));

        return Optional.of(commands.get(0));
    }

    private Collection<? extends Command> getCommandsByTarget() {
        return Collections2.filter(waitingCommands, new Predicate<Command>() {
            @Override
            public boolean apply(Command command) {
                return command.direction() == target;
            }
        });
    }
}
