package ru.kurau.exp;

import org.awaitility.core.ConditionTimeoutException;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author kurau (Yuri Kalinin)
 */
public class SharedResourcesTest {

    private static final String CONDITION = "1";
    private static final String NOT_EXIST = "not exist";

    private SharedResources<User> sharedResources;

    private User firstUser;

    @Before
    public void before() {
        sharedResources = new SharedResources<>(asList(
                new User("l1", "p1", "c1"),
                new User("l2", "p2", "c2"),
                new User("l3", "p3", "c3")
        ));
    }

    @Test
    public void shouldGetUserByCondition() {
        firstUser = sharedResources.occupy(user -> user.getCondition().contains(CONDITION));
        assertThat("Should get user", firstUser.getLogin(), is("l1"));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotGetNonExistUser() {
        firstUser = sharedResources.occupy(user -> user.getCondition().contains(NOT_EXIST));
    }

    @Test(expected = ConditionTimeoutException.class)
    public void shouldLockUser() {
        sharedResources.occupy(user -> user.getCondition().contains(CONDITION));
        sharedResources.occupy(user -> user.getCondition().contains(CONDITION));
    }

    @Test
    public void shouldUnlockUser() {
        firstUser = sharedResources.occupy(user -> user.getCondition().contains(CONDITION));
        sharedResources.release(firstUser);
        firstUser = sharedResources.occupy(user -> user.getCondition().contains(CONDITION));
        assertThat("Should get user", firstUser.getLogin(), is("l1"));
    }

}
