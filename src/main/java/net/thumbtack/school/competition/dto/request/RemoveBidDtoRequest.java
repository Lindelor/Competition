package net.thumbtack.school.competition.dto.request;

public class RemoveBidDtoRequest {

    private String token;
    private String userToken;

    public RemoveBidDtoRequest(String token, String userToken) {
        this.token = token;
        this.userToken = userToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
