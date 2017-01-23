package com.intellecteu.hyperledger.chaincode.common;

import com.google.protobuf.ByteString;
import com.intellecteu.hyperledger.json.user.UserKey;
import com.intellecteu.hyperledger.json.user.UserKeyFactory;
import com.intellecteu.hyperledger.json.user.UserValue;
import com.intellecteu.hyperledger.json.user.UserValueFactory;
import org.hyperledger.java.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Collections;

import static com.intellecteu.hyperledger.chaincode.common.Utils.getJsonStringFromChaincodeResponse;


public class UserDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    private UserDAO() {
    }

    private static UserValue selectUserValueFromKVS(ChaincodeStub stub, String mapChaincodeId, UserKey userKey) {
        if (userKey == null) {
            LOGGER.warn("User key was not provided");
            return null;
        }
        try {
            LOGGER.info("Select user by the key: {}", userKey.toJson());
            String storedJsonValue = stub.queryChaincode(mapChaincodeId,
                    "function", Collections.singletonList(ByteString.copyFromUtf8(userKey.toJson())));

            if (storedJsonValue == null || storedJsonValue.isEmpty()) {
                LOGGER.info("The result of the query is empty");
                return null;
            }

            storedJsonValue = getJsonStringFromChaincodeResponse(storedJsonValue);
            UserValue userValue = UserValueFactory.getJsonObject(storedJsonValue, UserValue.class);

            if (userValue == null) {
                LOGGER.warn("Can't parse selected result data into userValue: {}", storedJsonValue);
                return null;
            }

            return userValue;
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching results for userKey: {}", userKey, ex);
        }
        return null;
    }

    /**
     * Retrieves user value from key-value storage using auth token.
     *
     * @param stub           shim layer to work with chaincode
     * @param mapChaincodeId chaincode id to query KVS
     * @param authToken      auth token
     * @return {@link UserValue} object if found or {@code null} otherwise.
     */
    public static UserValue selectUserValueByAuthToken(ChaincodeStub stub, String mapChaincodeId, String authToken) {
        String jsonValue = Utils.base64Decode(authToken);
        UserKey userKey = UserKeyFactory.getJsonObject(jsonValue, UserKey.class);
        return selectUserValueFromKVS(stub, mapChaincodeId, userKey);
    }

    public static UserValue selectUserValueByBicAndLogin(ChaincodeStub stub, String mapChaincodeId, String bic, String login) {
        UserKey userKey = UserKeyFactory.getUserKey(bic, login);
        return selectUserValueFromKVS(stub, mapChaincodeId, userKey);
    }


}
