package ac.ems.ambulance.terrapinems.rest;

import android.location.Location;
import android.os.AsyncTask;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ac.ems.ambulance.terrapinems.data.AmbulanceLocationData;
import ac.ems.ambulance.terrapinems.data.EventInformation;
import ac.ems.ambulance.terrapinems.data.SimpleMessageData;
import ac.ems.ambulance.terrapinems.data.UserInformation;

/**
 * Created by ac010168 on 11/7/15.
 */
public class PostLocationRestOperation extends AsyncTask<Void, Void, SimpleMessageData> {

    public final static String postLocationURL = "http://107.188.249.238:8080/ac-ems-system-rest-0.9/ambulance";
    public static boolean tieToEventFlag = true;
    private AmbulanceLocationData locationData = null;

    private Location location;

    public PostLocationRestOperation(Location location) {
        this.location = location;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        locationData = new AmbulanceLocationData();
        locationData.setAmbulanceID(UserInformation.ambulanceID);
        locationData.setAmbLat(location.getLatitude());
        locationData.setAmbLon(location.getLongitude());

        if (tieToEventFlag)
            locationData.setEventID(EventInformation.eventID);
    }

    @Override
    protected SimpleMessageData doInBackground(Void... params) {
        //System.out.println("Submitting location update " + (tieToEventFlag ? "and associating to event" : ""));

        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        //System.out.println ("Submitting the post location call");
        // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
        SimpleMessageData response = null;
        try {
            response = restTemplate.postForObject(postLocationURL, locationData, SimpleMessageData.class);
            //System.out.println ("I got a response for the location!");
        } catch (Throwable t) {
            //t.printStackTrace();
            response = new SimpleMessageData();
            response.setErrorType("Connection Error");
            response.setErrorMessage("There were problems submitting the last POST command: " + t.getMessage());
        }

        return response;
    }

    @Override
    protected void onPostExecute(SimpleMessageData result) {
        if (result.getErrorType() != null) {
            System.out.println ("There was a problem posting the location.");
        } else {
            System.out.println ("I successfully posted the location");
        }
        //Reset the location data so it's not used again
        locationData = null;
    }

}
