package com.jccdex.rpc.core.serialized;

public interface BytesSink {
    void add(byte aByte);
    void add(byte[] bytes);
}
