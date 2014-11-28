package com.jayjaylab.androiddemo.app.greyhound.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.google.inject.Inject;
import com.jayjaylab.androiddemo.R;
import com.jayjaylab.androiddemo.app.greyhound.fragment.FragmentPathHistory;
import com.jayjaylab.androiddemo.app.greyhound.service.ServiceRecordingPath;
import com.jayjaylab.androiddemo.app.greyhound.util.Constants;
import com.jayjaylab.androiddemo.util.AndroidHelper;

import roboguice.activity.RoboActionBarActivity;
import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_greyhound_main)
public class ActivityMain extends RoboActionBarActivity {

    boolean isPaused = true;
    boolean isBound = false;
    Messenger serviceMessenger;
    Message messegeToBeSentOnceConnected;

    @Inject Handler handler;
    @Inject FragmentPathHistory fragmentPathHistory;
    @Inject ServiceRecordingPath serviceRecordingPath;

    // views
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.imagebutton_recordpause) ImageButton imagebuttonRecordPause;
    @InjectView(R.id.imagebutton_stop) ImageButton imagebuttonStop;

    public void onCreateEvent(@Observes OnCreateEvent event) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadPathHistoryFragment();
        setViews();
    }

    protected void setViews() {
        setToolbar();
        setImageButtonRecordPause();
        setImageButtonStop();
    }

    protected void setToolbar() {
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ln.d("onClick() : v : %s", v);
                finish();
//                NavUtils.getParentActivityIntent(ActivityMain.this);
            }
        });
    }

    protected void setImageButtonRecordPause() {
        imagebuttonRecordPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPaused) {
                    isPaused = false;
                    imagebuttonRecordPause.setImageResource(R.drawable.record);
                    startRecording();
                } else {
                    isPaused = true;
                    imagebuttonRecordPause.setImageResource(R.drawable.pause);
                    pauseRecording();
                }
            }
        });
    }

    protected void setImageButtonStop() {
        imagebuttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPaused) {
                    stopRecording();
                }
            }
        });
    }

    protected void startRecording() {
        startRecordingPathServiceIfNeed();
        bindServiceIfNeed();


        if(serviceMessenger == null) {
            messegeToBeSentOnceConnected =
                    Message.obtain(null, Constants.MSG_START_RECORDING, 0, 0);
        } else {
            Message msg = Message.obtain(null, Constants.MSG_START_RECORDING, 0, 0);
            try {
                serviceMessenger.send(msg);
            } catch(RemoteException e) {
                Ln.e(e);
            }
        }
    }

    protected void pauseRecording() {
        startRecordingPathServiceIfNeed();
        bindServiceIfNeed();

        if(serviceMessenger == null) {
            messegeToBeSentOnceConnected =
                    Message.obtain(null, Constants.MSG_PAUSE_RECORDING, 0, 0);
        } else {
            Message msg = Message.obtain(null, Constants.MSG_PAUSE_RECORDING, 0, 0);
            try {
                serviceMessenger.send(msg);
            } catch(RemoteException e) {
                Ln.e(e);
            }
        }
    }

    protected void stopRecording() {
        startRecordingPathServiceIfNeed();
        bindServiceIfNeed();

        if(serviceMessenger == null) {
            messegeToBeSentOnceConnected =
                    Message.obtain(null, Constants.MSG_STOP_RECORDING, 0, 0);
        } else {
            Message msg = Message.obtain(null, Constants.MSG_STOP_RECORDING, 0, 0);
            try {
                serviceMessenger.send(msg);
            } catch(RemoteException e) {
                Ln.e(e);
            }
        }
    }

    protected void startRecordingPathServiceIfNeed() {
        if(!AndroidHelper.isServiceRunning(this, ServiceRecordingPath.class)) {
            Intent intent = new Intent(this, ServiceRecordingPath.class);
            intent.putExtra("receiver", resultReceiver);
            startService(intent);
        }
    }

    protected void bindServiceIfNeed() {
        if(!isBound) {
            Intent intent = new Intent(this, ServiceRecordingPath.class);
            bindService(intent, connection, 0);
        }
    }

    protected void loadPathHistoryFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragmentPathHistory, fragmentPathHistory.TAG);
        transaction.commit();
    }

    protected ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Ln.d("onServiceConnected() : name : %s, service : %s", name, service);
            serviceMessenger = new Messenger(service);
            if(messegeToBeSentOnceConnected != null) {
                try {
                    serviceMessenger.send(messegeToBeSentOnceConnected);
                } catch(RemoteException e) {
                    Ln.e(e);
                }
            }
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Ln.d("onServiceDisconnected() : name : %s", name);
            serviceMessenger = null;
            isBound = false;
        }
    };

    protected ResultReceiver resultReceiver = new ResultReceiver(handler) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Ln.d("onReceiveResult() : resultCode : %d, resultData : %s", resultCode, resultData);

            super.onReceiveResult(resultCode, resultData);
        }
    };
}
