package com.jccdex.rpc.core.types.known.sle;

import com.jccdex.rpc.core.coretypes.Vector256;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.serialized.enums.LedgerEntryType;

public class LedgerHashes extends LedgerEntry {
    public LedgerHashes() {
        super(LedgerEntryType.LedgerHashes);
    }

    public Vector256 hashes() {
        return get(Vector256.Hashes);
    }

    public void hashes(Vector256 hashes) {
        put(Vector256.Hashes, hashes);
    }

    public UInt32 lastLedgerSequence() {
        return get(UInt32.LastLedgerSequence);
    }
}
