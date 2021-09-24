package com.jccdex.rpc.core.types.shamap;

import com.jccdex.core.serialized.BytesSink;
import com.jccdex.rpc.core.coretypes.hash.prefixes.Prefix;

abstract public class ShaMapItem<T> {
    abstract void toBytesSink(BytesSink sink);
    public abstract ShaMapItem<T> copy();
    public abstract T value();
    public abstract Prefix hashPrefix();
}
