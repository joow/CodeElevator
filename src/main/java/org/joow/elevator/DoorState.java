package org.joow.elevator;

public enum DoorState {
    OPENED, CLOSED;

    public static DoorState inverse(final DoorState doorState) {
        if (doorState == OPENED) {
            return CLOSED;
        } else {
            return OPENED;
        }
    }

    public static boolean isOpened(final DoorState doorState) {
        return doorState == OPENED;
    }
}
