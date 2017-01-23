package com.intellecteu.hyperledger.json.transaction.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.intellecteu.hyperledger.json.transaction.Transaction;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sender",
        "receiver",
        "amount",
        "currency",
        "type"
})
public class Transfer {

    @JsonProperty("sender")
    private Transaction.Organization sender;
    @JsonProperty("receiver")
    private Transaction.Organization receiver;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;

    /**
     * @return The sender
     */
    @JsonProperty("sender")
    public Transaction.Organization getSender() {
        return sender;
    }

    /**
     * @param sender The sender
     */
    @JsonProperty("sender")
    public void setSender(Transaction.Organization sender) {
        this.sender = sender;
    }

    public Transfer withSender(Transaction.Organization sender) {
        this.sender = sender;
        return this;
    }

    /**
     * @return The receiver
     */
    @JsonProperty("receiver")
    public Transaction.Organization getReceiver() {
        return receiver;
    }

    /**
     * @param receiver The receiver
     */
    @JsonProperty("receiver")
    public void setReceiver(Transaction.Organization receiver) {
        this.receiver = receiver;
    }

    public Transfer withReceiver(Transaction.Organization receiver) {
        this.receiver = receiver;
        return this;
    }

    /**
     * @return The amount
     */
    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount The amount
     */
    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }


    /**
     * @return The currency
     */
    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency The currency
     */
    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sender", sender)
                .add("receiver", receiver)
                .add("amount", amount)
                .add("currency", currency)
                .toString();
    }
}
