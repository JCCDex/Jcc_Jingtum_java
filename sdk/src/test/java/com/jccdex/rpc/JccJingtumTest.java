package com.jccdex.rpc;

import com.jccdex.core.client.Wallet;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class JccJingtumTest extends TestCase {

    JccJingtum jccJingtum;
    Wallet wallet1 = Wallet.fromSecret("ssVvAZrAUj7dxFfLdaVvoVH2VTij2");
    Wallet wallet2 = Wallet.fromSecret("ssEEef7JHubPGTCLwTLkuu4oqKtD6");
    public void setUp() throws Exception {
        super.setUp();
        ArrayList<String> rpcNodes = new ArrayList<String>();
        rpcNodes.add("http://39.98.243.77:50333");
        rpcNodes.add("http://58.243.201.56:50333");
        rpcNodes.add("http://59.175.148.101:50333");
        rpcNodes.add("http://65.95.56.49:5050");
        rpcNodes.add("http://1.13.2.21:5050");
        rpcNodes.add("http://117.78.44.90:5050");

        jccJingtum = new JccJingtum(100,"SWT", rpcNodes);
//        jccJingtum.setPlatform("");
    }
//
//    @Test
//    public void testCreateWallet() {
//
//        System.out.println("in testCreateWallet");
//        try {
//            String _wallet = jccJingtum.createWallet();
//            System.out.println(_wallet);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }

//    @Test
//    public void testSequence() {
//
//        System.out.println("in testSequence");
//        try {
//            String sequence = jccJingtum.sequence("jGRevGoFSnKf9tzvGMt8C6BeYYcBURJ8KX");
//            System.out.println(sequence);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//
    @Test
    public void testSafePayment() {

        System.out.println("in testPayment");
        try {
            long st = System.currentTimeMillis();
            String ret = jccJingtum.paymentWithCheck(wallet1.getSecret(),wallet2.getAddress(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
            System.out.println(ret);
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Test
    public void testSafeCreateTx() {

        System.out.println("in testSafeCreateTx");
        try {
            long st = System.currentTimeMillis();
            String ret = jccJingtum.createOrderWithCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","CNY","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
            System.out.println(ret);
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);


        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testFastPaymen() {
        System.out.println("in testFastPaymen");
        try {
            long st = System.currentTimeMillis();
            String ret = jccJingtum.paymentNoCheck(wallet1.getSecret(),wallet2.getAddress(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
            System.out.println(ret);
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testFastCreateTx() {
        System.out.println("in testCreateTx");
        try {
            long st = System.currentTimeMillis();
            String ret = jccJingtum.createOrderNoCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","CNY","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","test");
            System.out.println(ret);
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testCancleOrder() {

        System.out.println("in testCancleOrder");
        try {
            long st = System.currentTimeMillis();
            String ret = jccJingtum.cancleOrder(wallet1.getSecret(),"2345");
            System.out.println(ret);
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

//    @Test
//    public void testRequestTX() {
//
//        System.out.println("in testRequestTX");
//        try {
//            String res = jccJingtum.requestTx("819B51F9B6B72BB2D7FBC6FBEDC2F3518B03F228B952AD8000F3588E9F905B46");
//            System.out.println(res);
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

//    @Test
//    public void testGetMemoData() {
//
//        System.out.println("in testGetMemoData");
//        try {
//            String memo = jccJingtum.getMemoData("7B22656E645F74696D65223A312E36323134313138394531322C22706C616E223A2241222C22746F74616C5F616D6F756E74223A2230222C2273746172745F74696D65223A313632313431313032373435352C22757365725F77616C6C65745F61646472657373223A226A4D674B34427A36774E6E626638537775687A52326451454C324A6A7A38574D6233227D");
//            System.out.println(memo);
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

    public void tearDown() throws Exception {
    }
}