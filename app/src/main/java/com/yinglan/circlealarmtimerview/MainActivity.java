package com.yinglan.circlealarmtimerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yinglan.circleviewlibrary.CircleAlarmTimerView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private TextView textView1;
    private TextView textView2;
    private CircleAlarmTimerView circleAlarmTimerView;
    private Button setRandom, showTime;
    private CheckBox cbDigital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        textView1 = (TextView) findViewById(R.id.start);
        textView2 = (TextView) findViewById(R.id.end);

        circleAlarmTimerView = (CircleAlarmTimerView) findViewById(R.id.circletimerview);
        circleAlarmTimerView.setOnTimeChangedListener(new CircleAlarmTimerView.OnTimeChangedListener() {
            @Override
            public void start(String starting) {
                textView1.setText(starting);
            }

            @Override
            public void end(String ending) {
                textView2.setText(ending);
            }
        });

        circleAlarmTimerView.drawDigitalClock(true);

        setRandom = (Button) findViewById(R.id.btnSetRandomTime);
        setRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minute = new Random().nextInt(60);
                int hour = new Random().nextInt(12);
                Log.d("set time", hour + " " + minute);
                circleAlarmTimerView.setCurrentHour(hour);
                circleAlarmTimerView.setCurrentMinute(minute);
            }
        });

        showTime = (Button) findViewById(R.id.btnShowTimeToast);
        showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = circleAlarmTimerView.getCurrentHour() + ":" + circleAlarmTimerView.getCurrentMinute();
                Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();
            }
        });

        cbDigital = (CheckBox) findViewById(R.id.cbDrawDigital);
        cbDigital.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circleAlarmTimerView.drawDigitalClock(isChecked);
            }
        });

        setRandom.performClick();
    }
}
