package com.jccdex.rpc.token;

/**
 * 链通证基本数据对象
 * @author xdjiang
 */
public class BaseToken {
    /**
     * 通证名称
     */
    private final String name;

    /**
     * 发行银关
     */
    private final String issuer;

    /**
     * 是否是链的记账通证
     */
    private final boolean isNative;

    /**
     * 构造函数
     * @param name 通证名称
     * @param issuer 发行银关
     */
    public BaseToken(String name, String issuer) {
        this.name = name.toUpperCase();
        this.issuer = issuer;
        this.isNative = this.issuer.length() <= 0;
    }

    /**
     * 构造函数
     * @param name 通证名称
     */
    public BaseToken(String name) {
        this.name = name;
        this.issuer = "";
        this.isNative = true;
    }

    /**
     * 获取通证名称
     * @return 通证名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取通证发行银关
     * @return 通证发行银关
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * 是否是记账通证标志
     * @return 记账通证标志
     */
    public boolean isNative() {
        return isNative;
    }

    @Override
    public String toString() {
        if (isNative) {
            return this.name.toLowerCase();
        }
        return this.name.toLowerCase() + "." + this.issuer;
    }

}
