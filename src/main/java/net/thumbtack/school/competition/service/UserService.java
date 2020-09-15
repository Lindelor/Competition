package net.thumbtack.school.competition.service;

import com.google.gson.Gson;
import net.thumbtack.school.competition.dao.UserDAO;
import net.thumbtack.school.competition.daoimpl.UserDAOImpl;
import net.thumbtack.school.competition.dto.request.RegisterUserDtoRequest;
import net.thumbtack.school.competition.dto.request.RemoveUserDtoRequest;
import net.thumbtack.school.competition.dto.request.UserLoginDtoRequest;
import net.thumbtack.school.competition.dto.request.UserLogoutDtoRequest;
import net.thumbtack.school.competition.dto.response.TokenResponse;
import net.thumbtack.school.competition.model.User;
import net.thumbtack.school.competition.service.exception.ServiceErrorCode;
import net.thumbtack.school.competition.service.exception.ServiceException;


public class UserService {

    private final UserDAO userDao = new UserDAOImpl();

    public String registerUser(String jsonString) {
        Gson gson = new Gson();
        try {
            RegisterUserDtoRequest registerUserDtoRequest = gson.fromJson(jsonString, RegisterUserDtoRequest.class);
            validate(registerUserDtoRequest);
            User user = convertFromDto(registerUserDtoRequest);
            return gson.toJson(new TokenResponse(userDao.registerUser(user)));
        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }
    }

    public String loginUser(String jsonString) {
        Gson gson = new Gson();
        try {
            User user = convertFromDtoLogin(gson.fromJson(jsonString, UserLoginDtoRequest.class));
            return gson.toJson(new TokenResponse(userDao.login(user.getLogin(), user.getPassword())));
        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }
    }

    public void logout(String jsonString) {
        Gson gson = new Gson();
        UserLogoutDtoRequest userLogoutDtoRequest = gson.fromJson(jsonString, UserLogoutDtoRequest.class);
        String token = userLogoutDtoRequest.getToken();
        userDao.logout(token);
    }

    public boolean removeUser(String jsonString) {
        Gson gson = new Gson();
        RemoveUserDtoRequest removeUserDtoRequest = gson.fromJson(jsonString, RemoveUserDtoRequest.class);
        try {
            userDao.removeUser(removeUserDtoRequest.getToken());
        } catch (ServiceException e) {
            return false;
        }
        return true;
    }

    private void validate(RegisterUserDtoRequest registerUserDtoRequest) throws ServiceException {
        if (registerUserDtoRequest.getCompanyName() == null || registerUserDtoRequest.getCompanyName().length() < 3) {
            throw new ServiceException(ServiceErrorCode.WRONG_COMPANYNAME);
        } else if (registerUserDtoRequest.getFirstName() == null || registerUserDtoRequest.getFirstName().length() < 2) {
            throw new ServiceException(ServiceErrorCode.WRONG_FIRSTNAME);
        } else if (registerUserDtoRequest.getSecondName() == null || registerUserDtoRequest.getSecondName().length() < 2) {
            throw new ServiceException(ServiceErrorCode.WRONG_SECONDNAME);
        } else if (registerUserDtoRequest.getLogin() == null || registerUserDtoRequest.getLogin().length() < 3 ) {
            throw new ServiceException(ServiceErrorCode.WRONG_USERNAME);
        } else if(registerUserDtoRequest.getPassword() == null || registerUserDtoRequest.getPassword().length() < 6) {
            throw new ServiceException(ServiceErrorCode.WRONG_PASSWORD);
        }
    }

    private User convertFromDto(RegisterUserDtoRequest registerUserDtoRequest) {

        return new User(registerUserDtoRequest.getCompanyName(),
                registerUserDtoRequest.getFirstName(), registerUserDtoRequest.getSecondName(),
                registerUserDtoRequest.getLogin(), registerUserDtoRequest.getPassword());
    }

    private User convertFromDtoLogin(UserLoginDtoRequest userLoginDtoRequest) {
        return new User(null, null, null,
                userLoginDtoRequest.getLogin(), userLoginDtoRequest.getPassword());
    }


}
