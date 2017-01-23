package com.intellecteu.hyperledger.json.user;

import com.intellecteu.hyperledger.json.ObjectFactory;

public class UserKeyFactory extends ObjectFactory {
    public static UserKey getUserKey(String bic, String login) {
        return new UserKey(login, bic);
    }

}
