package com.jccdex.rpc;

import com.alibaba.fastjson.JSONObject;
import com.jccdex.core.client.Wallet;
import com.jccdex.rpc.client.bean.TransactionInfo;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.serialized.enums.EngineResult;
import com.jccdex.rpc.res.ServerInfo;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class JccJingtumTest extends TestCase {

    JccJingtum jccJingtum;

    Wallet wallet1;
    Wallet wallet2;

    ArrayList<String> rpcNodes ;

    public void setUp() throws Exception {
        super.setUp();

        rpcNodes = new ArrayList<String>();
        rpcNodes.add("****");

        jccJingtum = new JccJingtum.Builder(Boolean.FALSE,Boolean.TRUE,Boolean.TRUE).setRpcNodes(rpcNodes).build();

        wallet1 = Wallet.fromSecret("****");
        wallet2 = Wallet.fromSecret("****");
    }

//    @Test
//    public void testGetAddress() {
//        System.out.println("in testCreateWallet");
//            try {
//                String _address = jccJingtum.getAddress(wallet1.getSecret());
//                System.out.println(_address);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }

//    @Test
//    public void testSequence() {
//        System.out.println("in testSequence");
//        try {
//            UInt32 sequence = jccJingtum.getSequence(wallet1.getAddress());
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
//            String res = jccJingtum.requestTx("FB6C0A537C0D71092DF0066533F2BD1F5BE88279C450A4FAFDAB139AFC504AF4");
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
//            UInt32 seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            TransactionInfo transactionInfo = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","JJCC","100","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or",seq1,"test");
//            jccJingtum.submitBlob(transactionInfo.getTxBlob());
//            System.out.println(transactionInfo.toString());
//
//            TransactionInfo transactionInfo2 = jccJingtum.buildCancleOrder(wallet1.getSecret(),seq1,new UInt32(seq1.value()+1));
//            jccJingtum.submitBlob(transactionInfo2.getTxBlob());
//            System.out.println(transactionInfo2.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

//    @Test
//    public void testCancleOrderNotLocalSign() {
//
//        System.out.println("in testCancleOrder");
//        jccJingtum = new JccJingtum.Builder(Boolean.FALSE,Boolean.FALSE,Boolean.TRUE).setRpcNodes(rpcNodes).build();
//        long st = System.currentTimeMillis();
//        try {
//            UInt32 seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            TransactionInfo transactionInfo = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","JJCC","100","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or",seq1,"test");
//            jccJingtum.submitWithSecret(wallet1.getSecret(),transactionInfo.getTxJson());
//
//            TransactionInfo transactionInfo2= jccJingtum.buildCancleOrder(wallet1.getSecret(),seq1,new UInt32(seq1.value()+1));
//            jccJingtum.submitWithSecret(wallet1.getSecret(),transactionInfo2.getTxJson());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            long t = System.currentTimeMillis()-st;
//            System.out.println("耗时："+t);
//        }
//    }

//    @Test
//    public void testSignWithPayment() {
//        System.out.println("in testSignWithPayment");
//        int successCount = 0;
//        int preSeqCount = 0;
//        jccJingtum = new JccJingtum.Builder(Boolean.FALSE,Boolean.TRUE,Boolean.TRUE).setRpcNodes(rpcNodes).build();
//        Queue<String> blobList = new LinkedList<String>();
//        long st = System.currentTimeMillis();
//        try {
//            String add = wallet1.getAddress();
//            UInt32 seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            for (long i = seq1.value(); i < seq1.value() + 1; i++) {
//                String memoData = "{\"businessDataId\":\"514832558630895618\",\"businessDataStatus\":\"7\",\"businessDataTime\":\"1637742503733\",\"businessType\":\"1\",\"extraParams\":[{\"propertyName\":\"sysWaybillNum\",\"propertyValue\":\"SYD20211124000328\"}],\"fingerHash\":\"c64fa5fe7ef6dc17ced6be8800182cbc3a9a93c4c5c935f2ed09bddcac28780d\",\"platformId\":\"1\",\"userSequence\":\"1423463737654239233\"}";
//                JSONObject jsonObject = JSONObject.parseObject(memoData);
//                String testId = "test" + String.valueOf(i - seq1.value());
//                jsonObject.put("testid", testId);
//                String memo = jsonObject.toString();
//                TransactionInfo transactionInfo = jccJingtum.buildPayment(wallet1.getSecret(), wallet2.getAddress(), "SWT", "1", "jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", new UInt32(i), "");
//                blobList.offer(transactionInfo.getTxBlob());
//            }
//            int k = 0;
//            while (true) {
//                String blob = blobList.element();
//                String res = jccJingtum.submitBlob(blob);
//                System.out.println(res);
//
//                res = jccJingtum.submitBlob(blob);
//                System.out.println(res);
//
//                int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
//                EngineResult engineResult = EngineResult.fromNumber(engine_result_code);
//
//                if (EngineResult.isSuccess(engineResult)) {
//                    blobList.poll();
//                    successCount++;
//                } else if (EngineResult.isPreSeq(engineResult)) {
//                    preSeqCount++;
//                }
//
//                Thread.sleep(15);
//                if (blobList.size() == 0) break;
//                k++;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            long t = System.currentTimeMillis() - st;
//            System.out.println("耗时：" + t);
//            System.out.println("成功：" + successCount);
//            System.out.println("超前：" + preSeqCount);
//        }
//    }

//    @Test
//    public void testPaymentNotLocalSign() {
//        System.out.println("in testPaymentNotLocalSign");
//        int successCount = 0;
//        int preSeqCount = 0;
//        jccJingtum = new JccJingtum.Builder(Boolean.FALSE,Boolean.FALSE,Boolean.TRUE).setRpcNodes(rpcNodes).build();
//        Queue<String> blobList = new LinkedList<String>();
//        long st = System.currentTimeMillis();
//        try {
//            String add = wallet1.getAddress();
//            UInt32 seq1 = jccJingtum.getSequence(wallet1.getAddress());
//            for (long i = seq1.value(); i < seq1.value() + 1; i++) {
//                String memoData = "{\"businessDataId\":\"514832558630895618\",\"businessDataStatus\":\"7\",\"businessDataTime\":\"1637742503733\",\"businessType\":\"1\",\"extraParams\":[{\"propertyName\":\"sysWaybillNum\",\"propertyValue\":\"SYD20211124000328\"}],\"fingerHash\":\"c64fa5fe7ef6dc17ced6be8800182cbc3a9a93c4c5c935f2ed09bddcac28780d\",\"platformId\":\"1\",\"userSequence\":\"1423463737654239233\"}";
//                JSONObject jsonObject = JSONObject.parseObject(memoData);
//                String testId = "test" + String.valueOf(i - seq1.value());
//                jsonObject.put("testid", testId);
//                String memo = jsonObject.toString();
//                TransactionInfo transactionInfo = jccJingtum.buildPayment(wallet1.getSecret(), wallet2.getAddress(), "SWT", "1", "jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", new UInt32(i), "");
//                blobList.offer(transactionInfo.getTxJson());
//            }
//            int k = 0;
//            while (true) {
//                String blob = blobList.element();
//                String res = jccJingtum.submitWithSecret(wallet1.getSecret(),blob);
//                System.out.println(res);
//
//                int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
//                EngineResult engineResult = EngineResult.fromNumber(engine_result_code);
//
//                if (EngineResult.isSuccess(engineResult)) {
//                    blobList.poll();
//                    successCount++;
//                } else if (EngineResult.isPreSeq(engineResult)) {
//                    preSeqCount++;
//                }
//
//                Thread.sleep(15);
//                if (blobList.size() == 0) break;
//                k++;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            long t = System.currentTimeMillis() - st;
//            System.out.println("耗时：" + t);
//            System.out.println("成功：" + successCount);
//            System.out.println("超前：" + preSeqCount);
//        }
//    }


//    @Test
//    public void testGetServerState() {
//        System.out.println("in testGetServerState");
//        long st = System.currentTimeMillis();
//        jccJingtum = new JccJingtum.Builder(Boolean.FALSE,Boolean.FALSE,Boolean.TRUE).setRpcNodes(rpcNodes).build();
//        try {
//            ArrayList<com.jccdex.rpc.res.ServerInfo> serverList = jccJingtum.getServerState();
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