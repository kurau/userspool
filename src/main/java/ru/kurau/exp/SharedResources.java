package ru.kurau.exp;

import com.google.gson.Gson;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author kurau (Yuri Kalinin)
 */
@Log
public class SharedResources<T> {

    private List<T> resources;

    private final Locks locks;

    public SharedResources(List<T> resources) {
        this(resources, new HashMapLocks());
    }

    public SharedResources(List<T> resources, Locks locks) {
        this.resources = resources;
        this.locks = locks;
    }


    public void release(T resource) {
        synchronized (locks) {
            locks.unlock(getId(resource));
        }
    }

    public T occupy(Predicate<T> predicate) {
        List<T> available = resources.stream()
                .filter(predicate)
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            throw new IllegalStateException("There isn't user by condition");
        }

        return await().ignoreException(IllegalStateException.class)
                .pollInterval(1, SECONDS)
                .atMost(10, SECONDS)
                .until(() -> findFreeResource(available), notNullValue());
    }

    private T findFreeResource(List<T> available) {
        log.info("Search resource...");
        synchronized (locks) {
            Optional<T> freeResource = available.stream()
                    .filter(resource -> locks.isUnlocked(getId(resource)))
                    .findFirst();

            if (freeResource.isPresent()) {
                locks.lock(getId(freeResource.get()));
                return freeResource.get();
            }
        }
        throw new IllegalStateException("Cant find free resources");
    }

    private String getId(T resource) {
        return new Gson().toJson(resource).hashCode() + "";
    }
}
