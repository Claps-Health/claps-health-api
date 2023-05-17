package modules;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.datasource.DataSourceConfig;
import models.secure.AppEncryptManager;
import play.Logger;

import javax.inject.Provider;

public class EbeanServerProvider implements Provider<EbeanServer> {
    private EbeanServer dbServer;

    public EbeanServerProvider() {
        configEbeanServer();
    }

    EbeanServer configEbeanServer() {
        Config config = ConfigFactory.load();

        boolean secure= true;
        try {
            secure= config.getBoolean("db_config.secure");
        }
        catch (Exception ex) {
            Logger.debug("db run with default configuration");
            return null;
        }
        if(secure) Logger.debug("db run with secure configuration");

        ServerConfig serverConfig = new ServerConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();

        /* Read the config files */
        final String DB_DRIVER   = config.getString("db_config.driver");
        final String DB_URL      = config.getString("db_config.url");
        final String DB_USERNAME = config.getString("db_config.username");
        final String DB_PASSWORD = config.getString("db_config.password");

        /* Load the database driver */
        dataSourceConfig.setDriver(DB_DRIVER);
        try{
            Class.forName(DB_DRIVER).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Logger.debug("Unable to load database driver");
            return null;
        }

        /* Set the data source configs */
        dataSourceConfig.setUrl(DB_URL);
        try {
            dataSourceConfig.setUsername(secure?AppEncryptManager.getInstance().decrypt(DB_USERNAME):DB_USERNAME);
            dataSourceConfig.setPassword(secure?AppEncryptManager.getInstance().decrypt(DB_PASSWORD):DB_PASSWORD);
        } catch (Exception e) {
            //e.printStackTrace();
            Logger.error(e.getMessage());
        }

        dataSourceConfig.setCaptureStackTrace(true);

        serverConfig.setDataSourceConfig(dataSourceConfig);
//        serverConfig.setName("mysql");
        serverConfig.setDefaultServer(true);
        serverConfig.setDdlGenerate(false);
        serverConfig.setDdlRun(false);
        serverConfig.setRegister(true);

        //ConnectionPool
        dataSourceConfig.setHeartbeatFreqSecs(60);
        dataSourceConfig.setHeartbeatTimeoutSeconds(30);
        dataSourceConfig.setMinConnections(3);
        dataSourceConfig.setMaxConnections(30);
        dataSourceConfig.setLeakTimeMinutes(1);
        dataSourceConfig.setMaxInactiveTimeSecs(30);
        dataSourceConfig.setWaitTimeoutMillis(1000 * 60);
        dataSourceConfig.setTrimPoolFreqSecs(60);

        serverConfig.addClass(models.jpa.identity.UserIdentity.class);
        serverConfig.addClass(models.jpa.identity.UserIdentityVerification.class);


        dbServer=null;
        try {
            dbServer = EbeanServerFactory.create(serverConfig);
        }
        catch (Exception e){
            Logger.debug("Failed to create ebean server");
            return null;
        }

        return dbServer;
    }

    public void dispose() {
        if(dbServer!=null) {
            dbServer.shutdown(true, false);
        }
    }

    @Override
    public EbeanServer get() {
        return dbServer;
    }
}
