package hello;

import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {
    //private final UUID uuid = UUID.randomUUID();
    private String userName;
    private String password;
    private UserTypes userType; // This can't be NULL!
    boolean canSeeLog;

    public User()
    {
        this.userName = null;
        this.password = null;
        this.userType = null;
        this.canSeeLog = false;
    }

    // copy constructor
    public User(User pUser) {
        this.userName = pUser.getUserName();
        this.password = pUser.getPassword();
        this.userType = pUser.getUserType();
        this.canSeeLog = pUser.isCanSeeLog();
    }

    public User(String username, String password, UserTypes userType) {
        this.userName = username;
        this.password = password;
        this.userType = userType;
    }

    public boolean isCanSeeLog() {
        return canSeeLog;
    }

    public void setCanSeeLog() {
        this.canSeeLog = !canSeeLog;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserTypes getUserType() {
        return userType;
    }

    public void setUserType(UserTypes userType) {
        this.userType = userType;
    }

}