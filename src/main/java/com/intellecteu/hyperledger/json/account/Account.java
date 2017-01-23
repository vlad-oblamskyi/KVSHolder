package com.intellecteu.hyperledger.json.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Class is used as com.intellecteu.hyperledger.json mapper for account entity in KVS storage
 */
public class Account {

    public static class StorageKey {
        @JsonProperty("type")
        private String type;
        @JsonProperty("holder")
        private String holder;
        @JsonProperty("owner")
        private String owner;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("accountType")
        private String accountType;

        public StorageKey(String holder,
                String owner,
                String currency,
                String accountType) {
            this.type = "account";
            this.holder = holder;
            this.owner = owner;
            this.currency = currency;
            this.accountType = accountType;
        }

        StorageKey() {
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public String getHolder() {
            return holder;
        }

        public String getCurrency() {
            return currency;
        }

        public String getAccountType() {
            return accountType;
        }

        public String getOwner() {
            return owner;
        }

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            StorageKey that = (StorageKey) o;

            return type.equals(that.type) && owner.equals(that.owner) && holder.equals(that.holder) && currency.equals(that.currency) && accountType
                    .equals(that.accountType);

        }



        @Override public String toString() {
            final StringBuffer sb = new StringBuffer("StorageKey{");
            sb.append("type='").append(type).append('\'');
            sb.append(", holder='").append(holder).append('\'');
            sb.append(", owner='").append(owner).append('\'');
            sb.append(", currency='").append(currency).append('\'');
            sb.append(", accountType='").append(accountType).append('\'');
            sb.append('}');
            return sb.toString();
        }

        @Override public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + owner.hashCode();
            result = 31 * result + holder.hashCode();
            result = 31 * result + currency.hashCode();
            result = 31 * result + accountType.hashCode();
            return result;
        }
    }

    public static final class StorageValue {
        public final String amount;
        public final String currency;
        public final String type;
        public final String lastActivity;
        public final String number;

        @JsonCreator
        public StorageValue(@JsonProperty("amount") String amount,
                @JsonProperty("currency") String currency,
                @JsonProperty("type") String type,
                @JsonProperty("lastActivity") String lastActivity,
                @JsonProperty("number") String number
        ) {
            this.amount = amount;
            this.currency = currency;
            this.type = type;
            this.lastActivity = lastActivity;
            this.number = number;
        }
    }

    public static final class UserAccounts {
        public final List<ViewValue> accounts;

        @JsonCreator
        public UserAccounts(@JsonProperty("accounts") List<ViewValue> accounts) {
            this.accounts = accounts;
        }
    }

    public static final class ViewValue {
        public final String id;
        public final String bic;
        public final String number;
        public final String amount;
        public final String currency;
        public final String type;
        public final String lastActivity;
        public final String permissions;

        @JsonCreator
        public ViewValue(
                @JsonProperty("id") String id,
                @JsonProperty("bic") String bic,
                @JsonProperty("number") String number,
                @JsonProperty("amount") String amount,
                @JsonProperty("currency") String currency,
                @JsonProperty("type") String type,
                @JsonProperty("lastActivity") String lastActivity,
                @JsonProperty("permissions") String permissions) {
            this.id = id;
            this.bic = bic;
            this.number = number;
            this.amount = amount;
            this.currency = currency;
            this.type = type;
            this.lastActivity = lastActivity;
            this.permissions = permissions;
        }
    }

    public static class AccountType {

        public static final String NOSTRO = "nostro";
        public static final String VOSTRO = "vostro";
    }
}
