package org.joow.elevator2;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Actions {
    private final List<Action> actions = new CopyOnWriteArrayList<>();

    public void add(final Action action) {
        actions.add(action);
    }

    public void remove(final Action action) {
        actions.removeAll(Collections2.filter(actions, new FloorPredicate(action.floor())));
    }

    public void clear() {
        actions.clear();
    }

    public Optional<Action> next(final Cabin cabin) {
        if (actions.isEmpty()) {
            return Optional.absent();
        } else {
            return Optional.of(Paths.getBestPath(actions, cabin).first());
        }
    }
}
