package com.example.ngo2.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ngo2.MainActivity;
import com.example.ngo2.R;
import com.example.ngo2.SaveSharedPreference;
import com.example.ngo2.ui.profile.ProfileFragment;

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

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String emailId;
    Boolean isAsyncRunning = false, isAsyncRunning1 = false, isAsyncRunning2 = false;
    String jsonList;
    IssuedBookListAdapter issuedBookListAdapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    String name, isbn, issued_date, return_date, fine;
    IssuedBookList issuedBookList;
    View root;
    Context context_var;
    String studentId = ""; /* It is kept global so that can be used for other operations based on Student ID */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
         root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        context_var = container.getContext();

        emailId = SaveSharedPreference.getEmail(context_var);
        Log.i("info", "SK ---> >>> dashboard Fragment ---- >>>>>>> " + emailId);

        //if(!isAsyncRunning)
        {
            Log.i("info", "SK ---> >>> dashboard Fragment async ---- >>>>>>> ");
            GetStudentIdBackgroundWorker getStudentIdBackgroundWorker = new GetStudentIdBackgroundWorker(context_var);
            getStudentIdBackgroundWorker.execute();
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

    /* Background Thread to fetch student ID from Emaild ID of the student */
    class GetStudentIdBackgroundWorker extends AsyncTask<Void, Void, String> {

        Context context;
        ProgressDialog progressDialog;

        GetStudentIdBackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                String login_url = "http://192.168.1.40/library/app/getStudentId.php";
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

            isAsyncRunning=true;
            progressDialog = ProgressDialog.show(context_var,"", "Please Wait !!");
        }

        /* StudenId Fetched from SQL query is in the JSON Format and converted to Listview by setting adapter*/
        @Override
        protected void onPostExecute(String result) {

            studentId = result;
            TextView tv_studentid = root.findViewById(R.id.tv_profile_studentid);
            tv_studentid.setText(studentId);

            //if(!isAsyncRunning1)
            {
                GetIssuedBooksDetailsBackgroundWorker getIssuedBooksDetailsBackgroundWorker = new GetIssuedBooksDetailsBackgroundWorker(context_var);
                getIssuedBooksDetailsBackgroundWorker.execute();
            }
            //if(!isAsyncRunning2)
            {
                GetProfileFineBackgroundWorker getProfileFineBackgroundWorker = new GetProfileFineBackgroundWorker(context_var);
                getProfileFineBackgroundWorker.execute();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    /* Background Thread to fetch details of the books issued available */
    class GetIssuedBooksDetailsBackgroundWorker extends AsyncTask<Void, Void, String> {

        Context context;
        ProgressDialog progressDialog;

        GetIssuedBooksDetailsBackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(Void... Void) {

            try {
                String login_url = "http://192.168.1.40/library/app/issuedbooks.php";
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

            isAsyncRunning1=true;
            progressDialog = ProgressDialog.show(context_var,"", "Please Wait !!");
        }

        /* Data Fetched from SQL query is in the JSON Format and converted to Listview by setting adapter*/
        @Override
        protected void onPostExecute(String result) {

            jsonList = result;
            issuedBookListAdapter = new IssuedBookListAdapter(context_var, R.layout.issued_books_row_layout);
            listView = root.findViewById(R.id.listview);
            listView.setEmptyView(root.findViewById(R.id.emptyResults));
            listView.setAdapter(issuedBookListAdapter);

            try{
                jsonObject = new JSONObject(jsonList);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count=0;

                if(jsonArray.length() == 0)
                {
                    Log.i("info", "No books are issued");
                }

                else {
                    while (count < jsonArray.length()) {
                        JSONArray jo = jsonArray.getJSONArray(count);
                        name = jo.getString(0);
                        isbn = jo.getString(1);
                        issued_date = jo.getString(2);
                        return_date = jo.getString(3);
                        fine = jo.getString(4);

                        issuedBookList = new IssuedBookList(name, isbn, issued_date, return_date, fine);
                        issuedBookListAdapter.add(issuedBookList);
                        count++;
                    }
                }

                String issued_book_no = String.valueOf(count);
                TextView tv_issuedbooks = root.findViewById(R.id.tv_profile_issuedbooks);
                tv_issuedbooks.setText(issued_book_no);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    /* Background Thread to fetch the total fine of the user */
    class GetProfileFineBackgroundWorker extends AsyncTask<Void, Void, String> {

        Context context;
        ProgressDialog progressDialog;

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
            progressDialog = ProgressDialog.show(context_var,"", "Please Wait !!");
        }

        /* Data Fetched from SQL query is in the JSON Format and converted to Listview*/
        @Override
        protected void onPostExecute(String result) {
            TextView tv_fine = root.findViewById(R.id.tv_profile_fine);
            tv_fine.setText(result);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}