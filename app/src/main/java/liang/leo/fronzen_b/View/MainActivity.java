package liang.leo.fronzen_b.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import liang.leo.fronzen_b.BlockChainModel.Monitor;
import liang.leo.fronzen_b.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Monitor monitor = Monitor.getInstance();
        monitor.startMonitoring();
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        long time_run = 0;
        while (time_run<= 6000){
            end = System.currentTimeMillis();
            time_run = end-start;
        }
        monitor.endMonitoring();

        start = System.currentTimeMillis();
        monitor.startMonitoring();
        time_run = 0;
        while (time_run <= 7000){
            end = System.currentTimeMillis();
            time_run = end-start;
        }
        monitor.endMonitoring();
    }
}