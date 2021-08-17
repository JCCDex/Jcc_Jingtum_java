package com.jccdex.rpc.core.serialized;

import com.jccdex.rpc.core.fields.Type;

public interface SerializedType {
    Object toJSON();
    byte[] toBytes();
    String toHex();
    void toBytesSink(BytesSink to);
    Type type();
}
