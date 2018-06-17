package com.tech.tomoking.watchshareapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    // Timer Instance
    private Timer timer;
    private CountUpTimerTask timerTask;
    private Handler handler = new Handler();

    private TextView currentWatchText;
    private ListView lvRap;
    private Button playButton;
    private long count, delay, period;
    private ArrayList<String> rapCountList;
    private ArrayAdapter<String> rapAdapter;

    private String zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Initialize member variables
        delay = 0;
        period = 10; // Count Up with 10 msec
        zero = getString(R.string.zero);
        rapCountList = new ArrayList<>();
        rapCountList.add("Rap Time");

        // TextView for showing StopWatch
        currentWatchText = findViewById(R.id.timer);
        currentWatchText.setText(zero);

        // Set Up Rap List View
        lvRap = findViewById(R.id.rap_lv);
        rapAdapter = new ArrayAdapter<>(TimerActivity.this,
                android.R.layout.simple_list_item_1, rapCountList);
        lvRap.setAdapter(rapAdapter);

        // Set Listener to Start / Stop Button
        Button startButton = findViewById(R.id.play_btn);
        startButton.setOnClickListener(new playButtonClickListener());

        // Set Listener to Rap Button
        Button stopButton = findViewById(R.id.rap_btn);
        stopButton.setOnClickListener(new playButtonClickListener());

        // Set Listener to Reset Button
        Button rapButton = findViewById(R.id.reset_btn);
        rapButton.setOnClickListener(new playButtonClickListener());
    }

    // Get string of current count value
    private String getStringOfCountValue() {
        long mm = count*10 / 1000 / 60;
        long ss = count*10 / 1000 % 60;
        long ms = (count*10 - ss * 1000 - mm * 1000 * 60)/10;
        return String.format(Locale.US, "%1$02d'%2$02d''%3$02d", mm, ss, ms);
    }

    private class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    count++;
                    currentWatchText.setText(getStringOfCountValue());
                }
            });
        }
    }

    private class playButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Branch by id
            int id = v.getId();

            switch (id) {
                case R.id.play_btn:
                    playButton = findViewById(R.id.play_btn);
                    // If timer is null then start else then stop and keep current state
                    if (timer == null) {
                        // Start
                        playButton.setText(R.string.stop_btn);
                        timer = new Timer();
                        timerTask = new CountUpTimerTask();
                        timer.schedule(timerTask, delay, period);
                    } else {
                        // Stop
                        playButton.setText(R.string.start_btn);
                        timer.cancel();
                        timer = null;
                    }
                    break;
                case R.id.rap_btn:
                    // Rap timer
                    rapCountList.add(getStringOfCountValue());
                    rapAdapter.notifyDataSetChanged();
                    break;
                case R.id.reset_btn:
                    // If timer is not null then cancel
                    if (timer != null) {
                        // Cancel
                        timer.cancel();
                        timer = null;
                    }
                    // Clear Rap list view
                    rapCountList.clear();
                    rapCountList.add("Rap Time");
                    rapAdapter.notifyDataSetChanged();
                    // Clear stop watch
                    count = 0;
                    currentWatchText.setText(zero);
                    break;

            }
        }
    }
}
