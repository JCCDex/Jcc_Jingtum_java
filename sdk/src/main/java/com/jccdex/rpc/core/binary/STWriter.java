package com.jccdex.rpc.core.binary;

import com.jccdex.core.serialized.BytesSink;
import com.jccdex.rpc.core.coretypes.hash.Hash256;
import com.jccdex.rpc.core.serialized.BinarySerializer;
import com.jccdex.rpc.core.serialized.SerializedType;
import com.jccdex.rpc.core.types.known.sle.LedgerEntry;
import com.jccdex.rpc.core.types.known.tx.result.TransactionResult;

public class STWriter implements BytesSink {
    BytesSink sink;
    BinarySerializer serializer;
    public STWriter(BytesSink bytesSink) {
        serializer = new BinarySerializer(bytesSink);
        sink = bytesSink;
    }
    public void write(SerializedType obj) {
        obj.toBytesSink(sink);
    }
    public void writeVl(SerializedType obj) {
        serializer.addLengthEncoded(obj);
    }

    @Override
    public void add(byte aByte) {
        sink.add(aByte);
    }

    @Override
    public void add(byte[] bytes) {
        sink.add(bytes);
    }

    public void write(TransactionResult result) {
        write(result.hash);
        writeVl(result.txn);
        writeVl(result.meta);
    }

    public void write(Hash256 hash256, LedgerEntry le) {
        write(hash256);
        writeVl(le);
    }
}
