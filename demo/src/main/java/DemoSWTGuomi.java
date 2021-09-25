import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jccdex.core.client.WalletSM;
import com.jccdex.rpc.JccJingtum;
import com.jccdex.rpc.http.OkhttpUtil;

import java.util.ArrayList;

public class DemoSWTGuomi {
    public static void main(String[] args) {
        JccJingtum jccJingtum = null;

        WalletSM wallet1 = WalletSM.fromSecret("snHu1pSHRksTQQMJGa2gZH6gTSukK");//jpSojXsu7mfwStH7ig72yCg86ViKH5dHWN
        WalletSM wallet2 = WalletSM.fromSecret("snwsVxeKqgKqckhomX6xvguwj5Jci");//j35gQsGrcsTPSmmRmxgVRwtetkp6NGvN4Z
        WalletSM wallet3 = WalletSM.fromSecret("snH36VVwTHDEkuHfgAsfKg1UCYhcJ");//jG7NeW8QYQbm1xZvxhH1fEMFtYCP1MHqRY
        //初始化JccJingtum Lib
        try {
            ArrayList<String> rpcNodes = new ArrayList<String>();
            rpcNodes.add("http://139.198.19.157:4950");
            jccJingtum = new JccJingtum(rpcNodes,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建钱包
        try {
            System.out.println("创建钱包-------------------------------------------------------");
            String _wallet = jccJingtum.createWallet();
            System.out.println(_wallet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //转账(不校验)
        System.out.println("转账(不校验)-------------------------------------------------------");
        long st = System.currentTimeMillis();
        try {
                String ret = jccJingtum.paymentNoCheck(wallet1.getSecret(),wallet2.getAddress(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
                System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }

        //转账(校验)
        System.out.println("转账(校验)-------------------------------------------------------");
        st = System.currentTimeMillis();
        try {
            String ret = jccJingtum.paymentWithCheck(wallet1.getSecret(),wallet2.getAddress(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }

        //挂单(不校验)
        System.out.println("挂单(不校验)-------------------------------------------------------");
        st = System.currentTimeMillis();
        try {
            String ret = jccJingtum.createOrderNoCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","1","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }

        //挂单(校验)
        System.out.println("挂单(校验)-------------------------------------------------------");
        st = System.currentTimeMillis();
        try {
            String ret = jccJingtum.createOrderWithCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","1","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }

        //撤单
        System.out.println("撤单-------------------------------------------------------");
        st = System.currentTimeMillis();
        try {
            String ret1 = jccJingtum.createOrderWithCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","100","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
            long sequence = jccJingtum.getSequence(wallet1.getAddress());
            String ret2 = jccJingtum.cancleOrder(wallet1.getSecret(), sequence-1);
            System.out.println(ret2);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }

        //获取交易详情
        System.out.println("获取交易详情-------------------------------------------------------");
        try {
            String res = jccJingtum.requestTx("9EBFB6A0B5A98B8A9E617EE57F4045B44A0D2A8BFA5946C6F0D5925BE9B99D4C");
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取交易详情
        System.out.println("获取sequence-------------------------------------------------------");
        try {
            long sequence = jccJingtum.getSequence(wallet1.getAddress());
            System.out.println(sequence);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
