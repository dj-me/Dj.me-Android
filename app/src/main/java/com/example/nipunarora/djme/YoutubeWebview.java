package com.example.nipunarora.djme;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeWebview extends AppCompatActivity {
    private WebView webview;
    private RequestQueue mrequestqueue;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_webview);
        webview=(WebView)findViewById(R.id.webviewYTB);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Preferences",getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isNameThere",true );
        editor.commit();
        Intent i=new Intent(getApplicationContext(),HomeScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }
       /* mrequestqueue=VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        progressDialog = new ProgressDialog(YoutubeWebview.this);
        progressDialog.setCanceledOnTouchOutside(false);


        webview.setWebViewClient(new WebViewClient() {



                                     public void onPageFinished(WebView view, String url) {
                                         //check if the url is the one where we get the required data we close the drawere was an error loading the page show loader again....
                                         progressDialog.dismiss();
                                     }
                                     @Override
                                     public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                         super.onReceivedError(view, request, error);
                                         Log.d("error is",error.toString());
                                         Log.d("urlll",request.getUrl().toString());
                                         try {
                                             progressDialog.dismiss();
                                         }
                                         catch (Exception e) {
                                             Log.d("progressdialog","dialog exception on received error");
                                         }
                                     }



                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                         //intercepting the request to get data on the last page
                                         String regex = "^https://djme.herokuapp.com";
                                         Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                                          String url=request.getUrl().toString();
                                         Log.d("Request Url",url);
                                         Matcher matcher = pattern.matcher(url);
                                         if (matcher.find())
                                         {
                                             getYoutubeName(url);


                                         }
                                         else
                                         {
                                             view.loadUrl(url);
                                         }
                                         return true;
                                     }
                                     public void onLoadResource (WebView view, String url) {
                                         // in standard case YourActivity.this
                                         Log.d("it will load again", "true");
                                         Log.d("onload", url);
                                         progressDialog.show();
                                     }


                                 }
        );
        webview.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=33704938095-g3uhslf1enbgg84hb40k0924mgea5arm.apps.googleusercontent.com&redirect_uri=https://djme.herokuapp.com/home&scope=https://www.googleapis.com/auth/youtube");
    }*/


    private void getYoutubeName(String url)
    {
        StringRequest loadurl = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            //make transition to new activity
                            JSONObject res=new JSONObject(response);
                            String youtubeName=res.getString("name");
                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Preferences",getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("YoutubeName",youtubeName );



                        }
                        catch(Exception e)
                        {
                            Log.d("vollleyerror","yes there is");
                            e.printStackTrace();
                        }

                        // response
                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Preferences",getApplicationContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("isNameThere",true );
                        Log.d("linkedin Response", response);
                        Intent i=new Intent(getApplicationContext(),HomeScreen.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response","hahah");
                        Log.d("error", error.toString());
                    }
                }
        );
        loadurl.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mrequestqueue.add(loadurl);
    }
    }




