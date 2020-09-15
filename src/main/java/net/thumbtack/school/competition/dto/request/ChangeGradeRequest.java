package net.thumbtack.school.competition.dto.request;

public class ChangeGradeRequest {

    String token;
    Byte grade;
    String id;

    public ChangeGradeRequest(String token, Byte grade, String id) {
        this.token = token;
        this.grade = grade;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Byte getGrade() {
        return grade;
    }

    public void setGrade(Byte grade) {
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
