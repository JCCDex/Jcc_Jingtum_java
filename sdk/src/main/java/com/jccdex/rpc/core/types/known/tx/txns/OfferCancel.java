package com.jccdex.rpc.core.types.known.tx.txns;

import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.fields.Field;
import com.jccdex.rpc.core.serialized.enums.TransactionType;
import com.jccdex.rpc.core.types.known.tx.Transaction;

public class OfferCancel extends Transaction {

    public OfferCancel() {
        super(TransactionType.OfferCancel);
    }

    public OfferCancel(Boolean guomi) {
        super(TransactionType.OfferCancel, guomi);
    }
    
    public UInt32 offerSequence() {return get(UInt32.OfferSequence);}
    public void offerSequence(UInt32 val) {put(Field.OfferSequence, val);}
}
