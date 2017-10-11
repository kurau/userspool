package ru.kurau.exp;

import lombok.Setter;
import lombok.extern.java.Log;
import ru.kurau.exp.beans.Condition;
import ru.kurau.exp.beans.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.awaitility.Awaitility.await;
import static ru.kurau.exp.beans.Condition.condition;

/**
 * @author kurau (Yuri Kalinin)
 */
@Log
public class Accounts {

    private Map<User, Condition> users = new HashMap<>();

    public Accounts() {
        users.put(new User("l1", "p1"), condition().add("A").add("B").add("C"));
    }

    public User getAccountFor(Condition condition, String owner) {
        List<User> available = filter(condition);
        await().ignoreExceptions().pollInterval(1, SECONDS).atMost(5, SECONDS)
                .until(() -> lock(available, owner));
        return users.entrySet().stream()
                .filter(e -> e.getValue().getOwner().equals(owner)).findFirst().get().getKey();
    }

    private List<User> filter(final Condition condition) {
        List<User> available = users.entrySet().stream()
                .filter(e -> e.getValue().contains(condition))
                .map(Entry::getKey)
                .collect(toList());
        if (available.isEmpty()) {
            throw new IllegalStateException("There is't accounts for condition " + condition().getConditions());
        }
        return available;
    }

    private boolean lock(List<User> available, String owner) {
        synchronized (users) {
            log.info("Try to lock for " + owner);
            if (available.stream().anyMatch(a -> users.get(a).getOwner().isEmpty())) {
                User user = available.stream().filter(a -> users.get(a).getOwner().isEmpty()).findFirst().get();
                users.get(user).setOwner(owner);
                return true;
            }
            log.info("There aren't free accounts for " + owner);
            return false;
        }
    }

    public Accounts releaseAccount(User account) {
        synchronized (users) {
            users.get(account).setOwner("");
        }
        return this;
    }
}
