package org.joow.elevator2;

import org.joow.elevator.DoorState;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Test
public class PathTest {
    public void computeEmptyActions() {
        final Path path = new Path(Collections.<Action>emptyList(), new Cabin());
        Assert.assertEquals(path.cost(), Integer.valueOf(0));
    }

    public void computeOneGoAtSameFloorAsCabin() {
        final Action go = new Go(0, Direction.DOWN);
        final List<Action> actions = Arrays.asList(go);
        final Path path = new Path(actions, new Cabin());

        Assert.assertEquals(path.cost(), Integer.valueOf(0));
    }

    public void computeOneGoAtOneFloorAbove() {
        final Action go = new Go(1, Direction.UP);
        final List<Action> actions = Arrays.asList(go);
        final Path path = new Path(actions, new Cabin());

        Assert.assertEquals(path.cost(), Integer.valueOf(2));
    }

    public void computeOneGoAtTwoFloorsAbove() {
        final Action go = new Go(2, Direction.UP);
        final List<Action> actions = Arrays.asList(go);
        final Path path = new Path(actions, new Cabin());

        Assert.assertEquals(path.cost(), Integer.valueOf(4));
    }
    public void computeOneGoAtOneFloorBelow() {
        final Action go = new Go(0, Direction.DOWN);
        final List<Action> actions = Arrays.asList(go);
        final Path path = new Path(actions, new Cabin(1));

        Assert.assertEquals(path.cost(), Integer.valueOf(2));
    }
    public void computeOneGoAtFiveFloorsBelow() {
        final Action go = new Go(0, Direction.DOWN);
        final List<Action> actions = Arrays.asList(go);
        final Path path = new Path(actions, new Cabin(5));

        Assert.assertEquals(path.cost(), Integer.valueOf(10));
    }
    public void computeTwoGosAtSameFloor() {
        final Action go = new Go(0, Direction.DOWN);
        final List<Action> actions = Arrays.asList(go, go);
        final Path path = new Path(actions, new Cabin());

        Assert.assertEquals(path.cost(), Integer.valueOf(0));
    }
    public void computeTwoGosAtThreeFloorsAbove() {
        final Action go = new Go(3, Direction.UP);
        final List<Action> actions = Arrays.asList(go, go);
        final Path path = new Path(actions, new Cabin());

        Assert.assertEquals(path.cost(), Integer.valueOf(12));
    }

    public void computeTwoGosOrderedAtDifferentFloorAbove() {
        final Action go1 = new Go(1, Direction.UP);
        final Action go2 = new Go(2, Direction.UP);
        final List<Action> actions = Arrays.asList(go1, go2);
        final Path path = new Path(actions, new Cabin());

        Assert.assertEquals(path.cost(), Integer.valueOf(10));
    }

    public void computeCostOfOneCallUpAtSameFloor() {
        final Action call = new Call(0, Direction.UP);
        final List<Action> actions = Arrays.asList(call);
        final Path path = new Path(actions, new Cabin());

        Assert.assertEquals(path.cost(), Integer.valueOf(12));
    }

    public void computeCostOfOneGoUpFiveFloorsAboveWithDoorsOpened() {
        final Action go = new Go(5, Direction.UP);
        final List actions = Arrays.asList(go);
        final Path path = new Path(actions, new Cabin(0, DoorState.OPENED));

        Assert.assertEquals(path.cost(), Integer.valueOf(12));
    }
}
