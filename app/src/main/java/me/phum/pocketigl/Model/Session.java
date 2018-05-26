package me.phum.pocketigl.Model;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private List<User> users;
    private int numUsers;
    private String sessionCode;

    public Session() {
        this.sessionCode = generateSessionCode();
        this.numUsers = 0;
        this.users = new ArrayList<>();
    }

    /*
        add a user and return its id if the code matches the session's code, else return -1
     */
    public int authenticate(String code, User user) {
        if(code.equals(this.sessionCode)) {
            addUser(user);
            return numUsers;
        } else {
            return -1;
        }
    }

    private String generateSessionCode() {
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int count = 4;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public String getSessionCode() {
        return this.sessionCode;
    }

    /*
        add a user to the list, increase the number of users
     */
    private void addUser(User e) {
        users.add(e);
        numUsers++;
    }
}
