package com.intellecteu.hyperledger.json.user;

import com.google.common.base.Preconditions;
import com.intellecteu.hyperledger.chaincode.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

public class User {
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    UserKey key;
    UserValue details;

    public User(String bic, String login, String permissions) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(bic), "BIC can't be null or empty");
        Preconditions.checkArgument(StringUtils.isNotEmpty(login), "Login can't be null or empty");
        Preconditions.checkArgument(StringUtils.isNotEmpty(permissions), "Permissions can't be null or empty");
        try {
            String decodedPermissions = Utils.base64Decode(permissions);
            UserValue userDetails = UserValueFactory.getJsonObject(decodedPermissions, UserValue.class);
            if (userDetails == null)
                throw new IllegalArgumentException("Unable to extract parameters from " + permissions);

            key = new UserKey(login, bic);
            details = userDetails;

        } catch (Exception ex) {
            LOGGER.error("Exception occurred while creating user from bic: {}, login: {}, permissions: {}",
                    bic, login, permissions, ex);
            throw new IllegalArgumentException(ex);
        }
    }

    public String getKey() throws IOException {
        return key.toJson();
    }

    public String getDetails() throws IOException {
        return details.toJson();
    }
}
