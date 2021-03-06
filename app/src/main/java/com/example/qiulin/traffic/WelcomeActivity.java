package com.example.qiulin.traffic;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.example.qiulin.traffic.utils.DataUtil;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qiulin on 2015/1/16 0016.
 */
public class WelcomeActivity extends Activity {
    private Timer timer;
    final private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1314){
                timer.cancel();
                com.example.qiulin.traffic.beans.vo.User user = DataUtil.getDate(WelcomeActivity.this);
                if(null != user){
                    Intent intent = new Intent(WelcomeActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(WelcomeActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1314);
            }
        },2000,5000);
    }
    @Override
    protected void onDestroy(){
        timer.cancel();
        super.onDestroy();
    }
    public static void launch(Activity activity) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeCustomAnimation(activity,
                        R.anim.head_in, R.anim.head_out);
        Intent intent = new Intent(activity, WelcomeActivity.class);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
