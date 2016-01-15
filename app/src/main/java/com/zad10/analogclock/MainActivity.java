package com.zad10.analogclock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Timer timer = new Timer();
    private AnalogClock analogClock;

    private Time mTime = new Time();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//          handler odswieza tylko nasze nowe view - AnalogClock
            analogClock.invalidate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        analogClock = (AnalogClock) findViewById(R.id.analogClock);

//        nizej w komentarzu jak ustawic statycznie konkretna godzine
//        analogClock.setTime(12, 30, 0);
//        analogClock.invalidate();

        TimerTask task = new TimerTask() {  //TimerTask jest takim nowym rodzajem watku, implementuje Runnable
            @Override
            public void run() {
                mTime.setToNow();
                Log.i("TIME", mTime.hour + ":" + mTime.minute + ":" + mTime.second);

//                ustawiamy konkretną godzine z mTime
                analogClock.setTime(mTime.hour, mTime.minute, mTime.second);

//              używamy handlera bo zwykły wątek nie może zmienić UI a handler tak
                handler.sendEmptyMessage(0);
            }
        };

//        timer wykonuje metodę run z obiektu TimerTask co 1000ms czyli 1sekunde (bez opoznienia - drugi parametr)
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}
