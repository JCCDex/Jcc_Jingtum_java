import com.jccdex.core.client.Wallet;
import com.jccdex.core.client.WalletSM;
import com.jccdex.rpc.JccJingtum;
import com.jccdex.rpc.core.coretypes.uint.UInt32;

import java.util.ArrayList;

public class DemoSWT {
    public static void main(String[] args) {
        JccJingtum jccJingtum = null;
        Wallet wallet1 = Wallet.fromSecret("snJzg27EHUxJj6fiHU1t7nJea5Y3Q");//jsXw3znEHPYDB78w1f21T6Ya3hUVVUwT6H
        Wallet wallet2 = Wallet.fromSecret("snLpjnCcbG3QEEiGdvZAg6yBvMKHp");//jsys77BYKczqePgUA2S5oinctLtpd48qVP
        Wallet wallet3 = Wallet.fromSecret("ssjPGEtUY5pJdjLmESmzUHPXJxXbV");//jKmF8eJUm78mugQ2waBBrbS4SXu43F4JYL
        //初始化JccJingtum Lib
        ArrayList<String> rpcNodes = new ArrayList<String>();
        rpcNodes.add("https://stestswtcrpc.jccdex.cn");
        jccJingtum = new JccJingtum.Builder(Boolean.FALSE, Boolean.TRUE,Boolean.TRUE).setRpcNodes(rpcNodes).build();

        //创建钱包
        try {
            System.out.println("创建钱包-------------------------------------------------------");
            String _wallet = jccJingtum.createWallet();
            System.out.println(_wallet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //转账
        System.out.println("转账-------------------------------------------------------");
        long st = System.currentTimeMillis();
        try {
            UInt32 seq = jccJingtum.getSequence(wallet1.getAddress());
            String txBlob = jccJingtum.buildPayment(wallet1.getSecret(), wallet2.getAddress(), "SWT", "1", "jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", seq, "");
            jccJingtum.submitBlob(txBlob);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long t = System.currentTimeMillis()-st;
            System.out.println("耗时："+t);
        }


        //挂单(不校验)
        System.out.println("挂单和撤单-------------------------------------------------------");
        st = System.currentTimeMillis();
        try {
            UInt32 seq1 = jccJingtum.getSequence(wallet1.getAddress());
            String txBlob = jccJingtum.buildCreateOrder(wallet1.getSecret(),"SWT","1","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or","JJCC","100","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or",seq1,"test");
            jccJingtum.submitBlob(txBlob);

            String txBlob2 = jccJingtum.buildCancleOrder(wallet1.getSecret(),seq1,new UInt32(seq1.value()+1));
            jccJingtum.submitBlob(txBlob2);
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

        //获取sequence
        System.out.println("获取sequence-------------------------------------------------------");
        try {
            UInt32 sequence = jccJingtum.getSequence(wallet1.getAddress());
            System.out.println(sequence);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
