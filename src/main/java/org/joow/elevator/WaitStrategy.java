package org.joow.elevator;

public interface WaitStrategy {
    boolean shouldWait(final Cab cab, final int nbUsersWaiting);
}
