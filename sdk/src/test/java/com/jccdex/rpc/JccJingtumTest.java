package com.jccdex.rpc;

import com.jccdex.core.client.Wallet;
import com.jccdex.core.client.WalletSM;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class JccJingtumTest extends TestCase {

    JccJingtum jccJingtum;
//    Wallet wallet1 = Wallet.fromSecret("ssEEef7JHubPGTCLwTLkuu4oqKtD6");
//    Wallet wallet2 = Wallet.fromSecret("ssPFdoM4pZF1HSyrZ8ymnyxYzoE7h");
//    Wallet wallet3 = Wallet.fromSecret("ssVvAZrAUj7dxFfLdaVvoVH2VTij2");

    WalletSM wallet1 = WalletSM.fromSecret("snHu1pSHRksTQQMJGa2gZH6gTSukK");//jpSojXsu7mfwStH7ig72yCg86ViKH5dHWN
    WalletSM wallet2 = WalletSM.fromSecret("snwsVxeKqgKqckhomX6xvguwj5Jci");//j35gQsGrcsTPSmmRmxgVRwtetkp6NGvN4Z
    WalletSM wallet3 = WalletSM.fromSecret("snH36VVwTHDEkuHfgAsfKg1UCYhcJ");//jG7NeW8QYQbm1xZvxhH1fEMFtYCP1MHqRY


    public void setUp() throws Exception {
        super.setUp();
        ArrayList<String> rpcNodes = new ArrayList<String>();
//        rpcNodes.add("http://39.98.243.77:50333");
//        rpcNodes.add("http://58.243.201.56:50333");
//        rpcNodes.add("http://59.175.148.101:50333");
//        rpcNodes.add("http://65.95.56.49:5050");
//        rpcNodes.add("http://1.13.2.21:5050");
//        rpcNodes.add("https://stestswtcrpc.jccdex.cn");
        rpcNodes.add("http://139.198.19.157:4950");

        jccJingtum = new JccJingtum(true, rpcNodes);
//        System.out.println(jccJingtum.getAddress("ssPFdoM4pZF1HSyrZ8ymnyxYzoE7h"));
//        jccJingtum.setPlatform("");
    }

    @Test
    public void testCreateWallet() {
        System.out.println("in testCreateWallet");
        try {
            String _wallet = jccJingtum.createWallet();
            System.out.println(_wallet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSequence() {
        System.out.println("in testSequence");
        try {
            long sequence = jccJingtum.getSequence(wallet3.getAddress());
            System.out.println(sequence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPaymentNoCheck() {
        System.out.println("in testPaymentNoCheck");
        long st = System.currentTimeMillis();
        try {
            for(int i=0; i<1; i++) {
                String ret = jccJingtum.paymentNoCheck(wallet1.getSecret(),wallet2.getAddress(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
                System.out.println(ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }
    }

    @Test
    public void testPaymentWithCheck() {

        System.out.println("in paymentWithCheck");
        long st = System.currentTimeMillis();
        try {
            for(int i=0; i<1; i++) {
                String ret = jccJingtum.paymentWithCheck(wallet1.getSecret(),wallet2.getAddress(),"SWT","1.5","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
                System.out.println(ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }
    }

    @Test
    public void testCreateOrderWithCheck() {

        System.out.println("in createOrderWithCheck");
        long st = System.currentTimeMillis();
        try {
            String ret = jccJingtum.createOrderWithCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","1","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }
    }

    @Test
    public void testCreateOrderNoCheck() {
        System.out.println("in createOrderNoCheck");
        long st = System.currentTimeMillis();
        try {
            String ret = jccJingtum.createOrderNoCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","1","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }
    }

    @Test
    public void testCancleOrder() {

        System.out.println("in testCancleOrder");
        long st = System.currentTimeMillis();
        try {
            String ret1 = jccJingtum.createOrderWithCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","100","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
            long sequence = jccJingtum.getSequence(wallet1.getAddress());
            String ret2 = jccJingtum.cancleOrder(wallet1.getSecret(), sequence-1);
            System.out.println(ret2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }
    }
//
    @Test
    public void testRequestTX() {

        System.out.println("in testRequestTX");
        try {
            String res = jccJingtum.requestTx("819B51F9B6B72BB2D7FBC6FBEDC2F3518B03F228B952AD8000F3588E9F905B46");
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetMemoData() {

        System.out.println("in testGetMemoData");
        try {
            String memo = jccJingtum.getMemoData("7B22656E645F74696D65223A312E36323134313138394531322C22706C616E223A2241222C22746F74616C5F616D6F756E74223A2230222C2273746172745F74696D65223A313632313431313032373435352C22757365725F77616C6C65745F61646472657373223A226A4D674B34427A36774E6E626638537775687A52326451454C324A6A7A38574D6233227D");
            System.out.println(memo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void tearDown() throws Exception {
    }
}