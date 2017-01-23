package com.intellecteu.hyperledger.json.transaction.view;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.intellecteu.hyperledger.json.transaction.Transaction.AccountState;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "accountState",
        "transactions"
})
public class TransactionsView {

    @JsonProperty("accountState")
    private AccountState accountState;
    @JsonProperty("transactions")
    private List<TransactionView> transactions = new ArrayList<>();

    /**
     * @return The accountState
     */
    @JsonProperty("accountState")
    public AccountState getAccountState() {
        return accountState;
    }

    /**
     * @param accountState The accountState
     */
    @JsonProperty("accountState")
    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    /**
     * @return The transactions
     */
    @JsonProperty("transactions")
    public List<TransactionView> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions The transactions
     */
    @JsonProperty("transactions")
    public void setTransactions(List<TransactionView> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accountState", accountState)
                .add("transactions", transactions)
                .toString();
    }
}
