package com.jccdex.rpc.core.types.known.tx.txns;

import com.jccdex.rpc.core.coretypes.AccountID;
import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.coretypes.Blob;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.fields.Field;
import com.jccdex.rpc.core.serialized.enums.TransactionType;
import com.jccdex.rpc.core.types.known.tx.Transaction;

import java.math.BigDecimal;

public class TokenIssue extends Transaction {

    public TokenIssue() {
        super(TransactionType.TokenIssue);
    }

    public TokenIssue(Boolean guomi) {
        super(TransactionType.TokenIssue, guomi);
    }

    public AccountID issuer() {
        return get(AccountID.Issuer);
    }

    public void issuer(AccountID val) { put(Field.Issuer, val); }

    public UInt32 tokenSize() { return get(UInt32.TokenSize); }

    public void tokenSize(UInt32 val) { put(Field.TokenSize, val); }

    public Blob fundCode() { return get(Blob.FundCode); }

    public void fundCode(Blob val) { put(Field.FundCode, val); }

}
