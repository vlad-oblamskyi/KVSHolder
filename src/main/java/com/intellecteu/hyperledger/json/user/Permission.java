package com.intellecteu.hyperledger.json.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.intellecteu.hyperledger.json.account.Account;

/**
 * Use to define account permissions
 * <p>
 * {@link com.intellecteu.hyperledger.json.account.Account.StorageKey}
 */
public class Permission {

    private static final String transferPermission = "transfer";

    @JsonProperty("accountKey")
    private Account.StorageKey key;
    @JsonProperty("access")
    private String accessType;

    public boolean hasTransferPermission() {
        return accessType.equalsIgnoreCase(transferPermission);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Permission that = (Permission) o;

        if (!key.equals(that.key))
            return false;
        return accessType.equals(that.accessType);

    }

    public Permission(Account.StorageKey key, String accessType) {
        this.key = key;
        this.accessType = accessType;
    }

    public Permission() {
    }

    @Override public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + accessType.hashCode();
        return result;
    }

    public Account.StorageKey getKey() {
        return key;
    }

    public void setKey(Account.StorageKey key) {
        this.key = key;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
}
