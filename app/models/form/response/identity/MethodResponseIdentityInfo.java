package models.form.response.identity;

import models.form.response.MethodResponse;
import reference.identity.UserIdentityInfo;

public class MethodResponseIdentityInfo extends MethodResponse {
    public MethodResponseIdentityInfo(UserIdentityInfo data) {
        this.data= data;
    }
    private UserIdentityInfo data;

    public UserIdentityInfo getData() {
        return data;
    }
}