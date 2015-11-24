package ac.ems.ambulance.terrapinems.data;

/**
 * Created by ac010168 on 11/12/15.
 */
public class EventStatusChangeData {

    private String eventState;
    private String observedAge;
    private String observedSeverity;
    private long   targetHospitalID;
    private String changeDescription;
    private long   userID;

    public EventStatusChangeData() {
        eventState        = null;
        observedAge       = null;
        observedSeverity  = null;
        targetHospitalID  = -1L;
        changeDescription = null;
        userID            = -1L;
    }

    public long getTargetHospitalID() {
        return targetHospitalID;
    }

    public void setTargetHospitalID(long targetHospitalID) {
        this.targetHospitalID = targetHospitalID;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public String getObservedAge() {
        return observedAge;
    }

    public void setObservedAge(String observedAge) {
        this.observedAge = observedAge;
    }

    public String getObservedSeverity() {
        return observedSeverity;
    }

    public void setObservedSeverity(String observedSeverity) {
        this.observedSeverity = observedSeverity;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
