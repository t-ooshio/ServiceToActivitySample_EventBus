package jp.sio.sample.servicetoactivitysample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by NTT docomo on 2017/01/11.
 */

public class MessageEvent {
    public final String message;

    public MessageEvent(String message){
        this.message = message;
    }


}
