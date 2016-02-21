package com.example.nishil09.socialmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.pm.PackageManager;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.provider.ContactsContract;
import android.os.Build;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> number = new ArrayList<String>();
    String[] arr;
    String[] arr2;
    ListView listView;
    ArrayAdapter<String> adapter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact2);
        showContacts();

    }


    private void showContacts() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {

            // Android version is lesser than 6.0 or the permission is already granted.
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:f83757ea-687f-4d80-8462-7e60077abd81", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );
          final AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);


            listView = (ListView) findViewById(R.id.list);
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if(cur.getCount() > 0)
            {
                while(cur.moveToNext())
                {


                    String num = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                   num = num.replaceAll("[\\\\[\\\\](){}]","");
                    num = num.replaceAll(" ","");
                    num = num.replaceAll("-","");

                  userdata obj = mapper.load(userdata.class,num);
                  if(obj == null) {

                   }
                   else
                    {

                        number.add(num);
                        name.add(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    }

                }
                arr = number.toArray(new String[number.size()]);
                Log.i("Array Size",""+arr.length);
                arr2 = name.toArray(new String[name.size()]);

                Log.i("Array Size 1", "" + number.size());
                adapter = new ArrayAdapter<String>(ContactActivity.this,
                        android.R.layout.simple_list_item_2, android.R.id.text1, arr2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }
                });



            }

                }
            });

            thread.start();






            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                   String itemValue = (String) listView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();

                }

            });

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
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
}
