package liang.leo.fronzen_b.BlockChainModel;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ContractTest {
    public static void main(String[] args) throws Exception {
        getName();
    }

    public static String node = "https://goerli.infura.io/v3/204f40729eb14b1aa089e13b5270e421";
    static Web3j web3j = Web3j.build(new HttpService(node));
    static Credentials credentials = Credentials.create("55e1f43304a4f499254403e2b87e257f8a6e714d1b399d8644036a48ca36254c");


    public static final String contractAddress = "0x87e3b748aec53704a2bc8458e3aa01933b471f50";


    /**
     * 调用合约的只读方法，无需gas
     * @throws Exception
     */
    public static int getName() throws Exception {
        Function function = new Function(
                "retrieve",
                Collections.emptyList(),
                Arrays.asList(new TypeReference<Utf8String>(){}));

        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                        Transaction.createEthCallTransaction(null, contractAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        String hex = response.getResult();
        for(int i = 0 ; i < hex.length(); i++){
            char c = hex.charAt(i);
            if (c != '0' && c != 'x'){
                hex = hex.substring(i);
                break;
            }
            if(i == hex.length()-1){
                hex = "0";
            }
        }
        return parseInt(hex,16);
    }

    /**
     * 需要支付gas的方法
     * @throws Exception
     */
    public static void setName(int num) throws Exception {
        Function function = new Function(
                "store",
                Arrays.asList(new Uint256(num)),
                Collections.emptyList());
        BigInteger nonce = getNonce(credentials.getAddress());
        String encodedFunction = FunctionEncoder.encode(function);

        BigInteger gasLimit = new BigInteger("300000");
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, DefaultGasProvider.GAS_PRICE.multiply(BigInteger.valueOf(10)),gasLimit, contractAddress, encodedFunction);

        org.web3j.protocol.core.methods.response.EthSendTransaction response =
                web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                        .sendAsync()
                        .get();

        String transactionHash = response.getTransactionHash();
        System.out.println(transactionHash);
    }

    /**
     * 需要支付gas和value的合约方法调用
     * @throws Exception
     */
    public void payETH() throws Exception {
        BigInteger nonce = getNonce(credentials.getAddress());
        Function function = new Function("payETH",
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST);

        String functionEncode = FunctionEncoder.encode(function);
        BigInteger value = new BigInteger("200");
        // 与不需要支付的value的方法调用，差别就在于多传一个eth数量的value参数
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, DefaultGasProvider.GAS_PRICE,DefaultGasProvider.GAS_LIMIT, contractAddress, value,functionEncode);
        org.web3j.protocol.core.methods.response.EthSendTransaction response =
                web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                        .sendAsync()
                        .get();
        String transactionHash = response.getTransactionHash();
        System.out.println(transactionHash);
    }


    private static BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
        return ethGetTransactionCount.getTransactionCount();
    }

}

