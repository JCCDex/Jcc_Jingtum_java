package com.jccdex.rpc.res;

import com.jccdex.rpc.data.BalanceData;

public class BalanceResponse extends BaseResponse{
    private BalanceData data;

    public BalanceData getData() {
        return data;
    }

    public void setData(BalanceData data) {
        this.data = data;
    }
}
