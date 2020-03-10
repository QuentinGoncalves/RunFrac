package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class CountDownTimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_timer);


        final CircularProgressIndicator circularProgress = findViewById(R.id.countdowntimer_progress);
        circularProgress.setProgress(0, 15);

        final CountDownTimer count = new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                circularProgress.setCurrentProgress((millisUntilFinished/1000));
            }

            public void onFinish() {
                Intent intent = new Intent(CountDownTimerActivity.this, RunningActivity.class);
                Bundle extras = getIntent().getExtras();
                boolean interval = true;
                if (extras != null) {
                    interval = extras.getBoolean("interval");
                }
                if(!interval){
                    intent.putExtra("interval", interval);
                }
                startActivity(intent);
            }

        }.start();

    }
}
