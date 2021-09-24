package com.jccdex.rpc.core.types.shamap;

import com.jccdex.core.serialized.BytesSink;
import com.jccdex.rpc.core.coretypes.STObject;
import com.jccdex.rpc.core.coretypes.hash.prefixes.HashPrefix;
import com.jccdex.rpc.core.coretypes.hash.prefixes.Prefix;
import com.jccdex.rpc.core.types.known.sle.LedgerEntry;

public class LedgerEntryItem extends ShaMapItem<LedgerEntry> {
    public LedgerEntryItem(LedgerEntry entry) {
        this.entry = entry;
    }

    public LedgerEntry entry;

    @Override
    void toBytesSink(BytesSink sink) {
        entry.toBytesSink(sink);
    }

    @Override
    public String toString() {
        return entry.prettyJSON();
    }

    @Override
    public ShaMapItem<LedgerEntry> copy() {
        STObject object = STObject.translate.fromBytes(entry.toBytes());
        LedgerEntry le = (LedgerEntry) object;
        // TODO: what about other auxiliary (non serialized) fields
        le.index(entry.index());
        return new LedgerEntryItem(le);
    }

    @Override
    public LedgerEntry value() {
        return entry;
    }

    @Override
    public Prefix hashPrefix() {
        return HashPrefix.leafNode;
    }
}
