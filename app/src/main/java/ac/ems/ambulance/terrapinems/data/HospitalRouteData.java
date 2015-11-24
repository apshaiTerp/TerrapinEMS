package ac.ems.ambulance.terrapinems.data;

/**
 * Created by ac010168 on 11/11/15.
 */
public class HospitalRouteData {

    private long   hospitalID;
    private String hospitalName;
    private double hospitalLat;
    private double hospitalLon;
    private String hospitalAddress;
    private Double rawDistance;
    private String distanceText;
    private int    distanceValue;
    private String etaString;

    /** Basic Constructor */
    public HospitalRouteData() {
        hospitalID      = 0;
        hospitalName    = null;
        hospitalLat     = 0.0;
        hospitalLon     = 0.0;
        hospitalAddress = null;
        rawDistance     = 0.0;
        distanceText    = null;
        distanceValue   = 0;
        etaString       = null;
    }

    /**
     * @return the hospitalID
     */
    public long getHospitalID() {
        return hospitalID;
    }

    /**
     * @param hospitalID the hospitalID to set
     */
    public void setHospitalID(long hospitalID) {
        this.hospitalID = hospitalID;
    }

    /**
     * @return the hospitalName
     */
    public String getHospitalName() {
        return hospitalName;
    }

    /**
     * @param hospitalName the hospitalName to set
     */
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    /**
     * @return the hospitalLat
     */
    public double getHospitalLat() {
        return hospitalLat;
    }

    /**
     * @param hospitalLat the hospitalLat to set
     */
    public void setHospitalLat(double hospitalLat) {
        this.hospitalLat = hospitalLat;
    }

    /**
     * @return the hospitalLon
     */
    public double getHospitalLon() {
        return hospitalLon;
    }

    /**
     * @param hospitalLon the hospitalLon to set
     */
    public void setHospitalLon(double hospitalLon) {
        this.hospitalLon = hospitalLon;
    }

    /**
     * @return the hospitalAddress
     */
    public String getHospitalAddress() {
        return hospitalAddress;
    }

    /**
     * @param hospitalAddress the hospitalAddress to set
     */
    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    /**
     * @return the rawDistance
     */
    public double getRawDistance() {
        return rawDistance;
    }

    /**
     * @param rawDistance the rawDistance to set
     */
    public void setRawDistance(double rawDistance) {
        this.rawDistance = rawDistance;
    }

    /**
     * @return the distanceText
     */
    public String getDistanceText() {
        return distanceText;
    }

    /**
     * @param distanceText the distanceText to set
     */
    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    /**
     * @return the distanceValue
     */
    public int getDistanceValue() {
        return distanceValue;
    }

    /**
     * @param distanceValue the distanceValue to set
     */
    public void setDistanceValue(int distanceValue) {
        this.distanceValue = distanceValue;
    }

    /**
     * @return the etaString
     */
    public String getEtaString() {
        return etaString;
    }

    /**
     * @param etaString the etaString to set
     */
    public void setEtaString(String etaString) {
        this.etaString = etaString;
    }
}
