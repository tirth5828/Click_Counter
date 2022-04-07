package com.tirth5828.Click_Counter;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    TextView counter,timer,highscore;
    ImageView background_to_counter,reset;
    boolean is_counter_on,is_first_tick;
    int count;
    CountDownTimer counter_timer;
    int time = 30;
    String start_time = String.valueOf(time);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = findViewById(R.id.textView_counter);
        timer = findViewById(R.id.textView_timer);
        background_to_counter = findViewById(R.id.imageView);
        highscore = findViewById(R.id.textView_highscore);
        reset = findViewById(R.id.imageView_reset);

        reset.setVisibility(View.INVISIBLE);
        timer.setText(R.string.thirty_second);

        is_counter_on = true;
        is_first_tick = true;

        counter.setText(R.string.click_me);

        SharedPreferences sharedP = getSharedPreferences("score",MODE_PRIVATE);
        String hiscore = sharedP.getString("str","0");
        highscore.setText(hiscore);

        counter_timer = new CountDownTimer(time* 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timer.setText(String.format("%s",  f.format(sec)));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                timer.setText(R.string.zero_time);
                timer_over();
            }
        };
    }

    public void timer_over(){
        background_to_counter.setImageResource(R.drawable.red_circle);
        is_counter_on = false;
        sethighscore();
        reset.setVisibility(View.VISIBLE);
    }

    private void sethighscore() {

        highscore.setText(counter.getText().toString());

        SharedPreferences shp = getSharedPreferences("score",MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        int hiscore_prev = Integer.parseInt(shp.getString("str","0"));
        int hiscore_present = Integer.parseInt(counter.getText().toString());

        if (hiscore_present>hiscore_prev){
            highscore.setText(counter.getText().toString());
            editor.putString("str",String.valueOf(hiscore_present));
            editor.apply();
        }
        editor.apply();
    }

    public void click(View view) {
        if(is_first_tick) {
            counter_timer.start();
            is_first_tick = false;
        }
        try {
            count = Integer.parseInt(counter.getText().toString());
        }
        catch (NumberFormatException e){
            count = 0;
        }
        if(is_counter_on) count++;
        counter.setText(String.valueOf(count));
    }

    public void reset(View view) {
        is_first_tick = true;
        is_counter_on = true;
        count = 0;
        background_to_counter.setImageResource(R.drawable.black_circle);
        counter.setText(String.valueOf(0));
        counter_timer.cancel();
        timer.setText(start_time);
    }
}