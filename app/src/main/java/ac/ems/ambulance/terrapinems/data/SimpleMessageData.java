package ac.ems.ambulance.terrapinems.data;

/**
 * Created by ac010168 on 11/7/15.
 */
public class SimpleMessageData {

    /** A placeholder for the message type.  This may get converted to an enum at some point. */
    private String messageType;
    /** The text we want returned for this message. */
    private String message;
    /** A placeholder for the error type.  This may get converted to an enum at some point. */
    private String errorType;
    /** The text we want returned for this message. */
    private String errorMessage;

    public SimpleMessageData() {
        messageType  = null;
        message      = null;
        errorType    = null;
        errorMessage = null;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
