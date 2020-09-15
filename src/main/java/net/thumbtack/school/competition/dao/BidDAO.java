package net.thumbtack.school.competition.dao;

import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.service.exception.ServiceException;

public interface BidDAO {

    String addBid(String token, Bid bid) throws ServiceException;

    void removeBid(String id, String token);

}
