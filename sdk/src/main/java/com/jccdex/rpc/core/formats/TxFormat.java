package com.jccdex.rpc.core.formats;

import java.util.EnumMap;

import com.jccdex.rpc.core.fields.Field;
import com.jccdex.rpc.core.serialized.enums.TransactionType;

public class TxFormat extends Format {
    static public EnumMap<TransactionType, TxFormat> formats = new EnumMap<TransactionType, TxFormat>(TransactionType.class);
    public final TransactionType transactionType;

    static public TxFormat fromString(String name) {
        return getTxFormat(TransactionType.valueOf(name));
    }

    static public TxFormat fromNumber(Number ord) {
        return getTxFormat(TransactionType.fromNumber(ord));
    }

    static public TxFormat fromValue(Object o) {
        if (o instanceof Number) {
            return fromNumber(((Number) o).intValue());
        } else if (o instanceof String){
            return fromString((String) o);
        }
        else {
            return null;
        }
    }

    private static TxFormat getTxFormat(TransactionType key) {
        if (key == null) {
			return null;
		}
        return formats.get(key);
    }

    public TxFormat(TransactionType type, Object... args) {
        super(args);
        transactionType = type;
        addCommonFields();
        formats.put(transactionType, this);
    }

    @Override
    public void addCommonFields() {
        put(Field.TransactionType,     Requirement.REQUIRED);//1
        put(Field.Flags,               Requirement.OPTIONAL);//2
        put(Field.SourceTag,           Requirement.OPTIONAL);//3
        put(Field.LastLedgerSequence,  Requirement.OPTIONAL);//4
        put(Field.Account,             Requirement.REQUIRED);//5
        put(Field.Sequence,            Requirement.OPTIONAL);//6
        put(Field.Fee,                 Requirement.REQUIRED);//7
        put(Field.OperationLimit,      Requirement.OPTIONAL);//8
        put(Field.SigningPubKey,       Requirement.OPTIONAL);//9
        put(Field.TxnSignature,        Requirement.OPTIONAL);//10
        
     
        put(Field.PreviousTxnID,       Requirement.OPTIONAL);
        put(Field.AccountTxnID,        Requirement.OPTIONAL);
        put(Field.Memos,               Requirement.OPTIONAL);
        put(Field.Platform,            Requirement.OPTIONAL);

    }

    @Override
    public String name() {
        return transactionType.toString();
    }

    static public TxFormat AccountSet = new TxFormat(
            TransactionType.AccountSet,
            Field.EmailHash,       Requirement.OPTIONAL,
            Field.WalletLocator,   Requirement.OPTIONAL,
            Field.WalletSize,      Requirement.OPTIONAL,
            Field.MessageKey,      Requirement.OPTIONAL,
            Field.Domain,          Requirement.OPTIONAL,
            Field.TransferRate,    Requirement.OPTIONAL,
            Field.SetFlag,         Requirement.OPTIONAL,
            Field.ClearFlag,       Requirement.OPTIONAL);

    static public TxFormat TrustSet = new TxFormat(
            TransactionType.TrustSet,
            Field.LimitAmount,     Requirement.OPTIONAL,
            Field.QualityIn,       Requirement.OPTIONAL,
            Field.QualityOut,      Requirement.OPTIONAL);

    static public TxFormat OfferCreate = new TxFormat(
            TransactionType.OfferCreate,
            Field.TakerPays,       Requirement.REQUIRED,
            Field.TakerGets,       Requirement.REQUIRED,
            Field.Expiration,      Requirement.OPTIONAL,
            Field.OfferSequence,   Requirement.OPTIONAL);

    static public TxFormat OfferCancel = new TxFormat(
            TransactionType.OfferCancel,
            Field.OfferSequence,   Requirement.REQUIRED);

    static public TxFormat TicketCreate = new TxFormat(
            TransactionType.TicketCreate,
            Field.Target,     Requirement.OPTIONAL,
            Field.Expiration, Requirement.OPTIONAL);

    static public TxFormat TicketCancel = new TxFormat(
            TransactionType.TicketCancel,
            Field.TicketID,   Requirement.REQUIRED);

    static public TxFormat SetRegularKey = new TxFormat(
            TransactionType.SetRegularKey,
            Field.RegularKey,  Requirement.OPTIONAL);

    static public TxFormat Payment = new TxFormat(
            TransactionType.Payment,
            Field.Destination,     Requirement.REQUIRED,
            Field.Amount,          Requirement.REQUIRED,
            Field.SendMax,         Requirement.OPTIONAL,
            Field.Paths,           Requirement.DEFAULT,
            Field.InvoiceID,       Requirement.OPTIONAL,
            Field.DestinationTag,  Requirement.OPTIONAL);
    
    static public TxFormat RelationSet = new TxFormat(
            TransactionType.RelationSet,
            Field.Target,          	Requirement.REQUIRED,
            Field.LimitAmount,      Requirement.REQUIRED);


    static public TxFormat SuspendedPaymentCreate = new TxFormat(
            TransactionType.SuspendedPaymentCreate,
            Field.Destination,      Requirement.REQUIRED,
            Field.Amount,           Requirement.REQUIRED,
            Field.Digest,           Requirement.OPTIONAL,
            Field.CancelAfter,      Requirement.OPTIONAL,
            Field.FinishAfter,      Requirement.OPTIONAL,
            Field.DestinationTag,   Requirement.OPTIONAL);

    static public TxFormat SuspendedPaymentFinish = new TxFormat(
            TransactionType.SuspendedPaymentFinish,
            Field.Owner,            Requirement.REQUIRED,
            Field.OfferSequence,    Requirement.REQUIRED,
            Field.Method,           Requirement.OPTIONAL,
            Field.Digest,           Requirement.OPTIONAL,
            Field.Proof,            Requirement.OPTIONAL);

    static public TxFormat SuspendedPaymentCancel = new TxFormat(
            TransactionType.SuspendedPaymentCancel,
            Field.Owner,          Requirement.REQUIRED,
            Field.OfferSequence,  Requirement.REQUIRED);

    static public TxFormat EnableAmendment = new TxFormat(
            TransactionType.EnableAmendment,
            Field.Amendment,      Requirement.REQUIRED);

    static public TxFormat SetFee = new TxFormat(
            TransactionType.SetFee,
            Field.BaseFee,              Requirement.REQUIRED,
            Field.ReferenceFeeUnits,    Requirement.REQUIRED,
            Field.ReserveBase,          Requirement.REQUIRED,
            Field.ReserveIncrement,     Requirement.REQUIRED);
}
