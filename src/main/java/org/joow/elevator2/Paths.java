package org.joow.elevator2;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Paths {
    private Paths() {}

    public static Path bestPath(final List<Action> actions, final Cabin cabin) {
        final Collection<List<Action>> permutations = Collections2.permutations(actions);
        final List<Path> paths = new ArrayList<>(Collections2.transform(permutations, new Function<List<Action>, Path>() {
            @Override
            public Path apply(final List<Action> permutation) {
                //final PathCreator pathCreator = new PathCreator(permutation, cabin);
                //Path.FORK_JOIN_POOL.invoke(pathCreator);
                //return pathCreator.join();
                return new Path(permutation, cabin);
            }
        }));

        return bestPath(paths);
    }

    private static Path bestPath(final List<Path> paths) {
        if (paths.size() == 1) {
            return paths.get(0);
        } else {
            return min(paths.get(0), bestPath(paths.subList(1, paths.size())));
        }
    }

    private static Path min(final Path path1, final Path path2) {
        if (path1.cost() <= path2.cost()) {
            return path1;
        } else {
            return path2;
        }
    }

    private static class PathCreator extends RecursiveTask<Path> {
        private final List<Action> actions;

        private final Cabin cabin;

        public PathCreator(final List actions, final Cabin cabin) {
            this.actions = actions;
            this.cabin = cabin;
        }

        @Override
        protected Path compute() {
            return new Path(actions, cabin);
        }
    }
}
