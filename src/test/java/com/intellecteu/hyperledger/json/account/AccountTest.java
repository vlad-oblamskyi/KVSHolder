package com.intellecteu.hyperledger.json.account;

import java.io.IOException;

import com.intellecteu.hyperledger.json.ObjectFactory;
import org.junit.Assert;
import org.junit.Test;

import static com.intellecteu.hyperledger.json.account.Account.AccountType.NOSTRO;

public class AccountTest {

    @Test
    public void deSerializeKeyTest() throws IOException {
        String expectedJSON = "{\"type\":\"account\",\"holder\":\"CITIUS33\",\"owner\":\"SPXBUAUK\",\"currency\":\"USD\",\"accountType\":\"nostro\"}";
        Account.StorageKey accountKey = new Account.StorageKey("CITIUS33", "SPXBUAUK", "USD", NOSTRO);
        Assert.assertEquals(accountKey, ObjectFactory.getJsonObject(expectedJSON, Account.StorageKey.class));
    }
}