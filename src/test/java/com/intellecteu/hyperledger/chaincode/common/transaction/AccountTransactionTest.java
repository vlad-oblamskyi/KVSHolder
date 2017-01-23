package com.intellecteu.hyperledger.chaincode.common.transaction;

import com.intellecteu.hyperledger.json.account.Account;
import com.intellecteu.hyperledger.json.transaction.AccountTransaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by denis on 9/21/16.
 */
public class AccountTransactionTest {
    private final static String expectedJSON = "{\"accountKey\":{\"type\":\"account_transaction\",\"holder\":\"CITIUS33\",\"owner\":\"SPXBUAUK\",\"currency\":\"USD\",\"accountType\":\"nostro\"},\"transactions\":[\"1\",\"2\"]}";

    AccountTransaction accountTransaction;

    @Before
    public void setUp() {
        Account.StorageKey key = new Account.StorageKey("CITIUS33", "SPXBUAUK", "USD", "nostro");
        accountTransaction = new AccountTransaction(key);

        accountTransaction.addTransaction("1");
        accountTransaction.addTransaction("2");
        accountTransaction.addTransaction("1");

    }

    @Test
    public void testSerialiaze() throws Exception {
        assertEquals(expectedJSON, accountTransaction.toJson());
    }

    @Test
    public void initTransactions() throws Exception {
        String transactions = "[\"1\",\"2\"]";
        AccountTransaction transaction = new AccountTransaction();
        transaction.setTransactions(transactions);

        assertEquals(2, transaction.getTransactions().size());
        assertEquals(accountTransaction.getTransactions(), transaction.getTransactions());
        assertEquals(true, transaction.getTransactions().contains("1"));
        assertEquals(true, transaction.getTransactions().contains("2"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void initTransactionsExceptions() throws Exception {
        String transactions = "[";
        AccountTransaction transaction = new AccountTransaction();
        transaction.setTransactions(transactions);

    }
}
