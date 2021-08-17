import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jccdex.rpc.JccJingtum;
import com.jccdex.rpc.http.OkhttpUtil;

import java.util.ArrayList;

public class Demo {
    public static void main(String[] args) {
        JccJingtum jccJingtum = null;
        try {
            String serverConfigHost = "https://gateway.swtc.top/rpcservice";
            String res = OkhttpUtil.get(serverConfigHost);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode obj = mapper.readTree(res);
            String list = obj.get("rpcpeers").toString();
            ArrayList<String> rpcNodes = mapper.readValue(list, new TypeReference<ArrayList<String>>() {
            });
            jccJingtum = new JccJingtum(100,"SWT","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", rpcNodes);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        jccJingtum.setTryTimes(10);

//        //创建钱包
//        try {
//            String _wallet = jccJingtum.createWallet();
//            System.out.println(_wallet);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }

        //挂单
        try {
            long t1 = System.currentTimeMillis();
            String ret = jccJingtum.createOrderWithCheck("","SWT","1","CNY","1","test");
//            String ret = jccJingtum.paymentWithCheck("","","SWT","1","test");
            System.out.println(System.currentTimeMillis()-t1);
//            System.out.println(ret);


        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }
}
