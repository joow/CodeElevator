package org.joow.elevator;

public class CountdownWaitStrategy implements WaitStrategy {
    private static final int MAX_WAITING_TIME_AT_LOWER_FLOOR = 3;

    private int waitingTimeAtLowerFloor;

    @Override
    public boolean shouldWait(final Cab cab, final int nbUsersWaiting) {
        if (cab.floor() > 0) {
            return false;
        }

        if (cab.areDoorsOpened()) {
            return false;
        }

        final int waitTime = MAX_WAITING_TIME_AT_LOWER_FLOOR - nbUsersWaiting - waitingTimeAtLowerFloor;

        if (waitTime > 0) {
            waitingTimeAtLowerFloor++;
            return true;
        } else {
            waitingTimeAtLowerFloor = 0;
        }

        return false;
    }
}
