package org.joow.elevator;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Test
public class ElevatorControllerTest {
    public void shouldDoNothingWhenNoCommand() {
        final ElevatorController elevatorController = new ElevatorController();

        Assert.assertEquals(elevatorController.nextCommand(), Command.NOTHING);
    }

    public void shouldCloseDoorsWhenDoorsAreOpenedAndNoCommand() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().withDoors(DoorState.OPENED).build());

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void shouldOpenDoorsWhenAtRequestedFloor() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
    }

    public void shouldCloseDoorsWhenAtRequestedFloorWithDoorsOpened() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().withDoors(DoorState.OPENED).build());

        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void shouldCloseDoorsWhenNotAtRequestedFloorWithDoorsOpened() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().withDoors(DoorState.OPENED).build());

        elevatorController.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void shouldGoUpWhenRequestedFloorIsUpper() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
    }

    public void shouldGoDownWhenRequestedFloorIsBelow() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().at(1).build(), Direction.DOWN);

        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
    }

    public void callFromFloor0ToUpElevatorAtFloor0() {
        final ElevatorController elevatorController = new ElevatorController();
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.NOTHING);
    }

    public void callFromFloor1ToUpElevatorAtFloor0() {
        final ElevatorController elevatorController = new ElevatorController();
        elevatorController.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void callFromFloor5ToUpElevatorAtFloor0() {
        final ElevatorController elevatorController = new ElevatorController();
        elevatorController.callAt(5, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void callFromFloor0ElevatorAtFloor5() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().at(5).build(), Direction.DOWN);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.NOTHING);
    }

    public void callFromFloor4ToUpElevatorAtFloor0AndGoToFloor5() {
        final ElevatorController elevatorController = new ElevatorController();
        elevatorController.callAt(4, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void callFromFloor1ToUpElevatorAtFloorAndGoToFloor5WithCallsFromLowerFloor() {
        final ElevatorController elevatorController = new ElevatorController();
        elevatorController.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);

        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
    }

    public void makeTwoCallsFromSameFloorThanElevator() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(0, Direction.UP);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.NOTHING);
    }

    public void callFromOtherDirectionOnSameFloor() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().at(3).build(), Direction.UP);

        elevatorController.callAt(3, Direction.UP);
        elevatorController.callAt(3, Direction.DOWN);
        elevatorController.go(5);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
    }

    public void goFromFloor1ToFloor0() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.NOTHING);
    }

    public void exchangePeople() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(2);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void scenario1() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(4, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);
        elevatorController.go(3);
        elevatorController.callAt(0, Direction.UP);
        elevatorController.go(5);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(3, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
    }

    public void scenario2() {
        final ElevatorController elevatorController = new ElevatorController();
        elevatorController.callAt(5, Direction.DOWN);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);

        elevatorController.callAt(4, Direction.DOWN);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    @Test(enabled = false)
    public void scenario3() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);
        elevatorController.go(4);
        elevatorController.callAt(2, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(4, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(2);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(4);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(4, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);
        elevatorController.go(0);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
    }

    public void scenario4() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(0, Direction.UP);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);
        elevatorController.go(1);
        elevatorController.callAt(3, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void scenario5() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        elevatorController.go(1);
        elevatorController.go(2);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);

        elevatorController.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
    }

    public void scenario6() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(0, Direction.UP);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(3);
        elevatorController.go(1);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(4);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(1, Direction.DOWN);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(4);
        elevatorController.go(1);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        elevatorController.callAt(3, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void scenario7() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(0, Direction.UP);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(3);

        elevatorController.go(3);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);

        elevatorController.go(3);

        elevatorController.go(5);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(3, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(2);

        elevatorController.go(5);
        elevatorController.callAt(3, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(3, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);

        elevatorController.callAt(1, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);

        elevatorController.callAt(3, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(5);

        elevatorController.go(3);

        elevatorController.go(1);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(4, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(4);

        elevatorController.go(3);
        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(2, Direction.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void scenario8() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(0, Direction.UP);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(4);
        elevatorController.go(3);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(1);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(2, Direction.DOWN);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
    }

    public void shouldTakeCallToDownWhenAtUpperLevel() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().at(5).build());

        elevatorController.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);

        elevatorController.callAt(1, Direction.DOWN);

        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);

        elevatorController.go(0);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.CLOSE);
    }

    public void shouldServeTargetedCommandFirst() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().at(3).build());

        elevatorController.callAt(1, Direction.DOWN);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
    }

    public void shouldServeCallToCurrentDestinationFirst() {
        final ElevatorController elevatorController = new ElevatorController();

        elevatorController.callAt(3, Direction.DOWN);

        Assert.assertEquals(elevatorController.nextCommand(), Command.UP);

        elevatorController.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.OPEN);
    }

    public void shouldServeFurthestCallFirst() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().at(5).build());

        elevatorController.callAt(4, Direction.UP);
        elevatorController.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
    }

    public void shouldDoNothingIfAtFloor0AndNothingToDo() {
        final ElevatorController elevatorController = new ElevatorController();

        Assert.assertEquals(elevatorController.nextCommand(), Command.NOTHING);
    }

    public void shouldGoBackToFloor0IfNothingElseToDo() {
        final ElevatorController elevatorController = new ElevatorController(new Cab.Builder().at(5).build());

        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.DOWN);
        Assert.assertEquals(elevatorController.nextCommand(), Command.NOTHING);
    }

    @Test(enabled = false)
    public void shouldSupportMultipleCallers() throws InterruptedException {
        final ElevatorController elevatorController = new ElevatorController();
        final ExecutorService executorService = Executors.newCachedThreadPool();

        final Callable<Command> nextCommandCaller = new Callable<Command>() {
            @Override
            public Command call() {
                return elevatorController.nextCommand();
            }
        };

        final Callable<Void> actionCaller = new Callable<Void>() {
            @Override
            public Void call() {
                elevatorController.callAt(0, Direction.UP);
                return null;
            }
        };

        final List<Callable<Command>> callables1 = new ArrayList<>(10000);
        final List<Callable<Void>> callables2 = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            callables1.add(nextCommandCaller);
            callables2.add(actionCaller);
        }

        executorService.invokeAll(callables1);
        executorService.invokeAll(callables2);
        executorService.shutdown();
    }
}
