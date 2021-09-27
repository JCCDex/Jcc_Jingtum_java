import com.jccdex.core.client.Wallet;
import com.jccdex.core.client.WalletSM;
import com.jccdex.rpc.JccJingtum;

import java.util.ArrayList;

public class DemoSWT {
    public static void main(String[] args) {
        JccJingtum jccJingtum = null;
        Wallet wallet1 = Wallet.fromSecret("snJzg27EHUxJj6fiHU1t7nJea5Y3Q");//jsXw3znEHPYDB78w1f21T6Ya3hUVVUwT6H
        Wallet wallet2 = Wallet.fromSecret("snLpjnCcbG3QEEiGdvZAg6yBvMKHp");//jsys77BYKczqePgUA2S5oinctLtpd48qVP
        Wallet wallet3 = Wallet.fromSecret("ssjPGEtUY5pJdjLmESmzUHPXJxXbV");//jKmF8eJUm78mugQ2waBBrbS4SXu43F4JYL
        //初始化JccJingtum Lib
        try {
            ArrayList<String> rpcNodes = new ArrayList<String>();
            rpcNodes.add("https://stestswtcrpc.jccdex.cn");
            jccJingtum = new JccJingtum(false, rpcNodes);
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
            String ret = jccJingtum.createOrderNoCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","CNY","1","jpdP4YhqXxvGroMs26WM8YBcov9bdFeygc","test");
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
            String ret = jccJingtum.createOrderWithCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","CNY","1","jpdP4YhqXxvGroMs26WM8YBcov9bdFeygc","test");
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
            String ret1 = jccJingtum.createOrderWithCheck(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","CNY","100","jpdP4YhqXxvGroMs26WM8YBcov9bdFeygc","test");
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
            String res = jccJingtum.requestTx("FB6C0A537C0D71092DF0066533F2BD1F5BE88279C450A4FAFDAB139AFC504AF4");
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
