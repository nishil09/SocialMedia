package com.example.nishil09.socialmedia;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.Date;


public class shareMedia extends AppCompatActivity {

    Button b1;
    database obj;
    String[] ar2;
    Uri selectedImageUri;
    ListView listView;
    getServerData getdata;
    ArrayAdapter<String> adapter;
    private static int PICK_IMAGE= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_media);
        b1 = (Button)findViewById(R.id.btn);
        listView = (ListView) findViewById(R.id.chat);

         obj = new database(this);
        getdata = new getServerData(this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                     try
                     {
                         ar2 = obj.SelectDataFromSync();
                     }
                     catch (Exception e)
                     {

                     }
                    if(ar2.length == 0) {
                        getdata.getData(getIntent().getStringExtra("number"), "0");
                    }
                    else {
                        getdata.getData(getIntent().getStringExtra("number"), ar2[ar2.length-1]);
                    }
                    updateList();
                    try {
                        java.lang.Thread.sleep(20000);

                    }
                    catch(Exception e)
                    {
                        Log.i("Sleep", "EXCEPTION in Thered sleep");
                    }
                    }

            }
        });
        thread.start();
        updateList();
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openGallery(1);
            }
        });
    }
    public void openGallery(int req_code) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
          selectedImageUri = data.getData();


            if (requestCode == 1) {
             //  String selectedPath1 = getRealPathFromURI(selectedImageUri);
                final File myFile = new File(selectedImageUri.toString());
                Log.i("File Path",getRealPathFromURI(selectedImageUri));
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                                    getApplicationContext(),
                                    "us-east-1:f83757ea-687f-4d80-8462-7e60077abd81", // Identity Pool ID
                                    Regions.US_EAST_1 // Region
                            );

                            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
                            TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());
                          try {
                              long ms = new Date().getTime();
                              TransferObserver observer = transferUtility.upload(
                                      "uhcl",     /* The bucket to upload to */
                                      "" + ms + myFile.getName(),    /* The key for the uploaded object */
                                      new File(getRealPathFromURI(selectedImageUri))       /* The file where the data to upload exists */
                              );
                              String uploadedMedia = "" + ms + myFile.getName();
                              ms = new Date().getTime();
                             String to = getIntent().getStringExtra("number");
                              String from = getIntent().getStringExtra("number2");
                              String path = getRealPathFromURI(selectedImageUri);
                              String Dir = "OUT";

                              Log.i("Num",from);
                              /*
                              ContentValues values = new ContentValues();

                              values.put("Number", from);
                              values.put("Path", path);
                              values.put("TimeStamp", ""+ms);
                              values.put("Direction", Dir);
                              db.insertOrThrow("Chat", null, values);*/

                              new serverhandle(shareMedia.this).execute(from, to, uploadedMedia, path);
                              updateList();

                          }
                          catch(Exception e)
                          {
                              Log.i("ERROR",e.toString());
                              Log.i("Message",e.toString());
                          }
                            finally
                          {

                          }
                        }
                        catch(Exception e)
                        {

                            Log.i("Error",e.toString());
                        }


                    }
                });

thread.start();
            }




        }
    }
    private String getRealPathFromURI(Uri contentUri) {

            Cursor cursor = null;
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                cursor = getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share_media, menu);
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

    void updateList()
    {
        /*
        String que = "SELECT Path from Chat where Number = " + getIntent().getStringExtra("number2") + " ORDER BY TimeStamp ASC";

SQLiteDatabase db = openOrCreateDatabase("SocialMedia", Context.MODE_PRIVATE, null);

        Cursor cur = db.rawQuery(que, null);
        Log.i("DATA", ""+cur.getCount());
        try {
            int i = 0;
            String[] ar = new String[cur.getCount()];
            cur.moveToFirst();

            while (i < cur.getCount()) {
                ar[i] = cur.getString(0);
                Log.i("DATA", cur.getString(0));
                cur.moveToNext();
                i++;
            }*/
          //  cur.close();
        String[] ar = obj.SelectDataFromChat(getIntent().getStringExtra("number2"));
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, ar);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(adapter);
                }
            });
        }

    }

