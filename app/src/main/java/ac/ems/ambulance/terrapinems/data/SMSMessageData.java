package ac.ems.ambulance.terrapinems.data;

/**
 * Created by ac010168 on 11/19/15.
 */
public class SMSMessageData {

    private String textMessage;
    private String contactPhone;

    public SMSMessageData() {
        textMessage  = null;
        contactPhone = null;
    }

    /**
     * @return the textMessage
     */
    public String getTextMessage() {
        return textMessage;
    }

    /**
     * @param textMessage the textMessage to set
     */
    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    /**
     * @return the contactPhone
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * @param contactPhone the contactPhone to set
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
