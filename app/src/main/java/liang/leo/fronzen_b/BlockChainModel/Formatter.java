package liang.leo.fronzen_b.BlockChainModel;

public class Formatter {
    public static String getTime(Long time) {
        Long milsec = time%1000;
        time /= 1000;
        String timeStr="";
        if (time==null){
            return null;
        }
        //时
        Long hour = time / 60 / 60;
        //分
        Long minutes = time / 60 % 60;
        //秒
        Long remainingSeconds = time % 60;
        //判断时分秒是否小于10……
        if (hour < 10){
            timeStr =  minutes + "分" + remainingSeconds+"秒";
        }else if (minutes < 10){
            timeStr =  minutes + "分" + remainingSeconds+"秒";
        }else if (remainingSeconds < 10){
            timeStr =  minutes + "分" + "0" + remainingSeconds+"秒";
        }else {
            timeStr =  minutes + "分" + remainingSeconds+"秒";
        }

        timeStr += milsec+"微秒";
        return timeStr;
    }

}
