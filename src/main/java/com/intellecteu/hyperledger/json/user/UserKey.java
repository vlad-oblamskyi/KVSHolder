package com.intellecteu.hyperledger.json.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by denis on 9/14/16.
 */
public class UserKey {
    @JsonProperty("type")
    private String type;

    @JsonProperty("login")
    private String login;

    @JsonProperty("bic")
    private String bic;

     UserKey(String login, String bic) {
        this.type = "user";
        this.login = login;
        this.bic = bic;
    }

    UserKey() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getType() {
        return type;
    }

    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserKey userKey = (UserKey) o;

        if (!type.equals(userKey.type))
            return false;
        if (!login.equals(userKey.login))
            return false;
        return bic.equals(userKey.bic);

    }

    @Override public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + bic.hashCode();
        return result;
    }
}
