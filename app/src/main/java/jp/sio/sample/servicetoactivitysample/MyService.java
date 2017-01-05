package jp.sio.sample.servicetoactivitysample;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends Service {
    public MyService() {

    }
    //BroadcastReceiver用
    public static final String FILTER = "MyService";
    public static final String SUCCESS = "Success";
    public static final String FAIL = "FAIL";
    private BroadcastReceiver receiver;

    //結果のケース
    private static final int SUCCESS_NUM = 0;
    private static final int FAIL_NUM = 1;

    public void onCreate(){
        Log.d("TestService","onCreate");
        super.onCreate();

    }
    class MyService_Binder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d("TestService","onStartCommand");
        super.onStartCommand(intent,flags,startId);
        mStart(intent);

        return START_NOT_STICKY;
    }
    private void mStart(Intent intent) {
        int getnum = intent.getIntExtra("num",1);

        Log.d("TestService","mStart");
        switch(getnum){
            case SUCCESS_NUM:
                sendBroadcast(new  Intent(SUCCESS));
                break;
            case FAIL_NUM:
                sendBroadcast(new Intent(FAIL));
                break;
        }
        sendBroadcast(new  Intent(FILTER));
    }



    @Override
    public IBinder onBind(Intent intent) {
        return new MyService_Binder();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }
}
