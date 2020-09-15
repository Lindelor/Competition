package net.thumbtack.school.competition.dto.request;

import net.thumbtack.school.competition.model.Direction;

import java.util.Set;

public class GetBidByDirectionsRequest {

    String token;
    Set<Direction> directionSet;

    public GetBidByDirectionsRequest(String token, Set<Direction> directionSet) {
        this.token = token;
        this.directionSet = directionSet;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Direction> getDirectionsSet() {
        return directionSet;
    }

    public void setDirectionsSet(Set<Direction> directionSet) {
        this.directionSet = directionSet;
    }
}
