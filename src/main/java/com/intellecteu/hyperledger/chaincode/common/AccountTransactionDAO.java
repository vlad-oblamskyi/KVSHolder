package com.intellecteu.hyperledger.chaincode.common;

import com.intellecteu.hyperledger.json.account.Account;
import com.intellecteu.hyperledger.json.transaction.AccountTransaction;
import org.hyperledger.java.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.intellecteu.hyperledger.chaincode.common.Utils.getJsonStringFromChaincodeResponse;

public class AccountTransactionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountTransactionDAO.class);

    public static String putAccountTransactionInKVS(ChaincodeStub stub, String mapChainCodeId, AccountTransaction accountTransaction) {

        try {
            String key = accountTransaction.toJsonKey();
            String value = accountTransaction.toJsonTransactions();
            return KVSHolder.put(stub, mapChainCodeId, key, value);
        } catch (Exception ex) {
            LOGGER.error("Unable to save account transactions", ex);
        }
        return null;
    }

    public static AccountTransaction selectAccountTransactionsByKey(ChaincodeStub stub, String mapChainCodeId, Account.StorageKey accountKey) {
        if (accountKey == null) {
            LOGGER.error("Account key is null");
            return null;
        }

        AccountTransaction accountTransaction = new AccountTransaction(accountKey);

        try {
            LOGGER.info("Select account transactions by the key: {}", accountTransaction.getAccountKey());

            String jsonKey = AccountDAO.serializeAccountKey(accountTransaction.getAccountKey());

            String storedJsonValue = KVSHolder.get(stub, mapChainCodeId, jsonKey);
            if (storedJsonValue == null || storedJsonValue.isEmpty()) {
                LOGGER.info("The result of the query is empty");
                return accountTransaction;
            }

            storedJsonValue = getJsonStringFromChaincodeResponse(storedJsonValue);

            if (!storedJsonValue.isEmpty())
                accountTransaction.setTransactions(storedJsonValue);

            return accountTransaction;

        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching results for accountKey: {}", ex);
        }
        return null;
    }
}
