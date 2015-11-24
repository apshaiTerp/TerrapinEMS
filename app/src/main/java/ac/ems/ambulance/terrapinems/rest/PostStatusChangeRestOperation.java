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
import ac.ems.ambulance.terrapinems.data.EventInformation;
import ac.ems.ambulance.terrapinems.data.EventStatusChangeData;
import ac.ems.ambulance.terrapinems.data.SimpleMessageData;

/**
 * Created by ac010168 on 11/17/15.
 */
public class PostStatusChangeRestOperation extends AsyncTask<Void, Void, SimpleMessageData> {

    public final static String changeStatusURL = "http://107.188.249.238:8080/ac-ems-system-rest-0.9/event?id=";

    private EventStatusChangeData changeData;

    private Context  context;
    private Activity activity;
    private Intent   nextIntent;

    public PostStatusChangeRestOperation(EventStatusChangeData changeData, Context context, Activity activity, Intent nextIntent) {
        this.changeData = changeData;
        this.context    = context;
        this.activity   = activity;
        this.nextIntent = nextIntent;
    }

    @Override
    protected SimpleMessageData doInBackground(Void... params) {
        System.out.println ("About to submit an event status change: " + changeData.getEventState());

        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        System.out.println ("Submitting the put status change call");
        // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
        SimpleMessageData response = null;
        try {
            //response = restTemplate.putForObject(changeStatusURL + EventInformation.eventID, changeData, SimpleMessageData.class);
            restTemplate.put(changeStatusURL + EventInformation.eventID, changeData);
            response = new SimpleMessageData();
            response.setMessage("I got a valid response for the status change call");
        } catch (Throwable t) {
            t.printStackTrace();
            response = new SimpleMessageData();
            response.setErrorType("Connection Error");
            response.setErrorMessage("There were problems submitting the last PUT command: " + t.getMessage());
        }

        return response;
    }

    @Override
    protected void onPostExecute(SimpleMessageData result) {
        System.out.println ("I'm in the postExecute method...");

        if (result.getErrorType() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            System.out.println("I successfully posted the status update");

            //check to see if we have any of the other fields
            if (changeData.getTargetHospitalID() != -1) {

                System.out.println ("I need to update the full event data to sync back up.");
                //If this happens, we probably need to re-pull all the fields.
                //It will now handle the navigation
                GetEventRestOperation eventRestOperation = new GetEventRestOperation(activity, context, nextIntent);
                eventRestOperation.execute();
            } else {
                EventInformation.eventState = changeData.getEventState();
                activity.startActivity(nextIntent);
            }
        }
    }
}
