package liang.leo.fronzen_b.BlockChainModel;

import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Monitor {
    private static Monitor instance = null;

    private volatile boolean monitoring;
    private int cur_temp;
    private Thread thread;
    private Runnable monitorTask;
    private long monitorStartTime;

    private Monitor(){
        monitoring = false;
        cur_temp = TempContract.getTemp();
//        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0l,
//                TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    }

    //Returning the monitor in singleton mode
    public static Monitor getInstance(){
        if (instance == null){
            instance = new Monitor();
        }
        return instance;
    }

    public void startMonitoring(MutableLiveData<Integer> data){
        monitoring = true;
        createMonitorTask(data);
        thread.start();
        monitorStartTime = System.currentTimeMillis();
    }

    public void endMonitoring(){
        monitoring = false;
        thread.interrupt();
        long monitorTime = System.currentTimeMillis()-monitorStartTime;
        monitorStartTime = -1;

        System.out.println("Monitoring time:" + Formatter.getTime(monitorTime));
    }

    private void createMonitorTask(MutableLiveData<Integer> data){
        monitorTask = () -> {
            while (monitoring){
                try {
                    data.postValue(ContractTest.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {

                }
            }
        };
        thread = new Thread(monitorTask);

    }
}
