package net.thumbtack.school.competition.service.exception;

public enum ServiceErrorCode {

    WRONG_USERNAME("Incorrect or already taken Username"),
    WRONG_PASSWORD("Password must contains at least 6 characters"),
    WRONG_FIRSTNAME("Incorrect first name"),
    WRONG_SECONDNAME("Incorrect second name"),
    WRONG_COMPANYNAME("Incorrect company name"),
    ALREADY_TAKEN_LOGIN("This login is already taken"),
    EMPTY_DIRECTION_LIST("You should chose at least 1 direction"),
    ALREADY_REGISTER_USER("This user is already register"),
    WRONG_BID_NAME("Bid name should contains at least 5 characters"),
    WRONG_DESCRIPTION("Description should be at least 10 characters long"),
    WRONG_PRICE("Price should be positive value"),
    USER_NOT_REGISTER("We can't find user with this username"),
    WRONG_PASSWORD_LOGIN("Incorrect password, but you can try again"),
    WRONG_TOKEN("Wrong token"),
    WRONG_BID_ID("Wrong bid id"),
    WRONG_GRADE("Grade should be from 1 to 5"),
    ILLEGAL_USER("This expert doesnt grade this bid else"),
    ALL_OK("Operation is done correctly");



    private String errorString;

    ServiceErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return this.errorString;
    }


}
