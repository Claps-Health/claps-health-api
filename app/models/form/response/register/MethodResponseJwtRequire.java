package models.form.response.register;

import models.form.response.MethodResponse;

public class MethodResponseJwtRequire extends MethodResponse {
    public MethodResponseJwtRequire(Object data) {
        this.data= data;
    }

    private Object data;

    public Object getData() {
        return data;
    }
}
