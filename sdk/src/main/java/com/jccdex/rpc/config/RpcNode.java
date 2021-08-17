package com.jccdex.rpc.config;

import java.util.ArrayList;

public class RpcNode extends AServerNode {

    /**
     *
     * @param rpcNodes RPC服务器地址
     */
    public RpcNode(ArrayList<String> rpcNodes)
    {
        super(rpcNodes);
    }
}
