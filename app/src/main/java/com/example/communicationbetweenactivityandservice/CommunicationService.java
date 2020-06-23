package com.example.communicationbetweenactivityandservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class CommunicationService extends Service {

    public static final String TAG="communicationService";


    //Custom Binder
    private MyBinder mLocalBinder=new MyBinder();

    //Custom interface callback which is declared in this Service
    private MyCallBack mCallBack;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Getting the instance of Binder

        return mLocalBinder;

    }

    //CallBack Setter
    public void setCallBack(MyCallBack callBack) {
        this.mCallBack = callBack;
    }


    //Method to perform Long Running Task for User
    public void doLongRunningTaskInService(int timeInSec)
    {

        startMyTimer(timeInSec);
    }



    //callback interface
    public interface MyCallBack
    {
        void onOperationProgress(int progress);

        void onOperationCompleted();
    }


    //custom binder Class
    class MyBinder extends Binder
    {
        public CommunicationService getService()
        {
            return CommunicationService.this;
        }
    }




    public void startMyTimer(final Integer timeInSec)
    {

        CountDownTimer countDownTimer=new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG,"Time remaining "+ millisUntilFinished);
                int progress= (int) (10-(millisUntilFinished)/1000);
                mCallBack.onOperationProgress(progress);
            }

            @Override
            public void onFinish() {

                mCallBack.onOperationProgress(timeInSec);
                mCallBack.onOperationCompleted();

            }
        };

        countDownTimer.start();

    }


}
