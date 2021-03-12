package org.tomw.rachunki.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tomw.config.SelfIdentificationService;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.config.RachunkiConfigurationImpl;
import org.tomw.rachunki.core.RachunkiDao;
import org.tomw.rachunki.core.RachunkiDaoHibernateTestUtils;
import org.tomw.rachunki.core.SelfIdentificationServiceRachunki;

import java.io.File;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class TransakcjaTest {
    private RachunkiDao dao;
    private RachunkiConfiguration rachunkiConfig;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    private File testDirectory;

    @Before
    public void setUp() throws Exception {

        testDirectory = folder.newFolder("testDirectory");

        SelfIdentificationService selfIdentificationService = new SelfIdentificationServiceRachunki();

        rachunkiConfig = new RachunkiConfigurationImpl(selfIdentificationService);

        TomwFileUtils.deleteDirectoryWithFiles(rachunkiConfig.getFileStoreDirectory());

        dao = RachunkiDaoHibernateTestUtils.createDao(rachunkiConfig);

        TomwFileUtils.deleteDirectoryWithFiles(testDirectory);
        TomwFileUtils.mkdirs(testDirectory);

    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(rachunkiConfig.getFileStoreDirectory());
        TomwFileUtils.deleteDirectoryWithFiles(testDirectory);
    }

    @Test
    public void test_IsCleared() throws Exception {
        Konto a1 = new Konto();
        a1.setShortName("aaa");
        dao.save(a1);

        Konto buyer = new Konto();
        buyer.setShortName("buyer");
        dao.save(buyer);

        Konto seller = new Konto();
        seller.setShortName("seller");
        dao.save(seller);

        Transakcja t1 = new Transakcja();
        t1.setBuyer(buyer);
        t1.setSeller(seller);

        t1.setClearedSeller(false);
        t1.setClearedBuyer(true);

        dao.save(t1);

        assertTrue(t1.isCleared(buyer));
        assertTrue(t1.isClearedLocal(buyer));
        assertFalse(t1.isCleared(seller));
        assertFalse(t1.isClearedRemote(buyer));
    }

    @Test
    public void test_getCounterparty() throws Exception {
        Konto a1 = new Konto();
        a1.setShortName("aaa");
        dao.save(a1);

        Konto buyer = new Konto();
        buyer.setShortName("buyer");
        dao.save(buyer);

        Konto seller = new Konto();
        seller.setShortName("seller");
        dao.save(seller);

        Transakcja t1 = new Transakcja();
        t1.setBuyer(buyer);
        t1.setSeller(seller);

        dao.save(t1);

        assertEquals(buyer,t1.getCounterParty(seller));
        assertEquals(seller,t1.getCounterParty(buyer));

        boolean exceptionThrown=false;
        try{
            t1.getCounterParty(a1);
        }catch(Exception e){
            exceptionThrown=true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void test_setNewCounterParty() throws Exception {
        Konto a1 = new Konto();
        a1.setShortName("aaa");
        dao.save(a1);

        Konto buyer = new Konto();
        buyer.setShortName("buyer");
        dao.save(buyer);

        Konto seller = new Konto();
        seller.setShortName("seller");
        dao.save(seller);

        Transakcja t1 = new Transakcja(LocalDate.now(),buyer, seller);
        dao.save(t1);

        assertEquals(buyer,t1.getCounterParty(seller));
        assertEquals(seller,t1.getCounterParty(buyer));

        boolean exceptionThrown=false;
        try{
            t1.getCounterParty(a1);
        }catch(Exception e){
            exceptionThrown=true;
        }
        assertTrue(exceptionThrown);

        Konto newCounterparty = new Konto();
        newCounterparty.setShortName("newCounterparty");
        dao.save(newCounterparty);

        t1.setNewCounterParty(seller,newCounterparty);
        assertEquals(newCounterparty,t1.getCounterParty(seller));
    }

    @Test
    public void test_setClearedLocal() throws Exception {
        Konto a1 = new Konto();
        a1.setShortName("aaa");
        dao.save(a1);

        Konto buyer = new Konto();
        buyer.setShortName("buyer");
        dao.save(buyer);

        Konto seller = new Konto();
        seller.setShortName("seller");
        dao.save(seller);

        Transakcja t1 = new Transakcja();
        t1.setBuyer(buyer);
        t1.setSeller(seller);
        t1.setClearedBuyer(false);
        t1.setClearedSeller(false);

        dao.save(t1);

        t1.setClearedLocal(true,seller);
        assertTrue(t1.isClearedSeller());

        t1.setClearedBuyer(false);
        t1.setClearedSeller(false);
        assertFalse(t1.isClearedSeller());

        t1.setClearedLocal(true,buyer);
        assertTrue(t1.isClearedBuyer());

        boolean exceptionThrown=false;
        try{
            t1.setClearedLocal(true,a1);
        }catch(Exception e){
            exceptionThrown=true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void test_setClearedRemote() throws Exception {
        Konto a1 = new Konto();
        a1.setShortName("aaa");
        dao.save(a1);

        Konto buyer = new Konto();
        buyer.setShortName("buyer");
        dao.save(buyer);

        Konto seller = new Konto();
        seller.setShortName("seller");
        dao.save(seller);

        Transakcja t1 = new Transakcja();
        t1.setBuyer(buyer);
        t1.setSeller(seller);
        t1.setClearedBuyer(false);
        t1.setClearedSeller(false);

        dao.save(t1);

        t1.setClearedRemote(true,seller);
        assertTrue(t1.isClearedBuyer());

        t1.setClearedBuyer(false);
        t1.setClearedSeller(false);
        assertFalse(t1.isClearedBuyer());

        t1.setClearedRemote(true,buyer);
        assertTrue(t1.isClearedSeller());

        boolean exceptionThrown=false;
        try{
            t1.setClearedRemote(true,a1);
        }catch(Exception e){
            exceptionThrown=true;
        }
        assertTrue(exceptionThrown);
    }
}