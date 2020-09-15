package net.thumbtack.school.competition.dao;

import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.model.Direction;
import net.thumbtack.school.competition.model.Expert;
import net.thumbtack.school.competition.service.exception.ServiceException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public interface ExpertDAO {

    void addGrade(String token, byte grade, String id) throws ServiceException;

    void removeGrade(String token, String id) throws ServiceException;

    String registerExpert(Expert expert) throws ServiceException;

    void removeExpert(String token) throws ServiceException;

    void changeGrade(String token, byte grade, String id) throws ServiceException;

    Map<String, Bid> getDirectionsBid (String token, Set<Direction> directionList) throws ServiceException;

    Map<String, Bid> getGradedMap(String token) throws ServiceException;

    LinkedList<Bid> getFinalList(long totalMoney) throws ServiceException;

}
