package modules;

import com.google.inject.AbstractModule;
import io.ebean.EbeanServer;

public class EbeanBindingModule extends AbstractModule {
    //AbstractModule
    @Override
    protected void configure() {
        bind(EbeanServer.class).toProvider(EbeanServerProvider.class).asEagerSingleton();
    }
}
