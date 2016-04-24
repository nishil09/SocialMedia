package com.example.nishil09.socialmedia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class createGroup extends AppCompatActivity {
    String[] number;
    String[] name;
    ListView list;
    helper map;
    String[] ar;
    EditText text;
    int check = 0;
    String GroupName = "";
    Button submit;
    HashSet<String> set = new HashSet<String>();
    ArrayAdapter<String> adapter;
    database obj;

    String link;
    String data;
    BufferedReader bufferedReader;
    String result = "";
    String param = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        obj = new database(getApplicationContext());
        map = obj.SelectDataFromUser();
        number = new String[map.number.size()];
        name = new String[map.name.size()];
        number = map.number.toArray(number);
        name = map.name.toArray(name);

        text = (EditText) findViewById(R.id.groupname);
        submit = (Button) findViewById(R.id.group_submit);

        submit.setOnClickListener(listner);

        list = (ListView) findViewById(R.id.group_member);
        adapter = new ArrayAdapter<String>(createGroup.this,
                android.R.layout.simple_list_item_1, number);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // ListView Clicked item index
             /*   Intent intent = new Intent(createGroup.this,shareMedia.class);
                intent.putExtra("number",getActivity().getIntent().getStringExtra("number"));
                intent.putExtra("number2",arr[position]);
                startActivityForResult(intent, 1);*/

                if (set.size() <= 4) {
                    if (set.contains(number[position])) {
                        set.remove(number[position]);
                        view.setBackgroundColor(Color.WHITE);
                        Toast.makeText(getApplicationContext(), "User Removed", Toast.LENGTH_SHORT).show();
                    } else {
                        set.add(number[position]);
                        view.setBackgroundColor(Color.YELLOW);
                        Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Only 4 Users Allowed, Hit Submit to create a group", Toast.LENGTH_SHORT).show();

                }
            }

        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String groupName = text.getText().toString().trim();
           checkGroupExistence(groupName);

        }
    };

   void checkGroupExistence(String name) {

        //  String upload = arg0[2];
        // String timestamp = arg0[3];
param = name;

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   int j = 0;
                    j = set.size();
                    while(j <= 4)
                    {
                        set.add("NONE");
                        j++;
                    }


                    data = "?name=" + URLEncoder.encode(param, "UTF-8");
                    int i = 0;
                     ar = new String[set.size()];
                    ar = set.toArray(ar);
                    String[] ar2 = {"f1","f2","f3","f4"};
                    while(i < ar.length)
                    {
                        data +="&"+ar2[i]+"=" +  URLEncoder.encode(ar[i], "UTF-8");
                        i++;
                    }



                    // data += "&upload=" + URLEncoder.encode(upload, "UTF-8");
                    // data += "&timestamp=" + URLEncoder.encode(timestamp, "UTF-8");


                    link = "http://ec2-54-165-188-253.compute-1.amazonaws.com/checkgroup.php" + data;
                    URL url = new URL(link);

                    Log.i("URL", url.toString());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setDoInput(true);
                    con.connect();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    result = bufferedReader.readLine();
                    con.getInputStream().close();

                    Log.i("RESULT", result.toString());

                    //  result = bufferedReader.readLine();
                    // return result;
                } catch (Exception e) {
                    Log.i("Group JSON Error", e.toString());
                    Toast.makeText(getApplicationContext(), "URL WRONG DATA.", Toast.LENGTH_SHORT).show();

                }


                if (result != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        Log.i("Jsonobj", jsonObj.toString());
                        final String query_result = jsonObj.getString("query_result");
                        Log.i("JSON Response", query_result);
                        if (query_result.equals("0")) {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Group Exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );

                            check = 1;
                        } else {
                                        //Toast.makeText(getApplicationContext(), "Group does not exist.", Toast.LENGTH_SHORT).show();
                                        obj.addDataToGroup(param, ar, query_result);
                                        Intent intent = new Intent(createGroup.this,TabActivity.class);

                                        startActivity(intent);


                        }

                    } catch (Exception e) {
                        Log.i("JSON ERROR", e.toString());
                        runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "WRONG WITH JSON.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        );

                    }
                }

            }
        });
        t1.start();

    }
}