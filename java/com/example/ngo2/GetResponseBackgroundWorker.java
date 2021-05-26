/* MODIFICATION HISTORY
S.No.   Date            Name            Description
---------------------------------------------------------------------------------------------------
1.      16-04-2021      Sahil Kalra     Background Process to get response on Login and SignUp
---------------------------------------------------------------------------------------------------
*/
package com.example.ngo2;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

public class GetResponseBackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;
    GetResponseBackgroundWorker(Context ctx) { context = ctx; }

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        if(type.equals("login")) {
            try {
                String login_url = "http://192.168.1.40/library/app/userlogin.php";
                String emailID = params[1];
                String password = params[2];

                URL url = new URL(login_url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setDoInput(true);

                OutputStream outputStream = c.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("emailId","UTF-8")+"="+URLEncoder.encode(emailID,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = c.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
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
        }
        else if(type.equals("signup")) {
            try {
                String signup_url = "http://192.168.1.40/library/app/usersignup.php";
                String name = params[1];
                String mobileNo = params[2];
                String address = params[3];
                String emailID = params[4];
                String password = params[5];

                URL url = new URL(signup_url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setDoInput(true);

                OutputStream outputStream = c.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =  URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&" +
                                    URLEncoder.encode("mobileNo","UTF-8")+"="+URLEncoder.encode(mobileNo,"UTF-8")+"&" +
                                    URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&" +
                                    URLEncoder.encode("emailId","UTF-8")+"="+URLEncoder.encode(emailID,"UTF-8")+"&" +
                                    URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = c.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
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
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            delegate.processFinish(result);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
