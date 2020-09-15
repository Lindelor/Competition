package net.thumbtack.school.competition.service;

import com.google.gson.Gson;
import net.thumbtack.school.competition.dao.ExpertDAO;
import net.thumbtack.school.competition.daoimpl.ExpertDAOImpl;
import net.thumbtack.school.competition.dto.request.*;
import net.thumbtack.school.competition.dto.response.FinalListResponse;
import net.thumbtack.school.competition.dto.response.TokenResponse;
import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.model.Expert;
import net.thumbtack.school.competition.model.User;
import net.thumbtack.school.competition.service.exception.ServiceErrorCode;
import net.thumbtack.school.competition.service.exception.ServiceException;
import java.util.LinkedList;
import java.util.Map;

public class ExpertService {

    private final ExpertDAO expertDao = new ExpertDAOImpl();

    public String getFinalList(long totalMoney) {
        Gson gson = new Gson();
        try {
            LinkedList<Bid> bids = expertDao.getFinalList(totalMoney);
            LinkedList<FinalListResponse> finalList = new LinkedList<>();
            for (Bid bid : bids) {
                User user = bid.getOwner();
                finalList.add(new FinalListResponse(bid.getId(), bid.getName(), bid.getDescription(),
                        bid.getFields(), bid.getPrice(), bid.getAverage(), user.getCompanyName(),
                        user.getFirstName(), user.getSecondName(), user.getLogin()));
            }

            return gson.toJson(finalList, LinkedList.class);

        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }
    }

    public String registerExpert(String jsonString) {
        Gson gson = new Gson();
        try {
            RegisterExpertDtoRequest registerExpertDtoRequest = gson.fromJson(jsonString, RegisterExpertDtoRequest.class);
            validate(registerExpertDtoRequest);
            Expert expert = convertExpertFromDto(registerExpertDtoRequest);
            return gson.toJson(new TokenResponse(expertDao.registerExpert(expert)));
        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }

    }

    public String getGradedMap(String jsonString) {
        Gson gson = new Gson();

        try {
            GetGradedListRequest getGraded = gson.fromJson(jsonString, GetGradedListRequest.class);
            if (getGraded.getToken() == null || getGraded.getToken().equals("")) {
                return gson.toJson(new ServiceException(ServiceErrorCode.WRONG_TOKEN));
            }
            Map<String, Bid> map1 = expertDao.getGradedMap(getGraded.getToken());
            return gson.toJson(map1);
        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }
    }

    public String getBidsByDirection(String jsonString) {
        Gson gson = new Gson();

        try {
            GetBidByDirectionsRequest getBy = gson.fromJson(jsonString, GetBidByDirectionsRequest.class);
            validateGetByDirections(getBy);
            Map<String, Bid> map1 = expertDao.getDirectionsBid(getBy.getToken(), getBy.getDirectionsSet());
            return gson.toJson(map1);

        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }

    }

    public String changeGrade(String jsonString) {
        Gson gson = new Gson();
        try {
            ChangeGradeRequest change = gson.fromJson(jsonString, ChangeGradeRequest.class);
            if (validateChange(change)) {
                expertDao.changeGrade(change.getToken(), change.getGrade(), change.getId());
            }
            return gson.toJson(new ServiceException(ServiceErrorCode.ALL_OK));
        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }

    }

    public String addGrade(String jsonString) {
        Gson gson = new Gson();
        try {
            AddGradeDtoRequest request = gson.fromJson(jsonString, AddGradeDtoRequest.class);
            if (validateAdd(request)) {
                expertDao.addGrade(request.getExpertToken(), request.getGrade(), request.getBidID());
            }
            return gson.toJson(new ServiceException(ServiceErrorCode.ALL_OK));

        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }
    }

    public String removeGrade(String jsonString) {
        Gson gson = new Gson();
        try {
            RemoveGradeDtoRequest removeGrade = gson.fromJson(jsonString, RemoveGradeDtoRequest.class);

            if (validateRemove(removeGrade)) {
                expertDao.removeGrade(removeGrade.getExpertToken(), removeGrade.getBidID());
            }

            return gson.toJson(new ServiceException(ServiceErrorCode.ALL_OK));

        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }

    }

    public boolean removeExpert(String jsonString) {
        Gson gson = new Gson();
        RemoveExpertDtoRequest removeExpertDtoRequest = gson.fromJson(jsonString, RemoveExpertDtoRequest.class);
        try {
            expertDao.removeExpert(removeExpertDtoRequest.getToken());
        } catch (ServiceException e) {
            return false;
        }
        return true;
    }

    private void validate(RegisterExpertDtoRequest registerExpertDtoRequest) throws ServiceException {
        if (registerExpertDtoRequest.getFirstName() == null || registerExpertDtoRequest.getFirstName().length() < 2) {
            throw new ServiceException(ServiceErrorCode.WRONG_FIRSTNAME);
        } else if (registerExpertDtoRequest.getSecondName() == null || registerExpertDtoRequest.getSecondName().length() < 2) {
            throw new ServiceException(ServiceErrorCode.WRONG_SECONDNAME);
        } else if (registerExpertDtoRequest.getLogin() == null || registerExpertDtoRequest.getLogin().length() < 3 ) {
            throw new ServiceException(ServiceErrorCode.WRONG_USERNAME);
        } else if(registerExpertDtoRequest.getPassword() == null || registerExpertDtoRequest.getPassword().length() < 6) {
            throw new ServiceException(ServiceErrorCode.WRONG_PASSWORD);
        } else if (registerExpertDtoRequest.getDirectionList() == null || registerExpertDtoRequest.getDirectionList().isEmpty()) {
            throw new ServiceException(ServiceErrorCode.EMPTY_DIRECTION_LIST);
        }
    }

    private Expert convertExpertFromDto(RegisterExpertDtoRequest registerExpertDtoRequest) {

        return new Expert(registerExpertDtoRequest.getFirstName(), registerExpertDtoRequest.getSecondName(),
                                    registerExpertDtoRequest.getDirectionList(), registerExpertDtoRequest.getLogin(),
                                    registerExpertDtoRequest.getPassword());
    }

    private void validateGetByDirections(GetBidByDirectionsRequest getBy) throws ServiceException {
        if (getBy.getToken() == null || getBy.getToken().equals("")) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        }
    }

    private boolean validateAdd(AddGradeDtoRequest addGradeDtoRequest) throws ServiceException {
        if (addGradeDtoRequest.getExpertToken() == null || addGradeDtoRequest.getExpertToken().equals("")) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        } else if (addGradeDtoRequest.getBidID() == null || addGradeDtoRequest.getBidID().equals("")) {
            throw new ServiceException(ServiceErrorCode.WRONG_BID_ID);
        } else if (addGradeDtoRequest.getGrade() > 5 || addGradeDtoRequest.getGrade() < 1) {
            throw new ServiceException(ServiceErrorCode.WRONG_GRADE);
        }
        return true;
    }

    private boolean validateRemove(RemoveGradeDtoRequest removeGradeDtoRequest) throws ServiceException {

        if (removeGradeDtoRequest.getExpertToken() == null || removeGradeDtoRequest.getExpertToken().equals("")) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        } else if (removeGradeDtoRequest.getBidID() == null || removeGradeDtoRequest.getBidID().equals("")) {
            throw new ServiceException(ServiceErrorCode.WRONG_BID_ID);
        }

        return true;
    }

    private boolean validateChange(ChangeGradeRequest change) throws ServiceException {
        if (change.getToken() == null || change.getToken().equals("")) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        } else if (change.getId() == null || change.getId().equals("")) {
            throw new ServiceException(ServiceErrorCode.WRONG_BID_ID);
        } else if (change.getGrade() < 1 || change.getGrade() > 5) {
            throw new ServiceException(ServiceErrorCode.WRONG_GRADE);
        }
        return true;
    }
}
