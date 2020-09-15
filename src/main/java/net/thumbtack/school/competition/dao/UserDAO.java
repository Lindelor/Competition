package net.thumbtack.school.competition.dao;

import net.thumbtack.school.competition.model.User;
import net.thumbtack.school.competition.service.exception.ServiceException;

public interface UserDAO {

    void logout(String token);

    String registerUser(User user) throws ServiceException;

    void removeUser(String token) throws ServiceException;

    String login(String username, String password) throws ServiceException;

}
