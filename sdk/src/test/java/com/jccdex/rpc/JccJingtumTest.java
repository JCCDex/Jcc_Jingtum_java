package com.jccdex.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jccdex.core.client.Wallet;
import com.jccdex.core.client.WalletSM;
import com.jccdex.rpc.core.types.known.tx.signed.SignedTransaction;
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
//        rpcNodes.add("https://testskywelldrpc.ahggwl.com");

        jccJingtum = new JccJingtum(true, rpcNodes);
//        System.out.println(jccJingtum.getAddress("ssPFdoM4pZF1HSyrZ8ymnyxYzoE7h"));
        jccJingtum.setPlatform("jG7NeW8QYQbm1xZvxhH1fEMFtYCP1MHqRY");
    }

//    @Test
//    public void testCreateWallet() {
//        System.out.println("in testCreateWallet");
//            try {
//                String _wallet = jccJingtum.createWallet();
//                System.out.println(_wallet);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }
//
//    @Test
//    public void testSequence() {
//        System.out.println("in testSequence");
//        try {
//            long sequence = jccJingtum.getSequence(wallet3.getAddress());
//            System.out.println(sequence);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    public void testRequestTX() {
//
//        System.out.println("in testRequestTX");
//        try {
//            String res = jccJingtum.requestTx("819B51F9B6B72BB2D7FBC6FBEDC2F3518B03F228B952AD8000F3588E9F905B46");
//            System.out.println(res);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    public void testGetMemoData() {
//
//        System.out.println("in testGetMemoData");
//        try {
//            String memo = jccJingtum.getMemoData("7B2262617365223A7B226E616D65223A224A454F53222C22616D6F756E74223A22302E31353236353639227D2C22636F756E746572223A7B226E616D65223A224A55534454222C22616D6F756E74223A22302E32323935363536227D2C226379636C65223A2231363333353136303230222C22737461727454696D65223A2231363235333038313330222C22656E6454696D65223A2231363235373430313330222C22737461747573223A2230227D");
//            System.out.println(memo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    @Test
//    public void testSignWithCancleOrder() {
//
//        System.out.println("in testCancleOrder");
//        long st = System.currentTimeMillis();
//        try {
//            long seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            SignedTransaction signedTx1 = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","100","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W",seq1,"test");
//            jccJingtum.submitBlob(signedTx1.tx_blob);
//
//            SignedTransaction signedTx2 = jccJingtum.buildCancleOrder(wallet1.getSecret(),seq1,seq1+1);
//            jccJingtum.submitBlob(signedTx2.tx_blob);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

    @Test
    public void testCancleOrderNotLocalSign() {

        System.out.println("in testCancleOrder");
        long st = System.currentTimeMillis();
        try {
            long seq1 = jccJingtum.getSequence(wallet1.getAddress());
            String jsonTx1 = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","100","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
            String res1 =  jccJingtum.submitWithSecret(wallet1.getSecret(), jsonTx1);
            System.out.println(res1);

            Thread.sleep(10);

            String jsonTx2 = jccJingtum.buildCancleOrder(wallet1.getSecret(),seq1);
            String res2 = jccJingtum.submitWithSecret(wallet1.getSecret(),jsonTx2);
            System.out.println(res2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }
    }

//    @Test
//    public void testSignWithCreateOrder() {
//        System.out.println("in testSignWithCreateOrders");
//        long st = System.currentTimeMillis();
//        try {
//            long seq = jccJingtum.getSequence(wallet1.getAddress());
//            for (long i=seq; i<seq+5; i++) {
//                SignedTransaction tx = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","1","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W", i, "test");
//                String ret = jccJingtum.submitBlob(tx.tx_blob);
//                System.out.println(ret);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

//    @Test
//    public void testCreateOrderNotLocalSign() {
//        System.out.println("in testCreateOrderNotLocalSign");
//        long st = System.currentTimeMillis();
//        try {
//            for (long i=0; i<1; i++) {
//                String tx = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","1","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W",  "test");
//                String ret = jccJingtum.submitWithSecret(wallet1.getSecret(),tx);
//                System.out.println(ret);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

//    @Test
//    public void testSignWithPayment() {
//        System.out.println("in testSignWithPayment");
//        long st = System.currentTimeMillis();
//        try {
//            long seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            for (long i=seq1; i<seq1+10; i++) {
//                SignedTransaction tx1 = jccJingtum.signWthPayment(wallet1.getSecret(),wallet2.getAddress(),"SWT","0.0001","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", i,"test");
//                String ret1 = jccJingtum.submitNoCheck(tx1.tx_blob);
//                System.out.println(ret1);
//                Thread.sleep(10);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

//    @Test
//    public void testPaymentNotLocalSign() {
//        System.out.println("in testPaymentNotLocalSign");
//        long st = System.currentTimeMillis();
//        try {
//            for (int i=0; i<10; i++) {
//                String txJson = jccJingtum.buildPayment(wallet1.getSecret(),wallet2.getAddress(),"SWT","0.0001","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", "test");
////                System.out.println(txJson);
////                JSONObject jsonObject = JSON.parseObject(txJson);
////                System.out.println(jsonObject.toString());
//                String res = jccJingtum.submitWithSecret(wallet1.getSecret(), txJson);
//                System.out.println(res);
//                Thread.sleep(10);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

    public void tearDown() throws Exception {
    }
}