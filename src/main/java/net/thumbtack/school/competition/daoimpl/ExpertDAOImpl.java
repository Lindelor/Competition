package net.thumbtack.school.competition.daoimpl;

import net.thumbtack.school.competition.dao.ExpertDAO;
import net.thumbtack.school.competition.database.Database;
import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.model.Direction;
import net.thumbtack.school.competition.model.Expert;
import net.thumbtack.school.competition.service.exception.ServiceException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class ExpertDAOImpl implements ExpertDAO {

    @Override
    public LinkedList<Bid> getFinalList(long totalMoney) {
        return Database.DATABASE.getFinalList(totalMoney);
    }

    @Override
    public String registerExpert(Expert expert) throws ServiceException {
        return Database.DATABASE.addExpert(expert);
    }

    @Override
    public void addGrade(String token, byte grade, String id) throws ServiceException {
        Database.DATABASE.addGrade(token, grade, id);
    }

    @Override
    public void removeGrade(String token, String id) throws ServiceException {
        Database.DATABASE.removeGrade(token, id);
    }

    @Override
    public void removeExpert(String token) throws ServiceException {
        Database.DATABASE.removeExpert(token);
    }

    @Override
    public void changeGrade(String token, byte grade, String id) throws ServiceException {
        Database.DATABASE.changeGrade(token, grade, id);
    }

    @Override
    public Map<String, Bid> getDirectionsBid (String token, Set<Direction> directionList) throws ServiceException {
        return Database.DATABASE.getDirectionsBid(token, directionList);
    }

    @Override
    public Map<String, Bid> getGradedMap(String token) throws ServiceException {
        return Database.DATABASE.getBidMapExpert(token);
    }

}
