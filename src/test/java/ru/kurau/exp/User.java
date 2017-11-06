package ru.kurau.exp;


import lombok.Getter;

/**
 * @author kurau (Yuri Kalinin)
 */
@Getter
public class User {

    private String login;
    private String password;
    private String condition;

    public User(String login, String password, String condition) {
        this.login = login;
        this.password = password;
        this.condition = condition;
    }

}
