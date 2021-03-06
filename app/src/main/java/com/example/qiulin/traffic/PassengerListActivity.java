package com.example.qiulin.traffic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qiulin.traffic.adapter.PassengerListAdapter;
import com.example.qiulin.traffic.beans.DataBean;
import com.example.qiulin.traffic.beans.Passenger;
import com.example.qiulin.traffic.utils.DataUtil;
import com.example.qiulin.traffic.utils.Utils;
import com.example.qiulin.traffic.utils.okhttputils.OkHttpUtils;
import com.example.qiulin.traffic.utils.okhttputils.callback.BeanCallBack;
import com.example.qiulin.traffic.utils.okhttputils.request.PostRequest;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qiulin on 2015/1/20 0020.
 */
public class PassengerListActivity extends AppCompatActivity {
    private ListView listView;
    private PassengerListAdapter mAdapter;
    private static List<Passenger> alarmList;
    private ImageButton mFabButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengerlist);
        alarmList = getData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("客运、旅游客车登记信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassengerListActivity.this.onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        listView = (ListView) findViewById(R.id.list);
        mAdapter = new PassengerListAdapter(PassengerListActivity.this,alarmList);
        listView.setAdapter(mAdapter);
        mFabButton = (ImageButton) findViewById(R.id.fab_button);
        mFabButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_plus).color(Color.WHITE));
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassengerFormActivity.launch(PassengerListActivity.this);
            }
        });
        Utils.configureFab(mFabButton);
        listView.getOnItemClickListener();

    }
    public static void launch(Activity activity) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeCustomAnimation(activity,
                        R.anim.head_in, R.anim.head_out);
        Intent intent = new Intent(activity, PassengerListActivity.class);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.search:
                    break;
            }
            if(!msg.equals("")) {
                Toast.makeText(PassengerListActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.driver_menu, menu);
        return true;
    }
    private List<Passenger> getData() {
        String url = DataUtil.getUrl(PassengerListActivity.this,"passengerlist");
        OkHttpUtils.post(url).tag(this)
                .params("rowCountOfOnePage","100")
                .mediaType(PostRequest.MEDIA_TYPE_PLAIN)//
                .execute(new BeanCallBack<DataBean<Passenger>>() {
                    @Override
                    public void onResponse(boolean isFromCache, DataBean<Passenger> data, Request request, @Nullable Response response) {
                        if(data.getCode() != null && data.getCode() == 0){
                            alarmList = data.getData();
                            listView = (ListView) findViewById(R.id.list);
                            mAdapter = new PassengerListAdapter(PassengerListActivity.this,alarmList);
                            listView.setAdapter(mAdapter);
                        }
                    }
                }, PassengerListActivity.this);
        return alarmList;
    }
}
