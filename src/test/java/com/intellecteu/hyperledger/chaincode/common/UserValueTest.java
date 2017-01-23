package com.intellecteu.hyperledger.chaincode.common;

import com.intellecteu.hyperledger.json.account.Account;
import com.intellecteu.hyperledger.json.user.Permission;
import com.intellecteu.hyperledger.json.user.UserValue;
import com.intellecteu.hyperledger.json.user.UserValueFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class UserValueTest {

    private static final String expectedJSON = "{\"password\":\"Abcd1234\",\"permissions\":[{\"accountKey\":{\"type\":\"account\",\"holder\":\"CITIUS33\",\"owner\":\"SPXBUAUK\",\"currency\":\"USD\",\"accountType\":\"nostro\"},\"access\":\"transfer\"},{\"accountKey\":{\"type\":\"account\",\"holder\":\"CITIUS33\",\"owner\":\"SPXBUAUK\",\"currency\":\"USD\",\"accountType\":\"vostro\"},\"access\":\"read\"}]}";

    UserValue userValue;

    @Before
    public void setUp() throws Exception {

        Account.StorageKey account1 = new Account.StorageKey("CITIUS33", "SPXBUAUK", "USD", "nostro");
        Account.StorageKey account2 = new Account.StorageKey("CITIUS33", "SPXBUAUK", "USD", "vostro");

        Permission accountPermission_1 = new Permission(account1, "transfer");
        Permission accountPermission_2 = new Permission(account2, "read");

        List<Permission> permissions = new ArrayList<>();

        permissions.add(accountPermission_1);
        permissions.add(accountPermission_2);

        userValue = UserValueFactory.getUserValue("Abcd1234", permissions);
    }

    @Test
    public void serializeTest() throws IOException {
        assertEquals(expectedJSON, userValue.toJson());
    }

    @Test
    public void deSerializeTest() throws IOException {
        assertEquals(userValue, UserValueFactory.getJsonObject(expectedJSON, UserValue.class));
    }

}
