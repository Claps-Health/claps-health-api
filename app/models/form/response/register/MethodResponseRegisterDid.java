package models.form.response.register;

import models.form.response.MethodResponse;

public class MethodResponseRegisterDid extends MethodResponse {
    public MethodResponseRegisterDid(String data) {
        this.data= data;
    }

    private String data;

    public String getData() {
        return data;
    }
}
