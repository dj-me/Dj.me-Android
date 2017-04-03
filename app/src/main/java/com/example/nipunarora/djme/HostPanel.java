package com.example.nipunarora.djme;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HostPanel extends AppCompatActivity {
    private RequestQueue mRequestqueue;
    WebView we;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_panel);
        Uri playlistsUri = Uri.parse("content://com.google.android.music.MusicContent/playlists");
       /* Cursor playlists = getApplicationContext().getContentResolver().query(playlistsUri, new String[]{"_id", "playlist_name"}, null, null, null);
        playlists.moveToFirst();
        playlists.close();*/

        mRequestqueue=VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        getRequest();


            //////////////// WEb view //////////////
        we=new WebView(this);
        we.getSettings().setJavaScriptEnabled(true);
        we.getSettings().setPluginState(WebSettings.PluginState.ON);

        we.loadUrl("https://www.youtube.com/playlist?list=PLQfv_9TJOAG3DbcKHLQyrQiyEtTogLqNt");

    }
    private String [] getMusic() {
        try {
            final Cursor mCursor = managedQuery(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media.DISPLAY_NAME}, null, null,
                    "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

            int count = mCursor.getCount();

            String[] songs = new String[count];
            int i = 0;
            if (mCursor.moveToFirst()) {
                do {
                    songs[i] = mCursor.getString(0);
                    Log.d("Song Name", songs[i]);
                    i++;
                } while (mCursor.moveToNext());
            }

            mCursor.close();

            return songs;
        } catch (Exception e) {
            Log.d("Error in Cursor", e.toString());
            return null;
        }
    }
    void getRequest()
    {
        StringRequest getNews = new StringRequest(Request.Method.GET, "https://djme.herokuapp.com/song",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        /***************** JSON PARSING OF THE RESPONSE*********************/
                        try{
                            Log.d("hey","host request sent");
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
