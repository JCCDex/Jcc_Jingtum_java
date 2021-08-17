package com.jccdex.rpc.core.types.shamap;

import com.jccdex.rpc.core.coretypes.hash.prefixes.Prefix;
import com.jccdex.rpc.core.serialized.BytesSink;

abstract public class ShaMapItem<T> {
    abstract void toBytesSink(BytesSink sink);
    public abstract ShaMapItem<T> copy();
    public abstract T value();
    public abstract Prefix hashPrefix();
}
