package org.joow.elevator;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ElevatorEngineTest {
    public void callFromFloor0ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor1ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.callAt(1, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor2ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.callAt(2, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor3ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.callAt(3, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor4ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.callAt(4, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor5ElevatorAtFloor0() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.callAt(5, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor0ElevatorAtFloor5() {
        final ElevatorEngine elevatorEngine = ElevatorEngine.withElevator(new Elevator(5, Direction.NONE, false));
        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals("DOWN", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor4ToUpElevatorAtFloor0AndGoToFloor5() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
        elevatorEngine.callAt(4, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(5);
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void callFromFloor1ToUpElevatorAtFloorAndGoToFloor5WithCallsFromLowerFloor() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
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

    public void getNextCommandToServeWhenGoingUp() {
        final ElevatorEngine elevatorEngine = ElevatorEngine.withElevator(new Elevator(1, Direction.UP, false));

        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.go(5);

        final Command nextCommand = elevatorEngine.nextCommandToServe();
        Assert.assertEquals(5, nextCommand.floor());
        Assert.assertEquals(Direction.UP, nextCommand.direction());
    }

    public void makeTwoCallsFromSameFloorThanElevator() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

        elevatorEngine.callAt(0, Direction.UP);
        elevatorEngine.callAt(0, Direction.UP);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals("NOTHING", elevatorEngine.nextCommand());
    }

    public void callFromOtherDirectionOnSameFloor() {
        final ElevatorEngine elevatorEngine = ElevatorEngine.withElevator(new Elevator(3, Direction.UP, false));

        elevatorEngine.callAt(3, Direction.UP);
        elevatorEngine.callAt(3, Direction.DOWN);
        elevatorEngine.go(5);

        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
        Assert.assertEquals("CLOSE", elevatorEngine.nextCommand());
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
    }

    public void goFromFloor1ToFloor0() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

        elevatorEngine.callAt(1, Direction.DOWN);
        Assert.assertEquals(elevatorEngine.nextCommand(), "UP");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");

        elevatorEngine.go(0);
        Assert.assertEquals(elevatorEngine.nextCommand(), "CLOSE");
        Assert.assertEquals(elevatorEngine.nextCommand(), "DOWN");
        Assert.assertEquals(elevatorEngine.nextCommand(), "OPEN");
    }

    public void exchangePeople() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

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
    }

    public void scenario1() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

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
        final ElevatorEngine elevatorEngine = new ElevatorEngine();
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
        Assert.assertEquals("NOTHING", elevatorEngine.nextCommand());
    }

    public void scenario3() {
        final ElevatorEngine elevatorEngine = new ElevatorEngine();

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

        /*



        go?floorToGo=5
        userHasEntered
        go?floorToGo=0
        userHasExited
        call?atFloor=0&to=UP
        nextCommand CLOSE
        nextCommand UP
        nextCommand OPEN
        userHasExited
        call?atFloor=0&to=UP
        nextCommand CLOSE
        nextCommand DOWN
        nextCommand DOWN
        nextCommand DOWN
        nextCommand DOWN
        nextCommand DOWN
        nextCommand OPEN
        userHasEntered
        go?floorToGo=4
        userHasEntered
        go?floorToGo=4
        userHasExited
        call?atFloor=0&to=UP
        nextCommand CLOSE
        nextCommand UP
        nextCommand UP
        nextCommand UP
        nextCommand UP
        nextCommand OPEN
        userHasExited
                userHasExited
        call?atFloor=4&to=DOWN
        nextCommand NOTHING
        call?atFloor=0&to=UP
        nextCommand CLOSE
        nextCommand DOWN
        nextCommand DOWN
        nextCommand DOWN
        nextCommand DOWN
        nextCommand OPEN
        userHasEntered
        go?floorToGo=1
        userHasEntered
        go?floorToGo=3
        nextCommand CLOSE
        nextCommand UP
        nextCommand OPEN
        userHasExited
        call?atFloor=0&to=UP
        nextCommand CLOSE
        nextCommand UP
        nextCommand UP
        nextCommand OPEN
        userHasExited
        call?atFloor=0&to=UP
        nextCommand
        reset?cause=Read+timed+out
        */
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
}
