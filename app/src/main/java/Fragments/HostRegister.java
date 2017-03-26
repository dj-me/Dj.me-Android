package Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nipunarora.djme.HostPanel;
import com.example.nipunarora.djme.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nipunarora on 25/03/17.
 */

public class HostRegister extends Fragment {
    private View rootview;
    ImageView pass;
    EditText t;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.hostsession_register, container, false);
       pass=(ImageView)rootview.findViewById(R.id.sessionpass);
        t=(EditText)rootview.findViewById(R.id.input_session);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupwifi(t.getText().toString());
            }
        });
        return rootview;
    }

    public HostRegister() {
        super();
    }
    void askPermissions()
    {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }

    }
    void setupwifi(String h)
    {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);;

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
        boolean methodFound = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite( getActivity().getApplicationContext().getApplicationContext())){
                // Do stuff here
                Log.d("stato","already available");
            }
            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" +  getActivity().getApplicationContext().getApplicationContext().getPackageName()));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,0);
            }
        }
        for (Method method: wmMethods) {
            if (method.getName().equals("setWifiApEnabled")) {
                methodFound = true;
                Log.d("check","done");
                WifiConfiguration netConfig = new WifiConfiguration();
                netConfig.SSID = h;
                netConfig.preSharedKey="DataAnywhere";
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                try {
                    boolean apstatus = (Boolean) method.invoke(wifiManager, netConfig, true);

                    for (Method isWifiApEnabledmethod: wmMethods) {
                        if (isWifiApEnabledmethod.getName().equals("isWifiApEnabled")) {
                            while (!(Boolean) isWifiApEnabledmethod.invoke(wifiManager)) {};
                            for (Method method1: wmMethods) {
                                if (method1.getName().equals("getWifiApState")) {
                                    int apstate;
                                    apstate = (Integer) method1.invoke(wifiManager);
                                    Log.i(this.getClass().toString(), "Apstate ::: "+apstate);
                                }
                            }
                        }
                    }
                    if (apstatus) {
                        Log.d("Splash Activity", "Access Point created");
                        //move to next
                        Intent i=new Intent(getContext(), HostPanel.class);
                        startActivity(i);
                    } else {
                        Log.d("Splash Activity", "Access Point creation failed");
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!methodFound) {
            Log.d("Splash Activity",
                    "cannot configure an access point");
        }
    }
}
