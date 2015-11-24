package ac.ems.ambulance.terrapinems.rabbit;

import android.os.AsyncTask;

/**
 * Created by ac010168 on 11/13/15.
 */
public class ConnectToRabbitTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        RabbitMQHost.openRabbitConnection();
        return null;
    }
}
