package com.jccdex.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jccdex.core.client.Wallet;
import com.jccdex.core.client.WalletSM;
import com.jccdex.rpc.core.serialized.enums.EngineResult;
import com.jccdex.rpc.core.types.known.tx.signed.SignedTransaction;
import com.jccdex.rpc.res.ServerInfo;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class JccJingtumSMTest extends TestCase {

    JccJingtum jccJingtum;

    //慧联运
    WalletSM wallet1;
    WalletSM wallet2;

    public void setUp() throws Exception {
        super.setUp();
        ArrayList<String> rpcNodes = new ArrayList<String>();

        //内部测试节点
        rpcNodes.add("http://139.198.19.157:4950");

        //慧联运测试节点
//        rpcNodes.add("https://testskywelldrpc0.ahggwl.com ");
//        rpcNodes.add("https://testskywelldrpc1.ahggwl.com ");
//        rpcNodes.add("https://testskywelldrpc2.ahggwl.com ");
//        rpcNodes.add("https://testskywelldrpc3.ahggwl.com ");
//        rpcNodes.add("https://testskywelldrpc4.ahggwl.com ");

        jccJingtum = new JccJingtum(true, rpcNodes);

        //内部测试节点
        wallet1 = WalletSM.fromSecret("snap1eyu1ggMPpCEEKS45Jhv1fvjE");
        wallet2 = WalletSM.fromSecret("shstwqJpVJbsqFA5uYJJw1YniXcDF");

        //慧联运测试节点
//        wallet1 = WalletSM.fromSecret("snHu1pSHRksTQQMJGa2gZH6gTSukK");//jpSojXsu7mfwStH7ig72yCg86ViKH5dHWN
//        wallet2 = WalletSM.fromSecret("snap1eyu1ggMPpCEEKS45Jhv1fvjE");//jpAo1rFxGShchM8Rei3urcBxmVdPwtveee
    }

//    @Test
//    public void testCreateWallet() {
//        System.out.println("in testCreateWallet");
//            try {
//                String _address = jccJingtum.getAddress("shTV69r5xGwLc3B1fFA3HuzbyVPZe");
//                System.out.println(_address);
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

    @Test
    public void testSignWithCancleOrder() {

        System.out.println("in testCancleOrder");
        long st = System.currentTimeMillis();
        try {
            long seq1 = jccJingtum.getSequence(wallet1.getAddress());
            SignedTransaction signedTx1 = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","100","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W",seq1,"test");
            jccJingtum.submitBlob(signedTx1.tx_blob);

            SignedTransaction signedTx2 = jccJingtum.buildCancleOrder(wallet1.getSecret(),seq1,seq1+1);
            jccJingtum.submitBlob(signedTx2.tx_blob);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }
    }

//    @Test
//    public void testCancleOrderNotLocalSign() {
//
//        System.out.println("in testCancleOrder");
//        long st = System.currentTimeMillis();
//        try {
//            long seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            String jsonTx1 = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","TEST","100","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W","test");
//            String res1 =  jccJingtum.submitWithSecret(wallet1.getSecret(), jsonTx1);
//            System.out.println(res1);
//
//            Thread.sleep(10);
//
//            String jsonTx2 = jccJingtum.buildCancleOrder(wallet1.getSecret(),seq1);
//            String res2 = jccJingtum.submitWithSecret(wallet1.getSecret(),jsonTx2);
//            System.out.println(res2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

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
//        int successCount = 0;
//        int preSeqCount = 0;
//        Queue<String> blobList = new LinkedList<String>();
//        long st = System.currentTimeMillis();
//        try {
//            String add1 = wallet1.getAddress();
//            String add2 = wallet2.getAddress();
//            long seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            for (long i=seq1; i<seq1+1; i++) {
//                String memoData = "{\"businessDataId\":\"514832558630895618\",\"businessDataStatus\":\"7\",\"businessDataTime\":\"1637742503733\",\"businessType\":\"1\",\"extraParams\":[{\"propertyName\":\"sysWaybillNum\",\"propertyValue\":\"SYD20211124000328\"}],\"fingerHash\":\"c64fa5fe7ef6dc17ced6be8800182cbc3a9a93c4c5c935f2ed09bddcac28780d\",\"platformId\":\"1\",\"userSequence\":\"1423463737654239233\"}";
//                JSONObject jsonObject=JSONObject.parseObject(memoData);
//                String testId = "test" + String.valueOf(i-seq1);
//                jsonObject.put("testid",testId);
//                String memo = jsonObject.toString();
//                SignedTransaction tx1 = jccJingtum.signWthPayment(wallet1.getSecret(), wallet2.getAddress(), "TEST","0.01","jHgKXtmDXGJLupHWoeJyisirpZnrvnAA9W", i,"");
//                blobList.offer(tx1.tx_blob);
////                System.out.println(tx1.tx_blob);
//            }
//            int k = 0;
//            while(true) {
////                System.out.println(k);
//
//                String blob =  blobList.element();
//                String res = jccJingtum.submitBlob(blob);
//                System.out.println(res);
//                int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
//                EngineResult engineResult = EngineResult.fromNumber(engine_result_code);
//
//                if(EngineResult.isSuccess(engineResult)) {
//                    blobList.poll();
//                    successCount++;
//                } else if(EngineResult.isPreSeq(engineResult)) {
//                    preSeqCount++;
//                }
//
//                Thread.sleep(15);
//                if(blobList.size() == 0) break;
//                k++;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//            System.out.println("成功："+successCount);
//            System.out.println("超前："+preSeqCount);
//        }
//    }

//    @Test
//    public void testPaymentNotLocalSign() {
//        System.out.println("in testPaymentNotLocalSign");
//        long st = System.currentTimeMillis();
//        try {
//            for (int i=0; i<1; i++) {
//                String txJson = jccJingtum.buildPayment(wallet1.getSecret(),wallet4.getAddress(),"SWT","0.0001","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", "test");
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
//
//    @Test
//    public void testGetServerState() {
//        System.out.println("in testGetServerState");
//        long st = System.currentTimeMillis();
//        try {
//            ArrayList<ServerInfo> serverList = jccJingtum.getServerState();
//            System.out.println(serverList);
//            for(int i=0; i<serverList.size();i++) {
//                ServerInfo serverInfo = serverList.get(i);
//                System.out.println(serverInfo.host);
//                System.out.println(serverInfo.height);
//                System.out.println(serverInfo.lastLedgerHash);
//                System.out.println(serverInfo.lastLedgerTime);
//                System.out.println("----------------------------");
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