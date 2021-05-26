/* MODIFICATION HISTORY
S.No.   Date            Name            Description
-------------------------------------------------------------------------------
1.      15-04-2021      Sahil Kalra     SignUp Page and link to Login Page
-------------------------------------------------------------------------------
*/
package com.example.ngo2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse{

    private TextInputLayout editName;
    private TextInputLayout editMobileNo;
    private TextInputLayout editAddress;
    private TextInputLayout editEmailId;
    private TextInputLayout editPassword;
    private TextInputLayout editPassword1;
    private Button buttonRegister;
    private TextView toSignIn;
    private CheckBox check1;
    GetResponseBackgroundWorker getResponseBackgroundWorker = new GetResponseBackgroundWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        editName = findViewById(R.id.editName);
        editMobileNo = findViewById(R.id.editMobileNo);
        editAddress = findViewById(R.id.editAddress);
        editEmailId = findViewById(R.id.editEmailId);
        editPassword =  findViewById(R.id.editPassword);
        editPassword1=findViewById(R.id.editPassword1);
        buttonRegister =  findViewById(R.id.buttonRegister);
        toSignIn =  findViewById(R.id.toSignIn);
        check1=findViewById(R.id.check1);

        buttonRegister.setOnClickListener(this);
        toSignIn.setOnClickListener(this);
        check1.setOnClickListener(this);
    }

    private boolean verifyName()
    {
        String username = editName.getEditText().getText().toString().trim();
        if(username.isEmpty())
        {   editName.setErrorEnabled(true);
            editName.setError("Name Required");
            return true;
        }
        else
        {
            editName.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyMobileNo()
    {
        String usermobileNo = editMobileNo.getEditText().getText().toString().trim();
        if(usermobileNo.isEmpty())
        {   editMobileNo.setErrorEnabled(true);
            editMobileNo.setError("Mobile No. Required");
            return true;
        }
        else
        {
            editMobileNo.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyAddress()
    {
        String useraddress = editAddress.getEditText().getText().toString().trim();
        if(useraddress.isEmpty())
        {   editAddress.setErrorEnabled(true);
            editAddress.setError("Address No. Required");
            return true;
        }
        else
        {
            editAddress.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyEmailId()
    {
        String useremailId = editEmailId.getEditText().getText().toString().trim();
        if(useremailId.isEmpty())
        {   editEmailId.setErrorEnabled(true);
            editEmailId.setError(" Email ID Required");
            return true;
        }
        else if(!(isValidEmail(useremailId)))
        {
            editEmailId.setError(" Invalid Email ID");
        }
        else
        {
            editEmailId.setErrorEnabled(false);
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email)
    {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }

    private boolean verifyPass()
    {
        String userpass=editPassword.getEditText().getText().toString().trim();
        if(userpass.isEmpty())
        {   editPassword.setErrorEnabled(true);
            editPassword.setError(" Password Required");
            return true;
        }
        else
        {
            editPassword.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyPass1()
    {
        String userpass1 = editPassword1.getEditText().getText().toString().trim();
        String userpass = editPassword.getEditText().getText().toString().trim();
        if(userpass1.isEmpty())
        {   editPassword1.setErrorEnabled(true);
            editPassword1.setError("Confirm Password Required");
            return true;
        }
        else if(userpass.equals(userpass1))
        {
            editPassword1.setErrorEnabled(false);
            return false;
        }
        else
        {
            editPassword1.setErrorEnabled(true);
            editPassword1.setError("Passwords do not match");
            return true;
        }
    }

    private void registerUser()
    {
        boolean res= (verifyName() | verifyMobileNo() | verifyAddress() | verifyEmailId() | verifyPass() | verifyPass1());
        if(res == true)
            return;

        String name     = editName.getEditText().getText().toString().trim();
        String mobileNo = editMobileNo.getEditText().getText().toString().trim();
        String address  = editAddress.getEditText().getText().toString().trim();
        String EmailId  = editEmailId.getEditText().getText().toString().trim();
        String password = editPassword.getEditText().getText().toString().trim();

        String type = "signup";

        getResponseBackgroundWorker.delegate = this;
        getResponseBackgroundWorker.execute(type, name, mobileNo, address, EmailId, password);
    }

    @Override
    public void processFinish(String result){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        String newresult="";
        if(result.equals("3")) {
            newresult = " Registration Successful !!";
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish(); /* Kill the previous activity */
        }
        else if(result.equals("4")) {
            newresult = " Registration Failed. Please Try Again !!";
        }
        else if(result.equals("5")) {
            newresult = "Email Id Already Exists";
        }
        else {
            newresult = " Wrong Opcode !!";
        }
        Toast.makeText(getApplicationContext(), newresult, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        if(v == check1)
        {
            if(check1.isChecked()) {
                buttonRegister.setEnabled(true);
                buttonRegister.setBackgroundColor(getResources().getColor(R.color.starblue));
            }
            else{
                buttonRegister.setEnabled(false);
                buttonRegister.setBackgroundColor(getResources().getColor(R.color.grey));
            }

        }
        else if(v == buttonRegister)
            registerUser();

        else if(v == toSignIn) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}