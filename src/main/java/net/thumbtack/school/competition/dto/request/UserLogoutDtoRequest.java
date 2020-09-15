package net.thumbtack.school.competition.dto.request;

public class UserLogoutDtoRequest {

    private String token;

    public UserLogoutDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
