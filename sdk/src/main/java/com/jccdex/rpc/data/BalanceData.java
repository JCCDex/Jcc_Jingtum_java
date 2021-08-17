package com.jccdex.rpc.data;

import com.jccdex.rpc.token.TokenItem;

import java.util.ArrayList;

public class BalanceData {

    private String address;
    private int feeflag;
    private ArrayList<TokenItem> list;

    public BalanceData() {
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFeeflag(int feeflag) {
        this.feeflag = feeflag;
    }

    public void setList(ArrayList<TokenItem> list) {
        this.list = list;
    }

    public String getAddress() {
        return address;
    }

    public int getFeeflag() {
        return feeflag;
    }

    public ArrayList<TokenItem> getList() {
        return list;
    }

}
