package models.form.response;

import reference.error.ERROR_ENUM;

public class MethodResponseError extends MethodResponse {
    public MethodResponseError(ERROR_ENUM error) {
        super(error);
        this.data= null;
    }

    private Object data;

    public Object getData() {
        return data;
    }
}