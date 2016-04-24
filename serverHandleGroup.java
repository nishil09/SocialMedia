package com.example.nishil09.socialmedia;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by nishil09 on 4/23/16.
 */
public class serverHandleGroup extends AsyncTask<String, Void, String> {
    private Context context;
    int status = 0;
    String from;
    String to;
    String upload;
    String path;
    database obj;
    public serverHandleGroup(Context context) {
        this.context = context;
        obj = new database(context);
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        from = arg0[0];
        to = arg0[1];
        upload = arg0[2];
        path = arg0[3];


        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            data = "?from=" + URLEncoder.encode(from, "UTF-8");
            data += "&to=" + URLEncoder.encode(to, "UTF-8");
            data += "&upload=" + URLEncoder.encode(upload, "UTF-8");
            // data += "&timestamp=" + URLEncoder.encode(timestamp, "UTF-8");


            link = "http://ec2-54-165-188-253.compute-1.amazonaws.com/insert.php" + data;
            URL url = new URL(link);
            Log.i("Insert", url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            Log.i("RESULT",result);
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String jsonStr = result;
        Log.i("JsonRes", jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Log.i("Jsonobj", jsonObj.toString());
                String query_result = jsonObj.getString("query_result");

                if (!query_result.equals("0")) {
                    try
                    {
                        status = obj.addDataToChat(from,path,query_result,"OUT");
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, "Data could not be inserted.", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(context, "Data inserted successfully. ", Toast.LENGTH_SHORT).show();
                } else if (query_result.equals("0")) {
                    Toast.makeText(context, "Data could not be inserted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }

}
