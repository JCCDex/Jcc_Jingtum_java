package com.jccdex.rpc.core.types.known.tx.signed;

import java.util.Arrays;

import com.jccdex.core.crypto.ecdsa.IKeyPair;
import com.jccdex.core.crypto.ecdsa.Seed;
import com.jccdex.core.crypto.ecdsa.SeedSM;
import com.jccdex.rpc.core.serialized.BytesList;
import com.jccdex.rpc.core.serialized.MultiSink;
import com.jccdex.core.utils.SM3;
import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.coretypes.Blob;
import com.jccdex.rpc.core.coretypes.STObject;
import com.jccdex.rpc.core.coretypes.hash.HalfSha512;
import com.jccdex.rpc.core.coretypes.hash.Hash256;
import com.jccdex.rpc.core.coretypes.hash.prefixes.HashPrefix;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.serialized.enums.TransactionType;
import com.jccdex.rpc.core.types.known.tx.Transaction;


public class SignedTransaction {
    private SignedTransaction(Transaction of) {
        // TODO: is this just over kill ?
        txn = (Transaction) STObject.translate.fromBytes(of.toBytes());
        this.guomi = of.getGuomi();
    }

    // This will eventually be private
    @Deprecated
    public SignedTransaction() {}

    public Transaction txn;
    public Hash256 hash;

    public byte[] signingData;
    public byte[] previousSigningData;
    public String tx_blob;
    private Boolean guomi = false;

    public void sign(String base58Secret) {
        if(this.guomi) {
            sign(SeedSM.fromBase58(base58Secret).keyPair());
        } else {
            sign(Seed.fromBase58(base58Secret).keyPair());
        }

    }

    public static SignedTransaction fromTx(Transaction tx) {
        return new SignedTransaction(tx);
    }

    public void sign(IKeyPair keyPair) {
        prepare(keyPair, null, null, null);
    }

    public void prepare(IKeyPair keyPair,
                        Amount fee,
                        UInt32 Sequence,
                        UInt32 lastLedgerSequence) {

        Blob pubKey = new Blob(keyPair.canonicalPubBytes());

        // This won't always be specified
        if (lastLedgerSequence != null) {
            txn.put(UInt32.LastLedgerSequence, lastLedgerSequence);
        }
        if (Sequence != null) {
            txn.put(UInt32.Sequence, Sequence);
        }
        if (fee != null) {
            txn.put(Amount.Fee, fee);
        }

        txn.signingPubKey(pubKey);
       

        if (Transaction.CANONICAL_FLAG_DEPLOYED) {
            txn.setCanonicalSignatureFlag();
        }
       
        txn.checkFormat();
        signingData = txn.signingData();
        //System.out.println("------------");
        //System.out.println(JsonUtils.toJsonString(txn));
        if (previousSigningData != null && Arrays.equals(signingData, previousSigningData)) {
            return;
        }
        try {
            txn.txnSignature(new Blob(keyPair.signMessage(signingData)));
            BytesList blob = new BytesList();
            if(this.guomi) {
                SM3 id = SM3.prefixed256(HashPrefix.transactionID.bytes());
                txn.toBytesSink(new MultiSink(blob, id));
                tx_blob = blob.bytesHex();
                hash = new Hash256(id.finish());
            } else {
                HalfSha512 id = HalfSha512.prefixed256(HashPrefix.transactionID);
                txn.toBytesSink(new MultiSink(blob, id));
                tx_blob = blob.bytesHex();
                hash = id.finish();
            }

        } catch (Exception e) {
            // electric paranoia
            previousSigningData = null;
            throw new RuntimeException(e);
        } /*else {*/
        previousSigningData = signingData;
        // }
    }

    public TransactionType transactionType() {
        return txn.transactionType();
    }
}
