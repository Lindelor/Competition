package net.thumbtack.school.competition.dto.request;

public class RemoveGradeDtoRequest {

    String expertToken;
    String bidID;

    public RemoveGradeDtoRequest(String expertToken, String bidID) {
        this.expertToken = expertToken;
        this.bidID = bidID;
    }

    public String getExpertToken() {
        return expertToken;
    }

    public void setExpertToken(String expertToken) {
        this.expertToken = expertToken;
    }

    public String getBidID() {
        return bidID;
    }

    public void setBidID(String bidID) {
        this.bidID = bidID;
    }
}
