package liang.leo.fronzen_b.BlockChainModel;

public class TempContract {
    public static int getTemp(){
        int res = -1;
        try {
            res = ContractTest.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void setTemp(int temp){

    }
}
