package ru.kurau.exp;

/**
 * @author kurau (Yuri Kalinin)
 */
public interface Locks {

    void lock(String id);

    boolean isLocked(String id);

    default boolean isUnlocked(String id) {
        return !isLocked(id);
    }

    void unlock(String id);

}
