package net.thumbtack.school.competition.dto.request;

public class RegisterUserDtoRequest {

    private String companyName;
    private String firstName;
    private String secondName;
    private String login;
    private String password;

    public RegisterUserDtoRequest(String companyName, String firstName, String secondName,
                                                            String login, String password) {
        this.companyName = companyName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = login;
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
