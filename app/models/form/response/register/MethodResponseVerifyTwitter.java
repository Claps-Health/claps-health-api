package models.form.response.register;

import models.form.response.MethodResponse;

public class MethodResponseVerifyTwitter extends MethodResponse {
    public MethodResponseVerifyTwitter(String data) {
        this.data= data;
    }
    private String data;
    public String getData() {
        return data;
    }
}
