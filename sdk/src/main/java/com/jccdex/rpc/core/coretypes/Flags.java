package com.jccdex.rpc.core.coretypes;

import com.jccdex.core.serialized.BytesSink;
import com.jccdex.rpc.core.fields.Type;
import com.jccdex.rpc.core.serialized.SerializedType;

import java.util.BitSet;

// TODO
public class Flags extends BitSet implements SerializedType {
    @Override
    public Object toJSON() {
        return null;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public String toHex() {
        return null;
    }

    @Override
    public void toBytesSink(BytesSink to) {

    }

    @Override
    public Type type() {
        return Type.UInt32;
    }
}
