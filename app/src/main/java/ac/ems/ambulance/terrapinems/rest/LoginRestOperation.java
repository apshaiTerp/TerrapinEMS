package ac.ems.ambulance.terrapinems.rest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ac.ems.ambulance.terrapinems.activity.AcceptDispatchActivity;
import ac.ems.ambulance.terrapinems.activity.LoginActivity;
import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.data.LoginData;
import ac.ems.ambulance.terrapinems.data.LoginSuccessMessage;
import ac.ems.ambulance.terrapinems.data.UserInformation;

/**
 * Created by ac010168 on 11/5/15.
 */
public class LoginRestOperation extends AsyncTask<Void, Void, LoginSuccessMessage>{

    public final static String loginURL = "http://107.188.249.238:8080/ac-ems-system-rest-0.9/login";

    private LoginData loginData;
    private LoginActivity activity;
    private Context dialogContext;

    public LoginRestOperation(LoginData loginData, LoginActivity activity, Context dialogContext) {
        this.loginData     = loginData;
        this.activity      = activity;
        this.dialogContext = dialogContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LoginSuccessMessage doInBackground(Void... params) {
        System.out.println("Submitting uname/pass: " + loginData.getUserName() + "/" + loginData.getPassword());

        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        System.out.println ("Submitting the call");
        // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
        LoginSuccessMessage response = null;
        try  {
            response = restTemplate.postForObject(loginURL, loginData, LoginSuccessMessage.class);
            System.out.println ("I got a response!");
        } catch (Throwable t) {
            t.printStackTrace();
            response = new LoginSuccessMessage();
            response.setErrorType("Login Connection Error");
            response.setErrorMessage(t.getMessage());
        }

        return response;
    }

    @Override
    protected void onPostExecute(LoginSuccessMessage result) {
        if (result.getErrorType() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
            builder.setMessage(result.getErrorMessage());
            builder.setTitle(result.getErrorType());
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //NOOP
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            UserInformation.userID          = result.getUserID();
            UserInformation.userDisplayName = result.getUserName();
            UserInformation.userRole        = result.getUserRole();
            UserInformation.ambulanceID     = loginData.getAuthorizeID();

            System.out.println("Ready to move on to the next screen!");

            Intent nextIntent = new Intent(activity, AcceptDispatchActivity.class);
            activity.startActivity(nextIntent);


        }
        super.onPostExecute(result);
    }

}
