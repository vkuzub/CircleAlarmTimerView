package com.yinglan.circlealarmtimerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yinglan.circleviewlibrary.CircleAlarmTimerView;


public class MainActivity extends AppCompatActivity {


    private TextView textView1;
    private TextView textView2;
    private CircleAlarmTimerView circleAlarmTimerView;
    private Button setRandom, showTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
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

        setRandom = (Button) findViewById(R.id.btnSetRandomTime);
        setRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        showTime = (Button) findViewById(R.id.btnShowTimeToast);
        showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = circleAlarmTimerView.getCurrentHour()+":"+circleAlarmTimerView.getCurrentMinute();
                Toast.makeText(getApplicationContext(),time,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
