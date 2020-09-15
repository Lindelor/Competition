package net.thumbtack.school.competition.server;

import net.thumbtack.school.competition.database.Database;
import net.thumbtack.school.competition.model.Direction;
import net.thumbtack.school.competition.model.Expert;
import net.thumbtack.school.competition.model.User;
import net.thumbtack.school.competition.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TestServer {

    @Test
    public void testStopServer() {
        Server server = new Server();
        server.startServer("");
        User user = new User("OOO OOO", "Ivan",
                "Ivanov", "Ivan101", "111111");
        Set<Direction> ds = new HashSet<>();
        ds.add(Direction.LITERATURE);
        ds.add(Direction.GEOMETRY);
        Expert expert = new Expert("Semen", "Semenov", ds, "Semen11", "222222");
        try {
            Database.DATABASE.addUser(user);
            Database.DATABASE.addExpert(expert);
            server.stopServer("save.txt");
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
        }
        Map<String, User> testMap = Database.DATABASE.getTokenToUser();
        Map<String, Expert> testExp = Database.DATABASE.getTokenToExpert();
        assertEquals(1, testMap.size());
        assertEquals(1, testExp.size());
        testMap.clear();
        testExp.clear();
        Set<Direction> ds2 = new HashSet<>();
        ds2.add(Direction.LITERATURE);
        Database.DATABASE.setTokenToUser(testMap);
        Database.DATABASE.setTokenToExpert(testExp);
    }

    @Test
    public void testStartServer() {
        Server server = new Server();
        server.startServer("save.txt");
        Map<String, User> testMap = Database.DATABASE.getTokenToUser();
        Map<String, Expert> testExp = Database.DATABASE.getTokenToExpert();
        server.startServer("kek");
        server.startServer("");
        server.startServer(null);
        Map<String, User> testMap1 = Database.DATABASE.getTokenToUser();
        Map<String, Expert> testExp1 = Database.DATABASE.getTokenToExpert();

        assertNotEquals(testExp, testExp1);
        assertNotEquals(testMap, testMap1);
        assertTrue(testMap1.isEmpty());
        assertTrue(testExp1.isEmpty());
        assertFalse(testExp.isEmpty());
        assertFalse(testMap.isEmpty());
    }

}