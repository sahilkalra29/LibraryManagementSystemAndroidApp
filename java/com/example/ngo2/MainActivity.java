/* MODIFICATION HISTORY
S.No.   Date            Name            Description
----------------------------------------------------------------------------------
1.      15-04-2021      Sahil Kalra     Login Page and Link to SignUp Page
----------------------------------------------------------------------------------
*/
package com.example.ngo2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    GetResponseBackgroundWorker getResponseBackgroundWorker = new GetResponseBackgroundWorker(this);

    Boolean isAsyncRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if(SaveSharedPreference.getEmail(MainActivity.this).length() != 0)
        {
            Intent activity = new Intent(getApplicationContext(), UserDashboard.class);
            startActivity(activity);
            finish(); /* Kill the previous activity */
        }

        editID = findViewById(R.id.editID);
        editPass = findViewById(R.id.editPass);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        toSignUp = findViewById(R.id.toSignUp);
        buttonSignIn.setOnClickListener(this);
        toSignUp.setOnClickListener(this);
    }

    private TextInputLayout editID;
    private TextInputLayout editPass;
    private Button buttonSignIn;
    private TextView toSignUp;

    private boolean verifyEmailId()
    {
        String emailId=editID.getEditText().getText().toString().trim();
        if(emailId.isEmpty())
        {   editID.setErrorEnabled(true);
            editID.setError("Email ID Required");
            return true;
        }
        else
        {
            editID.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyPass()
    {
        String pass=editPass.getEditText().getText().toString().trim();
        if(pass.isEmpty())
        {   editPass.setErrorEnabled(true);
            editPass.setError("Password Required");
            return true;
        }
        else
        {
            editPass.setErrorEnabled(false);
            return false;
        }
    }

    private void signInUser() {

        boolean res= (verifyEmailId()|verifyPass());
        if(res==true)
            return;

        String emailId=editID.getEditText().getText().toString().trim();
        String password=editPass.getEditText().getText().toString().trim();
        String type = "login";

        GetResponseBackgroundWorker getResponseBackgroundWorker = new GetResponseBackgroundWorker(this);
        if(!isAsyncRunning) {
            getResponseBackgroundWorker.delegate = this;
            getResponseBackgroundWorker.execute(type, emailId, password);
        }
    }

    @Override
    public void processFinish(String result) throws URISyntaxException {
        //Here you will receive the result fired from async class of onPostExecute(result) method.
        String newresult="";
        try {
            if (result.equals("0")) {
                newresult = " Invalid Login Credentials !!";
            } else if (result.equals("1")) {

                String emailId = editID.getEditText().getText().toString().trim();

                SaveSharedPreference.setEmail(getApplicationContext(), emailId);

                newresult = " Login Successful !!";

                Intent i = new Intent(getApplicationContext(), UserDashboard.class);
                i.putExtra("emailId", emailId);
                startActivity(i);
                finish(); /* Kill the previous activity */
            } else if (result.equals("2")) {
                newresult = " Student ID Blocked. Please contact Administrator !!";
            } else {
                newresult = " Wrong Opcode !!";
            }
            Toast.makeText(getApplicationContext(), newresult, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Please Check Internet Connectivity !!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignIn)
            signInUser();

        else if (v == toSignUp) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    }
}