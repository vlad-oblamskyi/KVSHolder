package com.intellecteu.hyperledger.chaincode.common;

import com.intellecteu.hyperledger.json.ObjectFactory;
import com.intellecteu.hyperledger.json.transaction.Transaction;
import org.hyperledger.java.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.intellecteu.hyperledger.chaincode.common.Utils.getJsonStringFromChaincodeResponse;

public class TransactionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDAO.class);

    public static String putTransaction(ChaincodeStub stub, String mapChainCodeId, Transaction transaction) {
        try {
            String key = transaction.getTransactionId();
            String value = transaction.toJson();
            return KVSHolder.put(stub, mapChainCodeId, key, value);
        } catch (Exception ex) {
            LOGGER.error("Unable to save transaction");
        }
        return null;
    }

    public static Transaction selectTransactionsByKey(ChaincodeStub stub, String mapChainCodeId, String key) {
        Transaction transaction = new Transaction();
        try {
            LOGGER.debug("Select transactions by the key: {}", key);

            String storedValue = KVSHolder.get(stub, mapChainCodeId, key);

            if (storedValue == null || storedValue.isEmpty()) {
                LOGGER.info("The result of the query is empty");
                return transaction;
            }
            storedValue = getJsonStringFromChaincodeResponse(storedValue);
            return ObjectFactory.getJsonObject(storedValue, Transaction.class);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching results for transaction key: {}",key, ex);
        }
        return null;
    }
}
