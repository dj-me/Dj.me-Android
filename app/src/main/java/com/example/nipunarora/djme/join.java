package com.example.nipunarora.djme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class join extends AppCompatActivity {
    private ArrayList<String> nearby_Sessions;
    private ArrayAdapter<String> spinner_adapter;
    private Spinner spinner;
    private ImageView join;
    List<ScanResult> wifiAvailable;
    RequestQueue mRequestqueue;
    WifiManager mainWifi;
    BroadcastReceiver receiverWifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        spinner=(Spinner)findViewById(R.id.spinnersession);
        nearby_Sessions=new ArrayList<String>();

       /* nearby_Sessions.add("Vishrut");
        nearby_Sessions.add("Gautam");
        nearby_Sessions.add("Sukhad");
        nearby_Sessions.add("Harsh");*/
        spinner_adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,nearby_Sessions);
        //spinner.setSelection(0);
        //spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Tap for Sessions");

        spinner.setAdapter(spinner_adapter);

        join=(ImageView)findViewById(R.id.joinpass);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Process the syncing of the playlist to the respected session

                mRequestqueue=VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
                getRequest();

            }
        });

        mainWifi = (WifiManager)getApplicationContext().getSystemService(this.WIFI_SERVICE);


        // Check for wifi is disabled
        if (mainWifi.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();

            mainWifi.setWifiEnabled(true);
        }

        // wifi scaned value broadcast receiver
        receiverWifi = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                wifiAvailable=mainWifi.getScanResults();
                for(int i=0;i<wifiAvailable.size();++i)
                {

                    Log.d("found Something",wifiAvailable.get(i).SSID);
                    if(wifiAvailable.get(i).SSID.equals("VishrutsBash"))
                    {
                        Log.d("st","match found");
                        nearby_Sessions.add("VishrutsBash");
                        spinner_adapter.notifyDataSetChanged();
                    }
                    //Send the entire list of BSSIDs to te server to check the wifi sessions available there

                }

            }
        };

        // Register broadcast receiver
        // Broacast receiver will automatically call when number of wifi connections changed
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        Toast.makeText(this,"Starting Scan...",Toast.LENGTH_SHORT).show();
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
    }



    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    void getRequest()
    {
        StringRequest getNews = new StringRequest(Request.Method.GET, "https://djme.herokuapp.com/song2",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        /***************** JSON PARSING OF THE RESPONSE*********************/
                        try{
                            Log.d("hey","join request sent");
                            Toast.makeText(getApplicationContext(),"Join request sent",Toast.LENGTH_SHORT).show();


                        }
                        catch (Exception e)
                        {
                            Log.d("JSON Parse Error",e.toString());
                        }


                    }
                },
                //******************** Enable the starting of app even in the case when internet is no available with default banner images **********/
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                    }
                }
        );

        mRequestqueue.add(getNews);
    }

}
