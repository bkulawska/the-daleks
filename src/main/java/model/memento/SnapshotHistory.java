package model.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class SnapshotHistory {
    private final int maxCapacity;
    private final Deque<GridSnapshot> backupDeque;

    public SnapshotHistory(int maxSnapshots){
        this.maxCapacity = maxSnapshots;
        this.backupDeque = new ArrayDeque<>(maxSnapshots);
    }

    public void addSnapshot(GridSnapshot gridSnapshot) {
        if(backupDeque.size() >= maxCapacity && maxCapacity > 0) {
            backupDeque.removeLast();
        }
        backupDeque.offerFirst(gridSnapshot);
    }

    public Optional<GridSnapshot> getMostRecentSnapshot() {
        return Optional.ofNullable(backupDeque.pollFirst());
    }
}
