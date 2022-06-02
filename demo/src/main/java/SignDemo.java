import com.jccdex.core.client.Wallet;
import com.jccdex.core.client.WalletSM;
import com.jccdex.core.utils.Utils;
import com.jccdex.rpc.JccJingtum;

import java.util.ArrayList;
import java.util.Random;

public class SignDemo {
    public static void main(String[] args) {
        JccJingtum jccJingtum = null;
        Wallet wallet1 = Wallet.fromSecret("snJzg27EHUxJj6fiHU1t7nJea5Y3Q");//jsXw3znEHPYDB78w1f21T6Ya3hUVVUwT6H
        Wallet wallet2 = Wallet.fromSecret("snLpjnCcbG3QEEiGdvZAg6yBvMKHp");//jsys77BYKczqePgUA2S5oinctLtpd48qVP
        Wallet wallet3 = Wallet.fromSecret("ssjPGEtUY5pJdjLmESmzUHPXJxXbV");//jKmF8eJUm78mugQ2waBBrbS4SXu43F4JYL

        // 测试签名验签
        String randomStr = getRandomString(10);
        System.out.println("random string: " + randomStr);
        // 使用私钥签名
        String signedStr = wallet3.sign(randomStr);
        System.out.println("signed string: " + signedStr);
        // 使用公钥验签
        boolean verifyPass = wallet3.verify(randomStr, signedStr);
        System.out.println("signature verify result: " + (verifyPass? "OK":"NG"));

    }
    public static String getRandomString(int length){
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}

