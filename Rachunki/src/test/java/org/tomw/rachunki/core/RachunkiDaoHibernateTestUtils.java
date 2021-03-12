package org.tomw.rachunki.core;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.tomw.documentfile.DocumentFile;
import org.tomw.envutils.TomwEnvUtils;
import org.tomw.filestoredao.FileDao;
import org.tomw.filestoredao.FileDaoDirImpl;
import org.tomw.filestoredao.FileDaoDrirImplConfiguration;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;

import java.io.File;

public class RachunkiDaoHibernateTestUtils {
    public static RachunkiDao createDao(RachunkiConfiguration rachunkiConfig){

        File testFileDaoDirectory = rachunkiConfig.getFileStoreDirectory();
        FileDaoDrirImplConfiguration fileStoreConfiguration = new FileDaoDirImplConfigurationTest(testFileDaoDirectory);

        FileDao fileDaoForTest = new FileDaoDirImpl(fileStoreConfiguration);

        return new RachunkiDaoHibernateImpl(buildSessionFactory(),
                rachunkiConfig,fileDaoForTest);
    }

    public static SessionFactory buildSessionFactory(){
        Configuration configuration =  new Configuration()
                .configure()
                .addAnnotatedClass(CheckDocument.class)
                .addAnnotatedClass(DocumentFile.class)
                .addAnnotatedClass(Transakcja.class)
                .addAnnotatedClass(Konto.class);
        configuration.setProperty("hibernate.connection.driver","org.h2.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        //configuration.setProperty("hibernate.connection.url", "jdbc:h2:~/mem");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:file:~/testRachunkiDatabase");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return configuration.buildSessionFactory();
    }
}
