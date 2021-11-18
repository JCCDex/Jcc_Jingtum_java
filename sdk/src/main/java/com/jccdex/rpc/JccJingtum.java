package com.jccdex.rpc;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jccdex.core.client.Wallet;
import com.jccdex.core.client.WalletSM;
import com.jccdex.rpc.config.Config;
import com.jccdex.rpc.config.RpcNode;
import com.jccdex.rpc.core.coretypes.AccountID;
import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.coretypes.Currency;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.serialized.enums.EngineResult;
import com.jccdex.rpc.core.types.known.tx.signed.SignedTransaction;
import com.jccdex.rpc.core.types.known.tx.txns.OfferCancel;
import com.jccdex.rpc.core.types.known.tx.txns.OfferCreate;
import com.jccdex.rpc.core.types.known.tx.txns.Payment;
import com.jccdex.rpc.http.OkhttpUtil;
import com.jccdex.rpc.res.ServerInfo;
import com.jccdex.rpc.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 井通公链、联盟链RPC开发接口
 * @author xdjiang, shuonimei
 */
public class JccJingtum {
    private final RpcNode rpcNode;
    /**
     * 重复请求次数
     */
    private int tryTimes;
    private final String SUCCESS_CODE = "success";

    private final Map<String, UInt32> seqList = new HashMap<>();

    private Boolean guomi;

    /**
     * @param rpcNodes rpc节点服务器地址列表
     * @param guomi    是否国密链
     */
    public JccJingtum(Boolean guomi, ArrayList<String> rpcNodes) {
        this.guomi = guomi;
        this.tryTimes = rpcNodes.size() > 5 ? rpcNodes.size(): 5;
        rpcNode = new RpcNode(rpcNodes);
    }

    /**
     * 井通公链、联盟链RPC服务构造函数
     * @param fee 每笔交易燃料费(fee取值范围为10-1000000000的整数,燃料费计算公式=fee/1000000,)
     * @param baseToken 交易燃料手续费通证,也是公链的本币
     * @param guomi    是否国密链
     * @param rpcNodes rpc节点服务器地址列表
     */
    public JccJingtum(Integer fee, String baseToken, Boolean guomi, ArrayList<String> rpcNodes) {
        this(guomi, rpcNodes);
        Config.setFee(fee);
        Config.setCurrency(baseToken);
    }


    /**
     * 井通公链、联盟链RPC服务构造函数
     * @param alphabet 字母表，每一条联盟链都可以用不同的或者相同alphabet
     * @param fee 每笔交易燃料费(fee取值范围为10-1000000000的整数,燃料费计算公式=fee/1000000,)
     * @param baseToken 交易燃料手续费通证,也是公链的本币
     * @param guomi    是否国密链
     * @param rpcNodes rpc节点服务器地址列表
     */
    public JccJingtum(String alphabet, Integer fee, String baseToken, Boolean guomi, ArrayList<String> rpcNodes) {
        this(fee, baseToken, guomi, rpcNodes);
        Config.setAlphabet(alphabet);
    }

    /**
     * 井通公链、联盟链RPC服务构造函数
     * @param alphabet 字母表，每一条联盟链都可以用不同的或者相同alphabet
     * @param fee 每笔交易燃料费(fee取值范围为10-1000000000的整数,燃料费计算公式=fee/1000000,)
     * @param baseToken 交易燃料手续费通证,也是公链的本币
     * @param platform 交易的平台账号(与手续费有关)
     * @param guomi    是否国密链
     * @param rpcNodes rpc节点服务器地址列表
     */
    public JccJingtum(String alphabet, Integer fee, String baseToken, String platform, Boolean guomi, ArrayList<String> rpcNodes) {
        this(alphabet, fee, baseToken, guomi, rpcNodes);
        Config.setPlatform(platform);
    }

    /**
     * 设置每笔交易燃料费
     * @param fee 每笔交易燃料费(fee取值范围为10-1000000000的整数,燃料费计算公式=fee/1000000,)
     * @throws Exception 抛出异常
     */
    public void setFee(Integer fee) throws  Exception {
        if(fee < 10) {
            throw new Exception("燃料费不能小于等于0");
        }
        Config.setFee(fee);
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
     * @throws Exception 抛出异常
     */
    public void setPlatform(String platform) throws  Exception{
        if(!this.isValidAddress(platform)) {
            throw new Exception("平台账号不合法");
        }
        Config.setPlatform(platform);
    }

    /**
     * 获取交易平台账号
     * @return 交易平台账号
     */
    public String getPlatform() {
        return Config.PLATFORM;
    }

    /**
     * 设置银关地址
     * @param issuer 银关地址
     * @throws Exception 抛出异常
     */
    public void setIssuer(String issuer) throws  Exception{
        if(!this.isValidAddress(issuer)) {
            throw new Exception("平台账号不合法");
        }
        Config.setIssuer(issuer);
    }

    /**
     * 获取银关地址
     * @return 银关地址
     */
    public String getIssuer() {
        return Config.ISSUER;
    }

    /**
     * 获取钱包字母表
     * @return 钱包字母表
     */
    public String getAlphabet() {return  Config.Alphabet; }

    /**
     * 创建钱包(账号)
     * @return 钱包字符串,json格式 ({"secret":****,"address":****})
     * @throws Exception 抛出异常
     */
    public String createWallet()  throws Exception {
        try {
            ObjectNode data = new ObjectMapper().createObjectNode();
            if(this.guomi) {
                WalletSM walletSM = WalletSM.generate(Config.Alphabet);
                data.put("secret",walletSM.getSecret());
                data.put("address",walletSM.getAddress());
            } else {
                Wallet wallet = Wallet.generate(Config.Alphabet);
                data.put("secret",wallet.getSecret());
                data.put("address",wallet.getAddress());
            }

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
    public String getAddress(String secret) throws  Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(this.guomi) {
                WalletSM walletSM = WalletSM.fromSecret(secret);
                return walletSM.getAddress();
            } else {
                Wallet wallet = Wallet.fromSecret(secret);
                return wallet.getAddress();
            }
        } catch (Exception e) {
            throw new Exception("获取钱包地址异常");
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
    private String getSequence(String address, String rpcNode) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("account", address);
            ArrayList<ObjectNode> params = new ArrayList<>();
            params.add(object);
            ArrayNode array = mapper.valueToTree(params);
            data.put("method", "account_info");
            data.set("params", array);
            return OkhttpUtil.post(rpcNode, data.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取sequence
     * @param address 钱包地址
     * @return sequence
     * @throws Exception 抛出异常
     */
    public long getSequence(String address) throws Exception {
        try {
            int times = this.tryTimes;
            Boolean success = false;

            if(!this.isValidAddress(address)) {
                throw new Exception("钱包地址不合法");
            }

            UInt32 seq = seqList.get(address);
            String res = "";
            if(seq != null) {
                return seq.value().longValue();
            } else {
                do {
                    times--;
                    String url = rpcNode.randomUrl();
                    res = this.getSequence(address, url);
                    String sequence = "";
                    try {
                        String code = JSONObject.parseObject(res).getJSONObject("result").getString("status");
                        if(SUCCESS_CODE.equals(code)) {
                            sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("account_data").getString("Sequence");
                            seq = new UInt32(sequence);
                            success = true;
                            break;
                        }
                    } catch(Exception e) {
                        continue;
                    }
                } while(times > 0);
                if(success) {
                    seqList.put(address, seq);
                    return seq.value().longValue();
                } else {
                    throw new Exception(res);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 设置sequence
     * @param address 钱包地址
     * @param pSequence 交易序列号
     * @throws Exception 抛出异常
     */
    public void setSequence(String address, long pSequence) throws Exception {
        try {
            if(!this.isValidAddress(address)) {
                throw new Exception("钱包地址不合法");
            }

            if(pSequence < 0) {
                throw new Exception("sequence不合法,sequence不能小于0");
            }

            seqList.put(address,new UInt32(pSequence));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 向指定的rpc节点服务器获取获取交易详情
     * @param hash 交易hash
     * @param rpcNode rpc节点服务器
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    private String requestTx(String hash, String rpcNode) throws Exception {
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
            return res;
        }catch (Exception e) {
            throw e;
        }
    }

    /**
     * 根据hash获取交易详情
     * @param hash 交易hash
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String requestTx(String hash) throws Exception {
        int times = this.tryTimes;

        String res = "";
        Boolean success = false;
        try {
            ArrayList<String> list = rpcNode.getUrls();
            Iterator it = list.iterator();
            do {
                times--;
                String url = rpcNode.randomUrl();
                res = this.requestTx(hash,url);
                try {
                    String status = JSONObject.parseObject(res).getJSONObject("result").getString("status");
                    Boolean validated = JSONObject.parseObject(res).getJSONObject("result").getBoolean("validated");
                    if (SUCCESS_CODE.equals(status) && validated) {
                        success = true;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            } while(times > 0);
            if(success) {
                return res;
            } else {
                throw new Exception(res);
            }
        }catch (Exception e) {
            throw e;
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
            throw new Exception("内容转换失败");
        }
    }

    /**
     * 时间戳转换，区块链账本上的时间戳是相对于2000-01-01 08:00:00的偏移时间，换算成当前时间需要转换
     * @param blockTime  区块链账本上的时间戳
     * @return 标准时间戳(秒)
     */
    public long convertTime(Long blockTime) {
        return (blockTime + 946684800);
    }

    /**
     *  转账并校验，每笔交易都会校验是否成功，适合普通转账，优点：每笔交易都进行确认，缺点：转账效率低下
     * @param secret 发送者钱包密钥
     * @param receiver 接收者钱包地址
     * @param pToken 转账Token
     * @param pAmount 转账数量
     * @param pIssuer 银关地址
     * @param memos  交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String setTokenIssue721(String secret, String receiver, String pToken, String pAmount, String pIssuer,String memos) throws Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(!this.isValidAddress(receiver)) {
                throw new Exception("钱包地址不合法");
            }

            if(!this.isValidAddress(pIssuer)) {
                throw new Exception("银关地址不合法");
            }

            if(pToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pAmount.isEmpty()) {
                throw new Exception("数量不合法");
            }

            String sender = this.getAddress(secret);
            long sequence = this.getSequence(sender);

            ObjectMapper mapper = new ObjectMapper();
            String token = pToken.toUpperCase();
            Amount amount;
            Payment payment = new Payment(this.guomi);
            payment.as(AccountID.Account, sender);
            payment.as(AccountID.Destination, receiver);

            BigDecimal bigDecimal = new BigDecimal(pAmount);
            if(bigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(token)) {
                amount = new Amount(bigDecimal);
            } else {
                amount = new Amount(bigDecimal, Currency.fromString(token), AccountID.fromString(pIssuer));
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
            String res = this.submitBlob(tx.tx_blob);
            return res;
        } catch (Exception e) {
            throw new Exception("转账失败");
        }
    }

    /**
     * 判断钱包密钥合法性
     * @param secret 钱包密钥
     * @return 有效返回true,无效返回false
     */
    private boolean isValidSecret(String secret) {
        try {
            if(this.guomi) {
                return WalletSM.isValidSecret(secret, Config.Alphabet);
            } else {
                return Wallet.isValidSecret(secret, Config.Alphabet);
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断钱包密钥合法性
     * @param address 钱包地址
     * @return 有效返回true,无效返回false
     */
    private boolean isValidAddress(String address) {
        try {
            if(this.guomi) {
                return WalletSM.isValidAddress(address, Config.Alphabet);
            } else {
                return Wallet.isValidAddress(address, Config.Alphabet);
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *  构造撤单交易数据(本地签名)
     * @param secret 钱包密钥
     * @param pSequence 挂单序列号
     * @param sequence 交易序列号
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public SignedTransaction buildCancleOrder(String secret, long pSequence, long sequence) throws Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(pSequence < 1) {
                throw new Exception("sequence不能小于等于0");
            }

            String address = this.getAddress(secret);

            OfferCancel offerCancel = this.buildCancleOrderTx(address, pSequence);
            offerCancel.sequence(new UInt32(sequence));

            SignedTransaction tx = offerCancel.sign(secret);
            return tx;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  构造撤单交易数据(非本地签名)
     * @param secret 钱包密钥
     * @param pSequence 挂单序列号
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String buildCancleOrder(String secret, long pSequence) throws Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(pSequence < 1) {
                throw new Exception("sequence不能小于等于0");
            }

            String address = this.getAddress(secret);

            OfferCancel offerCancel = this.buildCancleOrderTx(address, pSequence);

            String txJson = offerCancel.prettyJSON();
            return txJson;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  构造撤单交易数据
     * @param address 钱包密钥
     * @param pSequence 挂单序列号
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    private OfferCancel buildCancleOrderTx(String address, long pSequence) throws Exception {
        try {
            OfferCancel offerCancel = new OfferCancel(this.guomi);
            offerCancel.as(AccountID.Account, address);
            offerCancel.as(UInt32.OfferSequence, pSequence);
            offerCancel.as(Amount.Fee, String.valueOf(Config.FEE));

            return offerCancel;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  构造转账交易数据(本地签名)
     * @param secret 发送者钱包密钥
     * @param receiver 接收者钱包地址
     * @param pToken 转账Token
     * @param pAmount 转账数量
     * @param pIssuer 银关地址
     * @param sequence  交易序列号
     * @param memos  交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public SignedTransaction signWthPayment(String secret, String receiver, String pToken, String pAmount, String pIssuer, long sequence, String memos) throws Exception {
        try {
            return buildPayment(secret, receiver, pToken, pAmount, pIssuer, sequence, memos);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  构造转账交易数据(本地签名)
     * @param secret 发送者钱包密钥
     * @param receiver 接收者钱包地址
     * @param pToken 转账Token
     * @param pAmount 转账数量
     * @param pIssuer 银关地址
     * @param sequence  交易序列号
     * @param memos  交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public SignedTransaction buildPayment(String secret, String receiver, String pToken, String pAmount, String pIssuer, long sequence, String memos) throws Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }
            if(!this.isValidAddress(receiver)) {
                throw new Exception("钱包地址不合法");
            }

            if(!this.isValidAddress(pIssuer)) {
                throw new Exception("银关地址不合法");
            }

            if(pToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pAmount.isEmpty()) {
                throw new Exception("数量不合法");
            }

            String sender = this.getAddress(secret);
            Payment payment = this.buildPaymentTx(sender, receiver, pToken, pAmount, pIssuer, memos);
            payment.sequence(new UInt32(sequence));
            payment.flags(new UInt32(0));

            SignedTransaction tx = payment.sign(secret);
            return tx;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  构造转账交易数据(非本地签名)
     * @param secret 发送者钱包密钥
     * @param receiver 接收者钱包地址
     * @param pToken 转账Token
     * @param pAmount 转账数量
     * @param pIssuer 银关地址
     * @param memos  交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String buildPayment(String secret, String receiver, String pToken, String pAmount, String pIssuer, String memos) throws Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }
            if(!this.isValidAddress(receiver)) {
                throw new Exception("钱包地址不合法");
            }

            if(!this.isValidAddress(pIssuer)) {
                throw new Exception("银关地址不合法");
            }

            if(pToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pAmount.isEmpty()) {
                throw new Exception("数量不合法");
            }

            String sender = this.getAddress(secret);

            Payment payment = this.buildPaymentTx(sender, receiver, pToken, pAmount, pIssuer, memos);

            String txJson = payment.prettyJSON();
            return txJson;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  构造转账交易数据
     * @param sender 发送者钱包地址
     * @param receiver 接收者钱包地址
     * @param pToken 转账Token
     * @param pAmount 转账数量
     * @param pIssuer 银关地址
     * @param memos  交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    private Payment buildPaymentTx(String sender, String receiver, String pToken, String pAmount, String pIssuer, String memos) throws Exception {
        try {
            String token = pToken.toUpperCase();
            Amount amount;
            Payment payment = new Payment(this.guomi);
            payment.as(AccountID.Account, sender);
            payment.as(AccountID.Destination, receiver);

            BigDecimal bigDecimal = new BigDecimal(pAmount);
            if(bigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(token)) {
                amount = new Amount(bigDecimal);
            } else {
                amount = new Amount(bigDecimal, Currency.fromString(token), AccountID.fromString(pIssuer));
            }

            payment.as(Amount.Amount, amount);
            payment.as(Amount.Fee, String.valueOf(Config.FEE));

            if (memos.length() > 0) {
                ArrayList<String> memoList = new ArrayList<>(1);
                memoList.add(memos);
                payment.addMemo(memoList);
            }

            return payment;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 构造挂单交易数据(本地签名)
     * @param secret 挂单方钱包密钥
     * @param pPayToke  挂单方支付的Token名称
     * @param pPayAmount 挂单方支付的Token数量
     * @param pPayIssuer 挂单方支付的Token的银关地址
     * @param pGetToken  挂单方期望得到的Token名称
     * @param pGetAmount 挂单方期望得到的Token数量
     * @param pGetIssuer 挂单方期望得到的Token的银关地址
     * @param sequence 交易序列号
     * @param memos 交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public SignedTransaction buildCreateOrder(String secret, String pPayToke, String pPayAmount, String pPayIssuer, String pGetToken, String pGetAmount, String pGetIssuer, long sequence, String memos) throws Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(!this.isValidAddress(pPayIssuer)) {
                throw new Exception("银关地址不合法");
            }
            if(!this.isValidAddress(pGetIssuer)) {
                throw new Exception("银关地址不合法");
            }

            if(pPayToke.isEmpty() || pGetToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pPayAmount.isEmpty() || pGetAmount.isEmpty()) {
                throw new Exception("token数量不合法");
            }

            String address = this.getAddress(secret);

            BigDecimal payBigDecimal = new BigDecimal(pPayAmount);
            BigDecimal getBigDecimal = new BigDecimal(pGetAmount);

            if(payBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(getBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            OfferCreate offerCreate = this.buildCreateOrderTX(address, pPayToke, pPayAmount, pPayIssuer, pGetToken, pGetAmount, pGetIssuer, memos);

            offerCreate.sequence(new UInt32(sequence));

            SignedTransaction tx = offerCreate.sign(secret);
            return tx;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 构造挂单交易数据(非本地签名)
     * @param secret 挂单方钱包密钥
     * @param pPayToke  挂单方支付的Token名称
     * @param pPayAmount 挂单方支付的Token数量
     * @param pPayIssuer 挂单方支付的Token的银关地址
     * @param pGetToken  挂单方期望得到的Token名称
     * @param pGetAmount 挂单方期望得到的Token数量
     * @param pGetIssuer 挂单方期望得到的Token的银关地址
     * @param memos 交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String buildCreateOrder(String secret, String pPayToke, String pPayAmount, String pPayIssuer, String pGetToken, String pGetAmount, String pGetIssuer, String memos) throws Exception {
        try {
            if(!this.isValidSecret(secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(!this.isValidAddress(pPayIssuer)) {
                throw new Exception("银关地址不合法");
            }
            if(!this.isValidAddress(pGetIssuer)) {
                throw new Exception("银关地址不合法");
            }

            if(pPayToke.isEmpty() || pGetToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(pPayAmount.isEmpty() || pGetAmount.isEmpty()) {
                throw new Exception("token数量不合法");
            }

            String address = this.getAddress(secret);

            BigDecimal payBigDecimal = new BigDecimal(pPayAmount);
            BigDecimal getBigDecimal = new BigDecimal(pGetAmount);

            if(payBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(getBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            OfferCreate offerCreate = this.buildCreateOrderTX(address, pPayToke, pPayAmount, pPayIssuer, pGetToken, pGetAmount, pGetIssuer, memos);

            String txJson = offerCreate.prettyJSON();
            return txJson;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 构造挂单交易数据(本地签名)
     * @param address 挂单方钱包地址
     * @param pPayToke  挂单方支付的Token名称
     * @param pPayAmount 挂单方支付的Token数量
     * @param pPayIssuer 挂单方支付的Token的银关地址
     * @param pGetToken  挂单方期望得到的Token名称
     * @param pGetAmount 挂单方期望得到的Token数量
     * @param pGetIssuer 挂单方期望得到的Token的银关地址
     * @param memos 交易备注(无就传"")
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    private OfferCreate buildCreateOrderTX(String address, String pPayToke, String pPayAmount, String pPayIssuer, String pGetToken, String pGetAmount, String pGetIssuer, String memos) throws Exception {
        try {

            String payToken = pPayToke.toUpperCase();
            String getToken = pGetToken.toUpperCase();

            OfferCreate offerCreate = new OfferCreate(this.guomi);
            offerCreate.as(AccountID.Account, address);
            offerCreate.as(AccountID.Platform, Config.PLATFORM);

            Amount payAmount;
            BigDecimal payBigDecimal = new BigDecimal(pPayAmount);
            BigDecimal getBigDecimal = new BigDecimal(pGetAmount);

            if(Config.CURRENCY.equals(payToken)) {
                payAmount = new Amount(payBigDecimal);
            } else {
                payAmount = new Amount(payBigDecimal, Currency.fromString(payToken), AccountID.fromString(pPayIssuer));
            }

            Amount getAmount;

            if(Config.CURRENCY.equals(getToken)) {
                getAmount = new Amount(getBigDecimal);
            } else {
                getAmount = new Amount(getBigDecimal, Currency.fromString(getToken), AccountID.fromString(pGetIssuer));
            }
            offerCreate.as(Amount.TakerPays, getAmount);
            offerCreate.as(Amount.TakerGets, payAmount);

            offerCreate.as(Amount.Fee, String.valueOf(Config.FEE));

            if (memos.length() > 0) {
                ArrayList<String> memoList = new ArrayList<>(1);
                memoList.add(memos);
                offerCreate.addMemo(memoList);
            }
            return offerCreate;
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
    public String submitNoCheck(String txBlob) throws Exception {
        try {
            return submitBlob(txBlob);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 向节点发送签名后的内容
     * @param txBlob 交易信息
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submitBlob(String txBlob) throws Exception {
        try {
            int times = this.tryTimes;
            String resTx = "";
            String res = "";
            String successRes = "";
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
                    String sender = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getString("Account");
                    int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
                    EngineResult engineResult = EngineResult.fromNumber(engine_result_code);

                    if(EngineResult.isPastSeq(engineResult)) {
                        seqList.remove(sender);
                        break;
                    }

                    if(!EngineResult.isRetry(engineResult)) {
                        break;
                    }

                    if(EngineResult.isSuccess(engineResult)) {
                        long sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getLongValue("Sequence");
                        this.setSequence(sender,++sequence);
                        break;
                    }

                    //延时50毫秒
                    Thread.sleep(50);
                }catch (Exception e) {
                    continue;
                }
            }while(times > 0);

            return res;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 向节点发送交易请求
     * @param txJson 交易信息
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submitWithSecret(String secret, String txJson) throws Exception {
        try {
            int times = this.tryTimes;
            String res = "";
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("secret", secret);
            JSONObject jsonObject = JSONObject.parseObject(txJson);
            object.putPOJO("tx_json", jsonObject);
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
                    String sender = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getString("Account");
                    int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
                    EngineResult engineResult = EngineResult.fromNumber(engine_result_code);

                    if(EngineResult.isPastSeq(engineResult)) {
                        seqList.remove(sender);
                        break;
                    }

                    if(!EngineResult.isRetry(engineResult)) {
                        break;
                    }

                    if(EngineResult.isSuccess(engineResult)) {
                        long sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getLongValue("Sequence");
                        this.setSequence(sender,++sequence);
                        break;
                    }

                    //延时50毫秒
                    Thread.sleep(50);
                }catch (Exception e) {
                    continue;
                }
            }while(times > 0);

            return res;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取节点状态
     * @return 节点列表和状态
     * @throws Exception
     */
    public ArrayList<ServerInfo> getServerState()  throws Exception {
        try {
            String res = "";
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode data = mapper.createObjectNode();

            data.put("method", "ledger");
            ArrayList<String> hostList = rpcNode.getUrls();
            ArrayList<ServerInfo> serverList = new ArrayList<>();;;
            for(int i=0; i< hostList.size(); i++)
            {
                try {
                    ServerInfo serverInfo = new ServerInfo();
                    String url = hostList.get(i);
                    res = OkhttpUtil.post(url, data.toString());
                    if("success".equals(JSONObject.parseObject(res).getJSONObject("result").getString("status"))) {
                        serverInfo.host = url;
                        serverInfo.lastLedgerHash = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("closed").getJSONObject("ledger").getString("ledger_hash");
                        serverInfo.height = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("closed").getJSONObject("ledger").getString("seqNum");
                        String closeTime = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("closed").getJSONObject("ledger").getString("close_time");
                        serverInfo.lastLedgerTime = this.convertTime(Long.valueOf(closeTime));
                        serverList.add(serverInfo);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            return serverList;
        } catch (Exception e) {
            throw e;
        }
    }

}
