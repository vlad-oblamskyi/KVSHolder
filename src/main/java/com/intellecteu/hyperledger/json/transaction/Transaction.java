package com.intellecteu.hyperledger.json.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.intellecteu.hyperledger.chaincode.common.Utils;
import com.intellecteu.hyperledger.json.account.Account;

import java.util.Objects;

/**
 * Use to define bank transaction
 */
public class Transaction {
    private String transactionId;
    private Organization sender;
    private Organization receiver;
    private TransferringOrganization transferring;
    private String time;
    private TransactionStatus transactionStatus;
    private Details details;

    public Transaction() {
    }

    public Transaction(Organization sender, Organization receiver, TransferringOrganization transferring, String time, TransactionStatus transactionStatus,
            Details details) {
        this.sender = sender;
        this.receiver = receiver;
        this.transferring = transferring;
        this.time = time;
        this.transactionStatus = transactionStatus;
        this.details = details;
    }

    public static final class Details {
        private String inputMessage;
        private String outputMessage;

        public Details(){}
        public Details(String inputMessage) {
            this.inputMessage = inputMessage;
        }

        public Details(String inputMessage, String outputMessage) {
            this.inputMessage = inputMessage;
            this.outputMessage = outputMessage;
        }

        public void setInputMessage(String inputMessage) {
            this.inputMessage = inputMessage;
        }

        public void setOutputMessage(String outputMessage) {
            this.outputMessage = outputMessage;
        }

        public String getInputMessage() {
            return inputMessage;
        }

        public String getOutputMessage() {
            return outputMessage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Details)) {
                return false;
            }
            Details details = (Details) o;
            return Objects.equals(getInputMessage(), details.getInputMessage()) &&
                    Objects.equals(getOutputMessage(), details.getOutputMessage());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getInputMessage(), getOutputMessage());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("inputMessage", inputMessage)
                    .add("outputMessage", outputMessage)
                    .toString();
        }
    }

    public static final class TransactionStatus {

        private Status status;
        private String comment;

        public TransactionStatus(){}

        public TransactionStatus(Status status, String comment) {
            this.status = status;
            this.comment = comment;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public enum Status {
            SUCCESS,
            FAILURE
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TransactionStatus)) {
                return false;
            }
            TransactionStatus status1 = (TransactionStatus) o;
            return getStatus() == status1.getStatus() &&
                    Objects.equals(getComment(), status1.getComment());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getStatus(), getComment());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("status", status)
                    .add("comment", comment)
                    .toString();
        }
    }

    public static final class AccountState {
        private String amount;
        private String currency;

        public AccountState(){}

        public AccountState(String amount, String currency) {
            this.amount = amount;
            this.currency = currency;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AccountState)) {
                return false;
            }
            AccountState that = (AccountState) o;
            return Objects.equals(getAmount(), that.getAmount()) &&
                    Objects.equals(getCurrency(), that.getCurrency());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getAmount(), getCurrency());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("amount", amount)
                    .add("currency", currency)
                    .toString();
        }
    }

    public static class Organization {
        private String bic;
        private String account;

        public Organization(){
        }

        public String getBic() {
            return bic;
        }

        public void setBic(String bic) {
            this.bic = bic;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public Organization(String bic, String account) {
            this.bic = bic;
            this.account = account;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Organization)) {
                return false;
            }
            Organization that = (Organization) o;
            return Objects.equals(getBic(), that.getBic()) &&
                    Objects.equals(getAccount(), that.getAccount());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getBic(), getAccount());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("bic", bic)
                    .add("account", account)
                    .toString();
        }
    }

    public static final class AccountDetail {
        private Account.StorageKey accountKey;
        private AccountState accountState;

        public AccountDetail(){
        }

        public AccountDetail(Account.StorageKey accountKey) {
            this.accountKey = accountKey;
        }

        public AccountDetail(Account.StorageKey accountKey, AccountState accountState) {
            this.accountKey = accountKey;
            this.accountState = accountState;
        }

        public Account.StorageKey getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(Account.StorageKey accountKey) {
            this.accountKey = accountKey;
        }

        public AccountState getAccountState() {
            return accountState;
        }

        public void setAccountState(AccountState accountState) {
            this.accountState = accountState;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AccountDetail)) {
                return false;
            }
            AccountDetail that = (AccountDetail) o;
            return Objects.equals(getAccountKey(), that.getAccountKey()) &&
                    Objects.equals(getAccountState(), that.getAccountState());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getAccountKey(), getAccountState());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("accountKey", accountKey)
                    .add("accountState", accountState)
                    .toString();
        }
    }

    public static final class TransferringOrganization extends Organization {
        private AccountDetail senderAccount;
        private AccountDetail receiverAccount;
        private String fee;
        private String amount;

        public TransferringOrganization(){

        }

        public AccountDetail getSenderAccount() {
            return senderAccount;
        }

        public void setSenderAccount(AccountDetail senderAccount) {
            this.senderAccount = senderAccount;
        }

        public AccountDetail getReceiverAccount() {
            return receiverAccount;
        }

        public void setReceiverAccount(AccountDetail receiverAccount) {
            this.receiverAccount = receiverAccount;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public TransferringOrganization(String bic, AccountDetail senderAccount,
                AccountDetail receiverAccount, String fee, String amount) {
            super(bic, null);
            this.senderAccount = senderAccount;
            this.receiverAccount = receiverAccount;
            this.fee = fee;
            this.amount = amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TransferringOrganization)) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            TransferringOrganization that = (TransferringOrganization) o;
            return Objects.equals(getSenderAccount(), that.getSenderAccount()) &&
                    Objects.equals(getReceiverAccount(), that.getReceiverAccount()) &&
                    Objects.equals(getFee(), that.getFee()) &&
                    Objects.equals(getAmount(), that.getAmount());
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), getSenderAccount(), getReceiverAccount(), getFee(), getAmount());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("senderAccount", senderAccount)
                    .add("receiverAccount", receiverAccount)
                    .add("fee", fee)
                    .add("amount", amount)
                    .toString();
        }
    }

    public Organization getSender() {
        return sender;
    }

    public void setSender(Organization sender) {
        this.sender = sender;
    }

    public Organization getReceiver() {
        return receiver;
    }

    public void setReceiver(Organization receiver) {
        this.receiver = receiver;
    }

    public TransferringOrganization getTransferring() {
        return transferring;
    }

    public void setTransferring(TransferringOrganization transferring) {
        this.transferring = transferring;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        Transaction that = (Transaction) o;
        return Objects.equals(getSender(), that.getSender()) &&
                Objects.equals(getReceiver(), that.getReceiver()) &&
                Objects.equals(getTransferring(), that.getTransferring()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getTransactionStatus(), that.getTransactionStatus()) &&
                Objects.equals(getDetails(), that.getDetails());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getReceiver(), getTransferring(), getTime(), getTransactionStatus(), getDetails());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sender", sender)
                .add("receiver", receiver)
                .add("transferring", transferring)
                .add("time", time)
                .add("transactionStatus", transactionStatus)
                .add("details", details)
                .toString();
    }
}
