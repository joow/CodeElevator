package org.joow.elevator;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class CabTest {
    public void shouldDoNothingWhenClosingDoorsAlreadyClosed() {
        final Cab cab = new Cab();

        final Command command = cab.closeDoors();

        Assert.assertEquals(command, Command.NOTHING);
    }

    public void shouldCloseDoorsWhenDoorsAreOpened() {
        final Cab cab = new Cab.Builder().withDoors(DoorState.OPENED).build();

        final Command command = cab.closeDoors();

        Assert.assertEquals(command, Command.CLOSE);
        Assert.assertFalse(cab.areDoorsOpened());
    }

    public void shouldDoNothingWhenOpeningDoorsAlreadyOpened() {
        final Cab cab = new Cab.Builder().withDoors(DoorState.OPENED).build();

        final Command command = cab.openDoors();

        Assert.assertEquals(command, Command.NOTHING);
    }

    public void shouldOpenDoorsWhenDoorsAreClosed() {
        final Cab cab = new Cab();

        final Command command = cab.openDoors();

        Assert.assertEquals(command, Command.OPEN);
        Assert.assertTrue(cab.areDoorsOpened());
    }

    public void shouldDoNothingWhenMovedToSameFloor() {
        final Cab cab = new Cab();

        final Command command = cab.moveTo(0);

        Assert.assertEquals(command, Command.NOTHING);
    }

    public void shouldGoUpWhenMovedOneFloorUp() {
        final Cab cab = new Cab();

        final Command command = cab.moveTo(1);

        Assert.assertEquals(command, Command.UP);
        Assert.assertEquals(cab.floor(), 1);
    }

    public void shouldGoUpWhenMovedSeveralFloorsUp() {
        final Cab cab = new Cab();

        final Command command = cab.moveTo(5);

        Assert.assertEquals(command, Command.UP);
        Assert.assertEquals(cab.floor(), 1);
    }

    public void shouldGoDownWhenMovedOneFloorDown() {
        final Cab cab = new Cab.Builder().at(1).build();

        final Command command = cab.moveTo(0);

        Assert.assertEquals(command, Command.DOWN);
        Assert.assertEquals(cab.floor(), 0);
    }

    public void shouldGoDownWhenMovedSeveralFloorsDown() {
        final Cab cab = new Cab.Builder().at(5).build();

        final Command command = cab.moveTo(0);

        Assert.assertEquals(command, Command.DOWN);
        Assert.assertEquals(cab.floor(), 4);
    }
}
