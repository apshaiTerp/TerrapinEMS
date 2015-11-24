package ac.ems.ambulance.terrapinems.data;

/**
 * Created by ac010168 on 11/5/15.
 */
public class LoginSuccessMessage {

    /** A placeholder for the message type.  This may get converted to an enum at some point. */
    private String messageType;
    /** The text we want returned for this message. */
    private String message;
    /** A placeholder for the error type.  This may get converted to an enum at some point. */
    private String errorType;
    /** The text we want returned for this message. */
    private String errorMessage;
    private String userName;
    private long   userID;
    private String userRole;

    public LoginSuccessMessage() {
        messageType  = null;
        message      = null;
        errorType    = null;
        errorMessage = null;
        userName     = null;
        userID       = -1L;
        userRole     = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
