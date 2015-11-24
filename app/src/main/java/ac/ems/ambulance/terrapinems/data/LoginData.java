package ac.ems.ambulance.terrapinems.data;

/**
 * Created by ac010168 on 11/5/15.
 */
public class LoginData {

    private String userName;
    private String password;
    private String userRole;
    private long   authorizeID;  //This value is optional

    public LoginData() {
        userName    = null;
        password    = null;
        userRole    = null;
        authorizeID = 0L;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the authorizeID
     */
    public long getAuthorizeID() {
        return authorizeID;
    }

    /**
     * @param authorizeID the authorizeID to set
     */
    public void setAuthorizeID(long authorizeID) {
        this.authorizeID = authorizeID;
    }
}
