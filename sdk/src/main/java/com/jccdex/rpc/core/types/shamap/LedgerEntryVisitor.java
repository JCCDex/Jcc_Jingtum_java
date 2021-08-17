package com.jccdex.rpc.core.types.shamap;

import com.jccdex.rpc.core.types.known.sle.LedgerEntry;

public interface LedgerEntryVisitor {
    public void onEntry(LedgerEntry entry);
}
