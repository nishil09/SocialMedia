package com.example.nishil09.socialmedia;

/**
 * Created by nishil09 on 3/25/16.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.nishil09.socialmedia.R;

import java.util.Date;
import java.util.Random;


public class TwoFragment extends Fragment{

    ListView listView;
    private Button check;
    database obj;
    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = new database(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // View view =  inflater.inflate(R.layout.fragment_one, container, false);
        check = (Button)  getActivity().findViewById(R.id.button);
        check.setOnClickListener(listner);
  listView =(ListView) getActivity().findViewById(R.id.groups);
        String[] arr2 = obj.SelectDataFromGroup();
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, arr2);
        listView.setAdapter(adapter);

    }
    private View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getActivity(),createGroup.class);
        startActivityForResult(intent, 1);
          //  startActivity(intent);
        }
    };

}