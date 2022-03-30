package com.blikoon.roosterplus.xmpp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;


import androidx.annotation.Nullable;

import com.blikoon.roosterplus.Constants;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager;

import java.io.IOException;

public class TalkGapChatConnectionService extends Service {

    private static  final  String LOGTAG =  "TChatConnectionServices";

    private boolean mActivive;//stores whether or not the thread is active
    private Thread mThread;
    private Handler mTHandler;// we use this handler to post message to the background thread.

    public static TalkGapChatConnection getConnection() {
        return mConnection;
    }

    //the  backgroud thread
    private static TalkGapChatConnection mConnection;



    public TalkGapChatConnectionService() {

    }

    private void initConnection()
    {
        Log.d(LOGTAG,"initConnection()");
        if(mConnection == null)

        {
            mConnection = new TalkGapChatConnection(this);
        }

        try{
            mConnection.connect();
        }catch (IOException |SmackException |XMPPException e){

            Log.d(LOGTAG, "Something went wrong while connecting, make sure the credentials are correct");

              Intent i = new Intent(Constants.BroadCastMessage.UI_CONNECTION_ERROR);
              i.setPackage(getApplicationContext().getPackageName());
              getApplicationContext().sendBroadcast(i);

              Log.d(LOGTAG, "Sent the broadcast for connection error from service");

              // STOP THE SERVICE ALL TOGETHER IF USER IS NOT LOGGED IN ALREADY.

            boolean logged_in_state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getBoolean("xmpp_logged_in", false);

            if (!logged_in_state)
            {
                Log.d(LOGTAG, "Logged in State :" + logged_in_state + "called stopself()");
                stopSelf();
            }  else
            {
                Log.d(LOGTAG, "Logged in state : " + logged_in_state);

            }

              e.printStackTrace();

         }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        ServerPingWithAlarmManager.onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // DO YOUR TASK HERE
        start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServerPingWithAlarmManager.onDestroy();
        stop();
    }

    public  void start()
    {
        Log.d(LOGTAG,"Service Start() function called. mActive : " + mActivive);
        if(!mActivive)
        {
            mActivive = true;
            if (mThread == null || !mThread.isAlive())
            {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mTHandler = new Handler();
                        initConnection();

                        //THE CODE HERE RUNS IN A BACKGROUND THREAD.
                        Looper.loop();
                    }
                });
                 mThread.start();
            }
        }
    }

    public void stop()
    {
        Log.d(LOGTAG,"stop()");
        mActivive = false;
        mTHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mConnection != null)
                {
                    mConnection.disconnected();
                }

            }
        });
    }


}
