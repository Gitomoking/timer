package com.tech.tomoking.watchshareapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    private Timer timer;
    private CountUpTimerTask timerTask;
    private Handler handler = new Handler();

    private TextView currentWatchText;
    private long count, delay, period;
    private String zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        delay = 0;
        period = 10; // Count Up with 10 msec
        // "00:00.00"
        zero = getString(R.string.zero);

        // TextView for showing StopWatch
        currentWatchText = findViewById(R.id.timer);
        currentWatchText.setText(zero);

        // Set Listener to Start Button
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new playButtonClickListener());

        // Set Listener to Stop Button
        Button stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new playButtonClickListener());

        // Set Listener to Rap Button
        Button rapButton = findViewById(R.id.rap_button);
        rapButton.setOnClickListener(new playButtonClickListener());
    }

    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    count++;
                    long mm = count*100 / 1000 / 60;
                    long ss = count*100 / 1000 % 60;
                    long ms = (count*100 - ss * 1000 - mm * 1000 * 60)/100;
                    // 桁数を合わせるために02d(2桁)を設定
                    currentWatchText.setText(
                            String.format(Locale.US, "%1$02d:%2$02d.%3$01d", mm, ss, ms));
                }
            });
        }
    }

    private class startButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // タイマーが走っている最中にボタンをタップされたケース
            if(null != timer){
                timer.cancel();
                timer = null;
            }

            // Timer インスタンスを生成
            timer = new Timer();

            // TimerTask インスタンスを生成
            timerTask = new CountUpTimerTask();

            // スケジュールを設定 100msec
            // public void schedule (TimerTask task, long delay, long period)
            timer.schedule(timerTask, delay, period);

            // カウンター
            count = 0;
            currentWatchText.setText(zero);

        }
    }

    private class playButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Branch by id
            int id = v.getId();

            switch (id) {
                case R.id.play_btn:
                    if (timer != null) {
                        // Stop
                    } else {
                        
                    }
                    break;
                case R.id.rap_btn:
                    // Rap timer
                    break;
                case R.id.reset_btn:
                    // If timer is not null then cancel
                    if (timer != null) {
                        // Cancel
                        timer.cancel();
                        timer = null;
                    }
                    currentWatchText.setText(zero);
                    break;

            }
        }
    }
}
