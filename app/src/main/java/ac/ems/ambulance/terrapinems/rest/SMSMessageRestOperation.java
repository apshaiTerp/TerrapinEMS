package ac.ems.ambulance.terrapinems.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ac.ems.ambulance.terrapinems.data.SMSMessageData;
import ac.ems.ambulance.terrapinems.data.SimpleMessageData;

/**
 * Created by ac010168 on 11/19/15.
 */
public class SMSMessageRestOperation extends AsyncTask<Void, Void, SimpleMessageData> {

    public final static String postSMSURL = "http://107.188.249.238:8080/ac-ems-system-rest-0.9/message/sms";

    private SMSMessageData messageData;
    private Context context;

    public SMSMessageRestOperation(SMSMessageData messageData, Context context) {
        this.messageData = messageData;
        this.context     = context;
    }

    @Override
    protected SimpleMessageData doInBackground(Void... params) {
        System.out.println ("About to submit a text message request to " + messageData.getContactPhone());

        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        System.out.println ("Submitting the put status change call");
        // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
        SimpleMessageData response = null;
        try {
            response = restTemplate.postForObject(postSMSURL, messageData, SimpleMessageData.class);
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
        System.out.println("I'm in the postExecute method...");
        if (result.getErrorType() != null) {
            System.out.println ("There was an error posting the text message: " + result.getErrorMessage());
        } else {
            Toast.makeText(context, "Family has been notified of Hospital Selection.", Toast.LENGTH_LONG).show();
        }
    }
}
