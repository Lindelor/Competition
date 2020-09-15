package net.thumbtack.school.competition.dto.request;

public class GetGradedListRequest {

    String token;

    public GetGradedListRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
