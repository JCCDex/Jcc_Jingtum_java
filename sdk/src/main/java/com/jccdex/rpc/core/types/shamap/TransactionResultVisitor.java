package com.jccdex.rpc.core.types.shamap;
import com.jccdex.rpc.core.types.known.tx.result.TransactionResult;

public interface TransactionResultVisitor {
    public void onTransaction(TransactionResult tx);
}
