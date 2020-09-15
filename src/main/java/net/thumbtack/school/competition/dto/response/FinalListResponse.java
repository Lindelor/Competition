package net.thumbtack.school.competition.dto.response;

import net.thumbtack.school.competition.model.Direction;
import java.util.Objects;
import java.util.Set;

public class FinalListResponse {

    private String bidID;
    private String bidName;
    private String bidDescription;
    private Set<Direction> ds;
    private long price;
    private float averageGrade;
    private String companyName;
    private String firstName;
    private String secondName;
    private String login;

    public FinalListResponse(String bidID, String bidName, String bidDescription, Set<Direction> ds, long price, float averageGrade, String companyName, String firstName, String secondName, String login) {
        this.bidID = bidID;
        this.bidName = bidName;
        this.bidDescription = bidDescription;
        this.ds = ds;
        this.price = price;
        this.averageGrade = averageGrade;
        this.companyName = companyName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = login;
    }

    public String getBidID() {
        return bidID;
    }

    public void setBidID(String bidID) {
        this.bidID = bidID;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public String getBidDescription() {
        return bidDescription;
    }

    public void setBidDescription(String bidDescription) {
        this.bidDescription = bidDescription;
    }

    public Set<Direction> getDs() {
        return ds;
    }

    public void setDs(Set<Direction> ds) {
        this.ds = ds;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public float getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(float averageGrade) {
        this.averageGrade = averageGrade;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalListResponse that = (FinalListResponse) o;
        return price == that.price &&
                Float.compare(that.averageGrade, averageGrade) == 0 &&
                bidID.equals(that.bidID) &&
                bidName.equals(that.bidName) &&
                bidDescription.equals(that.bidDescription) &&
                ds.equals(that.ds) &&
                companyName.equals(that.companyName) &&
                firstName.equals(that.firstName) &&
                secondName.equals(that.secondName) &&
                login.equals(that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bidID, bidName, bidDescription, ds, price, averageGrade, companyName, firstName, secondName, login);
    }
}
