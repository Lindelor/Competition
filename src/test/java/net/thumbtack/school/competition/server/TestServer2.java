package net.thumbtack.school.competition.server;

import com.google.gson.Gson;
import net.thumbtack.school.competition.database.Database;
import net.thumbtack.school.competition.dto.request.*;
import net.thumbtack.school.competition.dto.response.TokenResponse;
import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.model.Direction;
import net.thumbtack.school.competition.model.Expert;
import net.thumbtack.school.competition.service.exception.ServiceErrorCode;
import net.thumbtack.school.competition.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TestServer2 {

    @Test
    public void testRegister() {
        Server server = new Server();
        server.startServer("");

        Set<Direction> ds = new HashSet<>();
        ds.add(Direction.LITERATURE);
        ds.add(Direction.GEOMETRY);
        RegisterExpertDtoRequest registerExpertDtoRequest = new RegisterExpertDtoRequest("Leonid",
                "Ivanov", ds, "Lenya203", "111111");
        RegisterExpertDtoRequest registerExpertDtoRequest1 = new RegisterExpertDtoRequest("Leonid",
                "Ivanov", ds, "Lenya203", "111");
        RegisterExpertDtoRequest registerExpertDtoRequest2 = new RegisterExpertDtoRequest("Leonid",
                "Ivanov", null, "Lenya203", "111111");

        RegisterUserDtoRequest registerUserDtoRequest = new RegisterUserDtoRequest("Roga i",
                "Oleg", "Kuznetsov", "Oleg12", "123456");
        RegisterUserDtoRequest registerUserDtoRequest1 = new RegisterUserDtoRequest("Roga i",
                "Oleg", "Kuznetsov", "Oleg12", "123456");
        RegisterUserDtoRequest registerUserDtoRequest2 = new RegisterUserDtoRequest("Firma",
                "Kirill", "Yarmolin", "1", "93749273");
        RegisterUserDtoRequest registerUserDtoRequest3 = new RegisterUserDtoRequest("Firma",
                "Oleg", "Kuznet", "Oleg12", "123456");
        Gson gs1 = new Gson();
        String s1 = gs1.toJson(registerExpertDtoRequest);
        String s2 = gs1.toJson(registerExpertDtoRequest1);
        String s3 = gs1.toJson(registerExpertDtoRequest2);
        String s4 = gs1.toJson(registerUserDtoRequest);
        String s5 = gs1.toJson(registerUserDtoRequest1);
        String s6 = gs1.toJson(registerUserDtoRequest2);
        String s7 = gs1.toJson(registerUserDtoRequest3);
        String shortPass = gs1.toJson(new ServiceException(ServiceErrorCode.WRONG_PASSWORD));
        String emptyList = gs1.toJson(new ServiceException(ServiceErrorCode.EMPTY_DIRECTION_LIST));
        String alreadyTakenLogin = gs1.toJson(new ServiceException(ServiceErrorCode.ALREADY_TAKEN_LOGIN));
        String wrongLogin = gs1.toJson(new ServiceException(ServiceErrorCode.WRONG_USERNAME));
        String alreadyReg = gs1.toJson(new ServiceException(ServiceErrorCode.ALREADY_REGISTER_USER));

        String t1 = server.expertService.registerExpert(s1);
        String t2 = server.expertService.registerExpert(s2);
        String t3 = server.expertService.registerExpert(s3);
        String t4 = server.userService.registerUser(s4);
        String t5 = server.userService.registerUser(s5);
        String t6 = server.userService.registerUser(s6);
        String t7 = server.userService.registerUser(s7);

        assertTrue(Database.DATABASE.getTokenToExpert().containsKey(gs1.fromJson(t1, TokenResponse.class).getToken()));
        assertEquals(t2, shortPass);
        assertEquals(t3, emptyList);
        assertTrue(Database.DATABASE.getTokenToUser().containsKey(gs1.fromJson(t4, TokenResponse.class).getToken()));
        assertEquals(t5, alreadyReg);
        assertEquals(t6, wrongLogin);
        assertEquals(t7, alreadyTakenLogin);
        assertEquals(1, Database.DATABASE.getTokenToExpert().size());
        assertEquals(1, Database.DATABASE.getTokenToUser().size());
    }

    @Test
    public void testLoginAndLogout() {
        Server server = new Server();
        server.startServer("");
        Gson gson = new Gson();
        Set<Direction> ds = new HashSet<>();
        ds.add(Direction.LITERATURE);
        ds.add(Direction.GEOMETRY);

        RegisterUserDtoRequest user = new RegisterUserDtoRequest("kkkkkk", "Oleg", "Leonov",
                "Olegg123", "123456");
        RegisterExpertDtoRequest expert = new RegisterExpertDtoRequest("Grisha", "Dukalis",
                ds, "Duka940", "123456");
        ds.add(Direction.HISTORY);
        RegisterUserDtoRequest user2 = new RegisterUserDtoRequest("oooooo", "Igor", "Karpov",
                "Igor324", "111");
        RegisterExpertDtoRequest expert2 = new RegisterExpertDtoRequest("Ivan", "Gusev",
                ds, "Duka942", "123456");

        String token1 = server.userService.registerUser(gson.toJson(user));
        String token2 = server.userService.registerUser(gson.toJson(user2));
        String token3 = server.expertService.registerExpert(gson.toJson(expert));
        String token4 = server.expertService.registerExpert(gson.toJson(expert2));
        token1 = gson.fromJson(token1, TokenResponse.class).getToken();
        AddBidDtoRequest bid1 = new AddBidDtoRequest(token1,"bid1request", "Dengi dengi",
                ds, 30000);
        AddBidDtoRequest bid2 = new AddBidDtoRequest(token1,"Bid2request", "dengi dengi",
                ds, 25000);
        AddBidDtoRequest bid3 = new AddBidDtoRequest(token1, null, "kdkdkdkdk",
                ds, 12000);
        AddBidDtoRequest bid4 = new AddBidDtoRequest(token1, "Bid3bid3", "kdsikds",
                ds, -20000);

        String tokenbid1 = server.bidService.addBid(gson.toJson(bid1));
        server.bidService.addBid(gson.toJson(bid2));
        server.bidService.addBid(gson.toJson(bid3));
        server.bidService.addBid(gson.toJson(bid4));
        RemoveBidDtoRequest removeBidDtoRequest =
                new RemoveBidDtoRequest(gson.fromJson(tokenbid1, TokenResponse.class).getToken(), token1);

        assertEquals(2, Database.DATABASE.getIdToBid().size());
        server.bidService.removeBid(gson.toJson(removeBidDtoRequest));
        assertEquals(1, Database.DATABASE.getIdToBid().size());
        assertEquals(1, Database.DATABASE.getTokenToUser().size());
        assertEquals(2, Database.DATABASE.getTokenToExpert().size());
        server.userService.logout(gson.toJson(new UserLogoutDtoRequest(token1)));
        UserLoginDtoRequest userLoginDtoRequest = new UserLoginDtoRequest("Olegg123", "123456");
        token1 = server.userService.loginUser(gson.toJson(userLoginDtoRequest));
        assertEquals(1, Database.DATABASE.getTokenToUser().size());
        assertEquals(2, Database.DATABASE.getTokenToExpert().size());
        server.userService.logout(token3);
        server.expertService.removeExpert(token4);
        server.userService.removeUser(token1);
        assertNull(Database.DATABASE.getTokenToUser().get(token3));
        assertNull(Database.DATABASE.getTokenToExpert().get(token1));
    }

    @Test
    public void testGrades() {
        Server server = new Server();
        server.startServer(null);
        Gson gson = new Gson();
        Set<Direction> ds = new HashSet<>();
        ds.add(Direction.LITERATURE);
        ds.add(Direction.GEOMETRY);
        Set<Direction> ds2 = new HashSet<>();
        ds2.add(Direction.CHEMISTRY);
        ds2.add(Direction.LITERATURE);

        RegisterUserDtoRequest user = new RegisterUserDtoRequest("kkkkkk", "Oleg", "Leonov",
                "Olegg123", "123456");
        RegisterUserDtoRequest user2 = new RegisterUserDtoRequest("kkk123", "Oleg", "Druz",
                "Olegg093", "123456");
        RegisterExpertDtoRequest expert = new RegisterExpertDtoRequest("Grisha", "Dukalis",
                ds, "Duka940", "123456");
        RegisterExpertDtoRequest expert2 = new RegisterExpertDtoRequest("Ivan", "Ivanov",
                ds2, "Ivan903", "123456");

        String token1 = server.userService.registerUser(gson.toJson(user));
        String token2 = server.userService.registerUser(gson.toJson(user2));
        String token3 = server.expertService.registerExpert(gson.toJson(expert));
        String token4 = server.expertService.registerExpert(gson.toJson(expert2));
        token1 = gson.fromJson(token1, TokenResponse.class).getToken();
        token2 = gson.fromJson(token2, TokenResponse.class).getToken();
        token3 = gson.fromJson(token3, TokenResponse.class).getToken();
        token4 = gson.fromJson(token4, TokenResponse.class).getToken();

        AddBidDtoRequest bid1 = new AddBidDtoRequest(token1,"bid1request", "Dengi dengi",
                ds, 100000);
        AddBidDtoRequest bid2 = new AddBidDtoRequest(token1,"bid2request", "Dengi dengi2",
                ds, 30000);
        AddBidDtoRequest bid3 = new AddBidDtoRequest(token1,"bid3request", "Dengi dengi3",
                ds, 300000);
        AddBidDtoRequest bid4 = new AddBidDtoRequest(token2,"bid4request", "Dengi dengi",
                ds2, 35000);
        AddBidDtoRequest bid5 = new AddBidDtoRequest(token2,"bid5request", "Dengi dengi",
                ds2, 40000);
        AddBidDtoRequest bid6 = new AddBidDtoRequest(token2,"bid6request", "Dengi dengi",
                ds2, 10000);

        String bidToken1 = server.bidService.addBid(gson.toJson(bid1));
        String bidToken2 = server.bidService.addBid(gson.toJson(bid2));
        String bidToken3 = server.bidService.addBid(gson.toJson(bid3));
        String bidToken4 = server.bidService.addBid(gson.toJson(bid4));
        String bidToken5 = server.bidService.addBid(gson.toJson(bid5));
        String bidToken6 = server.bidService.addBid(gson.toJson(bid6));

        bidToken1 = gson.fromJson(bidToken1, TokenResponse.class).getToken();
        bidToken2 = gson.fromJson(bidToken2, TokenResponse.class).getToken();
        bidToken3 = gson.fromJson(bidToken3, TokenResponse.class).getToken();
        bidToken4 = gson.fromJson(bidToken4, TokenResponse.class).getToken();
        bidToken5 = gson.fromJson(bidToken5, TokenResponse.class).getToken();
        bidToken6 = gson.fromJson(bidToken6, TokenResponse.class).getToken();

        AddGradeDtoRequest addGrade = new AddGradeDtoRequest(token3, (byte)3, bidToken1);
        AddGradeDtoRequest addGrade1 = new AddGradeDtoRequest(token3, (byte)4, bidToken2);
        AddGradeDtoRequest addGrade2 = new AddGradeDtoRequest(token3, (byte)5, bidToken3);

        server.expertService.addGrade(gson.toJson(addGrade));
        server.expertService.addGrade(gson.toJson(addGrade1));
        server.expertService.addGrade(gson.toJson(addGrade2));

        Expert expert11 = Database.DATABASE.getTokenToExpert().get(token3);
        Expert expert12 = Database.DATABASE.getTokenToExpert().get(token4);

        assertEquals((byte) 3, Database.DATABASE.getIdToBid().get(bidToken1).getGrades().get(expert11.getLogin()));
        assertEquals((byte) 4, Database.DATABASE.getIdToBid().get(bidToken2).getGrades().get(expert11.getLogin()));
        assertEquals((byte) 5, Database.DATABASE.getIdToBid().get(bidToken3).getGrades().get(expert11.getLogin()));

        ChangeGradeRequest changeGrade = new ChangeGradeRequest(token3, (byte)4, bidToken1);
        server.expertService.changeGrade(gson.toJson(changeGrade));
        assertEquals((byte) 4, Database.DATABASE.getIdToBid().get(bidToken1).getGrades().get(expert11.getLogin()));
        ChangeGradeRequest changeGrade2 = new ChangeGradeRequest(token4, (byte)5, bidToken1);
        server.expertService.changeGrade(gson.toJson(changeGrade2));
        assertEquals((byte) 4, Database.DATABASE.getIdToBid().get(bidToken1).getGrades().get(expert11.getLogin()));
        RemoveGradeDtoRequest removeGrade = new RemoveGradeDtoRequest(token3, bidToken1);
        RemoveGradeDtoRequest removeGrade2 = new RemoveGradeDtoRequest(token4, bidToken2);

        server.expertService.removeGrade(gson.toJson(removeGrade));
        server.expertService.removeGrade(gson.toJson(removeGrade2));

        assertEquals(0, Database.DATABASE.getIdToBid().get(bidToken1).getGrades().size());
        assertEquals(1, Database.DATABASE.getIdToBid().get(bidToken2).getGrades().size());

    }

    @Test
    public void testGrade2() {
        Server server = new Server();
        server.startServer("");
        Gson gson = new Gson();
        Set<Direction> ds = new HashSet<>();
        ds.add(Direction.LITERATURE);
        ds.add(Direction.GEOMETRY);
        Set<Direction> ds2 = new HashSet<>();
        ds2.add(Direction.CHEMISTRY);
        ds2.add(Direction.LITERATURE);

        RegisterUserDtoRequest user = new RegisterUserDtoRequest("kkkkkk", "Oleg", "Leonov",
                "Olegg123", "123456");
        RegisterExpertDtoRequest expert = new RegisterExpertDtoRequest("Grisha", "Dukalis",
                ds, "Duka940", "123456");
        RegisterExpertDtoRequest expert2 = new RegisterExpertDtoRequest("Ivan", "Ivanov",
                ds2, "Ivan903", "123456");

        String token1 = server.userService.registerUser(gson.toJson(user));
        String token3 = server.expertService.registerExpert(gson.toJson(expert));
        String token4 = server.expertService.registerExpert(gson.toJson(expert2));
        token1 = gson.fromJson(token1, TokenResponse.class).getToken();
        token3 = gson.fromJson(token3, TokenResponse.class).getToken();
        token4 = gson.fromJson(token4, TokenResponse.class).getToken();

        AddBidDtoRequest bid1 = new AddBidDtoRequest(token1,"bid1request", "Dengi dengi",
                ds, 100000);
        AddBidDtoRequest bid2 = new AddBidDtoRequest(token1,"bid2request", "Dengi dengi2",
                ds, 30000);
        AddBidDtoRequest bid3 = new AddBidDtoRequest(token1,"bid3request", "Dengi dengi3",
                ds, 300000);
        AddBidDtoRequest bid4 = new AddBidDtoRequest(token1,"bid4request", "Dengi dengi",
                ds2, 35000);

        String bidToken1 = server.bidService.addBid(gson.toJson(bid1));
        String bidToken2 = server.bidService.addBid(gson.toJson(bid2));
        String bidToken3 = server.bidService.addBid(gson.toJson(bid3));
        String bidToken4 = server.bidService.addBid(gson.toJson(bid4));

        bidToken1 = gson.fromJson(bidToken1, TokenResponse.class).getToken();
        bidToken2 = gson.fromJson(bidToken2, TokenResponse.class).getToken();
        bidToken3 = gson.fromJson(bidToken3, TokenResponse.class).getToken();
        bidToken4 = gson.fromJson(bidToken4, TokenResponse.class).getToken();

        AddGradeDtoRequest addGrade = new AddGradeDtoRequest(token3, (byte)3, bidToken1);
        AddGradeDtoRequest addGrade1 = new AddGradeDtoRequest(token3, (byte)4, bidToken2);

        server.expertService.addGrade(gson.toJson(addGrade));
        server.expertService.addGrade(gson.toJson(addGrade1));

        GetGradedListRequest getGraded = new GetGradedListRequest(token3);
        GetGradedListRequest getGraded2 = new GetGradedListRequest(token4);

        Map<String, Bid> map1 = gson.fromJson((server.expertService.getGradedMap(gson.toJson(getGraded))), HashMap.class);
        Map<String, Bid> map2 = gson.fromJson((server.expertService.getGradedMap(gson.toJson(getGraded2))), HashMap.class);

        assertEquals(2, map1.size());
        assertEquals(0, map2.size());

        Set<Direction> set1 = new HashSet<>();
        set1.add(Direction.LITERATURE);
        GetBidByDirectionsRequest getBidsBy = new GetBidByDirectionsRequest(token3, set1);
        Set<Direction> set2 = new HashSet<>();
        set2.add(Direction.GEOMETRY);
        GetBidByDirectionsRequest getBidsBy2 = new GetBidByDirectionsRequest(token3, set2);
        GetBidByDirectionsRequest getBidsBy3 = new GetBidByDirectionsRequest(token4, set2);

        Map<String, Bid> map3 = gson.fromJson(server.expertService.getBidsByDirection(gson.toJson(getBidsBy)), HashMap.class);
        Map<String, Bid> map4 = gson.fromJson(server.expertService.getBidsByDirection(gson.toJson(getBidsBy2)), HashMap.class);
        Map<String, Bid> map5 = gson.fromJson(server.expertService.getBidsByDirection(gson.toJson(getBidsBy3)), HashMap.class);

        assertEquals(4, map3.size());
        assertEquals(3, map4.size());
        assertEquals(0, map5.size());

    }
}