package ru.kurau.exp;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.kurau.exp.beans.User;
import ru.kurau.exp.guice.AccountsModule;

import static ru.kurau.exp.beans.Condition.condition;

/**
 * @author kurau (Yuri Kalinin)
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(AccountsModule.class)
public class ATest {

    @Inject
    public Accounts accounts;

    @Test
    public void fastTest() throws InterruptedException {
        User user = accounts.getAccountFor(condition().add("A"), "1");
        System.out.println(user);
        Thread.sleep(1000);
        accounts.releaseAccount(user);
    }

    @Test
    public void slowTest() throws InterruptedException {
        Thread.sleep(500);
        User user = accounts.getAccountFor(condition().add("B"), "2");
        Thread.sleep(2000);
        accounts.releaseAccount(user);
    }
}
