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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 井通公链、联盟链RPC开发接口
 * @author xdjiang
 */
public class JccJingtum {

    /**
     * 字母表，每一条联盟链都可以用不同的或者相同alphabet
     */
    private final String alphabet;

    /**
     * 每笔交易燃料费(fee取值范围为10-1000000000的整数,燃料费计算公式=fee/1000000,)
     */
    private final Integer fee;

    /**
     * 链基础通证
     */
    private final String baseToken;

    /**
     * 平台手续费账号
     */
    private final String platform;

    /**
     * rpc节点
     */
    private final ArrayList<String> rpcNodes;
    /**
     * 重复请求次数
     */
    private final int tryTimes;

    /**
     * 是否支持国密
     */
    private final Boolean isGuomi;

    /**
     * 是否启动sequence缓存
     */
    private final Boolean enableSequenceCach;

    /**
     * 是否本地签名
     */
    private final Boolean isLocalSign;


    /**
     * 交易燃料手续费通证,也是公链的本币
     */
    private RpcNode rpcNode = null;

    private final String SUCCESS_CODE = "success";

    private Map<String, UInt32> seqList = null;


    private JccJingtum(Builder builder) {
        this.isGuomi = builder.isGuomi;
        this.isLocalSign = builder.isLocalSign;
        this.enableSequenceCach = builder.enableSequenceCach;

        this.alphabet = builder.alphabet;
        this.fee = builder.fee;
        this.baseToken = builder.baseToken;
        this.platform = builder.platform;

        this.rpcNodes = builder.rpcNodes;

        Config.setAlphabet(this.alphabet);
        Config.setFee(this.fee);
        Config.setCurrency(this.baseToken);
        Config.setPlatform(this.platform);

        if(this.rpcNodes != null && this.rpcNodes.size() > 0) {
            this.rpcNode = new RpcNode(this.rpcNodes);
            this.tryTimes = rpcNodes.size();
        } else {
            tryTimes = 5;
        }

        if(this.enableSequenceCach) {
            seqList = new HashMap<>();
        }
    }

    public static class Builder {
        private final Boolean isGuomi;
        private final Boolean isLocalSign;
        private final Boolean enableSequenceCach;


        private String alphabet = Config.DEFAULT_ALPHABET;
        private Integer fee = Config.FEE;
        private String baseToken = Config.DEFAULT_CURRENCY;
        private String platform = Config.DEFAULT_PLATFORM;
        private ArrayList<String> rpcNodes = null;

        public Builder(Boolean isGuomi, Boolean isLocalSign, Boolean enableSequenceCach) {
            this.isGuomi = isGuomi;
            this.isLocalSign = isLocalSign;
            this.enableSequenceCach = enableSequenceCach;
        }

        public Builder setAlphabet(String alphabet) {
            this.alphabet = alphabet;
            return this;
        }

        public Builder setFee(Integer fee) {
            this.fee = fee;
            return this;
        }

        public Builder setBaseToken(String baseToken) {
            this.baseToken = baseToken;
            return this;
        }

        public Builder setPlatform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder setRpcNodes(ArrayList<String> rpcNodes) {
            this.rpcNodes = rpcNodes;
            return this;
        }

        public JccJingtum build() {
            return new JccJingtum(this);
        }

    }

    /**
     * 获取钱包字母表
     * @return 钱包字母表
     */
    public String getAlphabet() { return  this.alphabet; }

    /**
     * 获取每笔交易燃料费
     * @return 每笔交易燃料费
     */
    public Integer getFee() {
        return this.fee;
    }

    /**
     * 获取链基础通证
     * @return 链基础通证
     */
    public String getBaseToken() {
        return this.baseToken;
    }

    /**
     * 获取交易平台账号
     * @return 交易平台账号
     */
    public String getPlatform() {
        return this.platform;
    }

    /**
     * 获取异常重试次数
     * @return 异常重试次数
     */
    public int getTryTimes() {
        return  this.tryTimes ;
    }

    /**
     * 获取rpc节点列表
     * @return rpc节点列表
     */
    public ArrayList<String> getRpcNodes() {
        return this.rpcNodes;
    }

    /**
     * 创建钱包(账号)
     * @return 钱包字符串,json格式 ({"secret":****,"address":****})
     * @throws Exception 抛出异常
     */
    public String createWallet()  throws Exception {
        try {
            ObjectNode data = new ObjectMapper().createObjectNode();
            if(this.isGuomi) {
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

            if(this.isGuomi) {
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
     * 获取sequence
     * @param address 钱包地址
     * @param rpcHost rpc节点服务器
     * @return sequence
     * @throws IOException 抛出异常
     */
    private String requectSequence(String address, String rpcHost) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();
        ObjectNode object = mapper.createObjectNode();
        object.put("account", address);
        ArrayList<ObjectNode> params = new ArrayList<>();
        params.add(object);
        ArrayNode array = mapper.valueToTree(params);
        data.put("method", "account_info");
        data.set("params", array);
        return OkhttpUtil.post(rpcHost, data.toString());
    }

    /**
     * 获取sequence
     * @param address 钱包地址
     * @return sequence
     * @throws Exception 抛出异常
     */
    public UInt32 getSequence(String address) throws Exception {
        if(this.rpcNode == null) {
            throw new Exception("RPC节点为空，请初始化时指定RPC节点");
        }

        int times = this.tryTimes;
        UInt32 seq = null;
        do {
            try {
                times--;
                String rpcHost = rpcNode.randomUrl();

                seq = this.getSequence(address, rpcHost);
                if(seq != null) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while(times > 0);
        return  seq;
    }

    /**
     * 向指定节点获取sequence
     * @param address 钱包地址
     * @param rpcHost rpc节点服务器
     * @return sequence
     * @throws Exception 抛出异常
     */
    public UInt32 getSequence(String address, String rpcHost) throws Exception {
        if(!this.isValidAddress(address)) {
            throw new Exception("钱包地址不合法");
        }

        if(rpcHost == null  || rpcHost.isEmpty()) {
            throw new Exception("RPC节点不能为空，请指定RPC节点");
        }

        UInt32 seq = null;
        if(this.enableSequenceCach) {
            seq= seqList.get(address);
            if(seq != null) {
                return seq;
            }
        }

        String res  = this.requectSequence(address, rpcHost);
        String code = JSONObject.parseObject(res).getJSONObject("result").getString("status");
        if(SUCCESS_CODE.equals(code)) {
            String sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("account_data").getString("Sequence");
            seq = new UInt32(sequence);
            this.addSequence(address,seq);
        }
        return seq;
    }

    /**
     * 更新sequence缓存值
     * @param address 钱包地址
     * @param pSequence 交易序列号
     * @throws Exception 抛出异常
     */
    private void addSequence(String address, UInt32 pSequence) throws Exception {
        if(!this.isValidAddress(address)) {
            throw new Exception("钱包地址不合法");
        }

        if(this.enableSequenceCach) {
            seqList.put(address,pSequence);
        }
    }

    /**
     * 移除sequence缓存值
     * @param address 钱包地址
     * @throws Exception 抛出异常
     */
    private void removeSequence(String address) throws Exception {
        if(!this.isValidAddress(address)) {
            throw new Exception("钱包地址不合法");
        }

        if(this.enableSequenceCach) {
            seqList.remove(address);
        }
    }

    /**
     * 向指定的rpc节点服务器获取获取交易详情
     * @param hash 交易hash
     * @param rpcHost rpc节点服务器
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    private String requestTraction(String hash, String rpcHost) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();
        ObjectNode object = mapper.createObjectNode();
        object.put("transaction", hash);
        object.put("binary", false);
        ArrayList<ObjectNode> params = new ArrayList<>();
        params.add(object);
        ArrayNode array = mapper.valueToTree(params);
        data.put("method", "tx");
        data.set("params", array);
        return OkhttpUtil.post(rpcHost, data.toString());
    }

    /**
     * 根据hash向指定节点获取交易详情
     * @param hash 交易hash
     * @param rpcHost rpc节点服务器
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String requestTx(String hash, String rpcHost) throws Exception {
        if(hash == null || hash.isEmpty()) {
            throw new Exception("hash不能为空");
        }

        if(rpcHost == null  || rpcHost.isEmpty()) {
            throw new Exception("RPC节点不能为空，请指定RPC节点");
        }

        String res = this.requestTraction(hash,rpcHost);
        String status = JSONObject.parseObject(res).getJSONObject("result").getString("status");
        Boolean validated = JSONObject.parseObject(res).getJSONObject("result").getBoolean("validated");
        if (SUCCESS_CODE.equals(status) && validated) {
            return  res;
        }
        return null;
    }

    /**
     * 根据hash获取交易详情
     * @param hash 交易hash
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String requestTx(String hash) throws Exception {
        if(this.rpcNode == null) {
            throw new Exception("RPC节点为空，请初始化时指定RPC节点");
        }

        int times = this.tryTimes;
        String res = null;
        do {
            times--;
            String rpcHost = rpcNode.randomUrl();
            try {
                res = this.requestTx(hash,rpcHost);
                if(res != null) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } while(times > 0);
        return  res;
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
     * 判断钱包密钥合法性
     * @param secret 钱包密钥
     * @return 有效返回true,无效返回false
     */
    private boolean isValidSecret(String secret) {
        try {
            if(this.isGuomi) {
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
            if(this.isGuomi) {
                return WalletSM.isValidAddress(address, Config.Alphabet);
            } else {
                return Wallet.isValidAddress(address, Config.Alphabet);
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *  构造撤单交易数据
     * @param secret 钱包密钥
     * @param pSequence 挂单序列号
     * @param sequence 交易序列号
     * @return 交易详情 json格式
     * @throws Exception 抛出异常
     */
    public String buildCancleOrder(String secret, UInt32 pSequence, UInt32 sequence) throws Exception {
        if(!this.isValidSecret(secret)) {
            throw new Exception("钱包密钥不合法");
        }

        String address = this.getAddress(secret);

        OfferCancel offerCancel = this.buildCancleOrderTx(address, pSequence);
        if(this.isLocalSign) {
            offerCancel.sequence(sequence);
            SignedTransaction tx = offerCancel.sign(secret);
            return  tx.tx_blob;
        } else {
            return offerCancel.prettyJSON();
        }
    }

    /**
     *  构造撤单交易数据
     * @param address 钱包密钥
     * @param pSequence 挂单序列号
     * @return 交易详情 json格式
     */
    private OfferCancel buildCancleOrderTx(String address, UInt32 pSequence) {
        OfferCancel offerCancel = new OfferCancel(this.isGuomi);
        offerCancel.as(AccountID.Account, address);
        offerCancel.as(UInt32.OfferSequence, pSequence);
        offerCancel.as(Amount.Fee, String.valueOf(Config.FEE));
        return offerCancel;
    }

    /**
     *  构造转账交易数据
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
    public String buildPayment(String secret, String receiver, String pToken, String pAmount, String pIssuer, UInt32 sequence, String memos) throws Exception {
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

        if(this.isLocalSign) {
            payment.sequence(sequence);
            payment.flags(new UInt32(0));
            SignedTransaction tx = payment.sign(secret);
            return  tx.tx_blob;
        } else {
            return payment.prettyJSON();
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
        String token = pToken.toUpperCase();
        Amount amount;
        Payment payment = new Payment(this.isGuomi);
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
    public String buildCreateOrder(String secret, String pPayToke, String pPayAmount, String pPayIssuer, String pGetToken, String pGetAmount, String pGetIssuer, UInt32 sequence, String memos) throws Exception {
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

        if(this.isLocalSign) {
            offerCreate.sequence(sequence);
            offerCreate.flags(new UInt32(0));
            SignedTransaction tx = offerCreate.sign(secret);
            return  tx.tx_blob;
        } else {
            return offerCreate.prettyJSON();
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
     */
    private OfferCreate buildCreateOrderTX(String address, String pPayToke, String pPayAmount, String pPayIssuer, String pGetToken, String pGetAmount, String pGetIssuer, String memos) {

        String payToken = pPayToke.toUpperCase();
        String getToken = pGetToken.toUpperCase();

        OfferCreate offerCreate = new OfferCreate(this.isGuomi);
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
    }

    /**
     * 向指定节点发送交易(签名后的数据)
     * @param txBlob 交易信息
     * @param rpcHost rpc节点服务器
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submitBlob(String txBlob, String rpcHost) throws Exception {
        if(txBlob == null || txBlob.isEmpty()) {
            throw  new Exception("交易内容不能为空");
        }

        if(rpcHost == null  || rpcHost.isEmpty()) {
            throw new Exception("RPC节点不能为空，请指定RPC节点");
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();
        ObjectNode object = mapper.createObjectNode();
        object.put("tx_blob", txBlob);
        ArrayList<ObjectNode> params = new ArrayList<>();
        params.add(object);
        ArrayNode array = mapper.valueToTree(params);

        data.put("method", "submit");
        data.set("params", array);

        String res = OkhttpUtil.post(rpcHost, data.toString());
        String sender = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getString("Account");
        int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
        EngineResult engineResult = EngineResult.fromNumber(engine_result_code);

        if(EngineResult.isPastSeq(engineResult)) {
            this.removeSequence(sender);
        }

        if(EngineResult.isSuccess(engineResult)) {
            long sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getLongValue("Sequence");
            this.addSequence(sender,new UInt32(++sequence));
        }

        return res;
    }

    /**
     * 向节点发送交易(签名后的数据)
     * @param txBlob 交易信息
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submitBlob(String txBlob) throws Exception {
        if(this.rpcNode == null) {
            throw new Exception("RPC节点为空，请初始化时指定RPC节点");
        }
        int times = this.tryTimes;
        String res = null;
        do{
            times--;
            try {
                String rpcHost = rpcNode.randomUrl();
                res = this.submitBlob(txBlob,rpcHost);
                int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
                EngineResult engineResult = EngineResult.fromNumber(engine_result_code);

                if(EngineResult.isPastSeq(engineResult)) {
                    break;
                }

                if(!EngineResult.isRetry(engineResult)) {
                    break;
                }

                if(EngineResult.isSuccess(engineResult)) {
                    break;
                }

                //延时50毫秒
                Thread.sleep(50);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }while(times > 0);

        return res;
    }

    /**
     * 向指定节点发送交易请求(非本地签名)
     * @param secret 钱包密钥
     * @param txJson 交易信息
     * @param rpcHost rpc节点服务器
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submitWithSecret(String secret, String txJson, String rpcHost) throws Exception {
        if(!this.isValidSecret(secret)) {
            throw new Exception("钱包密钥不合法");
        }

        if(txJson == null || txJson.isEmpty()) {
            throw  new Exception("交易内容不能为空");
        }

        if(rpcHost == null  || rpcHost.isEmpty()) {
            throw new Exception("RPC节点不能为空，请指定RPC节点");
        }

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

        String res = OkhttpUtil.post(rpcHost, data.toString());
        String sender = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getString("Account");
        int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
        EngineResult engineResult = EngineResult.fromNumber(engine_result_code);

        if(EngineResult.isPastSeq(engineResult)) {
            this.removeSequence(sender);
        }

        if(EngineResult.isSuccess(engineResult)) {
            long sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getLongValue("Sequence");
            this.addSequence(sender,new UInt32(++sequence));
        }

        return res;
    }

    /**
     * 向节点发送交易请求(非本地签名)
     * @param secret 钱包密钥
     * @param txJson 交易信息
     * @return 交易信息
     * @throws Exception 抛出异常
     */
    public String submitWithSecret(String secret, String txJson) throws Exception {
        if(this.rpcNode == null) {
            throw new Exception("RPC节点为空，请初始化时指定RPC节点");
        }

        if(!this.isValidSecret(secret)) {
            throw new Exception("钱包密钥不合法");
        }

        if(txJson == null || txJson.isEmpty()) {
            throw  new Exception("交易内容不能为空");
        }

        int times = this.tryTimes;
        String res = null;
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
                String rpcHost = rpcNode.randomUrl();
                res = this.submitWithSecret(secret, txJson, rpcHost);
                int engine_result_code = JSONObject.parseObject(res).getJSONObject("result").getIntValue("engine_result_code");
                EngineResult engineResult = EngineResult.fromNumber(engine_result_code);

                if(EngineResult.isPastSeq(engineResult)) {
                    break;
                }

                if(!EngineResult.isRetry(engineResult)) {
                    break;
                }

                if(EngineResult.isSuccess(engineResult)) {
                    break;
                }

                //延时50毫秒
                Thread.sleep(50);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }while(times > 0);

        return res;
    }

    /**
     * 获取指定节点状态
     * @param rpcHost rpc节点服务器
     * @return 节点列表和状态
     */
    public ServerInfo getServerState(String rpcHost)  throws Exception {
        if(rpcHost == null  || rpcHost.isEmpty()) {
            throw new Exception("RPC节点不能为空，请指定RPC节点");
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();

        data.put("method", "ledger");
        ServerInfo serverInfo = null;
        String res = OkhttpUtil.post(rpcHost, data.toString());
        if("success".equals(JSONObject.parseObject(res).getJSONObject("result").getString("status"))) {
            serverInfo = new ServerInfo();
            serverInfo.host = rpcHost;
            serverInfo.lastLedgerHash = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("closed").getJSONObject("ledger").getString("ledger_hash");
            serverInfo.height = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("closed").getJSONObject("ledger").getString("seqNum");
            String closeTime = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("closed").getJSONObject("ledger").getString("close_time");
            serverInfo.lastLedgerTime = this.convertTime(Long.valueOf(closeTime));
        }

        return serverInfo;
    }

    /**
     * 获取节点状态
     * @return 节点列表和状态
     */
    public ArrayList<ServerInfo> getServerState()  throws Exception {
        if(this.rpcNode == null) {
            throw new Exception("RPC节点为空，请初始化时指定RPC节点");
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();

        data.put("method", "ledger");
        ArrayList<String> hostList = rpcNode.getUrls();
        ArrayList<ServerInfo> serverList = new ArrayList<>();
        for (String s : hostList) {
            try {
                ServerInfo serverInfo = this.getServerState(s);
                if (serverInfo != null) {
                    serverList.add(serverInfo);
                }
            } catch (Exception e) {
                continue;
            }
            Thread.sleep(50);
        }
        return serverList;
    }

}
