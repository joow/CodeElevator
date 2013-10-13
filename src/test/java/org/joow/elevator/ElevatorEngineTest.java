package org.joow.elevator;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ElevatorEngineTest {
    public void callFromFloor0ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "NOTHING");
    }

    public void callFromFloor1ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void callFromFloor2ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(2, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void callFromFloor3ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(3, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void callFromFloor4ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(4, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void callFromFloor5ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(5, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void callFromFloor0ElevatorAtFloor5() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine(5, Direction.DOWN);
        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "NOTHING");
    }

    public void callFromFloor4ToUpElevatorAtFloor0AndGoToFloor5() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(4, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(5);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor1ToUpElevatorAtFloorAndGoToFloor5WithCallsFromLowerFloor() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");

        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(5);

        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void shouldGoUpWhenCalledFromUpAndCalledFromDown() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine(1, Direction.UP);
        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.go(5);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
    }

    public void makeTwoCallsFromSameFloorThanElevator() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();

        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "NOTHING");
    }

    public void callFromOtherDirectionOnSameFloor() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine(3, Direction.UP);

        elevatorEngine.callAt(3, Direction.UP);
        elevatorEngine.callAt(3, Direction.DOWN);
        elevatorEngine.go(5);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
    }

    public void goFromFloor1ToFloor0() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();

        elevatorEngine.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "NOTHING");
    }

    public void exchangePeople() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();

        elevatorEngine.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(1);
        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.go(2);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void scenario1() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();

        elevatorEngine.callAt(4, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("OPEN", elevatorEngine.nextCommand());

        elevatorEngine.go(1);
        elevatorEngine.go(3);
        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.go(5);
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(3, Direction.DOWN);
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void scenario2() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();
        elevatorEngine.callAt(5, Direction.DOWN);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");

        elevatorEngine.callAt(4, Direction.DOWN);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void scenario3() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine();

        elevatorEngine.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.go(0);
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(1);
        elevatorEngine.go(4);
        elevatorEngine.callAt(2, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(4, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(2);
        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(4);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(4, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(5);
        elevatorEngine.go(0);
        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
    }

    public void scenario4() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(1);
        elevatorEngine.go(1);
        elevatorEngine.callAt(3, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(5);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void scenario5() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.go(1);
        elevatorEngine.go(2);

        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void shouldReopenItsDoorWhenCalledAgainFromSameFloor() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.go(1);
        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.go(2);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void shouldTakeFurthestCaller() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine(5, Direction.DOWN);

        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.callAt(4, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.go(5);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.go(5);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void shouldTakeCallsToSameDirectionFirst() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine(2, Direction.DOWN);

        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.go(2);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");

        elevatorEngine.go(2);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }

    public void shouldTakeCallToDownWhenAtUpperLevel() {
        final ElevatorEngine elevatorEngine = new BetterElevatorEngine(5, Direction.DOWN);

        elevatorEngine.callAt(0, Direction.UP);
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");

        elevatorEngine.callAt(1, Direction.DOWN);

        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
    }
}
