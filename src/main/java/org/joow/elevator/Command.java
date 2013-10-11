package org.joow.elevator;

public final class Command {
    private final int floor;

    private final Direction direction;

    private final boolean isGo;

    public Command(int floor, Direction direction, final boolean isGo) {
        this.floor = floor;
        this.direction = direction;
        this.isGo = isGo;
    }

    public int floor() {
        return floor;
    }

    public Direction direction() {
        return direction;
    }

    public boolean isGo() {
        return isGo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Command)) {
            return false;
        }

        Command command = (Command) object;

        if (floor != command.floor) {
            return false;
        }

        if (direction != command.direction) {
            return false;
        }

        if (isGo != command.isGo) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = floor;
        result = 31 * result + direction.hashCode();
        return result;
    }
}
