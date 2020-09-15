package net.thumbtack.school.competition.server;

import com.google.gson.Gson;
import net.thumbtack.school.competition.database.Database;
import net.thumbtack.school.competition.service.BidService;
import net.thumbtack.school.competition.service.ExpertService;
import net.thumbtack.school.competition.service.UserService;
import java.io.*;
import java.util.HashMap;

public class Server {

    BidService bidService = new BidService();
    ExpertService expertService = new ExpertService();
    UserService userService = new UserService();

    public void startServer(String savedDataFileName) {
        if (savedDataFileName == null || savedDataFileName.length() < 1) {
            Database.DATABASE.setLoginToUser(new HashMap<>());
            Database.DATABASE.setLoginToExpert(new HashMap<>());
            Database.DATABASE.setTokenToExpert(new HashMap<>());
            Database.DATABASE.setTokenToUser(new HashMap<>());
            Database.DATABASE.setIdToBid(new HashMap<>());
            Database.DATABASE.setRegisterPersons(new HashMap<>());
            Database.DATABASE.setBidDirectionMap(new HashMap<>());
            System.out.println("Server started with empty DB");
        } else {
            Gson gson = new Gson();
            File file = new File(savedDataFileName);
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                Database db = gson.fromJson(br, Database.class);
                Database.DATABASE.setLoginToExpert(db.getLoginToExpert());
                Database.DATABASE.setLoginToUser(db.getLoginToUser());
                Database.DATABASE.setTokenToExpert(db.getTokenToExpert());
                Database.DATABASE.setTokenToUser(db.getTokenToUser());
                Database.DATABASE.setIdToBid(db.getIdToBid());
                Database.DATABASE.setRegisterPersons(db.getRegisterPersons());
                Database.DATABASE.setBidDirectionMap(db.getBidDirectionMap());
                System.out.println("Server started from DB");
            } catch (IOException e) {
                Database.DATABASE.setLoginToUser(new HashMap<>());
                Database.DATABASE.setLoginToExpert(new HashMap<>());
                Database.DATABASE.setTokenToExpert(new HashMap<>());
                Database.DATABASE.setTokenToUser(new HashMap<>());
                Database.DATABASE.setIdToBid(new HashMap<>());
                Database.DATABASE.setRegisterPersons(new HashMap<>());
                Database.DATABASE.setBidDirectionMap(new HashMap<>());
                System.out.println("Server started with empty DB");
            }
        }

    }

    public void stopServer(String savedDataFileName) throws IOException {
        Gson gson = new Gson();
        File file = new File(savedDataFileName);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(Database.DATABASE, bw);
        }

    }

}
