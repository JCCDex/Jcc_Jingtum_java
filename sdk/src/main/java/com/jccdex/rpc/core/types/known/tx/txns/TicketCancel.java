package com.jccdex.rpc.core.types.known.tx.txns;

import com.jccdex.rpc.core.coretypes.hash.Hash256;
import com.jccdex.rpc.core.serialized.enums.TransactionType;
import com.jccdex.rpc.core.types.known.tx.Transaction;

public class TicketCancel extends Transaction {
    public TicketCancel() {
        super(TransactionType.TicketCancel);
    }

    public TicketCancel(Boolean guomi) {
        super(TransactionType.TicketCancel, guomi);
    }

    public Hash256 ticketID() {
        return get(Hash256.TicketID);
    }
    public void ticketID(Hash256 id) {
        put(Hash256.TicketID, id);
    }
}
