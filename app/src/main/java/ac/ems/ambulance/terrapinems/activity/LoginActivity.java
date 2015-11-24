package ac.ems.ambulance.terrapinems.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ac.ems.ambulance.terrapinems.R;
import ac.ems.ambulance.terrapinems.data.LoginData;
import ac.ems.ambulance.terrapinems.rabbit.DisconnectFromRabbitTask;
import ac.ems.ambulance.terrapinems.rest.LoginRestOperation;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Spinner ambSpinner = (Spinner)findViewById(R.id.loginSpinner);
        ArrayAdapter<CharSequence> ambAdapter = ArrayAdapter.createFromResource(
                this, R.array.ambulances, R.layout.spinner_layout);
        ambAdapter.setDropDownViewResource(R.layout.spinner_layout);
        ambSpinner.setAdapter(ambAdapter);

        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("I clicked the Button");
                EditText userText = (EditText) findViewById(R.id.editText);
                EditText passText = (EditText) findViewById(R.id.editText2);
                Spinner ambSpinner = (Spinner) findViewById(R.id.loginSpinner);

                String userName = userText.getText().toString().trim();
                String password = passText.getText().toString().trim();
                String ambSelect = ambSpinner.getSelectedItem().toString().trim();

                System.out.println("The User Name is: " + userName);
                System.out.println("The Password  is: " + password);
                System.out.println("The Ambulance ID: " + ambSelect);

                if (userName.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.username_login_error);
                    builder.setTitle(R.string.login_dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //NOOP
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (password.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.password_login_error);
                    builder.setTitle(R.string.login_dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //NOOP
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (ambSelect.equalsIgnoreCase("Choose Your Ambulance")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.ambulance_login_error);
                    builder.setTitle(R.string.login_dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //NOOP
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    String ambulanceID = ambSelect.replace("Ambulance ", "");
                    System.out.println("Ambulance ID: " + ambulanceID);

                    //Now we're ready to attempt the login call.
                    LoginData loginData = new LoginData();
                    loginData.setUserName(userName);
                    loginData.setPassword(password);
                    loginData.setUserRole("AMBULANCE");
                    loginData.setAuthorizeID(Long.parseLong(ambulanceID));

                    //Now invoke the REST call.
                    LoginRestOperation login = new LoginRestOperation(loginData, getThis(), LoginActivity.this);
                    login.execute();
                }
            }
        });
    }

    public LoginActivity getThis() {
        return this;
    }

    @Override
    protected void onDestroy() {
        System.out.println("I'm blowing up!");
        DisconnectFromRabbitTask rabbitTask = new DisconnectFromRabbitTask();
        rabbitTask.execute();

        super.onDestroy();
    }
}
