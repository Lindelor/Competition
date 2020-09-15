package net.thumbtack.school.competition.service;

import com.google.gson.Gson;
import net.thumbtack.school.competition.dao.BidDAO;
import net.thumbtack.school.competition.daoimpl.BidDAOImpl;
import net.thumbtack.school.competition.dto.request.*;
import net.thumbtack.school.competition.dto.response.TokenResponse;
import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.service.exception.ServiceErrorCode;
import net.thumbtack.school.competition.service.exception.ServiceException;

public class BidService {

    private final BidDAO bidDao = new BidDAOImpl();

    public String addBid(String jsonString) {
        Gson gson = new Gson();
        try {
            AddBidDtoRequest addBidDtoRequest = gson.fromJson(jsonString, AddBidDtoRequest.class);
            validate(addBidDtoRequest);
            Bid bid = convertBidFromDto(addBidDtoRequest);
            return gson.toJson(new TokenResponse(bidDao.addBid(addBidDtoRequest.getToken(), bid)));
        } catch (ServiceException ex) {
            return gson.toJson(ex);
        }

    }

    public void removeBid(String jsonString) {
        Gson gson = new Gson();
        RemoveBidDtoRequest removeBidDtoRequest = gson.fromJson(jsonString, RemoveBidDtoRequest.class);
        bidDao.removeBid(removeBidDtoRequest.getToken(), removeBidDtoRequest.getUserToken());
    }

    private void validate(AddBidDtoRequest addBidDtoRequest) throws ServiceException {
        if (addBidDtoRequest.getName() == null || addBidDtoRequest.getName().length() < 5) {
            throw new ServiceException(ServiceErrorCode.WRONG_BID_NAME);
        } else if (addBidDtoRequest.getDescription() == null || addBidDtoRequest.getDescription().length() < 10) {
            throw new ServiceException(ServiceErrorCode.WRONG_DESCRIPTION);
        } else if (addBidDtoRequest.getDirectionList().isEmpty()) {
            throw new ServiceException(ServiceErrorCode.EMPTY_DIRECTION_LIST);
        }else if (addBidDtoRequest.getPrice() <= 0) {
            throw new ServiceException(ServiceErrorCode.WRONG_PRICE);
        }
    }

    private Bid convertBidFromDto(AddBidDtoRequest addBidDtoRequest) {

        return new Bid(addBidDtoRequest.getName(), addBidDtoRequest.getDescription(),
                        addBidDtoRequest.getDirectionList(), addBidDtoRequest.getPrice());
    }
}
