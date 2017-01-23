package com.intellecteu.hyperledger.chaincode.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.intellecteu.hyperledger.json.ObjectFactory;
import com.intellecteu.hyperledger.json.account.Account;
import org.hyperledger.java.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static com.intellecteu.hyperledger.chaincode.common.Utils.getJsonStringFromChaincodeResponse;

/**
 * Created by denis on 9/20/16.
 */
public class AccountDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

    private AccountDAO() {

    }

    public static String putAccountInKVS(ChaincodeStub stub, String mapChainCodeId, Account.StorageKey key, Account.StorageValue storageValue) {
        try {
            String accountKey = serializeAccountKey(key);
            String accountValue = serializeAccountValue(storageValue);
            return KVSHolder.put(stub, mapChainCodeId, accountKey, accountValue);
        } catch (Exception ex) {
            LOGGER.error("Unable to save the account");
        }
        return null;

    }

    public static String serializeAccountKey(Account.StorageKey storageKey) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(storageKey);
    }

    public static String serializeAccountValue(Account.StorageValue storageValue) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(storageValue);
    }

    public static Account.StorageValue selectAccountByKey(ChaincodeStub stub, String mapChainCodeId, Account.StorageKey accountKey) {
        if (accountKey == null) {
            LOGGER.error("Account key is null");
            return null;
        }

        try {
            LOGGER.info("Select account by the key: {}", accountKey);

            String jsonKey = serializeAccountKey(accountKey);

            String storedJsonValue = stub.queryChaincode(mapChainCodeId,
                    "function", Collections.singletonList(ByteString.copyFromUtf8(jsonKey)));

            if (storedJsonValue == null || storedJsonValue.isEmpty()) {
                LOGGER.info("The result of the query is empty");
                return null;
            }

            storedJsonValue = getJsonStringFromChaincodeResponse(storedJsonValue);
            Account.StorageValue account = ObjectFactory.getJsonObject(storedJsonValue, Account.StorageValue.class);

            if (account == null) {
                LOGGER.warn("Can't parse selected result data into accountValue: {}", storedJsonValue);
                return null;
            }

            return account;
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching results for accountKey: {}", ex);
        }
        return null;
    }
}
