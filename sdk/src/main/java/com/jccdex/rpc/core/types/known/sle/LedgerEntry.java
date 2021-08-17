package com.jccdex.rpc.core.types.known.sle;

import com.jccdex.rpc.core.coretypes.AccountID;
import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.coretypes.STObject;
import com.jccdex.rpc.core.coretypes.hash.Hash256;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.fields.Field;
import com.jccdex.rpc.core.formats.LEFormat;
import com.jccdex.rpc.core.serialized.enums.LedgerEntryType;
import com.jccdex.rpc.core.types.known.sle.entries.AccountRoot;
import com.jccdex.rpc.core.types.known.sle.entries.DirectoryNode;
import com.jccdex.rpc.core.types.known.sle.entries.Offer;
import com.jccdex.rpc.core.types.known.sle.entries.RippleState;

import java.util.TreeSet;

public class LedgerEntry extends STObject {
    public LedgerEntry(LedgerEntryType type) {
        setFormat(LEFormat.formats.get(type));
        put(Field.LedgerEntryType, type);
    }

    public LedgerEntryType ledgerEntryType() {return ledgerEntryType(this);}
    public Hash256 index() { return get(Hash256.index); }
    public UInt32 flags() {return get(UInt32.Flags);}
    public Hash256 ledgerIndex() {return get(Hash256.LedgerIndex);}

//    public void ledgerEntryType(UInt16 val) {put(Field.LedgerEntryType, val);}
//    public void ledgerEntryType(LedgerEntryType val) {put(Field.LedgerEntryType, val);}
    public void flags(UInt32 val) {put(Field.Flags, val);}
    public void ledgerIndex(Hash256 val) {put(Field.LedgerIndex, val);}

    public TreeSet<AccountID> owners() {
        TreeSet<AccountID> owners = new TreeSet<AccountID>();

        if (has(Field.LowLimit)) {
            owners.add(get(Amount.LowLimit).issuer());
        }
        if (has(Field.HighLimit)) {
            owners.add(get(Amount.HighLimit).issuer());
        }
        if (has(Field.Account)) {
            owners.add(get(AccountID.Account));
        }

        return owners;
    }

    public void index(Hash256 index) {
        put(Hash256.index, index);
    }

    public void setDefaults() {
        if (flags() == null) {
            flags(UInt32.ZERO);
        }
    }

    public static abstract class OnLedgerEntry {
        public abstract void onOffer(Offer of);
        public abstract void onDirectoryNode(DirectoryNode dn);
        public abstract void onRippleState(RippleState rs);
        public abstract void onAccountRoot(AccountRoot ar);
        public abstract void onAll(LedgerEntry le);

        public void onObject(STObject object) {
            if (object instanceof Offer) {
                onOffer(((Offer) object));
            } else if (object instanceof AccountRoot) {
                onAccountRoot((AccountRoot) object);
            } else if (object instanceof DirectoryNode) {
                onDirectoryNode((DirectoryNode) object);
            } else if (object instanceof RippleState) {
                onRippleState((RippleState) object);
            }
            onAll((LedgerEntry) object);
        }
    }
}
