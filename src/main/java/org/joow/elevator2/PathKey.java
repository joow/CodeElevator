package org.joow.elevator2;

import java.util.List;
import java.util.Objects;

public class PathKey {
    private final List<Action> actions;

    private final Cabin cabin;

    public PathKey(final List<Action> actions, final Cabin cabin) {
        this.actions = actions;
        this.cabin = cabin;
    }

    public List<Action> actions() {
        return actions;
    }

    public Cabin cabin() {
        return cabin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PathKey)) {
            return false;
        }

        final PathKey pathKey = (PathKey) obj;

        if (actions.size() != pathKey.actions.size()) {
            return false;
        }

        return actions.equals(pathKey.actions) && cabin.equals(pathKey.cabin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actions, cabin);
    }
}
