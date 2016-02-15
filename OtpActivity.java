package com.example.nishil09.socialmedia;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class OtpActivity extends AppCompatActivity {
    private Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        check = (Button) findViewById(R.id.button);
        check.setOnClickListener(listner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_otp, menu);
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
    private OnClickListener listner = new OnClickListener() {
        @Override
        public void onClick(View v) {

            EditText code = (EditText) findViewById(R.id.edit_message);
            final String otp = code.getText().toString();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                            getApplicationContext(),
                            "us-east-1:f83757ea-687f-4d80-8462-7e60077abd81", // Identity Pool ID
                            Regions.US_EAST_1 // Region
                    );
                    AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                    DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

                    final userNumber obj = mapper.load(userNumber.class,getIntent().getStringExtra("number"));

                    if((Integer.parseInt(obj.getCode().toString()) - Integer.parseInt(otp)) == 0)
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Your dialog code.
                                AlertDialog alertDialog = new AlertDialog.Builder(OtpActivity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("Code Mathced");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();



                        }
                    }

            );


                    }
                    else
                    {
                        System.out.println(obj.getCode());
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              // Your dialog code.
                                              AlertDialog alertDialog = new AlertDialog.Builder(OtpActivity.this).create();
                                              alertDialog.setTitle("Alert");
                                              alertDialog.setMessage(otp);
                                              alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                      new DialogInterface.OnClickListener() {
                                                          public void onClick(DialogInterface dialog, int which) {
                                                              dialog.dismiss();
                                                          }
                                                      });
                                              alertDialog.show();


                                          }
                                      }

                        );


                    }

                }
            });
            thread.start();
        }
    };

}
