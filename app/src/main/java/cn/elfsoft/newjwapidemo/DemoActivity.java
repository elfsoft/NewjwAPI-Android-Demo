package cn.elfsoft.newjwapidemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.List;


public class DemoActivity extends ActionBarActivity {
    private String sessionId;
    private HttpCent cent;
    private TextView displayView;
    private Bar bar;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                displayView.setText((String) msg.obj);
            } else {
                Toast.makeText(getApplicationContext(), "network error,please try again!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initUI();
        sessionId = Config.getSettings(this, "sessionid");
        cent = new HttpCent();
    }

    public void initUI() {
        displayView = (TextView) findViewById(R.id.displayView);
    }

    public void loadScheduleBar(View v) {
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    String result = cent.getScheduleBarInfo(sessionId);
                    List<Bar> list = JSON.parseArray(result, Bar.class);
                    if (list.size() > 0) {
                        bar = list.get(0);
                    }
                    msg.what = 1;
                    msg.obj = result;
                } catch (Exception e) {
                    msg.what = 10;
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void loadSchedule(View v) {
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    msg.what = 1;
                    if (bar != null) {
                        String result = cent.getScheduleInfo(sessionId, bar.getTerm(), bar.getHidyzm());
                        msg.obj = result;
                    } else {
                        msg.obj = "get hydizm faild";
                    }
                } catch (Exception e) {
                    msg.what = 10;
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
