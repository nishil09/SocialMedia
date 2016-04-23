package com.example.nishil09.socialmedia;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.HashMap;

public class createGroup extends AppCompatActivity {
String[] number;
    String[] name;
database obj = new database(getApplicationContext());
    HashMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        map= obj.SelectDataFromUser();
        number = new String[map.size()];
        name = new String[map.size()];
        number = map.keySet().toArray();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
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
}
