package ac.ems.ambulance.terrapinems.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.rabbit.ConnectToRabbitTask;
import ac.ems.ambulance.terrapinems.rabbit.RabbitMQHost;
import ac.ems.ambulance.terrapinems.rest.GetEventRestOperation;

public class AcceptDispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_dispatch);

        //Launch the Rabbit Connection
        //Open the Rabbit Connection Right away
        ConnectToRabbitTask rabbitTask = new ConnectToRabbitTask();
        rabbitTask.execute();

        RabbitMQHost.context  = this;
        RabbitMQHost.activity = this;

        Intent nextIntent = new Intent(this, AcceptEventActivity.class);
        GetEventRestOperation restOperation = new GetEventRestOperation(getThis(), AcceptDispatchActivity.this, nextIntent);
        restOperation.execute();
    }

    public AcceptDispatchActivity getThis() {
        return this;
    }

    public void hadNoHits() {
        System.out.println ("Do something here to show I didn't find any dispatches");
        TextView checkingTextView = (TextView)findViewById(R.id.checkingTextView);
        checkingTextView.setText("No Dispatches Found.\nWait for Dispatch to be received");
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
