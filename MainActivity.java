package com.example.nishil09.socialmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.telephony.SmsManager;
import android.content.Intent;
import java.util.List;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.mobileconnectors.cognito.*;
import java.util.Date;
import java.util.Random;
import android.app.ProgressDialog;
public class MainActivity extends AppCompatActivity{


private Button check;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check = (Button) findViewById(R.id.button);
        check.setOnClickListener(listner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

         EditText country  = (EditText) findViewById(R.id.edit_message);
         EditText number  = (EditText) findViewById(R.id.edit_message1);
         String code = country.getText().toString();
        final String num = number.getText().toString();int checkCode = checkCountryCode(code);
         int checkNum = checkNumber(num);
       final String data = "12345";
         final String phoneNumber = "+1"+num;
         if(checkNum == 1)
         {



             progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
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
                         long ms = new Date().getTime();
                         String s = ""+ms;


                         userNumber obj = new userNumber();
                         Random rnd = new Random();
                         String code = "" + (10000 + rnd.nextInt(90000));
                         obj.setNumber(phoneNumber);
                         obj.setCode(code);
                         obj.setTimestamp(s);
                         mapper.save(obj);
                         otpLambda ob = new otpLambda(phoneNumber,code);
                         lambdaInterface invoker = factory.build(lambdaInterface.class);
                         String result = invoker.sendOtp(ob);
                         progressDialog.dismiss();
                         Intent intent = new Intent(MainActivity.this,OtpActivity.class);
                         intent.putExtra("number",phoneNumber);
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
             AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
             alertDialog.setTitle("Alert");
             alertDialog.setMessage("Wrong");
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

    int checkCountryCode(String s1)
    {
        char[] code = s1.toCharArray();
        if(code.length < 3 && code[0] == '+')
        {
            for(int i = 1; i < code.length ; i++ )
            {

            }
        }
        return  1;
    }
    int checkNumber(String s1)
    {
        char[] number = s1.toCharArray();
        if(number.length == 10)
        { int counter = 0;
            for(int i = 0; i < number.length ; i++ )
            {
               if(!Character.isDigit(number[i]))
               {
                   return  0;
               }
            }
        }
        else
        {
            return 0;
        }

        return  1;
    }
}
