package liang.leo.fronzen_b.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import liang.leo.fronzen_b.R;
import liang.leo.fronzen_b.ViewModel.MonitorViewModel;

public class MonitorActivity extends AppCompatActivity {
    MonitorViewModel monitorViewModel;
    TextView temTv;
    LiveData<Integer> temLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViewModel();

    }

    private void bindViewModel(){
        monitorViewModel = new ViewModelProvider(this).get(MonitorViewModel.class);

        temTv = findViewById(R.id.Temperature_tv);

        Observer<Integer> temObserver = integer -> {
            if (temLiveData.getValue() != null) {
                temTv.setText("" + temLiveData.getValue());
            } else {
                temTv.setText(getResources().getString(R.string.temp_unavailable));
            }
        };

        Button monitor_btn = findViewById(R.id.monitor_button);
        monitor_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Set views and logic when is not monitoring
                if (!monitorViewModel.isMonitoring()) {
                    //Start the monitor
                    temLiveData = monitorViewModel.startMonitor();
                    temLiveData.observe(MonitorActivity.this, temObserver);
                    //Change button to end monitor form
                    monitor_btn.setText(getResources().getString(R.string.end_monitor));
                }else {
                    //End the monitor
                    monitorViewModel.endMonitor();
                    temLiveData.removeObserver(temObserver);
                    //Change button to start monitor form
                    monitor_btn.setText(getResources().getString(R.string.start_monitor));
                }
            }
        });

        Button button2 = findViewById(R.id.modify_button);
        button2.setOnClickListener(v -> {
            Intent intent1 = new Intent(MonitorActivity.this, ModifyActivity.class);
            startActivity(intent1);
        });
    }
}
