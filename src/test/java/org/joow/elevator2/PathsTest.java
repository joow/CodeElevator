package org.joow.elevator2;

import com.google.common.collect.Lists;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class PathsTest {
    public void getBestPathOfOneCall() {
        final Action action = new Call(0, Direction.UP);

        Assert.assertEquals(Paths.bestPath(Arrays.asList(action), new Cabin()).first(), action);
    }

    public void getBestPathOfOneGo() {
        final Action action = new Go(5, Direction.UP);

        Assert.assertEquals(Paths.bestPath(Arrays.asList(action), new Cabin()).first(), action);
    }

    public void getBestPathOfOneCallAndOneGo() {
        final Action call = new Call(0, Direction.UP);
        final Action go = new Go(5, Direction.UP);

        Assert.assertEquals(Paths.bestPath(Arrays.asList(call, go), new Cabin()).first(), call);
    }

    public void getBestPathOfOneCallBelowElevatorAndOneGo() {
        final Action call = new Call(0, Direction.UP);
        final Action go = new Go(5, Direction.UP);
        final List<Action> actions = Arrays.asList(call, go);

        Assert.assertEquals(new PathCalculator(actions, new Cabin(5)).compute(), Integer.valueOf(41));

        Assert.assertEquals(new PathCalculator(Lists.reverse(actions), new Cabin(5)).compute(), Integer.valueOf(19));

        Assert.assertEquals(Paths.bestPath(Arrays.asList(call, go), new Cabin(5)).first(), go);
    }
}
