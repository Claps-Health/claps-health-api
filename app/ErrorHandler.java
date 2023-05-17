import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {
    @Inject
    public ErrorHandler(
            Config config,
            Environment environment,
            OptionalSourceMapper sourceMapper,
            Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    protected CompletionStage<Result> onDevServerError(RequestHeader request, UsefulException exception) {
        return CompletableFuture.completedFuture(Results.redirect("/500"));
    }

    protected CompletionStage<Result> onProdServerError(RequestHeader request, UsefulException exception) {
        return CompletableFuture.completedFuture(Results.redirect("/500"));
    }

    protected CompletionStage<Result> onNotFound(RequestHeader var1, String var2) {
        return CompletableFuture.completedFuture(Results.redirect("/404"));
    }

    protected CompletionStage<Result> onBadRequest(RequestHeader var1, String var2) {
        return CompletableFuture.completedFuture(Results.redirect("/400"));
    }
}
