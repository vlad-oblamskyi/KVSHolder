package com.intellecteu.hyperledger.chaincode.common.transaction;

import com.intellecteu.hyperledger.chaincode.common.Utils;
import com.intellecteu.hyperledger.json.ObjectFactory;
import com.intellecteu.hyperledger.json.account.Account;
import com.intellecteu.hyperledger.json.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import static com.intellecteu.hyperledger.json.account.Account.AccountType.NOSTRO;
import static com.intellecteu.hyperledger.json.account.Account.AccountType.VOSTRO;
import static junit.framework.TestCase.assertEquals;

public class TransactionTest {

    private final static String transactionJSON = "{\"transactionId\":null,\"sender\":{\"bic\":\"SPXBUAUK\",\"account\":\"1234\"},\"receiver\":{\"bic\":\"SPXBUAU0\",\"account\":\"4321\"},\"transferring\":{\"bic\":\"CITIUS33\",\"account\":null,\"senderAccount\":{\"accountKey\":{\"type\":\"account\",\"holder\":\"CITIUS33\",\"owner\":\"SPXBUAUK\",\"currency\":\"USD\",\"accountType\":\"nostro\"},\"accountState\":{\"amount\":\"10500\",\"currency\":\"USD\"}},\"receiverAccount\":{\"accountKey\":{\"type\":\"account\",\"holder\":\"CITIUS33\",\"owner\":\"SPXBUAU0\",\"currency\":\"USD\",\"accountType\":\"vostro\"},\"accountState\":{\"amount\":\"1500\",\"currency\":\"USD\"}},\"fee\":\"50\",\"amount\":\"500\"},\"time\":\"now\",\"transactionStatus\":{\"status\":\"SUCCESS\",\"comment\":\"aW50ZXJuYWwgY29tbWVudHM=\"},\"details\":{\"inputMessage\":\"aW5wdXQgbWVzc2FnZQ==\",\"outputMessage\":\"b3V0cHV0IG1lc3NhZ2U=\"}}";

    Transaction transaction;

    @Before
    public void setUp() {
        //Sender
        Transaction.Organization sender = new Transaction.Organization("SPXBUAUK", "1234");
        Account.StorageKey senderAccount = new Account.StorageKey("CITIUS33", "SPXBUAUK", "USD", NOSTRO);
        Transaction.AccountState senderAccountState = new Transaction.AccountState("10500", "USD");
        Transaction.AccountDetail senderAccountDetail = new Transaction.AccountDetail(senderAccount, senderAccountState);

        //Receiver
        Transaction.Organization receiver = new Transaction.Organization("SPXBUAU0", "4321");
        Account.StorageKey receiverAccount = new Account.StorageKey("CITIUS33", "SPXBUAU0", "USD", VOSTRO);
        Transaction.AccountState receiverAccountState = new Transaction.AccountState("1500", "USD");
        Transaction.AccountDetail receiverAccountDetail = new Transaction.AccountDetail(receiverAccount, receiverAccountState);

        //Transaction
        Transaction.TransferringOrganization transferringOrganization = new Transaction.TransferringOrganization("CITIUS33", senderAccountDetail,
                receiverAccountDetail, "50", "500");
        Transaction.TransactionStatus status = new Transaction.TransactionStatus(Transaction.TransactionStatus.Status.SUCCESS,
                Utils.base64Encode("internal comments"));
        Transaction.Details details = new Transaction.Details(Utils.base64Encode("input message"),
                Utils.base64Encode("output message"));
        
        transaction = new Transaction(sender, receiver, transferringOrganization, "now", status, details);

    }

    @Test
    public void testTransactionSerialization() throws Exception {
        assertEquals(transactionJSON, transaction.toJson());
    }

    @Test
    public void testTransactionDeserialization() throws Exception {
        assertEquals(transaction, ObjectFactory.getJsonObject(transactionJSON, Transaction.class));
    }
}
