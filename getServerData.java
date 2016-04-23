package com.example.nishil09.socialmedia;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by nishil09 on 4/9/16.
 */
public class getServerData{


    private Context context;
    database obj;

    public getServerData(Context context) {
        this.context = context;
        obj = new database(context);

    }

    protected void onPreExecute() {

    }

   int getData(String arg,String arg1) {
       String to = arg;
       String timestamp = arg1;
       //  String upload = arg0[2];
       // String timestamp = arg0[3];


       String link;
       String data;
       BufferedReader bufferedReader;
       String result = "";

      try {
           data = "?to=" + URLEncoder.encode(to, "UTF-8");
          data += "&timestamp=" + URLEncoder.encode(timestamp, "UTF-8");
           // data += "&upload=" + URLEncoder.encode(upload, "UTF-8");
           // data += "&timestamp=" + URLEncoder.encode(timestamp, "UTF-8");


           link = "http://ec2-54-165-188-253.compute-1.amazonaws.com/select.php" + data;
           URL url = new URL(link);
          Log.i("URL",url.toString());
           HttpURLConnection con = (HttpURLConnection) url.openConnection();
           con.setRequestMethod("GET");
           con.setDoInput(true);
           con.connect();
           bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
           StringBuilder stringBuilder = new StringBuilder();
           String line = "";
           while ((line = bufferedReader.readLine()) != null) {
               stringBuilder.append(line);
           }
           con.getInputStream().close();
           result = stringBuilder.toString();
          // Log.i("RESULT",result.toString());
           //  result = bufferedReader.readLine();
           // return result;

              String s = "";
              JSONArray jsonArray = new JSONArray(result);
              Log.i("FROM",jsonArray.length()+"");
              for (int i = 0; i < jsonArray.length(); i++) {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);


                  final String from = jsonObject.getString("from");
                  Log.i("FROM",from);
                  final String to2 = jsonObject.getString("to");
                  Log.i("to",to2);
                  final String medialocation = jsonObject.getString("medialocation");
                  Log.i("Medialoc",medialocation);
                  final String timestamp2 = jsonObject.getString("timestamp");
                  if(i == jsonArray.length() - 1)
                  {
                      try {
                          obj.addDataToSync(timestamp2);
                      }
                      catch(Exception e)
                      {
                          Log.i("","Something went weong");

                      }
                  }
                  final String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/" + new Date().getTime()+".png";
                  Log.i("PATHHH",path);
                  Thread t = new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                                      context,
                                      "us-east-1:f83757ea-687f-4d80-8462-7e60077abd81", // Identity Pool ID
                                      Regions.US_EAST_1 // Region
                              );

                              AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
                              TransferUtility transferUtility = new TransferUtility(s3, context);

                             try {
                                 TransferObserver observer = transferUtility.download(
                                         "uhcl",     /* The bucket to upload to */
                                         medialocation,    /* The key for the uploaded object */
                                         new File(path));

                                 obj.addDataToChat(from, path, timestamp2, "IN");

                             }
                             catch(Exception e)
                             {
                                 e.printStackTrace();
                             }


                          } catch (Exception e) {
                            e.printStackTrace();
                          }
                          //obj.addDataToChat("from", path, "" + new Date().getTime(), "IN");
                      }

                  });
                  t.start();
              }
           //   shareMedia obj2 = new shareMedia();

             // obj2.updateList();
              //  textElement.setText(s);
          }

       catch (Exception e) {
           Log.i("EXCEPTION",e.toString());
           e.printStackTrace();
       }


     /*  try {
           String s = "";
           JSONArray jsonArray = new JSONArray(result);
           Log.i("FROM",jsonArray.length()+"");
           for (int i = 0; i < jsonArray.length(); i++) {
               JSONObject jsonObject = jsonArray.getJSONObject(i);


               final String from = jsonObject.getString("from");
               Log.i("FROM",from);
               String to2 = jsonObject.getString("to");
               final String medialocation = jsonObject.getString("medialocation");
               Log.i("Medialoc",medialocation);
               final String timestamp2 = jsonObject.getString("timestamp");
               final String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/" + new Date().getTime()+".png";
               Thread t = new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                                   context,
                                   "us-east-1:f83757ea-687f-4d80-8462-7e60077abd81", // Identity Pool ID
                                   Regions.US_EAST_1 // Region
                           );

                           AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
                           TransferUtility transferUtility = new TransferUtility(s3, context);
                           TransferObserver observer = transferUtility.download(
                                   "uhcl",     *//* The bucket to upload to *//*
                                   medialocation,    *//* The key for the uploaded object *//*
                                   new File(path));
                           try {
                               obj.addDataToChat(from, path, timestamp2, "IN");
                           } catch (Exception e) {
                               Log.i("Mes", "WRONG SELECT");
                           }

                       } catch (Exception e) {

                       }
                       obj.addDataToChat("from", path, "" + new Date().getTime(), "IN");
                   }

               });
               t.start();
           }
           shareMedia obj2 = new shareMedia();
           obj2.updateList();
           //  textElement.setText(s);
       } catch (Exception e) {
           return 1;
       }*/
       return 0;
    }


}
