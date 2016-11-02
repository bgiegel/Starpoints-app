package fr.softeam.starpointsapp.util;

import fr.softeam.starpointsapp.domain.User;

public class UserBuilder {
    public static final String DEFAULT_PASSWORD = "$2a$10$qMNi7ijFejR1n8yP0750IuDnrMeQ.p67PjiHuEZgV0VTW939S5Zbq";
    public static final String DEFAULT_LOGIN = "defaultLogin";

    private User user;

    public UserBuilder() {
        user = new User(DEFAULT_LOGIN, DEFAULT_PASSWORD);
    }

    public UserBuilder(String login) {
        if (login != null) {
            user = new User(login, DEFAULT_PASSWORD);
        }else {
            user = new User(DEFAULT_LOGIN, DEFAULT_PASSWORD);
        }
    }

    public User build(){
        return user;
    }
}
