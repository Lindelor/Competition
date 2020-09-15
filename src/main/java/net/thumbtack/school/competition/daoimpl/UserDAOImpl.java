package net.thumbtack.school.competition.daoimpl;

import net.thumbtack.school.competition.dao.UserDAO;
import net.thumbtack.school.competition.database.Database;
import net.thumbtack.school.competition.model.User;
import net.thumbtack.school.competition.service.exception.ServiceException;

public class UserDAOImpl implements UserDAO {

    @Override
    public void logout(String token) {
        Database.DATABASE.logout(token);
    }

    @Override
    public void removeUser(String token) throws ServiceException {
        Database.DATABASE.removeUser(token);
    }

    @Override
    public String login(String username, String password) throws ServiceException {
        return Database.DATABASE.login(username, password);
    }

    @Override
    public String registerUser(User user) throws ServiceException {
        return Database.DATABASE.addUser(user);
    }

}
