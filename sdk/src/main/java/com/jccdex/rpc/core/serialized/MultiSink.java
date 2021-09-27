package com.jccdex.rpc.core.serialized;

import com.jccdex.core.serialized.BytesSink;

public class MultiSink implements BytesSink {
    final private BytesSink[] sinks;
    public MultiSink(BytesSink... sinks) {
        this.sinks = sinks;
    }
    @Override
    public void add(byte b) {
        for (BytesSink sink : sinks) {
            sink.add(b);
        }
    }
    @Override
    public void add(byte[] b) {
        for (BytesSink sink : sinks) {
            sink.add(b);
        }
    }
}
