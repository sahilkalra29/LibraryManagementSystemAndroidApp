package com.example.ngo2.ui.catalogue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ngo2.MainActivity;
import com.example.ngo2.R;
import com.example.ngo2.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CatalogueFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    Boolean isAsyncRunning = false;
    String jsonList;
    CatalogueListAdapter catalogueListAdapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    String name, category, author, isbn, total_quantity, issued_quantity;
    CatalogueList catalogueList;
    Context context_var;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_catalogue, container, false);
        context_var = container.getContext();

        if(!isAsyncRunning) {
            GetBooksCatalogueBackgroundWorker getBooksCatalogueBackgroundWorker = new GetBooksCatalogueBackgroundWorker(context_var);
            getBooksCatalogueBackgroundWorker.execute();
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
    /* Background Thread to fetch all the books available */
    class GetBooksCatalogueBackgroundWorker extends AsyncTask<Void, Void, String> {

        Context context;

        GetBooksCatalogueBackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                String login_url = "http://192.168.1.40/library/app/bookscatalogue.php";
                URL url = new URL(login_url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setDoInput(true);

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

        /* Data Fetched from SQL query is in the JSON Format and converted to Listview by setting adapter*/
        @Override
        protected void onPostExecute(String result) {
            jsonList = result;
            catalogueListAdapter = new CatalogueListAdapter(context_var, R.layout.books_catalogue_row_layout);
            listView = root.findViewById(R.id.listview);
            listView.setAdapter(catalogueListAdapter);

            try{
                jsonObject = new JSONObject(jsonList);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count=0;

                while(count< jsonArray.length()){
                    JSONArray jo = jsonArray.getJSONArray(count);
                    name = jo.getString(0);
                    category = jo.getString(1);
                    author = jo.getString(2);
                    isbn = jo.getString(3);
                    total_quantity = jo.getString(4);
                    issued_quantity = jo.getString(5);

                    catalogueList = new CatalogueList(name, category, author, isbn, total_quantity, issued_quantity);
                    catalogueListAdapter.add(catalogueList);
                    count++;
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
}