package com.jccdex.rpc;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jccdex.core.client.Wallet;
import com.jccdex.rpc.config.Config;
import com.jccdex.rpc.config.RpcNode;
import com.jccdex.rpc.core.coretypes.AccountID;
import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.coretypes.Currency;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.types.known.tx.signed.SignedTransaction;
import com.jccdex.rpc.core.types.known.tx.txns.OfferCancel;
import com.jccdex.rpc.core.types.known.tx.txns.OfferCreate;
import com.jccdex.rpc.core.types.known.tx.txns.Payment;
import com.jccdex.rpc.http.OkhttpUtil;
import com.jccdex.rpc.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
//
/**
 * 井通公链、联盟链RPC开发接口
 * @author xdjiang, shuonimei
 */
public class JccJingtum {
    private final RpcNode rpcNode;
    //请求次数

    /**
     * 重复请求次数
     */
    private int tryTimes;
    private final String SUCCESS_CODE = "success";
    private final String TX_SUCCESS_CODE = "tesSUCCESS";

    /**
     * @param rpcNodes rpc节点服务器地址列表
     */
    public JccJingtum(ArrayList<String> rpcNodes) {
        this.tryTimes = rpcNodes.size();
        rpcNode = new RpcNode(rpcNodes);
    }

    /**
     * 井通公链、联盟链RPC服务构造函数
     * @param fee 每笔交易燃料费(燃料费计算公式=fee/1000000)
     * @param baseToken 交易燃料手续费通证,也是公链的本币
     * @param issuer 银关地址
     * @param rpcNodes rpc节点服务器地址列表
     */
    public JccJingtum(Integer fee, String baseToken, String issuer, ArrayList<String> rpcNodes) {
        this(rpcNodes);
        Config.setFee(fee);
        Config.setCurrency(baseToken);
        Config.setIssuer(issuer);
    }


    /**
     * 井通公链、联盟链RPC服务构造函数
     * @param alphabet 字母表，每一条联盟链都可以用不同的或者相同alphabet
     * @param fee 每笔交易燃料费(燃料费计算公式=fee/1000000)
     * @param baseToken 交易燃料手续费通证,也是公链的本币
     * @param issuer 银关地址
     * @param rpcNodes rpc节点服务器地址列表
     */
    public JccJingtum(String alphabet, Integer fee, String baseToken, String issuer, ArrayList<String> rpcNodes) {
        this(fee,baseToken,issuer,rpcNodes);
        Config.setAlphabet(alphabet);
    }

    /**
     * 井通公链、联盟链RPC服务构造函数
     * @param alphabet 字母表，每一条联盟链都可以用不同的或者相同alphabet
     * @param fee 交易手续费(燃料费计算公式=fee/1000000)
     * @param baseToken 交易燃料手续费通证,也是公链的本币
     * @param issuer 银关地址
     * @param platform 交易平台账号
     * @param rpcNodes rpc节点服务器地址列表
     */
    public JccJingtum(String alphabet, Integer fee, String baseToken, String issuer, String platform, ArrayList<String> rpcNodes) {
        this(alphabet,fee,baseToken,issuer,rpcNodes);
        Config.setPlatform(platform);
    }

    /**
     * 设置每笔交易燃料费
     * @param fee (燃料费计算公式=fee/1000000)
     */
    public void setFee(Integer fee) throws  Exception{
        try {
            if(fee <=0) {
                throw new Exception("燃料费不能小于等于0");
            }
            Config.setFee(fee);
        } catch (Exception e) {
            throw new Exception("设置燃料费异常");
        }
    }

    /**
     * 获取每笔交易燃料费
     * @return 每笔交易燃料费
     */
    public Integer getFee() {
        return Config.FEE;
    }

    /**
     * 设置交易平台账号
     * @param platform 交易平台账号
     */
    public void setPlatform(String platform) throws  Exception{
        try {
            if(!Wallet.isValidAddress(platform)) {
                throw new Exception("平台账号不合法");
            }
            Config.setPlatform(platform);
        } catch (Exception e) {
            throw new Exception("设置交易平台账号异常");
        }
    }

    /**
     * 获取交易平台账号
     * @return 交易平台账号
     */
    public String getPlatform() {
        return Config.PLATFORM;
    }

    /**
     * 创建钱包(账号)
     * @return 钱包字符串,json格式 ({"secret":****,"address":****})
     * @throws Exception 抛出异常
     */
    public String createWallet()  throws Exception {
        try {
            ObjectNode data = new ObjectMapper().createObjectNode();
            Wallet wallet = Wallet.generate();
            data.put("secret",wallet.getSecret());
            data.put("address",wallet.getAddress());
            return data.toString();
        } catch (Exception e) {
            throw new Exception("创建钱包异常");
        }
    }

    /**
     * 通过钱包密钥获取钱包地址
     * @param secret 钱包密钥
     * @return 钱包地址
     * @throws Exception 抛出异常
     */
    public String getWalletAddress(String secret) throws  Exception {
        try {
            if(!Wallet.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }
            Wallet wallet = Wallet.fromSecret(secret);
            return wallet.getAddress();
        } catch (Exception e) {
            throw new Exception("创建钱包异常");
        }
    }

    /**
     * 设置出错尝试次数
     * @param tryTimes 次数
     */
    public void setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
    }
    /**
     * 获取sequence
     * @param address 钱包地址
     * @return sequence
     * @throws Exception 抛出异常
     */
    private String getSequence(String address) throws Exception {
        try {
            if(!Wallet.isValidAddress(address)) {
                throw new Exception("钱包地址不合法");
            }
        } catch (Exception e) {
            throw e;
        }

        String sequence = "";
        int times = this.tryTimes;
        do {
            try {
                times--;
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode data = mapper.createObjectNode();
                ObjectNode object = mapper.createObjectNode();
                object.put("account", address);
                ArrayList<ObjectNode> params = new ArrayList<>();
                params.add(object);
                ArrayNode array = mapper.valueToTree(params);
                data.put("method", "account_info");
                data.set("params", array);

                String url = rpcNode.randomUrl();
                String res = OkhttpUtil.post(url, data.toString());
                String code = JSONObject.parseObject(res).getJSONObject("result").getString("status");
                if(SUCCESS_CODE.equals(code)) {
                    sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("account_data").getString("Sequence");
                }

                if(!sequence.isEmpty()) {
                    break;
                }

                //延时500毫秒
                Thread.sleep(500);
            } catch (Exception ignored) {
            }
        } while(times > 0);

        if(sequence.isEmpty()) {
            throw new Exception("获取sequence失败");
        } else {
            return sequence;
        }
    }

    /**
     * 向指定的rpc节点服务器获取获取交易详情
     * @param hash 交易hash
     * @param rpcNode rpc节点服务器
     * @return 交易详情 json格式
     */
    private String requestTx(String hash, String rpcNode) {
        String tx = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("transaction", hash);
            object.put("binary", false);
            ArrayList<ObjectNode> params = new ArrayList();
            params.add(object);
            ArrayNode array = (ArrayNode) mapper.valueToTree(params);
            data.put("method", "tx");
            data.set("params", array);
            String res = OkhttpUtil.post(rpcNode, data.toString());
            String status = JSONObject.parseObject(res).getJSONObject("result").getString("status");
            Boolean validated = JSONObject.parseObject(res).getJSONObject("result").getBoolean("validated");
            if (SUCCESS_CODE.equals(status) && validated) {
                tx = res;
            }
            return tx;
        }catch (Exception e) {
            return tx;
        }
    }

    /**
     * 根据hash获取交易详情
     * @param hash 交易hash
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String requestTx(String hash) throws Exception {
        String tx = "";
        try {
            ArrayList<String> list = rpcNode.getUrls();
            Iterator it = list.iterator();
            while(it.hasNext()) {
                String url = (String) it.next();
                tx = this.requestTx(hash,url);
                if (!tx.isEmpty()) {
                    break;
                }
            }
        }catch (Exception e) {
            throw new Exception("获取交易信息失败");
        }

        if(tx.isEmpty()) {
            throw new Exception("获取交易信息失败");
        } else {
            return tx;
        }
    }
    /**
     * 16进制备注内容直接转换成为字符串(无需Unicode解码)
     * @param hexStrMemData 16进制备注内容
     * @return 备注内容
     * @throws Exception 抛出异常
     */
    public String getMemoData(String hexStrMemData) throws Exception{
        try {
            return Utils.hexStrToStr(hexStrMemData);
        } catch (Exception e) {
            throw new Exception("获取备注内容失败");
        }
    }

    /**
     *  转账并校验，每笔交易都会校验是否成功，适合普通转账，优点：每笔交易都进行确认，缺点：转账效率低下
     * @param secret 发送者钱包密钥
     * @param receiver 接收者钱包地址
     * @param pToken 转账Token
     * @param pAmount 转账数量
     * @param memos  转账备注
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String paymentWithCheck(String secret, String receiver, String pToken, String pAmount, String memos) throws Exception {
        try {
            if(!Wallet.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }
            if(!Wallet.isValidAddress(receiver)) {
                throw new Exception("钱包地址不合法");
            }

            if(pToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pAmount.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            Wallet wallet = Wallet.fromSecret(secret);
            String sender = wallet.getAddress();
            String sequence = this.getSequence(sender);

            ObjectMapper mapper = new ObjectMapper();
            String token = pToken.toUpperCase();
            Amount amount;
            Payment payment = new Payment();
            payment.as(AccountID.Account, sender);
            payment.as(AccountID.Destination, receiver);

            BigDecimal bigDecimal = new BigDecimal(pAmount);
            if(bigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(token)) {
                amount = new Amount(bigDecimal);
            } else {
                amount = new Amount(bigDecimal, Currency.fromString(token), AccountID.fromString(Config.ISSUER));
            }


            payment.as(Amount.Amount, amount);
            payment.as(Amount.Fee, String.valueOf(Config.FEE));
            payment.sequence(new UInt32(sequence));
            payment.flags(new UInt32(0));

            if (memos.length() > 0) {
                ArrayList<String> memoList = new ArrayList<>(1);
                memoList.add(memos);
                payment.addMemo(memoList);
            }

            SignedTransaction tx = payment.sign(secret);
            String res = this.submit(tx.tx_blob, tx.hash.toHex());
            return res;
        } catch (Exception e) {
            throw new Exception("转账失败");
        }
    }

    /**
     *  快速转账，每笔交易不校验是否成功，适用于批量转账，优点：转账效率高，缺点：交易成功率无法保证，需要调用者自己进行校验
     * @param secret 发送者钱包密钥
     * @param receiver 接收者钱包地址
     * @param pToken 转账Token
     * @param pAmount 转账数量
     * @param memos  转账备注
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String paymentNoCheck(String secret, String receiver, String pToken, String pAmount, String memos) throws Exception {
        try {
            if(!Wallet.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }
            if(!Wallet.isValidAddress(receiver)) {
                throw new Exception("钱包地址不合法");
            }
            if(pToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pAmount.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            Wallet wallet = Wallet.fromSecret(secret);
            String sender = wallet.getAddress();
            String sequence = this.getSequence(sender);

            ObjectMapper mapper = new ObjectMapper();
            String token = pToken.toUpperCase();
            Amount amount;
            Payment payment = new Payment();
            payment.as(AccountID.Account, sender);
            payment.as(AccountID.Destination, receiver);

            BigDecimal bigDecimal = new BigDecimal(pAmount);
            if(bigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(token)) {
                amount = new Amount(bigDecimal);
            } else {
                amount = new Amount(bigDecimal, Currency.fromString(token), AccountID.fromString(Config.ISSUER));
            }


            payment.as(Amount.Amount, amount);
            payment.as(Amount.Fee, String.valueOf(Config.FEE));
            payment.sequence(new UInt32(sequence));
            payment.flags(new UInt32(0));

            if (memos.length() > 0) {
                ArrayList<String> memoList = new ArrayList<>(1);
                memoList.add(memos);
                payment.addMemo(memoList);
            }

            SignedTransaction tx = payment.sign(secret);
            String res = this.submit(tx.tx_blob);
            return res;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 挂单并校验，每笔挂单都会校验是否成功，适合普通调用，优点：每笔交易都进行确认，缺点：效率低下
     * @param secret 挂单方钱包密钥
     * @param pPayToke  挂单方支付的Token名称
     * @param pPayAmount 挂单方支付的Token数量
     * @param pGetToken  挂单方期望得到的Token名称
     * @param pGetAmount 挂单方期望得到的Token数量
     * @param memos 备注
     * @return 交易详情 json格式
     */
    public String createOrderWithCheck(String secret, String pPayToke, String pPayAmount, String pGetToken, String pGetAmount, String memos) throws Exception{
        try {
            if(!Wallet.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(pPayToke.isEmpty() || pGetToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pPayAmount.isEmpty() || pGetAmount.isEmpty()) {
                throw new Exception("token数量不合法");
            }

            Wallet wallet = Wallet.fromSecret(secret);
            String address = wallet.getAddress();

            ObjectMapper mapper = new ObjectMapper();
            String payToken = pPayToke.toUpperCase();
            String getToken = pGetToken.toUpperCase();

            OfferCreate offerCreate = new OfferCreate();
            offerCreate.as(AccountID.Account, address);
            offerCreate.as(AccountID.Platform, Config.PLATFORM);

            Amount payAmount;
            BigDecimal payBigDecimal = new BigDecimal(pPayAmount);
            BigDecimal getBigDecimal = new BigDecimal(pGetAmount);

            if(payBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(getBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(payToken)) {
                payAmount = new Amount(payBigDecimal);
            } else {
                payAmount = new Amount(payBigDecimal, Currency.fromString(payToken), AccountID.fromString(Config.ISSUER));
            }

            Amount getAmount;

            if(Config.CURRENCY.equals(getToken)) {
                getAmount = new Amount(getBigDecimal);
            } else {
                getAmount = new Amount(getBigDecimal, Currency.fromString(getToken), AccountID.fromString(Config.ISSUER));
            }
            offerCreate.as(Amount.TakerPays, getAmount);
            offerCreate.as(Amount.TakerGets, payAmount);

            offerCreate.as(Amount.Fee, String.valueOf(Config.FEE));

            String sequence = this.getSequence(address);
            offerCreate.sequence(new UInt32(sequence));

            if (memos.length() > 0) {
                ArrayList<String> memoList = new ArrayList<>(1);
                memoList.add(memos);
                offerCreate.addMemo(memoList);
            }

            SignedTransaction tx = offerCreate.sign(secret);
            String res = this.submit(tx.tx_blob, tx.hash.toHex());
            return res;
        } catch (Exception e) {
            throw new Exception("挂单失败");
        }
    }

    /**
     * 快速挂单，每笔交易不校验是否成功，适用于批量挂单，优点：挂单效率高，缺点：交易成功率无法保证，需要调用者自己进行校验
     * @param secret 挂单方钱包密钥
     * @param pPayToke  挂单方支付的Token名称
     * @param pPayAmount 挂单方支付的Token数量
     * @param pGetToken  挂单方期望得到的Token名称
     * @param pGetAmount 挂单方期望得到的Token数量
     * @param memos 备注
     * @return 交易详情 json格式
     */
    public String createOrderNoCheck(String secret, String pPayToke, String pPayAmount, String pGetToken, String pGetAmount, String memos) throws Exception{
        try {
            if(!Wallet.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(pPayToke.isEmpty() || pGetToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pPayAmount.isEmpty() || pGetAmount.isEmpty()) {
                throw new Exception("token数量不合法");
            }

            Wallet wallet = Wallet.fromSecret(secret);
            String address = wallet.getAddress();

            String payToken = pPayToke.toUpperCase();
            String getToken = pGetToken.toUpperCase();

            OfferCreate offerCreate = new OfferCreate();
            offerCreate.as(AccountID.Account, address);
            offerCreate.as(AccountID.Platform, Config.PLATFORM);

            Amount payAmount;
            BigDecimal payBigDecimal = new BigDecimal(pPayAmount);
            BigDecimal getBigDecimal = new BigDecimal(pGetAmount);

            if(payBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(getBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(payToken)) {
                payAmount = new Amount(payBigDecimal);
            } else {
                payAmount = new Amount(payBigDecimal, Currency.fromString(payToken), AccountID.fromString(Config.ISSUER));
            }

            Amount getAmount;

            if(Config.CURRENCY.equals(getToken)) {
                getAmount = new Amount(getBigDecimal);
            } else {
                getAmount = new Amount(getBigDecimal, Currency.fromString(getToken), AccountID.fromString(Config.ISSUER));
            }
            offerCreate.as(Amount.TakerPays, getAmount);
            offerCreate.as(Amount.TakerGets, payAmount);

            offerCreate.as(Amount.Fee, String.valueOf(Config.FEE));

            String sequence = this.getSequence(address);
            offerCreate.sequence(new UInt32(sequence));

            if (memos.length() > 0) {
                ArrayList<String> memoList = new ArrayList<>(1);
                memoList.add(memos);
                offerCreate.addMemo(memoList);
            }

            SignedTransaction tx = offerCreate.sign(secret);
            String res = this.submit(tx.tx_blob);
            return res;
        } catch (Exception e) {
            throw new Exception("挂单失败");
        }

    }

    /**
     * 取消挂单
     * @param secret 钱包密钥
     * @param pSequence 挂单序列号
     * @return 交易详情 json格式
     */
    public String cancleOrder(String secret, String pSequence) throws Exception{
        try {
            if(!Wallet.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            BigDecimal bigDecimal = new BigDecimal(pSequence);

            if(bigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("sequence不能小于等于0");
            }


            Wallet wallet = Wallet.fromSecret(secret);
            String address = wallet.getAddress();

            OfferCancel offerCancel = new OfferCancel();
            offerCancel.as(AccountID.Account, address);
            offerCancel.as(UInt32.OfferSequence, bigDecimal.longValue());
            offerCancel.as(Amount.Fee, String.valueOf(Config.FEE));
            String sequence = this.getSequence(address);
            offerCancel.sequence(new UInt32(sequence));

            SignedTransaction tx = offerCancel.sign(secret);
            String res = this.submit(tx.tx_blob);
            return res;
        } catch (Exception e) {
            throw new Exception("撤单失败");
        }
    }

    /**
     * 向节点发送交易请求，并且根据签名得到的hash进行交易确认
     * @param txBlob 交易信息
     * @param hash hash
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submit(String txBlob, String hash) throws Exception {
        try {
            int times = this.tryTimes;
            String resTx = "";
            String result = "";
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("tx_blob", txBlob);
            ArrayList<ObjectNode> params = new ArrayList<>();
            params.add(object);
            ArrayNode array = mapper.valueToTree(params);

            data.put("method", "submit");
            data.set("params", array);

            do{
                times--;
                try {
                    String url = rpcNode.randomUrl();
                    result = OkhttpUtil.post(url, data.toString());
                    Thread.sleep(5000);
                    resTx = this.requestTx(hash);
                    if(!resTx.isEmpty()) {
                        break;
                    }
                }catch (Exception e) {
                    continue;
                }
                //延时1000毫秒
                Thread.sleep(1000);
            }while(times > 0);

            if(resTx.isEmpty()) {
                return  result;
            } else {
                return resTx;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 向节点发送交易请求
     * @param txBlob 交易信息
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submit(String txBlob) throws Exception {
        try {
            int times = this.tryTimes;
            String res = "";

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("tx_blob", txBlob);
            ArrayList<ObjectNode> params = new ArrayList<>();
            params.add(object);
            ArrayNode array = mapper.valueToTree(params);

            data.put("method", "submit");
            data.set("params", array);

            do{
                times--;
                try {
                    String url = rpcNode.randomUrl();
                    res = OkhttpUtil.post(url, data.toString());
                    String status = JSONObject.parseObject(res).getJSONObject("result").getString("engine_result");
                    if(TX_SUCCESS_CODE.equals(status)) {
                        break;
                    }
                }catch (Exception e) {
                    continue;
                }
                Thread.sleep(1000);
            }while(times > 0);

            return res;

        } catch (Exception e) {
            throw new Exception("挂单失败");
        }
    }

}
