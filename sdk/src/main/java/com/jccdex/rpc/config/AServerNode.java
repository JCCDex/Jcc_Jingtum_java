package com.jccdex.rpc.config;

import java.util.ArrayList;
import java.util.Random;

public abstract class AServerNode {
    /**
     * 服务器地址
     */
    private ArrayList<String> urls;

    public AServerNode(ArrayList<String> _urls) {
        this.urls = new ArrayList<String>();
        this.urls = (ArrayList<String>) _urls.clone();
    }

    /**
     * 从服务器列表中随机获取一个服务器地址
     * @return 服务器地址
     * @throws Exception
     */
    public String randomUrl() throws Exception{
        try {
            Random random = new Random();
            int index = random.nextInt(this.urls.size());
            return this.urls.get(index);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取rpc服务器列表
     * @return 获取rpc服务器列表
     * @throws Exception
     */
    public ArrayList<String> getUrls() throws Exception {
        return this.urls;
    }
}
