package com.intellecteu.hyperledger.json.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.intellecteu.hyperledger.chaincode.common.AccountDAO;
import com.intellecteu.hyperledger.json.ObjectFactory;
import com.intellecteu.hyperledger.json.account.Account;

import java.util.Set;
import java.util.TreeSet;

public class AccountTransaction {
    private Account.StorageKey accountKey;
    private Set<String> transactions;

    public AccountTransaction() {
        transactions = new TreeSet<>();
    }

    public boolean addTransaction(String transactionId) {
        return transactions.add(transactionId);
    }

    public Account.StorageKey getAccountKey() {
        return accountKey;
    }

    public Set<String> getTransactions() {
        return transactions;
    }

    public AccountTransaction(Account.StorageKey accountKey) {
        this.accountKey = new Account.StorageKey(accountKey.getHolder(), accountKey.getOwner(), accountKey.getCurrency(), accountKey.getAccountType());
        this.accountKey.setType("account_transaction");
        this.transactions = new TreeSet<>();
    }

    public String toJsonKey() throws JsonProcessingException {
        return AccountDAO.serializeAccountKey(getAccountKey());
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public String toJsonTransactions() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.getTransactions());
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AccountTransaction that = (AccountTransaction) o;

        if (!accountKey.equals(that.accountKey))
            return false;
        return transactions.equals(that.transactions);

    }

    public void setTransactions(String json) {
        this.transactions = ObjectFactory.getJsonObject(json, TreeSet.class);
        Preconditions.checkArgument(transactions != null, "Unable to deserialize transactions");
    }

    @Override public int hashCode() {
        int result = accountKey.hashCode();
        result = 31 * result + transactions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accountKey", accountKey)
                .add("transactions", transactions)
                .toString();
    }
}
