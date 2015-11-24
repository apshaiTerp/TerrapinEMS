package ac.ems.ambulance.terrapinems.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.data.EventInformation;
import ac.ems.ambulance.terrapinems.data.EventStatusChangeData;
import ac.ems.ambulance.terrapinems.data.LoginData;
import ac.ems.ambulance.terrapinems.data.UserInformation;
import ac.ems.ambulance.terrapinems.gps.GPSTrackerHost;
import ac.ems.ambulance.terrapinems.rabbit.ConnectToRabbitTask;
import ac.ems.ambulance.terrapinems.rabbit.DisconnectFromRabbitTask;
import ac.ems.ambulance.terrapinems.rabbit.RabbitMQHost;
import ac.ems.ambulance.terrapinems.rest.LoginRestOperation;
import ac.ems.ambulance.terrapinems.rest.PostStatusChangeRestOperation;

public class TravelToIncidentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_to_incident);

        RabbitMQHost.context = this;
        RabbitMQHost.activity = this;

        //Hook up the three buttons
        Button incidentButton       = (Button)findViewById(R.id.incidentButton);
        Button onSiteButton         = (Button)findViewById(R.id.onSiteButton);
        Button cancelResponseButton = (Button)findViewById(R.id.cancelResponseButton);

        incidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Need to initiate the Directions View
                String uriString = "google.navigation:q=" + EventInformation.incidentLat + "," + EventInformation.incidentLon;
                System.out.println ("Here's the URI String: " + uriString);

                Uri gmmIntentUri = Uri.parse(uriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        onSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PUT the state change to Accepted
                EventStatusChangeData changeData = new EventStatusChangeData();
                changeData.setEventState("On Site");
                changeData.setChangeDescription("Ambulance " + UserInformation.ambulanceID + " has now arrived on site and is beginning Triage.");
                changeData.setUserID(UserInformation.userID);

                Intent nextIntent = new Intent(getThis(), DiagnoseForHospitalActivity.class);

                //Invoke the call
                PostStatusChangeRestOperation changeRestOperation = new PostStatusChangeRestOperation(changeData, TravelToIncidentActivity.this, getThis(), nextIntent);
                changeRestOperation.execute();
            }
        });

        cancelResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PUT the state change to Call Cancelled
                EventStatusChangeData changeData = new EventStatusChangeData();
                changeData.setEventState("Call Cancelled");
                changeData.setChangeDescription("Ambulance " + UserInformation.ambulanceID + " is no longer needed.  Call Cancelled");
                changeData.setUserID(UserInformation.userID);

                Intent nextIntent = new Intent(getThis(), AcceptDispatchActivity.class);

                //Stop tracking GPS
                GPSTrackerHost.stopGPSTracking();

                //Invoke the call
                PostStatusChangeRestOperation changeRestOperation = new PostStatusChangeRestOperation(changeData, TravelToIncidentActivity.this, getThis(), nextIntent);
                changeRestOperation.execute();
            }
        });
    }

    public TravelToIncidentActivity getThis() {
        return this;
    }

    @Override
    public void onPause() {
        //This will typically happen when control is yielded to the Google Maps App
        System.out.println ("Oh No!  I've been paused!");

        RabbitMQHost.context = null;
        RabbitMQHost.activity = null;

        super.onPause();
    }

    @Override
    public void onResume() {
        //This will typically happen when regaining focus following the Google Maps excursion
        System.out.println ("Yeah!  I'm back");

        RabbitMQHost.context = this;
        RabbitMQHost.activity = this;

        super.onResume();
    }
}
