package ru.kurau.exp;

import com.google.gson.Gson;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Map<Integer, Boolean> locks = new HashMap<>();


    public SharedResources(List<T> resources) {
        this.resources = resources;
    }


    public void release(T resource) {
        synchronized (locks) {
            locks.replace(getId(resource), false);
        }
    }

    public T occupy(Predicate<T> predicate) {
        List<T> available = resources.stream()
                .filter(predicate)
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            throw new IllegalStateException("There isn't user by condition");
        }

        return await().ignoreExceptions()
                .pollInterval(1, SECONDS)
                .atMost(10, SECONDS)
                .until(() -> findFreeResource(available), notNullValue());
    }

    private T findFreeResource(List<T> available) {
        log.info("Search resource...");
        synchronized (locks) {
            Optional<T> freeResource = available.stream()
                    .peek(resource -> locks.putIfAbsent(getId(resource), false))
                    .filter(resource -> !locks.get(getId(resource)))
                    .findFirst();

            if (freeResource.isPresent()) {
                locks.replace(getId(freeResource.get()), true);
                return freeResource.get();
            }
        }
        return null;
    }

    private Integer getId(T resource) {
        return new Gson().toJson(resource).hashCode();
    }
}
