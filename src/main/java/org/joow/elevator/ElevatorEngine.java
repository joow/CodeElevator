package org.joow.elevator;

import java.util.*;

public final class ElevatorEngine {
    private Elevator elevator = new Elevator(0, Direction.NONE, false);

    private Direction target = Direction.NONE;

    private final Set<Command> commands = new HashSet<>();

    public static ElevatorEngine withElevator(final Elevator elevator) {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.elevator = elevator;

        return elevatorEngine;
    }

    public void callAt(final int floorAt, Direction direction) {
        commands.add(new Command(floorAt, direction, false));

        if (target == Direction.NONE) {
            target = direction;
        }
    }

    public void go(final int toFloor) {
        final Direction direction = (toFloor < elevator.floor()) ? Direction.DOWN : Direction.UP;
        commands.add(new Command(toFloor, direction, true));
    }

    public void reset() {
        elevator = new Elevator(0, Direction.UP, false);
        target = Direction.NONE;
        commands.clear();
    }

    public String nextCommand() {
        if (commands.isEmpty()) {
            if (elevator.doorsAreOpened()) {
                elevator = new Elevator(elevator.floor(), elevator.direction(), false);
                return "CLOSE";
            } else {
                return "NOTHING";
            }
        }

        final Command command = nextCommandToServe();

        if (command.floor() == elevator.floor()) {
            removeCommands(command.floor());
            elevator = new Elevator(elevator.floor(), elevator.direction(), !elevator.doorsAreOpened());
            if (elevator.doorsAreOpened()) {
                return "OPEN";
            } else {
                return "CLOSE";
            }
        }

        if (command.floor() != elevator.floor() && elevator.doorsAreOpened()) {
            elevator = new Elevator(elevator.floor(), elevator.direction(), false);
            return "CLOSE";
        }

        if (command.isGo()) {
            target = command.direction();
        }

        if (command.floor() > elevator.floor()) {
            elevator = new Elevator(elevator.floor() + 1, Direction.UP, false);
            return "UP";
        }

        if (command.floor() < elevator.floor()) {
            elevator = new Elevator(elevator.floor() - 1, Direction.DOWN, false);
            return "DOWN";
        }

        throw new IllegalStateException();
    }

    private void removeCommands(int floor) {
        for (Iterator<Command> it = commands.iterator(); it.hasNext(); ) {
            if (it.next().floor() == floor) {
                it.remove();
            }
        }
    }

    public Command nextCommandToServe() {
        List<Command> commandsToServe = filterCommands();

        if (commandsToServe.isEmpty()) {
            target = Direction.inverseOf(target);
            elevator = new Elevator(elevator.floor(), Direction.NONE, elevator.doorsAreOpened());
            commandsToServe = filterCommands();
        }

        if (commandsToServe.isEmpty()) {
            commandsToServe.addAll(commands);
        }

        Collections.sort(commandsToServe, new Comparator<Command>() {
            @Override
            public int compare(Command command1, Command command2) {
                if (target == Direction.UP) {
                    return Integer.compare(command1.floor(), command2.floor());
                } else {
                    return -Integer.compare(command1.floor(), command2.floor());
                }
            }
        });

        return commandsToServe.get(0);
    }

    private List<Command> filterCommands() {
        final List<Command> filtered = new ArrayList<>();

        for (final Command command : commands) {
            if (command.direction() == target) {
                if (elevator.direction() == Direction.NONE || command.isGo()) {
                    filtered.add(command);
                } else if ((elevator.direction() == Direction.UP) && (command.floor() >= elevator.floor())) {
                    filtered.add(command);
                } else if ((elevator.direction() == Direction.DOWN && (command.floor() <= elevator.floor()))) {
                    filtered.add(command);
                }
            }
        }

        return filtered;
    }
}
