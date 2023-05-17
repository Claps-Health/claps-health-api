import com.google.inject.AbstractModule;
import jwt.JwtControllerHelper;
import jwt.JwtControllerHelperImpl;
import jwt.JwtValidator;
import jwt.JwtValidatorImpl;
import services.ApplicationTimer;

import java.time.Clock;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        bind(ApplicationTimer.class).asEagerSingleton();

        bind(JwtValidator.class).to(JwtValidatorImpl.class).asEagerSingleton();
        bind(JwtControllerHelper.class).to(JwtControllerHelperImpl.class).asEagerSingleton();
    }
}
