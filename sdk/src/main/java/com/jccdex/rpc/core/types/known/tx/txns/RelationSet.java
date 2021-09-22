package com.jccdex.rpc.core.types.known.tx.txns;

import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.fields.Field;
import com.jccdex.rpc.core.serialized.enums.TransactionType;
import com.jccdex.rpc.core.types.known.tx.Transaction;

public class RelationSet extends Transaction {

    public RelationSet() {
        super(TransactionType.RelationSet);
    }

    public RelationSet(Boolean guomi) {
        super(TransactionType.RelationSet, guomi);
    }

    public Amount limitAmount() {return get(Amount.LimitAmount);}
    public void limitAmount(Amount val) {put(Field.LimitAmount, val);}
    
}