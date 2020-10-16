package com.vpapps.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.vpapps.qurancloudonline.MainActivity;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RegisterApp extends AsyncTask<Void, Void, String> {

    private static final String TAG = "RegisterApp";
    Context ctx;
    String gcm;
    String SENDER_ID = "343594554298";
    String regid = null;
    private int appVersion;

    public RegisterApp(Context ctx, String gcm, int appVersion) {
        this.ctx = ctx;
        this.gcm = gcm;
        this.appVersion = appVersion;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(Void... arg0) {
        String msg = "";
            regid = gcm;
            msg = "Device registered, registration ID=" + regid;
            Log.e("Atiar - registerApp", msg);
            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            sendRegistrationIdToBackend();

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(ctx, regid);
            return msg;
    }

    private void storeRegistrationId(Context ctx, String regid) {
        final SharedPreferences prefs = ctx.getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("registration_id", regid);
        editor.putInt("appVersion", appVersion);
        editor.commit();

    }


    private void sendRegistrationIdToBackend1() {
        URI url = null;
        try {
            url = new URI("https://pushstar.net/api/save_device/?device_token=" + regid + "&device_type=android");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        request.setURI(url);
        try {
            httpclient.execute(request);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void sendRegistrationIdToBackend() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url ="https://pushstar.net/api/save_device/?device_token=" + regid + "&device_type=android&channels_id=1,2&Access-Token=591a9cb37da212fd3bf347e73747ff5b";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("Atiar - volley", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Atiar - volley", "did not work");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Toast.makeText(ctx, "Registration Completed. Now you can see the notifications", Toast.LENGTH_SHORT).show();
        //Log.e(TAG, result);
        //Log.e(TAG, "Registration Completed. Now you can see the notifications");
    }


}
