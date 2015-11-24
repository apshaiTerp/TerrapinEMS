package ac.ems.ambulance.terrapinems.data;

import java.util.Date;

/**
 * Created by ac010168 on 11/7/15.
 */
public class EventInformation {

    //Core Event Fields
    /** The eventID that was generated for this event */
    public static long   eventID                 = -1L;
    /** The dispatch ID that was generated for this dispatch */
    public static long   dispatchID              = -1L;
    /** The hospital ID recommended at dispatch */
    public static long   recommendedHospitalID   = -1L;
    /** The current state of the event */
    public static String eventState              = null;
    /** The date the event began */
    public static Date eventStartDate            = null;

    //Event Fields that don't get set until activily transporting patients
    /** The actual destination hospital.  Can be blank (-1). */
    public static long   targetHospitalID        = -1L;
    /** The date transport of the patient began.  Can be null. */
    public static Date beginTransportDate        = null;
    /** The observed age range of the patient */
    public static String actualAgeRange          = null;  //Should be limited to "child", "teen", "adult", "unknown"
    /** The observed severity of the patient's condition */
    public static String observedSeverity        = null;

    //Core fields from the Dispatch
    /** The patient name.  Can be undefined. */
    public static String patientName             = null;
    /** The patient Gender */
    public static String patientGender           = null;   //Should be limited to "Male", "Female", "Unknown"
    /** The age range of the patient */
    public static String patientAgeRange         = null;  //Should be limited to "child", "teen", "adult", "unknown"
    /** The address where response is requested */
    public static String patientAddress          = null;
    /** The primary complaint recorded as part of the dispatch */
    public static String patientComplaint        = null;
    /** The reported initial severity level (May not be known) */
    public static String reportedSeverity        = null;
    /** The name of the reporter */
    public static String reportedByName          = null;
    /** Need to store this somewhere */
    public static double incidentLat             = 0.0;
    /** Need to store this somewhere */
    public static double incidentLon             = 0.0;

    //Potential Additional details from Hospital data
    /** Text name for the recommended Hospital, if one exists */
    public static String recommendedHospitalName = null;
    /** Text name for the target Hospital, if assigned */
    public static String targetHospitalName      = null;

    public static long   divertHospitalID        = -1L;

    public static String contactPhone            = null;

    public static void assignEventData(AmbulanceDispatchData data) {
        eventID                 = data.getEventID();
        dispatchID              = data.getDispatchID();
        recommendedHospitalID   = data.getRecommendedHospitalID();
        targetHospitalID        = data.getTargetHospitalID();
        eventState              = data.getEventState();
        eventStartDate          = data.getEventStartDate();
        beginTransportDate      = data.getBeginTransportDate();
        actualAgeRange          = data.getActualAgeRange();
        observedSeverity        = data.getObservedSeverity();

        patientName             = data.getPatientName();
        patientGender           = data.getPatientGender();
        patientAgeRange         = data.getPatientAgeRange();
        patientAddress          = data.getPatientAddress();
        patientComplaint        = data.getPatientComplaint();
        reportedSeverity        = data.getReportedSeverity();
        reportedByName          = data.getReportedByName();
        incidentLat             = data.getIncidentLat();
        incidentLon             = data.getIncidentLon();

        recommendedHospitalName = data.getRecommendedHospitalName();
        targetHospitalName      = data.getTargetHospitalName();

        divertHospitalID        = -1L;

        contactPhone            = data.getContactPhone();
    }

    public static void resetEventData() {
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

        divertHospitalID        = -1L;

        contactPhone            = null;
    }
}
