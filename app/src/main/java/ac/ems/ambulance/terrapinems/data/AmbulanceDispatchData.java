package ac.ems.ambulance.terrapinems.data;

import java.util.Date;

/**
 * Created by ac010168 on 11/7/15.
 */
public class AmbulanceDispatchData {

    /** A placeholder for the message type.  This may get converted to an enum at some point. */
    private String messageType;
    /** The text we want returned for this message. */
    private String message;
    /** A placeholder for the error type.  This may get converted to an enum at some point. */
    private String errorType;
    /** The text we want returned for this message. */
    private String errorMessage;

    //Core Event Fields
    /** The eventID that was generated for this event */
    private long   eventID;
    /** The dispatch ID that was generated for this dispatch */
    private long   dispatchID;
    /** The hospital ID recommended at dispatch */
    private long   recommendedHospitalID;
    /** The current state of the event */
    private String eventState;
    /** The date the event began */
    private Date eventStartDate;

    //Event Fields that don't get set until activily transporting patients
    /** The actual destination hospital.  Can be blank (-1). */
    private long   targetHospitalID;
    /** The date transport of the patient began.  Can be null. */
    private Date   beginTransportDate;
    /** The observed age range of the patient */
    private String actualAgeRange;  //Should be limited to "child", "teen", "adult", "unknown"
    /** The observed severity of the patient's condition */
    private String observedSeverity;

    //Core fields from the Dispatch
    /** The patient name.  Can be undefined. */
    private String patientName;
    /** The patient Gender */
    private String patientGender;   //Should be limited to "Male", "Female", "Unknown"
    /** The age range of the patient */
    private String patientAgeRange;  //Should be limited to "child", "teen", "adult", "unknown"
    /** The address where response is requested */
    private String patientAddress;
    /** The primary complaint recorded as part of the dispatch */
    private String patientComplaint;
    /** The reported initial severity level (May not be known) */
    private String reportedSeverity;
    /** The name of the reporter */
    private String reportedByName;
    /** Need to store this somewhere */
    private double incidentLat;
    /** Need to store this somewhere */
    private double incidentLon;

    //Potential Additional details from Hospital data
    /** Text name for the recommended Hospital, if one exists */
    private String recommendedHospitalName;
    /** Text name for the target Hospital, if assigned */
    private String targetHospitalName;
    /** Contact Phone Number */
    private String contactPhone;

    public AmbulanceDispatchData() {
        messageType             = null;
        message                 = null;
        errorType               = null;
        errorMessage            = null;

        eventID                 = -1L;
        dispatchID              = -1L;
        recommendedHospitalID   = -1L;
        targetHospitalID        = -1L;
        eventState              = null;
        eventStartDate          = null;
        beginTransportDate      = null;
        actualAgeRange          = null;
        observedSeverity        = null;

        patientName             = null;
        patientGender           = null;
        patientAgeRange         = null;
        patientAddress          = null;
        patientComplaint        = null;
        reportedSeverity        = null;
        reportedByName          = null;
        incidentLat             = 0.0;
        incidentLon             = 0.0;

        recommendedHospitalName = null;
        targetHospitalName      = null;

        contactPhone            = null;
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

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public long getDispatchID() {
        return dispatchID;
    }

    public void setDispatchID(long dispatchID) {
        this.dispatchID = dispatchID;
    }

    public long getRecommendedHospitalID() {
        return recommendedHospitalID;
    }

    public void setRecommendedHospitalID(long recommendedHospitalID) {
        this.recommendedHospitalID = recommendedHospitalID;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public long getTargetHospitalID() {
        return targetHospitalID;
    }

    public void setTargetHospitalID(long targetHospitalID) {
        this.targetHospitalID = targetHospitalID;
    }

    public Date getBeginTransportDate() {
        return beginTransportDate;
    }

    public void setBeginTransportDate(Date beginTransportDate) {
        this.beginTransportDate = beginTransportDate;
    }

    public String getActualAgeRange() {
        return actualAgeRange;
    }

    public void setActualAgeRange(String actualAgeRange) {
        this.actualAgeRange = actualAgeRange;
    }

    public String getObservedSeverity() {
        return observedSeverity;
    }

    public void setObservedSeverity(String observedSeverity) {
        this.observedSeverity = observedSeverity;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientAgeRange() {
        return patientAgeRange;
    }

    public void setPatientAgeRange(String patientAgeRange) {
        this.patientAgeRange = patientAgeRange;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPatientComplaint() {
        return patientComplaint;
    }

    public void setPatientComplaint(String patientComplaint) {
        this.patientComplaint = patientComplaint;
    }

    public String getReportedSeverity() {
        return reportedSeverity;
    }

    public void setReportedSeverity(String reportedSeverity) {
        this.reportedSeverity = reportedSeverity;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public double getIncidentLat() {
        return incidentLat;
    }

    public void setIncidentLat(double incidentLat) {
        this.incidentLat = incidentLat;
    }

    public double getIncidentLon() {
        return incidentLon;
    }

    public void setIncidentLon(double incidentLon) {
        this.incidentLon = incidentLon;
    }

    public String getRecommendedHospitalName() {
        return recommendedHospitalName;
    }

    public void setRecommendedHospitalName(String recommendedHospitalName) {
        this.recommendedHospitalName = recommendedHospitalName;
    }

    public String getTargetHospitalName() {
        return targetHospitalName;
    }

    public void setTargetHospitalName(String targetHospitalName) {
        this.targetHospitalName = targetHospitalName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
