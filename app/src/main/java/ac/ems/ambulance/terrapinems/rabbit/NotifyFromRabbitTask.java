package ac.ems.ambulance.terrapinems.rabbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.activity.AcceptDispatchActivity;
import ac.ems.ambulance.terrapinems.activity.AcceptEventActivity;
import ac.ems.ambulance.terrapinems.data.EventInformation;
import ac.ems.ambulance.terrapinems.rest.GetEventRestOperation;

/**
 * Created by ac010168 on 11/13/15.
 */
public class NotifyFromRabbitTask extends AsyncTask<Void, Void, Void> {
    private String message;
    private Context context;
    private Activity activity;

    public NotifyFromRabbitTask(String message) {
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... params) {
        context  = RabbitMQHost.context;
        activity = RabbitMQHost.activity;




        return null;
    }

    @Override
    protected void onPostExecute(Void param) {
        System.out.println ("I've got a message to show: " + message);

        //If we have a context, we're still in control, and show a dialog.
        //If not, we need to send a system notification
        if (context != null) {
            //There are three possible states the system could be in:
            //1) We have no events, and are being notified we have a new event.
            //2) We have an event, and are in an Assigned or Responding state.  The only valid message is to Cancel the call
            //3) We have an event, and we are Transporting the patient.  The only valid message is to Divert.

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message);
            builder.setTitle("System Notification");

            if (EventInformation.eventState == null) {
                //This is Scenario 1
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (activity != null) {
                            Intent nextIntent = new Intent(activity, AcceptEventActivity.class);
                            GetEventRestOperation restOperation = new GetEventRestOperation(activity, context, nextIntent);
                            restOperation.execute();
                        }
                    }
                });

            } else if (EventInformation.eventState.equalsIgnoreCase("Assigned") || EventInformation.eventState.equalsIgnoreCase("Responding")) {
                //This is scenario 2
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (activity != null) {
                            EventInformation.resetEventData();
                            Intent nextIntent = new Intent(context, AcceptDispatchActivity.class);
                            activity.startActivity(nextIntent);
                        }
                    }
                });
            } else if (EventInformation.eventState.equalsIgnoreCase("Transporting")) {
                //This is scenario 3
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println ("I've got a diversion Request!");
                        EventInformation.divertHospitalID = EventInformation.targetHospitalID;

                        //TODO - Do something about resetting this data
                        //if (activity != null) {
                        //    Intent nextIntent = new Intent(context, activity.getClass());
                        //    activity.startActivity(nextIntent);
                        //}
                    }
                });
            }

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            //There are two possible states we could be in:
            //1) We are in the Google Maps view en route to incident.  The only valid message is to Cancel the Call
            //2) We are in the Google Maps view en route to Hospital.  THe only valid message is to Divert.

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setSound(soundUri)
                            .setContentTitle("TerrapinEMS Alert")
                            .setContentText(message);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, activity.getClass());

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(activity.getClass());
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
