package org.joow.elevator2;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Path {
    public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    private final List<Action> actions;

    private final Integer cost;

    public Path(final List<Action> actions, final Cabin cabin) {
        this.actions = actions;
        this.cost = computeCost(cabin);
    }

    public Action first() {
        return actions.get(0);
    }

    public Integer cost() {
        return cost;
    }

    private Integer computeCost(final Cabin cabin) {
        final PathCalculator pathCalculator = new PathCalculator(actions, cabin);
        FORK_JOIN_POOL.invoke(pathCalculator);

        return pathCalculator.join();
    }
}
