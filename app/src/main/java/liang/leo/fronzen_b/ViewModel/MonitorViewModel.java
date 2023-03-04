package liang.leo.fronzen_b.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import liang.leo.fronzen_b.BlockChainModel.ContractTest;
import liang.leo.fronzen_b.BlockChainModel.Monitor;


public class MonitorViewModel extends ViewModel {
    MutableLiveData<Integer> temperature;
    boolean monitoring = false;
    Monitor monitor;

    public MutableLiveData<Integer> startMonitor(){
        monitoring = true;
        if (temperature == null){
            temperature = new MutableLiveData<>();
        }

        if (monitor == null){
            monitor = Monitor.getInstance();
        }

        monitor.startMonitoring(temperature);

        return temperature;
    }

    public void endMonitor(){
        monitoring = false;
        monitor.endMonitoring();
    }

    public boolean isMonitoring(){
        return this.monitoring;
    }


}
