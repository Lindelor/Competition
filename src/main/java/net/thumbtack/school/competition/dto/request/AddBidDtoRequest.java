package net.thumbtack.school.competition.dto.request;

import net.thumbtack.school.competition.model.Direction;

import java.util.Set;

public class AddBidDtoRequest {

    private String token;
    private String name;
    private String description;
    private Set<Direction> directionList;
    private long price;

    public AddBidDtoRequest(String token, String name, String description, Set<Direction> directionList, long price) {
        this.token = token;
        this.name = name;
        this.description = description;
        this.directionList = directionList;
        this.price = price;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Direction> getDirectionList() {
        return directionList;
    }

    public void setDirectionList(Set<Direction> directionList) {
        this.directionList = directionList;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
