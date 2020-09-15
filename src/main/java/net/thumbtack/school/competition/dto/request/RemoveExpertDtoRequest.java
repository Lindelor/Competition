package net.thumbtack.school.competition.dto.request;

public class RemoveExpertDtoRequest {

    private String token;

    public RemoveExpertDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
