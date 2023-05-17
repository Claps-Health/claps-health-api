package models.twitter;

public class TwitterApiAuthorization {
    private String api_key;

    public TwitterApiAuthorization(String api_key) {
        this.api_key = api_key;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
}
