package net.thumbtack.school.competition.database;

import net.thumbtack.school.competition.model.Bid;
import net.thumbtack.school.competition.model.Direction;
import net.thumbtack.school.competition.model.Expert;
import net.thumbtack.school.competition.model.User;
import net.thumbtack.school.competition.service.exception.ServiceErrorCode;
import net.thumbtack.school.competition.service.exception.ServiceException;
import java.util.*;

public class Database {

    public static Database DATABASE = new Database();
    private Map<String, User> tokenToUser = new HashMap<>(); // token to user
    private Map<String, Expert> tokenToExpert = new HashMap<>(); // token to expert
    private Map<String, Bid> idToBid = new HashMap<>(); // id to bid
    private Map<String, User> loginToUser = new HashMap<>(); // login to user
    private Map<String, Expert> loginToExpert = new HashMap<>(); // login to expert
    private Map<String, String> registerPersons = new HashMap<>(); //fio to login
    private Map<Direction, Set<String>> bidDirectionMap = new HashMap<>(); //direct to set
    private static final byte MIN_GRADE = 2;

    private Database() {}

    public Map<Direction, Set<String>> getBidDirectionMap() {
        return bidDirectionMap;
    }

    public void setBidDirectionMap(Map<Direction, Set<String>> bidDirectionMap) {
        this.bidDirectionMap = bidDirectionMap;
    }

    public Map<String, User> getLoginToUser() {
        return loginToUser;
    }

    public void setLoginToUser(Map<String, User> loginToUser) {
        this.loginToUser = loginToUser;
    }

    public Map<String, Expert> getLoginToExpert() {
        return loginToExpert;
    }

    public void setLoginToExpert(Map<String, Expert> loginToExpert) {
        this.loginToExpert = loginToExpert;
    }

    public Map<String, String> getRegisterPersons() {
        return registerPersons;
    }

    public void setRegisterPersons(Map<String, String> registerPersons) {
        this.registerPersons = registerPersons;
    }

    public void setTokenToUser(Map<String, User> tokenToUser) {
        this.tokenToUser = tokenToUser;
    }

    public void setTokenToExpert(Map<String, Expert> tokenToExpert) {
        this.tokenToExpert = tokenToExpert;
    }

    public void setIdToBid(Map<String, Bid> idToBid) {
        this.idToBid = idToBid;
    }

    public Map<String, User> getTokenToUser() {
        return tokenToUser;
    }

    public Map<String, Expert> getTokenToExpert() {
        return tokenToExpert;
    }

    public Map<String, Bid> getIdToBid() {
        return idToBid;
    }

    public String addUser(User user) throws ServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getFirstName()).append(" ").append(user.getSecondName())
                .append(" ").append(user.getCompanyName());
        if (registerPersons.containsKey(sb.toString())) {
            throw new ServiceException(ServiceErrorCode.ALREADY_REGISTER_USER);
        } else if (checkLogin(user.getLogin())) {
            throw new ServiceException(ServiceErrorCode.ALREADY_TAKEN_LOGIN);
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        loginToUser.put(user.getLogin(), user);
        tokenToUser.put(token, user);
        registerPersons.put(sb.toString(), token);
        return token;
    }

    public String addExpert(Expert expert) throws ServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append(expert.getFirstName()).append(" ").append(expert.getSecondName());
        if (registerPersons.containsKey(sb.toString())) {
            throw new ServiceException(ServiceErrorCode.ALREADY_REGISTER_USER);
        } else if (checkLogin(expert.getLogin())) {
            throw new ServiceException(ServiceErrorCode.ALREADY_TAKEN_LOGIN);
        }
        String token = UUID.randomUUID().toString();
        expert.setToken(token);
        loginToExpert.put(expert.getLogin(), expert);
        tokenToExpert.put(token, expert);
        registerPersons.put(sb.toString(), token);
        return token;
    }

    public String addBid(String token, Bid bid) throws ServiceException {

        if (tokenToUser.get(token) == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        }
        String id = UUID.randomUUID().toString();

        bid.setId(id);
        tokenToUser.get(token).addBidId(id);
        bid.setOwner(tokenToUser.get(token));
        idToBid.put(id, bid);
        for (Direction item : bid.getFields()) {
            if (bidDirectionMap.containsKey(item)) {
                bidDirectionMap.get(item).add(id);
            } else {
                Set<String> bidSet = new HashSet<>();
                bidSet.add(id);
                bidDirectionMap.put(item, bidSet);
            }
        }
        return id;
    }

    public void addGrade(String token, byte grade, String id) throws ServiceException {
        Expert expert = tokenToExpert.get(token);
        Bid bid = idToBid.get(id);
        if (expert == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        } else if (bid == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_BID_ID);
        }

        boolean check = false;
        for (Direction item : expert.getFields()) {
            if (bid.getFields().contains(item)) {
                check = true;
                break;
            }
        }
        if (check) {
            expert.addBidId(id);
            tokenToExpert.replace(token, expert);
            bid.addGrade(expert, grade);
            idToBid.replace(id, bid);
        }
    }

    public void removeGrade(String token, String id) throws ServiceException {
        Expert expert = tokenToExpert.get(token);
        Bid bid = idToBid.get(id);
        if (expert == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        } else if (bid == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_BID_ID);
        }

        if (!bid.getGrades().containsKey(expert.getLogin())) {
            throw new ServiceException(ServiceErrorCode.ILLEGAL_USER);
        } else {
            expert.removeBidId(id);
            bid.removeGrade(expert);
            idToBid.replace(id, bid);
        }

    }

    public void removeUser(String token) throws ServiceException {
        User user = tokenToUser.get(token);
        if (user == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        }
        for (String bid : user.getBidIdSet()) {
            idToBid.remove(bid);
        }
        loginToUser.remove(user.getLogin());
        tokenToUser.remove(token);
    }

    public void removeExpert(String token) throws ServiceException {
        Expert expert = tokenToExpert.get(token);
        if (expert == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        }
        expert.getBidIdSet().forEach((v) -> idToBid.get(v).removeGrade(tokenToExpert.get(token)));
        loginToExpert.remove(expert.getLogin());
        tokenToExpert.remove(token);
    }

    public void removeBid(String id, String token) {
        if (tokenToUser.get(token) == null) {
            return;
        }
        if (idToBid.get(id).getOwner().equals(tokenToUser.get(token))) {
            Map<String, Byte> map1 = idToBid.get(id).getGrades();
            map1.forEach((k, v) -> loginToExpert.get(k).removeBidId(id));
            tokenToUser.get(token).removeBidId(id);
            for (Direction item : idToBid.get(id).getFields()) {
                bidDirectionMap.get(item).remove(id);
            }
            idToBid.remove(id);
        }
    }

    public void logout(String token) {
        if (tokenToUser.containsKey(token)) {
            tokenToUser.remove(token);
        } else tokenToExpert.remove(token);
    }

    public String login(String userName, String password) throws ServiceException {
        String token = "-1";
        checkLoginUser(userName, password);

        if (loginToUser.containsKey(userName)) {
            token = UUID.randomUUID().toString();
            loginToUser.get(userName).setToken(token);
            tokenToUser.put(token, loginToUser.get(userName));
        }

        if (loginToExpert.containsKey(userName)) {
            token = UUID.randomUUID().toString();
            loginToExpert.get(userName).setToken(token);
            tokenToExpert.put(token, loginToExpert.get(userName));
        }

        return token;
    }

    public Map<String, Bid> getBidMapExpert(String token) throws ServiceException {
        Expert expert = tokenToExpert.get(token);
        if (expert == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        }
        Map<String, Bid> outBids = new HashMap<>();

        for (String id : expert.getBidIdSet()) {
            outBids.put(id, idToBid.get(id));
        }

        return outBids;
    }

    public Map<String, Bid> getDirectionsBid(String token, Set<Direction> directionSet) throws ServiceException {
        Expert expert = tokenToExpert.get(token);
        if (expert == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        }

        Map<String, Bid> outBids = new HashMap<>();
        for (Direction item : directionSet) {
            if (expert.getFields().contains(item)) {
                for (String string : bidDirectionMap.get(item)) {
                    outBids.put(string, idToBid.get(string));
                }
            }
        }

        return outBids;
    }

    public void changeGrade(String token, byte grade, String id) throws ServiceException {
        Expert expert = tokenToExpert.get(token);
        Bid bid = idToBid.get(id);
        if (expert == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_TOKEN);
        } else if (bid == null) {
            throw new ServiceException(ServiceErrorCode.WRONG_BID_ID);
        } else if (!bid.getGrades().containsKey(expert.getLogin())) {
            throw new ServiceException(ServiceErrorCode.ILLEGAL_USER);
        }

        bid.removeGrade(expert);
        bid.addGrade(expert, grade);
        idToBid.replace(id, bid);
    }

    public LinkedList<Bid> getFinalList(long totalMoney) {
        long total = totalMoney;
        List<Bid> startList = new ArrayList<>();

        idToBid.forEach((k, v) -> startList.add(v));

        firstSort(startList);
        finalSort(startList);

        LinkedList<Bid> bidList = new LinkedList<>();
        for (Bid bid : startList) {
            if (total - bid.getPrice() > 0 && getAverage(bid) > MIN_GRADE) {
                bid.setAverage(getAverage(bid));
                bidList.add(bid);
                total = total - bid.getPrice();
            }
        }

        return bidList;

    }

    private boolean checkLogin(String login) {
        return loginToUser.get(login) != null && loginToExpert.get(login) == null;
    }

    private void checkLoginUser(String username, String password) throws ServiceException {

        if (loginToExpert.get(username) != null) {
            if (loginToExpert.get(username).getPassword().equals(password)) {
                return;
            } else {
                throw new ServiceException(ServiceErrorCode.WRONG_PASSWORD_LOGIN);
            }
        }

        if (loginToUser.get(username) != null) {
            if (loginToUser.get(username).getPassword().equals(password)) {
                return;
            } else {
                throw new ServiceException(ServiceErrorCode.WRONG_PASSWORD_LOGIN);
            }
        }

        throw new ServiceException(ServiceErrorCode.USER_NOT_REGISTER);
    }

    private void firstSort(List<Bid> startList) {
        startList.sort((o1, o2) -> {
            float EPS = 0.0001f;
            float averageGrade1 = getAverage(o1);
            float averageGrade2 = getAverage(o2);

            if (Math.abs(averageGrade1 - averageGrade2) < Math.abs(EPS)) {
                return Long.compare(o2.getPrice(), o1.getPrice());
            } else if (averageGrade1 - averageGrade2 > 0) {
                return -1;
            } else return 1;
        });

    }

    private float getAverage(Bid bid) {

        float aver = 0;
        if (bid.getGrades().isEmpty()) {
            return aver;
        }

        Collection<Byte> list = bid.getGrades().values();

        for (byte item : list) {
            aver += item;
        }

        aver = aver / list.size();
        return aver;
    }

    private void finalSort(List<Bid> second) {
        List<Bid> thirdList = new ArrayList<>();
        for (int i = 0, count = 0; i < second.size() - 1; i++) {
            if (getAverage(second.get(i)) == getAverage(second.get(i + 1)) &&
                    second.get(i).getPrice() == second.get(i + 1).getPrice()) {

                thirdList.add(second.get(i));

                for (int j = i; j < second.size() - 1
                        && (second.get(j).getPrice() == second.get(j + 1).getPrice())
                        && getAverage(second.get(j)) == getAverage(second.get(j + 1)); j++) {
                    thirdList.add(second.get(j+1));
                    count = j+1;
                }

                Collections.shuffle(thirdList);

                if (count > i) {
                    second.subList(i, count + 1).clear();
                }

                second.addAll(i, thirdList);
                i = count;
            }
        }

    }

}
