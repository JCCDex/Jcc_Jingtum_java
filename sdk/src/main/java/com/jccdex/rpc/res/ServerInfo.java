package com.jccdex.rpc.res;

public class ServerInfo {

    public String host;//节点地址
    public String height;//区块高度
    public String lastLedgerHash;//区块hash
    public long lastLedgerTime;//最新区块时间

    public void setHost(String host) {
        this.host = host;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public  void setLastLedgerHash(String lastLedgerHash) {
        this.lastLedgerHash = lastLedgerHash;
    }

    public  void setLastLedgerTime(long lastLedgerTime) {
        this.lastLedgerTime = lastLedgerTime;
    }

    public String getHost() {
        return host;
    }

    public String getHeight() {
        return height;
    }

    public String getLastLedgerHash() {
        return lastLedgerHash;
    }

    public long getLastLedgerTime() {
        return lastLedgerTime;
    }
}

