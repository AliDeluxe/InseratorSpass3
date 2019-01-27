package de.unidue.inf.is.domain;

public final class User {

    private String username;
    private String firstname;
    private String lastname;


    public User() {
    }


    public User(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;

    }


    public String getFirstname() {
        return firstname;
    }


    public String getLastname() {
        return lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }


}