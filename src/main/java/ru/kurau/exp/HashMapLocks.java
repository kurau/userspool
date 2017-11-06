package ru.kurau.exp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kurau (Yuri Kalinin)
 */
public class HashMapLocks implements Locks {

    private final Map<String, Boolean> locks;

    public HashMapLocks() {
        this.locks = new HashMap<>();
    }

    @Override
    public void lock(String id) {
        locks.put(id, true);
    }

    @Override
    public boolean isLocked(String id) {
        return locks.containsKey(id) && locks.get(id);
    }

    @Override
    public void unlock(String id) {
        locks.put(id, false);
    }

}
