package com.vpapps.fcm;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vpapps.qurancloudonline.BuildConfig;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static String token="";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        token = s;
        Log.e("Atiar - New TOken", s);
        new RegisterApp(getApplicationContext(),s, BuildConfig.VERSION_CODE);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log.e("Atiar msg =" , remoteMessage.getRawData().toString());
    }

    public static String getToken() {
        if (token==null || token.equals("") || token.equals("empty")){
            token =  FirebaseInstanceId.getInstance().getToken();
        }
        return token;
    }


}
