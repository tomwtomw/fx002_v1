package org.tomw.rachunki.config;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.tomw.documentfile.DocumentFile;
import org.tomw.hibernate.SessionFactoryFactory;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;

public class SessionFactoryFactoryRachunkiImpl implements SessionFactoryFactory {
    private final static Logger LOGGER = Logger.getLogger(SessionFactoryFactoryRachunkiImpl.class.getName());

    private RachunkiConfiguration config;

    public SessionFactoryFactoryRachunkiImpl(RachunkiConfiguration config) {
        this.config = config;
    }

    @Override
    public SessionFactory buildSessionFactory() {
        if (config.getSelfIdentificationService().isDevel()) {
            return buildSessionFactoryDevel();
        }
        if (config.getSelfIdentificationService().isTest()) {
            return buildSessionFactoryTest();
        }
        if (config.getSelfIdentificationService().isPreprod()) {
            return buildSessionFactoryPreprod();
        }
        if (config.getSelfIdentificationService().isProd()) {
            return buildSessionFactoryProd();
        }
        String message = "Failed to build session factory object";
        LOGGER.error(message);
        throw new RuntimeException(message);
    }

    /**
     * build prod instance of session factory
     * TODO this should be config file
     * @return
     */
    private SessionFactory buildSessionFactoryProd() {
        Configuration configuration = new Configuration()
                .configure();
        addAnnotatedClasses(configuration);
        configuration.setProperty("hibernate.connection.driver", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/rachunki_prod?autoReconnect=true&useSSL=false");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.connection.user", "rachunkiprod");
        configuration.setProperty("hibernate.connection.password", "rachunkiprod");
        return configuration.buildSessionFactory();
    }

    /**
     * build preprod hibernate connection
     * TODO move this to config file instead of hardcoded
     *
     * @return
     */
    private SessionFactory buildSessionFactoryPreprod() {
        Configuration configuration = new Configuration()
                .configure();
        addAnnotatedClasses(configuration);
        configuration.setProperty("hibernate.connection.driver", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/rachunki_preprod?autoReconnect=true&useSSL=false");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.connection.user", "rachunkipreprod");
        configuration.setProperty("hibernate.connection.password", "rachunkipreprod");
        return configuration.buildSessionFactory();
    }

    //TODO hinbernate config should be in config file
    public SessionFactory buildSessionFactoryTest() {
        Configuration configuration = new Configuration()
                .configure()
                .addAnnotatedClass(DocumentFile.class)
                .addAnnotatedClass(CheckDocument.class)
                .addAnnotatedClass(Transakcja.class)
                .addAnnotatedClass(Konto.class);
        configuration.setProperty("hibernate.connection.driver", "org.h2.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        //configuration.setProperty("hibernate.connection.url", "jdbc:h2:~/mem");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:file:~/testRachunkiDatabase");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return configuration.buildSessionFactory();
    }

    /**
     * Build development instance of hibernate session factory
     *  TODO hibernate config should be in config file
     * @return
     */
    public static SessionFactory buildSessionFactoryDevel() {
        return new Configuration().configure()
                .addAnnotatedClass(DocumentFile.class)
                .addAnnotatedClass(CheckDocument.class)
                .addAnnotatedClass(Transakcja.class)
                .addAnnotatedClass(Konto.class)
                .buildSessionFactory();
    }

    private void addAnnotatedClasses(Configuration configuration) {
        configuration
                .addAnnotatedClass(DocumentFile.class)
                .addAnnotatedClass(CheckDocument.class)
                .addAnnotatedClass(Transakcja.class)
                .addAnnotatedClass(Konto.class);
    }
}
