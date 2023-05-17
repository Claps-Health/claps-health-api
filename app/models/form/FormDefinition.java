package models.form;

public class FormDefinition {

    protected FormDefinition() {
        throw new IllegalStateException("FormDefinition class");
    }

    public static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";
    public static final String AUTH_EXPIRY = "authExpiry";
}
