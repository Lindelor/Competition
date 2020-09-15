package net.thumbtack.school.competition.dto.request;

import net.thumbtack.school.competition.model.Direction;

import java.util.Set;

public class RegisterExpertDtoRequest {

    private String firstName;
    private String secondName;
    private Set<Direction> directionList;
    private String login;
    private String password;

    public RegisterExpertDtoRequest(String firstName, String secondName, Set<Direction> directionList, String login, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.directionList = directionList;
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

    public Set<Direction> getDirectionList() {
        return directionList;
    }

    public void setDirectionList(Set<Direction> directionList) {
        this.directionList = directionList;
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
}
