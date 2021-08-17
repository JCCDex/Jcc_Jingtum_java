package com.jccdex.rpc.token;

/**
 * 链通证对象
 * @author xdjiang
 */
public class TokenItem extends BaseToken {
    /**
     * 通证余额
     */
    private String value;

    /**
     * 通证冻结金额
     */
    private String frozen;

    public TokenItem(String name, String issuer, String value, String frozen) {
        super(name, issuer);
        this.frozen = frozen;
        this.value = value;
    }

    public TokenItem(String name, String value, String frozen) {
        super(name);
        this.frozen = frozen;
        this.value = value;
    }

    /**
     * 获取余额
     * @return 余额
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取冻结金额
     * @return  冻结金额
     */
    public String getFrozen() {
        return frozen;
    }

    /**
     * 设置余额
     * @param value 余额
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 设置冻结金额
     * @param frozen 冻结金额
     */
    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }
}
