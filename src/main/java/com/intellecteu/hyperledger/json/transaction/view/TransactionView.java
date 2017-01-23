package com.intellecteu.hyperledger.json.transaction.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.intellecteu.hyperledger.json.transaction.Transaction;
import com.intellecteu.hyperledger.json.transaction.Transaction.AccountState;
import com.intellecteu.hyperledger.json.transaction.Transaction.Details;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "transfer",
        "time",
        "transactionStatus",
        "accountState",
        "details"
})
public class TransactionView {

    @JsonProperty("id")
    private String id;
    @JsonProperty("transfer")
    private Transfer transfer;
    @JsonProperty("time")
    private String time;
    @JsonProperty("transactionStatus")
    private Transaction.TransactionStatus transactionStatus;
    @JsonProperty("accountState")
    private AccountState accountState;
    @JsonProperty("details")
    private Details details;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The transfer
     */
    @JsonProperty("transfer")
    public Transfer getTransfer() {
        return transfer;
    }

    /**
     * @param transfer The transfer
     */
    @JsonProperty("transfer")
    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    /**
     * @return The time
     */
    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }


    /**
     * @return The transactionStatus
     */
    @JsonProperty("transactionStatus")
    public Transaction.TransactionStatus getStatus() {
        return transactionStatus;
    }

    /**
     * @param transactionStatus The transactionStatus
     */
    @JsonProperty("transactionStatus")
    public void setStatus(Transaction.TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }


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
     * @return The details
     */
    @JsonProperty("details")
    public Details getDetails() {
        return details;
    }

    /**
     * @param details The details
     */
    @JsonProperty("details")
    public void setDetails(Details details) {
        this.details = details;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("transfer", transfer)
                .add("time", time)
                .add("transactionStatus", transactionStatus)
                .add("accountState", accountState)
                .add("details", details)
                .toString();
    }
}
