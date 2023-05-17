package models.form.response.register;

import models.form.response.MethodResponse;

public class MethodResponseJwtSignedTokenGenerate extends MethodResponse {
    public MethodResponseJwtSignedTokenGenerate(String data) {
        this.data= data;
    }

    private String data;

    public String getData() {
        return data;
    }
}
