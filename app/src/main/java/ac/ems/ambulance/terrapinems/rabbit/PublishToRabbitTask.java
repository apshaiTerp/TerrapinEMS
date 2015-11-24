package ac.ems.ambulance.terrapinems.rabbit;

import android.os.AsyncTask;

/**
 * Created by ac010168 on 11/13/15.
 */
public class PublishToRabbitTask extends AsyncTask<Void, Void, Void> {

    private String message;
    private long   hospitalID;

    public PublishToRabbitTask(String message, long hospitalID) {
        this.message    = message;
        this.hospitalID = hospitalID;
    }

    @Override
    protected Void doInBackground(Void... params) {
        RabbitMQHost.publishMessage(message, hospitalID);
        return null;
    }
}
