package ac.ems.ambulance.terrapinems.gps;

import android.content.Context;
import android.location.Location;

import ac.ems.ambulance.terrapinems.gps.GPSTracker;

/**
 * Created by ac010168 on 11/11/15.
 */
public class GPSTrackerHost {

    public static GPSTracker tracker = null;

    public static void beginGPSTracking(Context context) {
        if (tracker == null) {
            tracker = new GPSTracker(context);

            if (!tracker.canGetLocation()) {
                tracker.showSettingsAlert();
            }
        }
    }

    public static void stopGPSTracking() {
        if (tracker != null) {
            tracker.stopUsingGPS();
        }
    }

    public static void updateContext(Context context) {
        if (tracker != null) {
            tracker.updateContext(context);
        }
    }

    public static Location getCurrentLocation() {
        if (tracker != null && tracker.canGetLocation()) {
            return tracker.getLastKnownLocation();
        }
        return null;
    }
}
