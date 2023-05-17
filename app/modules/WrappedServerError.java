package modules;

public class WrappedServerError extends Exception {
    public WrappedServerError (String message)
    {
        super (message);
    }
}
