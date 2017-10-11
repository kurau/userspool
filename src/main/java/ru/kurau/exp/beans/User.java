package ru.kurau.exp.beans;


import lombok.Getter;

/**
 * @author kurau (Yuri Kalinin)
 */
@Getter
public class User {

    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final User other = (User) obj;
        if (login.isEmpty()) {
            if (other.getLogin().isEmpty())
                return false;
        } else if (!login.equals(other.getLogin()))
            return false;
        return true;
    }

}
