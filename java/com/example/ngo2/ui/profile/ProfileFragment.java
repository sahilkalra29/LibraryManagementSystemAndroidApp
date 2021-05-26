package com.example.ngo2.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ngo2.MainActivity;
import com.example.ngo2.R;
import com.example.ngo2.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private String emailId;
    Boolean isAsyncRunning = false, isAsyncRunning1 = false, isAsyncRunning2 = false;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String jsonData;
    String studentId = ""; /* It is kept global so that can be used for other operations based on Student ID */
    public Context context_var;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        context_var = container.getContext();

        root = inflater.inflate(R.layout.fragment_profile, container, false);
        emailId = SaveSharedPreference.getEmail(context_var);

        if(!isAsyncRunning) {
            GetProfileBackgroundWorker getProfileBackgroundWorker = new GetProfileBackgroundWorker(context_var);
            getProfileBackgroundWorker.execute(emailId);
        }
        return root;
    }

    @SuppressLint("ResourceType")
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getLayoutInflater().inflate(R.menu.menu_main, (ViewGroup) menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout) {
            SaveSharedPreference.clearEmail(context_var);
            Intent i = new Intent(context_var, MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /* Background Thread to fetch all the profile details */
    class GetProfileBackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;

        GetProfileBackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String emailId = params[0];
            try {
                String login_url = "http://192.168.1.40/library/app/profile.php";
                URL url = new URL(login_url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setDoInput(true);

                OutputStream outputStream = c.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =  URLEncoder.encode("emailId","UTF-8")+"="+URLEncoder.encode(emailId,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = c.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                c.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            isAsyncRunning = true;
        }

        /* Data Fetched from SQL query is in the JSON Format and converted to Listview*/
        @Override
        protected void onPostExecute(String result) {
            TextView tv_studentid = root.findViewById(R.id.tv_profile_studentid);
            TextView tv_name = root.findViewById(R.id.tv_profile_name);
            TextView tv_email = root.findViewById(R.id.tv_profile_email);
            TextView tv_address = root.findViewById(R.id.tv_profile_address);
            TextView tv_mobileno = root.findViewById(R.id.tv_profile_mobileno);
            TextView tv_regDate = root.findViewById(R.id.tv_profile_regDate);

            jsonData = result;
            try{
                jsonObject = new JSONObject(jsonData);
                jsonArray = jsonObject.getJSONArray("server_response");
                String regDate = "", updationDate, status, name = "", mobileno="", address = "";
                int count=0;
                while(count< jsonArray.length()) {
                    JSONArray jo = jsonArray.getJSONArray(count);
                    studentId = jo.getString(0);
                    name = jo.getString(1);
                    mobileno = jo.getString(2);
                    address = jo.getString(3);
                    status = jo.getString(4);
                    regDate = jo.getString(5);
                    updationDate = jo.getString(6);
                    count++;
                }

                tv_name.setText(name);
                tv_email.setText(emailId);
                tv_address.setText(address);
                tv_mobileno.setText(mobileno);
                tv_regDate.setText(regDate);
                tv_studentid.setText(studentId);

                if(!isAsyncRunning1) {
                    GetProfileIssuedBooksBackgroundWorker getProfileIssuedBooksBackgroundWorker = new GetProfileIssuedBooksBackgroundWorker(context_var);
                    getProfileIssuedBooksBackgroundWorker.execute();
                }
                if(!isAsyncRunning2) {
                    GetProfileFineBackgroundWorker getProfileFineBackgroundWorker = new GetProfileFineBackgroundWorker(context_var);
                    getProfileFineBackgroundWorker.execute();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    /* Background Thread to fetch all the issued books details of the user */
    class GetProfileIssuedBooksBackgroundWorker extends AsyncTask<Void, Void, String> {

        Context context;

        GetProfileIssuedBooksBackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(Void... Void) {
            try {
                String login_url = "http://192.168.1.40/library/app/profileissuedbooks.php";
                URL url = new URL(login_url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setDoInput(true);

                OutputStream outputStream = c.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =  URLEncoder.encode("studentId","UTF-8")+"="+URLEncoder.encode(studentId,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = c.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                c.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            isAsyncRunning1 = true;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tv_issuedbooks = root.findViewById(R.id.tv_profile_issuedbooks);
            tv_issuedbooks.setText(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    /* Background Thread to fetch the total fine of the user */
    class GetProfileFineBackgroundWorker extends AsyncTask<Void, Void, String> {

        Context context;

        GetProfileFineBackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(Void... Void) {

            try {
                String login_url = "http://192.168.1.40/library/app/profilefine.php";
                URL url = new URL(login_url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setDoInput(true);

                OutputStream outputStream = c.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =  URLEncoder.encode("studentId","UTF-8")+"="+URLEncoder.encode(studentId,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = c.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                c.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            isAsyncRunning2 = true;
        }

        /* Data Fetched from SQL query is in the JSON Format and converted to Listview*/
        @Override
        protected void onPostExecute(String result) {
            TextView tv_fine = root.findViewById(R.id.tv_profile_fine);
            tv_fine.setText(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}