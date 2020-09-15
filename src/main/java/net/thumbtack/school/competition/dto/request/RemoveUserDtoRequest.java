package net.thumbtack.school.competition.dto.request;

public class RemoveUserDtoRequest {

    private String token;

    public RemoveUserDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
