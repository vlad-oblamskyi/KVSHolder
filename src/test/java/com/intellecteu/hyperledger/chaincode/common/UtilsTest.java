package com.intellecteu.hyperledger.chaincode.common;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void getJsonStringFromChaincodeResponse() throws Exception {
        Assert.assertEquals("Some wrapping character which occurrs after chaincode form chaincode invokation shall be removed",
                "{\"amount\":\"13045.65\",\"currency\":\"USD\",\"type\":\"nostro\",\"lastActivity\":\"2016-09-15T13:32:11.828\"}",
                Utils.getJsonStringFromChaincodeResponse(
                        "_{\"amount\":\"13045.65\",\"currency\":\"USD\",\"type\":\"nostro\",\"lastActivity\":\"2016-09-15T13:32:11.828\"}\"$71bffc09-1828-4ca1-81a8-d69d61ab2bde"));

        Assert.assertEquals("Array Clean Up",
                "[\"3a8431eb-aaf7-41a8-97fe-71dfdf9c565d\",\"5b560ca8-b0a2-4b12-8897-2820b09320eb\",\"ffb2ea42-89b0-4d56-9628-4f76fd40f568\"]",
                Utils.getJsonStringFromChaincodeResponse(
                        "v[\"3a8431eb-aaf7-41a8-97fe-71dfdf9c565d\",\"5b560ca8-b0a2-4b12-8897-2820b09320eb\",\"ffb2ea42-89b0-4d56-9628-4f76fd40f568\"]\"$ad13715a-9b76-45e8-85aa-c5bb6ca13239"));

        Assert.assertEquals("Valid array",
                "[\"3a8431eb-aaf7-41a8-97fe-71dfdf9c565d\",\"5b560ca8-b0a2-4b12-8897-2820b09320eb\",\"ffb2ea42-89b0-4d56-9628-4f76fd40f568\"]",
                Utils.getJsonStringFromChaincodeResponse(
                        "[\"3a8431eb-aaf7-41a8-97fe-71dfdf9c565d\",\"5b560ca8-b0a2-4b12-8897-2820b09320eb\",\"ffb2ea42-89b0-4d56-9628-4f76fd40f568\"]")
        );

    }

    @Test
    public void testReturnErrorMethod() throws Exception {
        Assert.assertEquals("{\"Error\":\"Incorrect number of arguments. Expecting 3 arguments: bic,login,password\"}",
                Utils.Error("Incorrect number of arguments. Expecting 3 arguments: bic,login,password"));
    }

    @Test
    public void testReturnFailureMethod() throws Exception {
        Assert.assertEquals("{\"status\":\"Failure\",\"message\":\"Incorrect number of arguments. Expecting 3 arguments: bic,login,password\"}",
                Utils.Failure("Incorrect number of arguments. Expecting 3 arguments: bic,login,password"));
    }

    @Test
    public void testReturnAuthTokenMethod() throws Exception {
        Assert.assertEquals("{\"status\":\"OK\",\"authToken\":\"token\"}",
                Utils.AuthToken("token"));
    }

}