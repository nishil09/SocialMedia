package com.example.nishil09.socialmedia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Date;
import java.util.Random;

public class RegisterUser extends AppCompatActivity {

    private Button check;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        check = (Button) findViewById(R.id.button);
        check.setOnClickListener(listner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_user, menu);
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

            final EditText nameField  = (EditText) findViewById(R.id.edit_message);
            EditText emailField  = (EditText) findViewById(R.id.edit_message1);
            final String name = nameField.getText().toString();
            final String email = emailField.getText().toString();
            if(name != "" && email != "")
            {



                progressDialog = ProgressDialog.show(RegisterUser.this, "", "Loading...");
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                                    getApplicationContext(),
                                    "us-east-1:f83757ea-687f-4d80-8462-7e60077abd81", // Identity Pool ID
                                    Regions.US_EAST_1 // Region
                            );
                            LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext(),
                                    Regions.US_EAST_1,
                                    credentialsProvider);
                            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
                            userdata obj = new userdata();
                            obj.setNumber(getIntent().getStringExtra("number"));
                            obj.setName(name);
                            obj.setEmail(email);
                            mapper.save(obj);

                            progressDialog.dismiss();
                            Intent intent = new Intent(RegisterUser.this,ContactActivity.class);
                            intent.putExtra("number",getIntent().getStringExtra("number"));
                            startActivity(intent);





                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();


             /*
             catch (Exception e)
             {

                 System.out.println(e.fillInStackTrace());
                 AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                 alertDialog.setTitle("Alert");
                 alertDialog.setMessage(e.getMessage());
                 alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                             }
                         });
                 alertDialog.show();
             }*/

                //  String message = "Hello World! Now we are going to demonstrate " +
                // "how to send a message with more than 160 characters from your Android application.";

                //  SmsManager smsManager = SmsManager.getDefault();
                //   ArrayList<String> parts = smsManager.divideMessage(message);
                //smsManager.sendTextMessage(phoneNumber,null,data,null,null);

            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterUser.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Something is Wrong");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }


        }
    };
}
