package ac.ems.ambulance.terrapinems.rabbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.data.UserInformation;

/**
 * Created by ac010168 on 11/13/15.
 */
public class RabbitMQHost {

    public static Context context   = null;
    public static Activity activity = null;

    private static SimpleRabbitMQ rabbitMQ = null;

    public static void openRabbitConnection() {
        rabbitMQ = new SimpleRabbitMQ(UserInformation.ambulanceID);
    }

    public static void closeRabbitConnection() {
        if (rabbitMQ != null) {
            rabbitMQ.closeConnection();
        }
    }

    public static void publishMessage(String message, long hospitalID) {
        if (rabbitMQ != null) {
            rabbitMQ.publishMessage(message, hospitalID);
        }
    }

    public static void publishDialog(String message) {
        System.out.println ("I'm about to notify you of some good stuff!");

    }
}
