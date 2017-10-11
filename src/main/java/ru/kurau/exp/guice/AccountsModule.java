package ru.kurau.exp.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import ru.kurau.exp.Accounts;

/**
 * @author kurau (Yuri Kalinin)
 */
public class AccountsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Accounts.class).in(Singleton.class);
    }
}
