package net.thumbtack.school.competition.daoimpl;

import net.thumbtack.school.competition.dao.BidDAO;
import net.thumbtack.school.competition.database.Database;
import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.service.exception.ServiceException;

public class BidDAOImpl implements BidDAO {

    @Override
    public String addBid(String token, Bid bid) throws ServiceException {
        return Database.DATABASE.addBid(token, bid);
    }

    @Override
    public void removeBid(String id, String token) {
        Database.DATABASE.removeBid(id, token);
    }

}
