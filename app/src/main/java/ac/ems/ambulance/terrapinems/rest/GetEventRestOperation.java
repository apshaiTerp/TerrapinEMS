package ac.ems.ambulance.terrapinems.rest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.activity.AcceptDispatchActivity;
import ac.ems.ambulance.terrapinems.data.AmbulanceDispatchData;
import ac.ems.ambulance.terrapinems.data.EventInformation;
import ac.ems.ambulance.terrapinems.data.UserInformation;

/**
 * Created by ac010168 on 11/7/15.
 */
public class GetEventRestOperation extends AsyncTask<Void, Void, AmbulanceDispatchData> {

    public final static String getEventURL = "http://107.188.249.238:8080/ac-ems-system-rest-0.9/ambulance/event?id=";

    private Activity activity;
    private Context dialogContext;
    private Intent  nextIntent;

    public GetEventRestOperation(Activity activity, Context dialogContext, Intent nextIntent) {
        this.activity      = activity;
        this.dialogContext = dialogContext;
        this.nextIntent    = nextIntent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected AmbulanceDispatchData doInBackground(Void... params) {
        System.out.println("Requesting dispatches for ambulanceID: " + UserInformation.ambulanceID);

        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        System.out.println("Submitting the get event call");
        AmbulanceDispatchData result = null;
        try {
            result = restTemplate.getForObject(getEventURL + UserInformation.ambulanceID, AmbulanceDispatchData.class);
            System.out.println ("I got a response for the get event call!");
        } catch (Throwable t) {
            t.printStackTrace();
            result = new AmbulanceDispatchData();
            result.setErrorType("Request Connection Error");
            result.setErrorMessage(t.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(AmbulanceDispatchData result) {
        if (result.getErrorType() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
            builder.setMessage(result.getErrorMessage());
            builder.setTitle(result.getErrorType());
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //NOOP
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        } else {
            if (result.getMessageType().equalsIgnoreCase("No Results Found")) {

                ((AcceptDispatchActivity)activity).hadNoHits();
            } else {
                EventInformation.assignEventData(result);

                System.out.println ("About to navigate to the new Intent");
                //Navigate to the next page
                if (activity != null) {
                    activity.startActivity(nextIntent);
                }
            }
        }
    }
}
