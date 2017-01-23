package com.intellecteu.hyperledger.chaincode.common;

import com.intellecteu.hyperledger.json.user.User;
import org.junit.Test;

import java.util.Base64;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class UserTest {
    private final static String incomingPermission = "eyJwYXNzd29yZCI6IkFiY2QxMjM0IiwicGVybWlzc2lvbnMiOlt7ImFjY291bnRLZXkiOnsidHlwZSI6ImFjY291bnQiLCJob2xkZXIiOiJDSVRJVVMzMyIsIm93bmVyIjoiU1BYQlVBVUsiLCJjdXJyZW5jeSI6IlVTRCIsImFjY291bnRUeXBlIjoidm9zdHJvIn0sImFjY2VzcyI6InJlYWQifSx7ImFjY291bnRLZXkiOnsidHlwZSI6ImFjY291bnQiLCJob2xkZXIiOiJDSVRJVVMzMyIsIm93bmVyIjoiU1BYQlVBVUsiLCJjdXJyZW5jeSI6IlVTRCIsImFjY291bnRUeXBlIjoidm9zdHJvIn0sImFjY2VzcyI6InJlYWQifV19";

    @Test
    public void deserializeUser() throws Exception {
        User user = new User("SPXBUAUK", "super", incomingPermission);

        assertNotNull(user);
        assertEquals("{\"type\":\"user\",\"login\":\"super\",\"bic\":\"SPXBUAUK\"}", user.getKey());
        assertEquals(Utils.base64Decode(incomingPermission), user.getDetails());
    }

}
