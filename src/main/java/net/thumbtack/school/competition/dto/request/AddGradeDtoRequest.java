package net.thumbtack.school.competition.dto.request;

public class AddGradeDtoRequest {

    String expertToken;
    byte grade;
    String bidID;

    public AddGradeDtoRequest(String expertToken, byte grade, String bidID) {
        this.expertToken = expertToken;
        this.grade = grade;
        this.bidID = bidID;
    }

    public String getExpertToken() {
        return expertToken;
    }

    public void setExpertToken(String expertToken) {
        this.expertToken = expertToken;
    }

    public byte getGrade() {
        return grade;
    }

    public void setGrade(byte grade) {
        this.grade = grade;
    }

    public String getBidID() {
        return bidID;
    }

    public void setBidID(String bidID) {
        this.bidID = bidID;
    }
}
