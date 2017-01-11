package jp.sio.sample.servicetoactivitysample;
/**
 * 2016/12/27
 * LBS試験チーム
 * ServiceからActivityへ同期してデータを通知する
 */

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Handler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import jp.sio.sample.servicetoactivitysample.MyService;
import jp.sio.sample.servicetoactivitysample.R;

public class MainActivity extends AppCompatActivity {

    private final FooReciever receiver = new FooReciever();
    private MyService myService;

    private boolean D = true;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.MyService_Binder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn = (Button)findViewById(R.id.button);

        final Intent foo_intent = new Intent(this,MyService.class);

        //ボタンを押して別スレッドで処理を開始
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO:実行する処理を書く
                        if(D) Log.d("TEST","StartService");
                        foo_intent.putExtra("num",60);
                        startService(foo_intent);
                        IntentFilter intentFilter = new IntentFilter(MyService.FILTER);
                        registerReceiver(receiver,intentFilter);

                        bindService(foo_intent,serviceConnection,Context.BIND_AUTO_CREATE);
                    }
                }).start();
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    //This method will be called when a MessageEvent is posted
    @Subscribe
    public void onMessageEvent(MessageEvent event){
        Log.d("ServicetoActivity",event.message);
    }


    //Intentを受け取ったときの処理を記述
    public class FooReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 時間がかかる処理なら別スレッドで処理を行うこと
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //TODO:実行する処理を書く
                    if(D) Log.d("TEST","Recieve");
                }
            }).start();

        }
    }
}

