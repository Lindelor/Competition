package net.thumbtack.school.competition.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Person {

    private String firstName;
    private String secondName;
    private String login;
    private String password;
    private String token;
    private Set<String> bidIdSet = new HashSet<>();

    public Person(String firstName, String secondName, String login, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getBidIdSet() {
        return bidIdSet;
    }

    public void setBidIdSet(Set<String> bidIdSet) {
        this.bidIdSet = bidIdSet;
    }

    public void addBidId(String id) {
        this.bidIdSet.add(id);
    }

    public void removeBidId(String id) {
        this.bidIdSet.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(secondName, person.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, secondName, login);
    }
}
