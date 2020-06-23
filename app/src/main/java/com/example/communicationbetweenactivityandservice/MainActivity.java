package com.example.communicationbetweenactivityandservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ServiceConnection, CommunicationService.MyCallBack {

    public static final String TAG="communication";

    TextView txtTimer;
    Button btnStartTimer;

    CommunicationService communicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer = findViewById(R.id.txtTimer);
        btnStartTimer = findViewById(R.id.btnStartTimer);

        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTriggerLongRunningOperation();

            }
        });


       bindMyService();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindMyService();
    }

    private void bindMyService()
    {
        Intent intentData = new Intent(this, CommunicationService.class);
        bindService(intentData, this, BIND_AUTO_CREATE);
        Log.i(TAG," Service binded");
    }

   private void unBindMyService()
   {
       //unbind Service if activity is in onPause state
       if (communicationService != null) {
           communicationService.setCallBack(null);
           unbindService(this);
           Log.i(TAG," Service UnBinded");

       }
   }




    public void onTriggerLongRunningOperation() {

        Log.i(TAG,"Inside onTriggerLongRunningOperation ");


        if (communicationService != null) {
            Log.i(TAG,"calling doLongRunningTaskInService ");
            communicationService.doLongRunningTaskInService(10);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {

        Toast.makeText(this, "Service Connected", Toast.LENGTH_SHORT).show();

        communicationService = ((CommunicationService.MyBinder) iBinder).getService();

        communicationService.setCallBack(this);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        Toast.makeText(this, "Service Disconnected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onOperationProgress(int progress) {
        //callback result from the service
        txtTimer.setText(progress + "");


    }

    @Override
    public void onOperationCompleted() {

        //resultant output from service after task completion
        Toast.makeText(this, "Timer Completed", Toast.LENGTH_SHORT).show();
    }
}
