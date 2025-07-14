package models;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public static User getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@ya.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }
}
