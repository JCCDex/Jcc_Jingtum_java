package com.jccdex.rpc.core.types.known.tx.txns;

import com.jccdex.rpc.core.coretypes.AccountID;
import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.coretypes.PathSet;
import com.jccdex.rpc.core.coretypes.hash.Hash256;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.fields.Field;
import com.jccdex.rpc.core.serialized.enums.TransactionType;
import com.jccdex.rpc.core.types.known.tx.Transaction;

public class Payment extends Transaction {
	public Payment() {
		super(TransactionType.Payment);
	}
	
	
	public UInt32 destinationTag() {
		return get(UInt32.DestinationTag);
	}
	
	public Hash256 invoiceID() {
		return get(Hash256.InvoiceID);
	}
	
	public Amount amount() {
		return get(Amount.Amount);
	}
	
	public Amount sendMax() {
		return get(Amount.SendMax);
	}
	
	public AccountID destination() {
		return get(AccountID.Destination);
	}
	
	public PathSet paths() {
		return get(PathSet.Paths);
	}
	
	public void destinationTag(UInt32 val) {
		put(Field.DestinationTag, val);
	}
	
	public void invoiceID(Hash256 val) {
		put(Field.InvoiceID, val);
	}
	
	public void amount(Amount val) {
		put(Field.Amount, val);
	}
	
	public void sendMax(Amount val) {
		put(Field.SendMax, val);
	}
	
	public void destination(AccountID val) {
		put(Field.Destination, val);
	}
	
	public void paths(PathSet val) {
		put(Field.Paths, val);
	}
	
}
