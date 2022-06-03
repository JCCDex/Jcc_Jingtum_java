package com.jccdex.rpc.client.bean;

/**
 * 返回的支付信息对象
 * 描述：buildPaymentTx返回对象
 */
public class TransactionInfo {
	
	// 请求结果 
	private String engineResult;
	// 请求结果编码
	private String engineResultCode;
	// 请求结果message信息 
	private String engineResultMessage;
	// 交易内容(签名后的bolb，本地签名内容)
	private String txBlob;
	// 交易内容(交易数据，非本地签名内容)
	private String txJson;
	// 交易Hash
	private String txHash;
	
	public String getEngineResult() {
		return engineResult;
	}
	public void setEngineResult(String engineResult) {
		this.engineResult = engineResult;
	}
	public String getEngineResultCode() {
		return engineResultCode;
	}
	public void setEngineResultCode(String engineResultCode) {
		this.engineResultCode = engineResultCode;
	}
	public String getEngineResultMessage() {
		return engineResultMessage;
	}
	public void setEngineResultMessage(String engineResultMessage) {
		this.engineResultMessage = engineResultMessage;
	}
	public String getTxBlob() {
		return txBlob;
	}
	public void setTxBlob(String txBlob) {
		this.txBlob = txBlob;
	}
	public String getTxJson() {
		return txJson;
	}
	public void setTxJson(String txJson) {
		this.txJson = txJson;
	}
	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}


	@Override
	public String toString() {
		return "TransactionInfo{" +
				"engineResult='" + engineResult + '\'' +
				", engineResultCode='" + engineResultCode + '\'' +
				", engineResultMessage='" + engineResultMessage + '\'' +
				", txBlob='" + txBlob + '\'' +
				", txJson='" + txJson + '\'' +
				", txHash='" + txHash + '\'' +
				'}';
	}
}

