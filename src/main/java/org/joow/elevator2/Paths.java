package org.joow.elevator2;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

public class Paths {
    private Paths() {}

    public static Path getBestPath(final List<Action> actions, final Cabin cabin) {
        final Collection<List<Action>> permutations = Collections2.permutations(actions);
        final List<Path> paths = ImmutableList.copyOf(Collections2.transform(permutations, new Function<List<Action>, Path>() {
            @Override
            public Path apply(final List<Action> permutation) {
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
}
