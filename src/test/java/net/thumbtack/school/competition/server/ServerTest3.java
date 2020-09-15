package net.thumbtack.school.competition.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.competition.database.Database;
import net.thumbtack.school.competition.dto.request.*;
import net.thumbtack.school.competition.dto.response.FinalListResponse;
import net.thumbtack.school.competition.dto.response.TokenResponse;
import net.thumbtack.school.competition.model.Direction;
import static org.junit.jupiter.api.Assertions.*;
import net.thumbtack.school.competition.service.exception.ServiceErrorCode;
import net.thumbtack.school.competition.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

class ServerTest3 {

    @Test
    public void finalListTest() {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        Set<Direction> ds1 = new HashSet<>();
        ds1.add(Direction.LITERATURE);
        ds1.add(Direction.CHEMISTRY);

        Set<Direction> ds2 = new HashSet<>();
        ds2.add(Direction.HISTORY);
        ds2.add(Direction.INFORMATICS);

        Set<Direction> ds3 = new HashSet<>();
        ds3.add(Direction.LITERATURE);

        String userToken1 = userCreator("ABCD", "Ivan", "Ivanov", "Ivanov10", "123456");
        String userToken2 = userCreator("qwert", "Nikita", "Hrushev", "Hrushev10", "123456");
        String userToken3 = userCreator("Socio", "Pitirim", "Sorokin", "Sorokin10", "123456");
        String expertToken1 = expertCreator("Oleg", "Tinkov", ds1, "Tinkov1", "123456");
        String expertToken2 = expertCreator("Richard", "Hakimov", ds2, "Hakimov", "123456");
        String expertToken3 = expertCreator("Misha", "Lermontov", ds3, "Lermontov1", "123456");

        String bidToken11 = bidCreator(userToken1, "Bid11", "Dengi dengi", ds1, 100000);
        String bidToken12 = bidCreator(userToken1, "Bid12", "Dengi dengi dengi", ds2, 50000);
        String bidToken21 = bidCreator(userToken2, "Bid21", "Drugie dengi", ds1, 100000);
        String bidToken22 = bidCreator(userToken2, "Bid22", "Drugie dengi 2", ds2, 60000);
        String bidToken31 = bidCreator(userToken3, "Bid31", "Eshe denegdfs", ds1, 10000);
        String bidToken32 = bidCreator(userToken3, "Bid32", "qwertyui jgif", ds3, 400000);
        String bidToken33 = bidCreator(userToken3, "Bid33", "oiuytrewodplfjgd", ds3, 30000);
        String bidToken34 = bidCreator(userToken3, "Bid34", "Low gradesdsf", ds2, 1);

        createGrade(expertToken1, 5, bidToken11);
        createGrade(expertToken1, 5, bidToken21);
        createGrade(expertToken1, 5, bidToken31);
        createGrade(expertToken1, 4, bidToken32);
        createGrade(expertToken1, 4, bidToken33);

        createGrade(expertToken2, 3, bidToken12);
        createGrade(expertToken2, 3, bidToken22);
        createGrade(expertToken2, 1, bidToken34);

        createGrade(expertToken3, 4, bidToken11);
        createGrade(expertToken3, 4, bidToken21);
        createGrade(expertToken3, 2, bidToken31);
        createGrade(expertToken3, 3, bidToken32);
        createGrade(expertToken3, 5, bidToken33);

        Type type = new TypeToken<LinkedList<FinalListResponse>>(){}.getType();
        LinkedList<FinalListResponse> list1 = gson.fromJson(server.expertService.getFinalList(200000), type);
        LinkedList<FinalListResponse> list2 = gson.fromJson(server.expertService.getFinalList(200000), type);
        List<String> bids2 = new LinkedList<>();
        bids2.add(bidToken11);
        bids2.add(bidToken21);
        bids2.add(bidToken33);
        bids2.add(bidToken31);
        bids2.add(bidToken12);

        assertTrue(checkAllInside(list2, bids2));
        assertEquals(4, list1.size());
        assertTrue(checkRandom(200000));
        list2 = gson.fromJson(server.expertService.getFinalList(1), type);
        assertEquals(0, list2.size());
        assertEquals(8, Database.DATABASE.getIdToBid().size());

        server.userService.logout(gson.toJson(new UserLogoutDtoRequest(userToken1)));
        String test1 = server.userService.loginUser(gson.toJson(new UserLoginDtoRequest("Ivanov10", "654321")));
        assertEquals(gson.toJson(new ServiceException(ServiceErrorCode.WRONG_PASSWORD_LOGIN)), test1);
        server.userService.loginUser(gson.toJson(new UserLoginDtoRequest("Ivanov10", "123456")));
        assertEquals(3, Database.DATABASE.getTokenToUser().size());

        try {
            server.stopServer("Finaltest.txt");
            server.startServer(null);
            assertEquals(0, Database.DATABASE.getIdToBid().size());
            server.startServer("Finaltest.txt");
            assertEquals(8, Database.DATABASE.getIdToBid().size());
            assertEquals(4, Database.DATABASE.getBidDirectionMap().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkRandom(long price) {
        Gson gson = new Gson();
        Server server = new Server();
        Type type = new TypeToken<LinkedList<FinalListResponse>>(){}.getType();
        LinkedList<FinalListResponse> list1 = gson.fromJson(server.expertService.getFinalList(price), type);
        LinkedList<FinalListResponse> list2 = gson.fromJson(server.expertService.getFinalList(price), type);

        for (int i = 0; i < 100; i++) {
            if (!list1.equals(list2)) {
                return true;
            }
            list1 = gson.fromJson(server.expertService.getFinalList(price), type);
        }
        return false;
    }

    private boolean checkAllInside(LinkedList<FinalListResponse> list, List<String> bids) {
        for (String id : bids) {
            list.removeIf(item -> item.getBidID().equals(id));
        }
        return list.size() == 0;
    }

    private void createGrade(String expertToken, int grade, String bidID) {
        Server server = new Server();
        Gson gson = new Gson();
        AddGradeDtoRequest addGrade = new AddGradeDtoRequest(expertToken, (byte)grade, bidID);
        server.expertService.addGrade(gson.toJson(addGrade));
    }

    private String bidCreator(String token, String name, String description, Set<Direction> ds, long price) {
        Server server = new Server();
        Gson gson = new Gson();
        AddBidDtoRequest addBid = new AddBidDtoRequest(token, name, description, ds, price);
        String id = server.bidService.addBid(gson.toJson(addBid));

        return gson.fromJson(id, TokenResponse.class).getToken();

    }

    private String userCreator(String company, String first, String second, String login, String password) {
        Server server = new Server();
        RegisterUserDtoRequest registerUserDtoRequest = new RegisterUserDtoRequest(company, first, second, login, password);
        Gson gson = new Gson();

        String token = server.userService.registerUser(gson.toJson(registerUserDtoRequest));
        return gson.fromJson(token, TokenResponse.class).getToken();

    }

    private String expertCreator(String first, String second, Set<Direction> ds, String login, String password) {
        Server server =  new Server();
        Gson gson = new Gson();

        RegisterExpertDtoRequest registerExpertDtoRequest = new RegisterExpertDtoRequest(first, second, ds, login, password);
        String token = server.expertService.registerExpert(gson.toJson(registerExpertDtoRequest));
        return gson.fromJson(token, TokenResponse.class).getToken();
    }

}