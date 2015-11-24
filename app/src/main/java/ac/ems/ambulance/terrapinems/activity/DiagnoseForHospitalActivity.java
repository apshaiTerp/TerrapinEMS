package ac.ems.ambulance.terrapinems.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.data.EventInformation;
import ac.ems.ambulance.terrapinems.data.EventStatusChangeData;
import ac.ems.ambulance.terrapinems.data.HospitalRouteData;
import ac.ems.ambulance.terrapinems.data.SMSMessageData;
import ac.ems.ambulance.terrapinems.data.UserInformation;
import ac.ems.ambulance.terrapinems.gps.GPSTrackerHost;
import ac.ems.ambulance.terrapinems.rabbit.PublishToRabbitTask;
import ac.ems.ambulance.terrapinems.rabbit.RabbitMQHost;
import ac.ems.ambulance.terrapinems.rest.NearestHospitalRestOperation;
import ac.ems.ambulance.terrapinems.rest.PostStatusChangeRestOperation;
import ac.ems.ambulance.terrapinems.rest.SMSMessageRestOperation;

public class DiagnoseForHospitalActivity extends AppCompatActivity {

    private Button childButton, teenButton, adultButton;
    private Button nonCritButton, minorButton, severeButton, burnButton, stemiButton, strokeButton;
    private Button recommendButton;

    private Button hospButton1, hospButton2, hospButton3;
    private TextView hospText1, etaLabelText1, hospETAText1, distLabelText1, distText1;
    private TextView hospText2, etaLabelText2, hospETAText2, distLabelText2, distText2;
    private TextView hospText3, etaLabelText3, hospETAText3, distLabelText3, distText3;

    private int ageFlag      = -1;
    private int criteriaFlag = -1;

    public final static int CHILD_FLAG = 0;
    public final static int TEEN_FLAG  = 1;
    public final static int ADULT_FLAG = 2;

    public final static int NONCRIT_FLAG = 0;
    public final static int MINOR_FLAG   = 1;
    public final static int SEVERE_FLAG  = 2;
    public final static int BURN_FLAG    = 3;
    public final static int STEMI_FLAG   = 4;
    public final static int STROKE_FLAG  = 5;

    private HospitalRouteData[] routeResults = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_for_hospital);

        RabbitMQHost.context = this;
        RabbitMQHost.activity = this;

        GPSTrackerHost.updateContext(DiagnoseForHospitalActivity.this);

        //Read in our first set of 'toggle' buttons
        childButton = (Button)findViewById(R.id.childButton);
        teenButton  = (Button)findViewById(R.id.teenButton);
        adultButton = (Button)findViewById(R.id.adultButton);

        nonCritButton = (Button)findViewById(R.id.nonCritButton);
        minorButton   = (Button)findViewById(R.id.minorButton);
        severeButton  = (Button)findViewById(R.id.severeButton);
        burnButton    = (Button)findViewById(R.id.burnButton);
        stemiButton   = (Button)findViewById(R.id.stemiButton);
        strokeButton  = (Button)findViewById(R.id.strokeButton);

        recommendButton = (Button)findViewById(R.id.hospitalButton);

        //Read in the components for showing the generated hospital data
        hospButton1    = (Button)findViewById(R.id.hospButton1);
        hospText1      = (TextView)findViewById(R.id.hospText1);
        etaLabelText1  = (TextView)findViewById(R.id.etaLabelText1);
        hospETAText1   = (TextView)findViewById(R.id.hospETAText1);
        distLabelText1 = (TextView)findViewById(R.id.distLabelText1);
        distText1      = (TextView)findViewById(R.id.distText1);

        hospButton2    = (Button)findViewById(R.id.hospButton2);
        hospText2      = (TextView)findViewById(R.id.hospText2);
        etaLabelText2  = (TextView)findViewById(R.id.etaLabelText2);
        hospETAText2   = (TextView)findViewById(R.id.hospETAText2);
        distLabelText2 = (TextView)findViewById(R.id.distLabelText2);
        distText2      = (TextView)findViewById(R.id.distText2);

        hospButton3    = (Button)findViewById(R.id.hospButton3);
        hospText3      = (TextView)findViewById(R.id.hospText3);
        etaLabelText3  = (TextView)findViewById(R.id.etaLabelText3);
        hospETAText3   = (TextView)findViewById(R.id.hospETAText3);
        distLabelText3 = (TextView)findViewById(R.id.distLabelText3);
        distText3      = (TextView)findViewById(R.id.distText3);

        //Build a group for the age ranges
        childButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teenButton.setBackgroundResource(android.R.drawable.btn_default);
                adultButton.setBackgroundResource(android.R.drawable.btn_default);
                childButton.setBackgroundResource(android.R.drawable.btn_default);

                childButton.setBackgroundColor(0xFF1F4BEA);
                ageFlag = CHILD_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });
        teenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teenButton.setBackgroundResource(android.R.drawable.btn_default);
                adultButton.setBackgroundResource(android.R.drawable.btn_default);
                childButton.setBackgroundResource(android.R.drawable.btn_default);

                teenButton.setBackgroundColor(0xFF1F4BEA);
                ageFlag = TEEN_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });
        adultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teenButton.setBackgroundResource(android.R.drawable.btn_default);
                adultButton.setBackgroundResource(android.R.drawable.btn_default);
                childButton.setBackgroundResource(android.R.drawable.btn_default);

                adultButton.setBackgroundColor(0xFF1F4BEA);
                ageFlag = ADULT_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });

        //Build a group for the criteria buttons
        nonCritButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonCritButton.setBackgroundResource(android.R.drawable.btn_default);
                minorButton.setBackgroundResource(android.R.drawable.btn_default);
                severeButton.setBackgroundResource(android.R.drawable.btn_default);
                burnButton.setBackgroundResource(android.R.drawable.btn_default);
                stemiButton.setBackgroundResource(android.R.drawable.btn_default);
                strokeButton.setBackgroundResource(android.R.drawable.btn_default);

                nonCritButton.setBackgroundColor(0xFF1F4BEA);
                criteriaFlag = NONCRIT_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });
        minorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonCritButton.setBackgroundResource(android.R.drawable.btn_default);
                minorButton.setBackgroundResource(android.R.drawable.btn_default);
                severeButton.setBackgroundResource(android.R.drawable.btn_default);
                burnButton.setBackgroundResource(android.R.drawable.btn_default);
                stemiButton.setBackgroundResource(android.R.drawable.btn_default);
                strokeButton.setBackgroundResource(android.R.drawable.btn_default);

                minorButton.setBackgroundColor(0xFF1F4BEA);
                criteriaFlag = MINOR_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });
        severeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonCritButton.setBackgroundResource(android.R.drawable.btn_default);
                minorButton.setBackgroundResource(android.R.drawable.btn_default);
                severeButton.setBackgroundResource(android.R.drawable.btn_default);
                burnButton.setBackgroundResource(android.R.drawable.btn_default);
                stemiButton.setBackgroundResource(android.R.drawable.btn_default);
                strokeButton.setBackgroundResource(android.R.drawable.btn_default);

                severeButton.setBackgroundColor(0xFF1F4BEA);
                criteriaFlag = SEVERE_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });
        burnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonCritButton.setBackgroundResource(android.R.drawable.btn_default);
                minorButton.setBackgroundResource(android.R.drawable.btn_default);
                severeButton.setBackgroundResource(android.R.drawable.btn_default);
                burnButton.setBackgroundResource(android.R.drawable.btn_default);
                stemiButton.setBackgroundResource(android.R.drawable.btn_default);
                strokeButton.setBackgroundResource(android.R.drawable.btn_default);

                burnButton.setBackgroundColor(0xFF1F4BEA);
                criteriaFlag = BURN_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });
        stemiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonCritButton.setBackgroundResource(android.R.drawable.btn_default);
                minorButton.setBackgroundResource(android.R.drawable.btn_default);
                severeButton.setBackgroundResource(android.R.drawable.btn_default);
                burnButton.setBackgroundResource(android.R.drawable.btn_default);
                stemiButton.setBackgroundResource(android.R.drawable.btn_default);
                strokeButton.setBackgroundResource(android.R.drawable.btn_default);

                stemiButton.setBackgroundColor(0xFF1F4BEA);
                criteriaFlag = STEMI_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });
        strokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonCritButton.setBackgroundResource(android.R.drawable.btn_default);
                minorButton.setBackgroundResource(android.R.drawable.btn_default);
                severeButton.setBackgroundResource(android.R.drawable.btn_default);
                burnButton.setBackgroundResource(android.R.drawable.btn_default);
                stemiButton.setBackgroundResource(android.R.drawable.btn_default);
                strokeButton.setBackgroundResource(android.R.drawable.btn_default);

                strokeButton.setBackgroundColor(0xFF1F4BEA);
                criteriaFlag = STROKE_FLAG;

                if ((ageFlag != -1) && (criteriaFlag != -1)) {
                    recommendButton.setEnabled(true);
                    recommendButton.setBackgroundColor(0xFF1F4BEA);
                    hideHospitalFields();
                }
            }
        });

        //Now we need to wire up the hospital choice buttons
        hospButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHospital(1);
            }
        });
        hospButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHospital(2);
            }
        });
        hospButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHospital(3);
            }
        });

        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearestHospitalRestOperation restOperation = new NearestHospitalRestOperation(ageFlag, criteriaFlag, getThis(), getThis());
                restOperation.execute();
            }
        });
    }

    private void selectHospital(int hospitalChoice) {
        HospitalRouteData chosenHospital = routeResults[hospitalChoice - 1];

        System.out.println("I want to choose Hospital " + hospitalChoice + " - " + chosenHospital.getHospitalName());

        //There need to be three things done:
        //1) POST the Status Update for the Event and rebuild our data
        //2) Send the message to the Hospital letting them know
        //3) Send a text to the contact number informing them of the choice

        //1) POST the Status Update for the Event and rebuild our data
        EventStatusChangeData changeData = new EventStatusChangeData();
        changeData.setEventState("Transporting");
        changeData.setChangeDescription("Ambulance " + UserInformation.ambulanceID + " will be transporting patient to " + chosenHospital.getHospitalName());
        changeData.setUserID(UserInformation.userID);
        switch(ageFlag) {
            case CHILD_FLAG : changeData.setObservedAge("Child"); break;
            case TEEN_FLAG  : changeData.setObservedAge("Teen");  break;
            case ADULT_FLAG : changeData.setObservedAge("Adult"); break;
        }
        switch(criteriaFlag) {
            case NONCRIT_FLAG : changeData.setObservedSeverity("basicER"); break;
            case MINOR_FLAG   : changeData.setObservedSeverity("minor"); break;
            case SEVERE_FLAG  : changeData.setObservedSeverity("severe"); break;
            case BURN_FLAG    : changeData.setObservedSeverity("burn"); break;
            case STEMI_FLAG   : changeData.setObservedSeverity("stemi"); break;
            case STROKE_FLAG  : changeData.setObservedSeverity("stroke"); break;
        }
        changeData.setTargetHospitalID(chosenHospital.getHospitalID());

        Intent nextIntent = new Intent(this, TravelToHospitalActivity.class);
        //Invoke the call
        PostStatusChangeRestOperation changeRestOperation = new PostStatusChangeRestOperation(changeData, DiagnoseForHospitalActivity.this, this, nextIntent);
        changeRestOperation.execute();

        //2) Send the message to the Hospital letting them know
        String hospitalMessage = "Ambulance " + UserInformation.ambulanceID + " is inbound with a patient.\n" +
                "The triage assessment indicates ";
        switch(criteriaFlag) {
            case NONCRIT_FLAG : hospitalMessage += (ageFlag == ADULT_FLAG) ? "Non-Critical Injuries" : "Pediatric Non-Critical Injuries"; break;
            case MINOR_FLAG   : hospitalMessage += (ageFlag == ADULT_FLAG) ? "Minor Trauma" : "Pediatric Trauma"; break;
            case SEVERE_FLAG  : hospitalMessage += (ageFlag == ADULT_FLAG) ? "Severe Trauma" : "Pediatric Trauma"; break;
            case BURN_FLAG    : hospitalMessage += (ageFlag == ADULT_FLAG) ? "Severe Burns" : "Pediatric Severe Burns"; break;
            case STEMI_FLAG   : hospitalMessage += "STEMI"; break;
            case STROKE_FLAG  : hospitalMessage += "Stroke"; break;
        }

        PublishToRabbitTask rabbitPublish = new PublishToRabbitTask(hospitalMessage, chosenHospital.getHospitalID());
        rabbitPublish.execute();

        //3) Send a text to the contact number informing them of the choice
        String textMessageBody = "Patient " + EventInformation.patientName + " is being transported to " +
                routeResults[hospitalChoice - 1].getHospitalName() + ".  Estimated ETA is " + routeResults[hospitalChoice - 1].getEtaString() +
                " mins.  Please do not respond to this message.";
        SMSMessageData messageData = new SMSMessageData();
        messageData.setContactPhone(EventInformation.contactPhone);
        messageData.setTextMessage(textMessageBody);

        SMSMessageRestOperation messageRestOperation = new SMSMessageRestOperation(messageData, this);
        messageRestOperation.execute();
    }

    private void hideHospitalFields() {
        //To avoid all the UI changes if not necessary
        if (hospButton1.getVisibility() == View.INVISIBLE)
            return;

        hospButton1.setVisibility(View.INVISIBLE);
        hospButton2.setVisibility(View.INVISIBLE);
        hospButton3.setVisibility(View.INVISIBLE);

        hospText1.setVisibility(View.INVISIBLE);
        etaLabelText1.setVisibility(View.INVISIBLE);
        hospETAText1.setVisibility(View.INVISIBLE);
        distLabelText1.setVisibility(View.INVISIBLE);
        distText1.setVisibility(View.INVISIBLE);

        hospText2.setVisibility(View.INVISIBLE);
        etaLabelText2.setVisibility(View.INVISIBLE);
        hospETAText2.setVisibility(View.INVISIBLE);
        distLabelText2.setVisibility(View.INVISIBLE);
        distText2.setVisibility(View.INVISIBLE);

        hospText3.setVisibility(View.INVISIBLE);
        etaLabelText3.setVisibility(View.INVISIBLE);
        hospETAText3.setVisibility(View.INVISIBLE);
        distLabelText3.setVisibility(View.INVISIBLE);
        distText3.setVisibility(View.INVISIBLE);

        hospETAText1.setText("");
        distText1.setText("");
        hospETAText2.setText("");
        distText2.setText("");
        hospETAText3.setText("");
        distText3.setText("");
    }

    private void showHospitalFields() {
        hospButton1.setVisibility(View.VISIBLE);
        hospButton2.setVisibility(View.VISIBLE);
        hospButton3.setVisibility(View.VISIBLE);

        hospText1.setVisibility(View.VISIBLE);
        etaLabelText1.setVisibility(View.VISIBLE);
        hospETAText1.setVisibility(View.VISIBLE);
        distLabelText1.setVisibility(View.VISIBLE);
        distText1.setVisibility(View.VISIBLE);

        hospText2.setVisibility(View.VISIBLE);
        etaLabelText2.setVisibility(View.VISIBLE);
        hospETAText2.setVisibility(View.VISIBLE);
        distLabelText2.setVisibility(View.VISIBLE);
        distText2.setVisibility(View.VISIBLE);

        hospText3.setVisibility(View.VISIBLE);
        etaLabelText3.setVisibility(View.VISIBLE);
        hospETAText3.setVisibility(View.VISIBLE);
        distLabelText3.setVisibility(View.VISIBLE);
        distText3.setVisibility(View.VISIBLE);
    }

    public void showHospitalResults(HospitalRouteData[] routeResults) {
        hospETAText1.setText("");
        distText1.setText("");
        hospETAText2.setText("");
        distText2.setText("");
        hospETAText3.setText("");
        distText3.setText("");

        this.routeResults = routeResults;

        hospText1.setText(routeResults[0].getHospitalName());
        hospETAText1.setText(routeResults[0].getEtaString() + " mins");
        distText1.setText(routeResults[0].getDistanceText());

        hospText2.setText(routeResults[1].getHospitalName());
        hospETAText2.setText(routeResults[1].getEtaString() + " mins");
        distText2.setText(routeResults[1].getDistanceText());

        hospText3.setText(routeResults[2].getHospitalName());
        hospETAText3.setText(routeResults[2].getEtaString() + " mins");
        distText3.setText(routeResults[2].getDistanceText());

        showHospitalFields();
    }

    public DiagnoseForHospitalActivity getThis() {
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
