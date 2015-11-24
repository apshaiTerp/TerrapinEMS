package ac.ems.ambulance.terrapinems.activity;

import android.content.Intent;
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

public class AcceptEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_event);

        RabbitMQHost.context = this;
        RabbitMQHost.activity = this;

        //We need to fill out the actual field values right now
        TextView headerText           = (TextView)findViewById(R.id.headerText);
        TextView nameTextView         = (TextView)findViewById(R.id.nameTextView);
        TextView addressTextView      = (TextView)findViewById(R.id.addressTextView);
        TextView demoTextView         = (TextView)findViewById(R.id.demoTextView);
        TextView severityTextView     = (TextView)findViewById(R.id.severityTextView);
        TextView reportedTextView     = (TextView)findViewById(R.id.reportedTextView);
        TextView complaintTextView    = (TextView)findViewById(R.id.complaintTextView);
        TextView hospitalNameTextView = (TextView)findViewById(R.id.hospitalNameTextView);

        headerText.setText("Dispatch Assignment Details for Dispatch " + EventInformation.dispatchID);

        nameTextView.setText(EventInformation.patientName);
        addressTextView.setText(EventInformation.patientAddress);
        severityTextView.setText(EventInformation.reportedSeverity);
        reportedTextView.setText(EventInformation.reportedByName);
        complaintTextView.setText(EventInformation.patientComplaint);

        hospitalNameTextView.setText(EventInformation.recommendedHospitalName == null ? "No Recommendation at this time" : EventInformation.recommendedHospitalName);

        if (EventInformation.patientGender.equalsIgnoreCase("Unknown") && EventInformation.patientAgeRange.equalsIgnoreCase("Unknown"))
            demoTextView.setText("Unknown");
        else
            demoTextView.setText(EventInformation.patientGender + " " + EventInformation.patientAgeRange);

        Button acceptButton = (Button)findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAcceptEvent();
            }
        });
    }

    private void processAcceptEvent() {
        //Start up the GPS Tracker
        GPSTrackerHost.beginGPSTracking(AcceptEventActivity.this);

        //PUT the state change to Accepted
        EventStatusChangeData changeData = new EventStatusChangeData();
        changeData.setEventState("Responding");
        changeData.setChangeDescription("Ambulance " + UserInformation.ambulanceID + " is now responding to Event " + EventInformation.eventID);
        changeData.setUserID(UserInformation.userID);

        Intent nextIntent = new Intent(this, TravelToIncidentActivity.class);

        //Invoke the call
        PostStatusChangeRestOperation changeRestOperation = new PostStatusChangeRestOperation(changeData, AcceptEventActivity.this, this, nextIntent);
        changeRestOperation.execute();
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
        System.out.println("Yeah!  I'm back");

        RabbitMQHost.context = this;
        RabbitMQHost.activity = this;

        super.onResume();
    }
}
