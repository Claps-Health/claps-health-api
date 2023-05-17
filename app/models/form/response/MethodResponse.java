package models.form.response;

import reference.error.ERROR_ENUM;

public class MethodResponse {
    private int error;
    private String msg;

    protected MethodResponse() {
        this.error= ERROR_ENUM.ERR_NOERROR.getId();
        this.msg = ERROR_ENUM.ERR_NOERROR.getMsg();
    }

    public MethodResponse(ERROR_ENUM error) {
        this.error = error.getId();
        this.msg = error.getMsg();
    }

    public int getError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
