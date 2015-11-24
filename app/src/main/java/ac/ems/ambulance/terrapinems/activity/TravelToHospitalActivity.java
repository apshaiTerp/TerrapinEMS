package ac.ems.ambulance.terrapinems.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.data.EventInformation;
import ac.ems.ambulance.terrapinems.data.EventStatusChangeData;
import ac.ems.ambulance.terrapinems.data.UserInformation;
import ac.ems.ambulance.terrapinems.gps.GPSTrackerHost;
import ac.ems.ambulance.terrapinems.rabbit.RabbitMQHost;
import ac.ems.ambulance.terrapinems.rest.PostStatusChangeRestOperation;

public class TravelToHospitalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_to_hospital);

        RabbitMQHost.context = this;
        RabbitMQHost.activity = this;

        TextView titleText = (TextView)findViewById(R.id.hospitalHeaderView);
        titleText.setText("Transporting to " + EventInformation.targetHospitalName);

        //Hook up the three buttons
        Button navHospitalButton  = (Button)findViewById(R.id.navHospitalButton);
        Button atHospitalButton   = (Button)findViewById(R.id.atHospitalButton);
        Button changeTriageButton = (Button)findViewById(R.id.changeTriageButton);

        navHospitalButton.setOnClickListener(new View.OnClickListener() {
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

        atHospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PUT the state change to Arrived
                EventStatusChangeData changeData = new EventStatusChangeData();
                changeData.setEventState("Arrived");
                changeData.setChangeDescription("Ambulance " + UserInformation.ambulanceID + " has arrived at the Hospital.  Response Complete.");
                changeData.setUserID(UserInformation.userID);

                Intent nextIntent = new Intent(getThis(), AcceptDispatchActivity.class);

                //Stop tracking GPS
                GPSTrackerHost.stopGPSTracking();

                //Invoke the call
                PostStatusChangeRestOperation changeRestOperation = new PostStatusChangeRestOperation(changeData, TravelToHospitalActivity.this, getThis(), nextIntent);
                changeRestOperation.execute();
            }
        });

        changeTriageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //In this case, we just need to navigate back to the Diagnose page
                Intent nextIntent = new Intent(getThis(), DiagnoseForHospitalActivity.class);
                startActivity(nextIntent);
            }
        });
    }

    public TravelToHospitalActivity getThis() {
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
