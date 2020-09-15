package net.thumbtack.school.competition.model;

import java.util.Objects;

public class User extends Person{

    private String companyName;

    public User(String companyName, String firstName, String secondName, String login, String password) {
        super(firstName, secondName, login, password);
        setCompanyName(companyName);

    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(companyName, user.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), companyName);
    }
}
