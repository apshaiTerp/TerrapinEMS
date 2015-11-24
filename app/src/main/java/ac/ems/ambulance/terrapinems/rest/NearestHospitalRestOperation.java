package ac.ems.ambulance.terrapinems.rest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.activity.DiagnoseForHospitalActivity;
import ac.ems.ambulance.terrapinems.gps.GPSTrackerHost;
import ac.ems.ambulance.terrapinems.data.HospitalRouteData;

/**
 * Created by ac010168 on 11/11/15.
 */
public class NearestHospitalRestOperation extends AsyncTask<Void, Void, HospitalRouteData[]> {

    public final static String nearestHospitalsURL = "http://107.188.249.238:8080/ac-ems-system-rest-0.9/nearesthospital?";

    private DiagnoseForHospitalActivity activity;
    private Context dialogContext;

    private int ageFlag, criteriaFlag;

    public NearestHospitalRestOperation(int ageFlag, int criteriaFlag, DiagnoseForHospitalActivity activity, Context context) {
        this.ageFlag       = ageFlag;
        this.criteriaFlag  = criteriaFlag;
        this.activity      = activity;
        this.dialogContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HospitalRouteData[] doInBackground(Void... params) {
        System.out.println("Requesting nearest hospitals...");

        //We need to get our coordinates
        Location location = GPSTrackerHost.getCurrentLocation();
        String newUrl = nearestHospitalsURL + "amblat=" + location.getLatitude() + "&amblon=" + location.getLongitude();

        switch(ageFlag) {
            case DiagnoseForHospitalActivity.CHILD_FLAG : newUrl += "&age=child"; break;
            case DiagnoseForHospitalActivity.TEEN_FLAG  : newUrl += "&age=teen"; break;
            case DiagnoseForHospitalActivity.ADULT_FLAG : newUrl += "&age=adult"; break;
            default : break;
        }

        switch(criteriaFlag) {
            case DiagnoseForHospitalActivity.NONCRIT_FLAG : newUrl += "&condition=basicER"; break;
            case DiagnoseForHospitalActivity.MINOR_FLAG   : newUrl += "&condition=minor"; break;
            case DiagnoseForHospitalActivity.SEVERE_FLAG  : newUrl += "&condition=severe"; break;
            case DiagnoseForHospitalActivity.BURN_FLAG    : newUrl += "&condition=burn"; break;
            case DiagnoseForHospitalActivity.STEMI_FLAG   : newUrl += "&condition=stemi"; break;
            case DiagnoseForHospitalActivity.STROKE_FLAG  : newUrl += "&condition=stroke"; break;
            default : break;
        }

        System.out.println ("newURL: " + newUrl);

        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        System.out.println("Submitting the call");
        HospitalRouteData[] routeData = null;
        try {
            routeData = restTemplate.getForObject(newUrl, HospitalRouteData[].class);
        } catch (Throwable t) {
            t.printStackTrace();
            routeData = new HospitalRouteData[0];
        }

        return routeData;
    }

    @Override
    protected void onPostExecute(HospitalRouteData[] result) {
        if (result == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
            builder.setMessage("Null was returned.  Please try again.");
            builder.setTitle("Hospital Selection Error!");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //NOOP
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        if (result.length == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
            builder.setMessage("Somehow we got no results.  Please try again.");
            builder.setTitle("Hospital Selection Error!");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //NOOP
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        //Now we know we have something
        System.out.println ("I'm looking at " + result.length + " hospitals!");
        activity.showHospitalResults(result);
    }

}
