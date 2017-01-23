package com.intellecteu.hyperledger.chaincode.common;

import com.intellecteu.hyperledger.json.user.UserKey;
import com.intellecteu.hyperledger.json.user.UserKeyFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by denis on 9/14/16.
 */

public class UserKeyTest {

    private final static String expectedJSON = "{\"type\":\"user\",\"login\":\"super\",\"bic\":\"SPXBUAUK\"}";

    UserKey userKey;

    @Before
    public void setUp() throws Exception {
        userKey = UserKeyFactory.getUserKey("SPXBUAUK", "super");
    }

    @Test
    public void serializeTest() throws IOException {
        assertEquals(expectedJSON, userKey.toJson());
    }

    @Test
    public void deSerializeTest() throws IOException {
        assertEquals(userKey, UserKeyFactory.getJsonObject(expectedJSON, UserKey.class));
    }

}
