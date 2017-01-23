package com.intellecteu.hyperledger.json.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellecteu.hyperledger.json.account.Account;

import java.io.IOException;
import java.util.List;

/**
 * Created by denis on 9/14/16.
 */
public class UserValue {

    @JsonProperty("password")
    private String password;

    @JsonProperty("permissions")
    private List<Permission> permissions;

    UserValue(String password, List<Permission> permissions) {
        this.password = password;
        this.permissions = permissions;
    }

    UserValue() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserValue userValue = (UserValue) o;

        if (!password.equals(userValue.password))
            return false;
        return permissions.equals(userValue.permissions);

    }

    @Override public int hashCode() {
        int result = password.hashCode();
        result = 31 * result + permissions.hashCode();
        return result;
    }

    public boolean allowTransfer(Account.StorageKey storageKey) {
        for (Permission permission : getPermissions()) {
            if (permission.getKey().equals(storageKey))
                return permission.hasTransferPermission();
        }
        return false;
    }

    public boolean hasViewPermission(Account.StorageKey storageKey) {
        for (Permission permission : getPermissions()) {
            if (permission.getKey().equals(storageKey))
                return true;
        }
        return false;
    }
    
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
