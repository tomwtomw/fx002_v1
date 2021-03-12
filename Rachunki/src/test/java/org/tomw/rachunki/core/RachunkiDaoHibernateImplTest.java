package org.tomw.rachunki.core;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tomw.config.SelfIdentificationService;
import org.tomw.documentfile.DocumentFile;
import org.tomw.filestoredao.FileDao;
import org.tomw.filestoredao.FileDaoDirImpl;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.config.RachunkiConfigurationImpl;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RachunkiDaoHibernateImplTest {

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
    public void test_GetKonto() throws Exception {
        System.out.println("test_GetKonto");
        Konto konto = new Konto();
        konto.setFullName("first account");
        dao.save(konto);

        Konto recovered = dao.getKonto(konto.getId());
        assertEquals(konto,recovered);
    }

    @Test
    public void test_GetKontoByOldId() throws Exception {
        System.out.println("test_GetKontoByOldId");
        Konto konto = new Konto();
        konto.setFullName("second account");
        konto.getOldIds().add("xxxxxxxxxxxxx");
        dao.save(konto);

        Konto recovered = dao.getKonto(konto.getId());
        assertEquals(konto,recovered);
    }

    @Test
    public void test_GetAllAccounts() throws Exception {
        System.out.println("test_GetAllAccounts");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();
        Konto konto4 = new Konto();

        konto1.setFullName("konto1");
        konto2.setFullName("konto2");
        konto3.setFullName("konto3");

        Collection<Konto> before = dao.getAllAccounts();

        assertFalse(before.contains(konto1));
        assertFalse(before.contains(konto2));
        assertFalse(before.contains(konto3));
        assertFalse(before.contains(konto4));

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        Collection<Konto> after = dao.getAllAccounts();

        assertTrue(before.size()<after.size());

        assertTrue(after.contains(konto1));
        assertTrue(after.contains(konto2));
        assertTrue(after.contains(konto3));
        assertFalse(after.contains(konto4));
    }

    @Test
    public void test_GetAllActiveAccounts() throws Exception {
        System.out.println("test_GetAllActiveAccounts");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();

        konto1.setFullName("konto1");
        konto2.setFullName("konto2");

        konto1.setAccountActive(true);
        konto2.setAccountActive(false);

        Collection<Konto> before = dao.getAllAccounts();

        assertFalse(before.contains(konto1));
        assertFalse(before.contains(konto2));

        dao.save(konto1);
        dao.save(konto2);

        Collection<Konto> after = dao.getAllActiveAccounts();

        assertTrue(before.size()<after.size());

        assertTrue(after.contains(konto1));
        assertFalse(after.contains(konto2));
    }

    @Test
    public void test_GetAllLocalAccounts() throws Exception {
        System.out.println("test_GetAllLocalAccounts");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();

        konto1.setFullName("konto1");
        konto2.setFullName("konto2");

        konto1.setAccountLocal(true);
        konto2.setAccountLocal(false);

        Collection<Konto> before = dao.getAllAccounts();

        assertFalse(before.contains(konto1));
        assertFalse(before.contains(konto2));

        dao.save(konto1);
        dao.save(konto2);

        Collection<Konto> after = dao.getAllLocalAccounts();

        assertTrue(before.size()<after.size());

        assertTrue(after.contains(konto1));
        assertFalse(after.contains(konto2));
    }

    @Test
    public void test_GetAllPrimaryAccounts() throws Exception {
        System.out.println("test_GetAllPrimaryAccounts");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();

        konto1.setFullName("konto1");
        konto2.setFullName("konto2");

        konto1.setAccountPrimary(true);
        konto2.setAccountPrimary(false);

        Collection<Konto> before = dao.getAllAccounts();

        assertFalse(before.contains(konto1));
        assertFalse(before.contains(konto2));

        dao.save(konto1);
        dao.save(konto2);

        Collection<Konto> after = dao.getAllPrimaryAccounts();

        assertTrue(before.size()<after.size());

        assertTrue(after.contains(konto1));
        assertFalse(after.contains(konto2));
    }

    @Test
    public void test_GetAllActiveLocalAccounts() throws Exception {
        System.out.println("test_GetAllActiveLocalAccounts");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();
        Konto konto4 = new Konto();

        konto1.setFullName("konto1");
        konto2.setFullName("konto2");
        konto3.setFullName("konto3");
        konto4.setFullName("konto4");

        konto1.setAccountLocal(false);
        konto2.setAccountLocal(true);
        konto3.setAccountLocal(false);
        konto4.setAccountLocal(true);

        konto1.setAccountActive(false);
        konto2.setAccountActive(false);
        konto3.setAccountActive(true);
        konto4.setAccountActive(true);

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);
        dao.save(konto4);

        Collection<Konto> before = dao.getAllActiveLocalAccounts();

        assertFalse(before.contains(konto1));
        assertFalse(before.contains(konto2));
        assertFalse(before.contains(konto3));
        assertTrue(before.contains(konto4));
    }

    @Test
    public void test_Save() throws Exception {
        System.out.println("test_Save");
        test_save_and_delete_konto();
    }

    @Test
    public void test_Delete() throws Exception {
        System.out.println("test_Delete");
        test_save_and_delete_konto();
    }

    @Test
    public void test_DeleteKonto() throws Exception {
        System.out.println("test_DeleteKonto");
        test_save_and_delete_konto();
    }

    private void test_save_and_delete_konto(){
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        assertEquals(konto1,dao.getKonto(konto1.getId()));
        assertEquals(konto2,dao.getKonto(konto2.getId()));
        assertEquals(konto3,dao.getKonto(konto3.getId()));

        dao.delete(konto1);

        assertNull(dao.getKonto(konto1.getId()));
        assertEquals(konto2,dao.getKonto(konto2.getId()));
        assertEquals(konto3,dao.getKonto(konto3.getId()));

        dao.deleteKonto(konto2.getId());

        assertNull(dao.getKonto(konto1.getId()));
        assertNull(dao.getKonto(konto2.getId()));
        assertEquals(konto3,dao.getKonto(konto3.getId()));
    }

    @Test
    public void test_GetTransakcja() throws Exception {
        System.out.println("test_GetTransakcja");
        //TODO save all, check documents, documents, all.

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();

        File uploadFile=new File(testDirectory,"tempFile1.txt");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile df = dao.upload(uploadFile,false);


        Transakcja transakcja = new Transakcja();
        transakcja.setBuyer(konto1);
        transakcja.setSeller(konto2);
        transakcja.setTransactionAmount(3.14);

        transakcja.getDocuments().add(df);

        dao.save(konto1);
        dao.save(konto2);
        dao.save(transakcja);

        Transakcja tFrom = dao.getTransakcja(transakcja.getId());

        assertNotNull(tFrom);
        assertEquals(transakcja,tFrom);
        assertEquals(konto1,tFrom.getBuyer());
        assertEquals(konto2,tFrom.getSeller());
        assertEquals(transakcja.getTransactionAmount(),tFrom.getTransactionAmount(),0.001);
    }

    @Test
    public void test_GetTransakcjaByOldId() throws Exception {
        System.out.println("test_GetTransakcjaByOldId");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        transakcja1.getOldIds().add("AAA");
        transakcja2.getOldIds().add("BBB");
        transakcja2.getOldIds().add("CCC");
        transakcja3.getOldIds().add("DDD");

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Transakcja t1 = dao.getTransakcjaByOldId("BBB");
        assertEquals(t1,transakcja2);

    }

    @Test
    public void test_GetTransactionForCheckDocument() throws Exception {
        System.out.println("test_GetTransactionForCheckDocument");
        //============  check 1 ==============

        CheckDocument check1 = new CheckDocument();
        check1.setCheckNumber("1001");

        File uploadFileF = new File(testDirectory, "temp_1001F.jpg");
        FileUtils.writeStringToFile(uploadFileF, "asdfg", TomwStringUtils.ENCODING_ISO);
        File uploadFileB = new File(testDirectory, "temp_1001B.jpg");
        FileUtils.writeStringToFile(uploadFileF, "asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df1 = dao.upload(uploadFileF);
        DocumentFile df2 = dao.upload(uploadFileB);

        check1.setCheckImageFront(df1);
        check1.setCheckImageBack(df2);

        //============  check 2 ==============

        CheckDocument check2 = new CheckDocument();

        check2.setCheckNumber("1002");

        uploadFileF = new File(testDirectory, "temp_1002F.jpg");
        FileUtils.writeStringToFile(uploadFileF, "asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB = new File(testDirectory, "temp_1002B.jpg");
        FileUtils.writeStringToFile(uploadFileF, "asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df3 = dao.upload(uploadFileF);
        DocumentFile df4 = dao.upload(uploadFileB);

        check2.setCheckImageFront(df3);
        check2.setCheckImageBack(df4);

        //============  check 3 ==============

        CheckDocument check3 = new CheckDocument();

        check3.setCheckNumber("1002");

        uploadFileF = new File(testDirectory, "temp_1003F.jpg");
        FileUtils.writeStringToFile(uploadFileF, "asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB = new File(testDirectory, "temp_1003B.jpg");
        FileUtils.writeStringToFile(uploadFileF, "asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df5 = dao.upload(uploadFileF);
        DocumentFile df6 = dao.upload(uploadFileB);

        check3.setCheckImageFront(df5);
        check3.setCheckImageBack(df6);

        //======================================

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();
        Konto konto4 = new Konto();

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);
        dao.save(konto4);

        dao.save(check1);
        dao.save(check2);
        dao.save(check3);

        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        transakcja1.setBuyer(konto1);
        transakcja2.setBuyer(konto2);
        transakcja3.setBuyer(konto3);

        transakcja1.setSeller(konto2);
        transakcja2.setSeller(konto3);
        transakcja3.setSeller(konto1);

        transakcja1.setCheckDocument(check1);
        transakcja2.setCheckDocument(check2);

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Transakcja transactionForCheck1 = dao.getTransactionForCheckDocument(check1);
        Transakcja transactionForCheck2 = dao.getTransactionForCheckDocument(check2);
        Transakcja transactionForCheck3 = dao.getTransactionForCheckDocument(check3);

        assertEquals(transakcja1, transactionForCheck1);
        assertEquals(transakcja2, transactionForCheck2);
        assertEquals(null, transactionForCheck3);
    }

    @Test
    public void test_Delete1() throws Exception {
        System.out.println("test_Delete1");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();

        dao.save(konto1);
        dao.save(konto2);

        Transakcja transakcja = new Transakcja();
        transakcja.setBuyer(konto1);
        transakcja.setSeller(konto2);
        transakcja.setTransactionAmount(3.14);

        dao.save(transakcja);
        assertEquals(transakcja,dao.getTransakcja(transakcja.getId()));
        dao.delete(transakcja);
        assertNull(dao.getTransakcja(transakcja.getId()));
    }

    @Test
    public void test_DeleteTransakcja() throws Exception {
        System.out.println("test_DeleteTransakcja");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();

        dao.save(konto1);
        dao.save(konto2);

        Transakcja transakcja = new Transakcja();
        transakcja.setBuyer(konto1);
        transakcja.setSeller(konto2);
        transakcja.setTransactionAmount(3.14);

        dao.save(transakcja);
        assertEquals(transakcja,dao.getTransakcja(transakcja.getId()));
        dao.deleteTransakcja(transakcja.getId());
        assertNull(dao.getTransakcja(transakcja.getId()));
    }

    @Test
    public void test_GetTransactionsForCheckNumber() throws Exception {
        System.out.println("test_GetTransactionsForCheckNumber");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        CheckDocument check1 = new CheckDocument();
        check1.setCheckNumber("1001");
        transakcja1.setCheckDocument(check1);

        CheckDocument check2 = new CheckDocument();
        check2.setCheckNumber("1001");
        transakcja2.setCheckDocument(check2);

        CheckDocument check3 = new CheckDocument();
        check3.setCheckNumber("1003");
        transakcja3.setCheckDocument(check3);

        dao.save(check1);
        dao.save(check2);
        dao.save(check3);
        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Collection<Transakcja> list1 = dao.getTransactionsForCheckNumber("1001");
        assertEquals(2,list1.size());
        assertTrue(list1.contains(transakcja1));
        assertTrue(list1.contains(transakcja2));

        Collection<Transakcja> list2 = dao.getTransactionsForCheckNumber("1003");
        assertEquals(1,list2.size());
        assertTrue(list2.contains(transakcja3));
        assertFalse(list2.contains(transakcja2));

    }


    @Test
    public void test_GetCheckDocument() throws Exception {
        System.out.println("test_GetCheckDocument");
        CheckDocument check = new CheckDocument();
        check.setCheckNumber("1234");

        File uploadFile=new File(testDirectory,"temp_1234F.jpg");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile checkFront = dao.upload(uploadFile,true);

        File uploadFile2=new File(testDirectory,"temp_1234B.jpg");
        FileUtils.writeStringToFile(uploadFile2,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile2.exists());

        DocumentFile checkBack = dao.upload(uploadFile2,true);

        check.setCheckImageFront(checkFront);
        check.setCheckImageBack(checkBack);

        dao.save(checkFront);
        dao.save(checkBack);
        dao.save(check);

        CheckDocument checkOut = dao.getCheckDocument(check.getId());
        assertNotNull(checkOut);
        assertEquals(checkOut.getCheckImageBack(),check.getCheckImageBack());
        assertEquals(checkOut.getCheckImageFront(),check.getCheckImageFront());
        assertEquals(checkOut.getCheckNumber(),check.getCheckNumber());

    }

    @Test
    public void test_GetCheckDocumentsInvolvingAccount() throws Exception {
        System.out.println("test_GetCheckDocumentsInvolvingAccount");

        //============  check 1 ==============

        CheckDocument check1 = new CheckDocument();
        check1.setCheckNumber("1001");

        File uploadFileF=new File(testDirectory,"temp_1001F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        File uploadFileB=new File(testDirectory,"temp_1001B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df1 = dao.upload(uploadFileF);
        DocumentFile df2 = dao.upload(uploadFileB);

        check1.setCheckImageFront(df1);
        check1.setCheckImageBack(df2);

        //============  check 2 ==============

        CheckDocument check2 = new CheckDocument();

        check2.setCheckNumber("1002");

        uploadFileF=new File(testDirectory,"temp_1002F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB=new File(testDirectory,"temp_1002B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df3 = dao.upload(uploadFileF);
        DocumentFile df4 = dao.upload(uploadFileB);

        check2.setCheckImageFront(df3);
        check2.setCheckImageBack(df4);

        //============  check 3 ==============

        CheckDocument check3 = new CheckDocument();

        check3.setCheckNumber("1002");

        uploadFileF=new File(testDirectory,"temp_1003F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB=new File(testDirectory,"temp_1003B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df5 = dao.upload(uploadFileF);
        DocumentFile df6 = dao.upload(uploadFileB);

        check3.setCheckImageFront(df5);
        check3.setCheckImageBack(df6);

        //======================================

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();
        Konto konto4 = new Konto();

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);
        dao.save(konto4);

        dao.save(check1);
        dao.save(check2);
        dao.save(check3);

        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        transakcja1.setBuyer(konto1);
        transakcja2.setBuyer(konto2);
        transakcja3.setBuyer(konto3);

        transakcja1.setSeller(konto2);
        transakcja2.setSeller(konto3);
        transakcja3.setSeller(konto1);

        transakcja1.setCheckDocument(check1);
        transakcja2.setCheckDocument(check2);
        transakcja3.setCheckDocument(check3);

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Collection<CheckDocument>documentsForKonto1 =  dao.getCheckDocumentsInvolvingAccount(konto1);
        Collection<CheckDocument>documentsForKonto2 =  dao.getCheckDocumentsInvolvingAccount(konto2);
        Collection<CheckDocument>documentsForKonto3 =  dao.getCheckDocumentsInvolvingAccount(konto3);
        Collection<CheckDocument>documentsForKonto4 =  dao.getCheckDocumentsInvolvingAccount(konto4);

        assertEquals(2,documentsForKonto1.size());
        assertEquals(2,documentsForKonto2.size());
        assertEquals(2,documentsForKonto3.size());
        assertEquals(0,documentsForKonto4.size());

        assertTrue(documentsForKonto1.contains(check1));
        assertTrue(documentsForKonto1.contains(check3));

        assertTrue(documentsForKonto2.contains(check1));
        assertTrue(documentsForKonto3.contains(check2));

        assertTrue(documentsForKonto3.contains(check3));
        assertTrue(documentsForKonto3.contains(check2));
    }

    @Test
    public void test_GetAllCheckDocumentsWithCheckNumber() throws Exception {
        System.out.println("test_GetAllCheckDocumentsWithChecknumber");
        //============  check 1 ==============

        CheckDocument check1 = new CheckDocument();
        check1.setCheckNumber("1001");

        File uploadFileF=new File(testDirectory,"temp_1001F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        File uploadFileB=new File(testDirectory,"temp_1001B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df1 = dao.upload(uploadFileF);
        DocumentFile df2 = dao.upload(uploadFileB);

        check1.setCheckImageFront(df1);
        check1.setCheckImageBack(df2);

        //============  check 2 ==============

        CheckDocument check2 = new CheckDocument();

        check2.setCheckNumber("1002");

        uploadFileF=new File(testDirectory,"temp_1002F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB=new File(testDirectory,"temp_1002B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df3 = dao.upload(uploadFileF);
        DocumentFile df4 = dao.upload(uploadFileB);

        check2.setCheckImageFront(df3);
        check2.setCheckImageBack(df4);

        //============  check 3 - has the same check number as check 2 ==============

        CheckDocument check3 = new CheckDocument();

        check3.setCheckNumber("1002");

        uploadFileF=new File(testDirectory,"temp_1003F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB=new File(testDirectory,"temp_1003B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df5 = dao.upload(uploadFileF);
        DocumentFile df6 = dao.upload(uploadFileB);

        check3.setCheckImageFront(df5);
        check3.setCheckImageBack(df6);

        //============  check 4 - has the same check number as check 2 ==============

        CheckDocument check4 = new CheckDocument();

        check4.setCheckNumber("1004");

        uploadFileF=new File(testDirectory,"temp_1003F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB=new File(testDirectory,"temp_1003B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df7 = dao.upload(uploadFileF);
        DocumentFile df8 = dao.upload(uploadFileB);

        check4.setCheckImageFront(df7);
        check4.setCheckImageBack(df8);

        //======================================

        dao.save(check1);
        dao.save(check2);
        dao.save(check3);
        dao.save(check4);

        Collection<CheckDocument> checks1002 = dao.getAllCheckDocumentsWithCheckNumber("1002");
        assertEquals(2,checks1002.size());
        assertTrue(checks1002.contains(check2));
        assertTrue(checks1002.contains(check3));

        Collection<CheckDocument> checks1009 = dao.getAllCheckDocumentsWithCheckNumber("1009");
        assertEquals(0,checks1009.size());
    }

    @Test
    public void test_GetAllCheckDocuments() throws Exception {
        System.out.println("test_GetAllCheckDocuments");
        //============  check 1 ==============

        CheckDocument check1 = new CheckDocument();
        check1.setCheckNumber("1001");

        File uploadFileF=new File(testDirectory,"temp_1001F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        File uploadFileB=new File(testDirectory,"temp_1001B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df1 = dao.upload(uploadFileF);
        DocumentFile df2 = dao.upload(uploadFileB);

        check1.setCheckImageFront(df1);
        check1.setCheckImageBack(df2);

        //============  check 2 ==============

        CheckDocument check2 = new CheckDocument();

        check2.setCheckNumber("1002");

        uploadFileF=new File(testDirectory,"temp_1002F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB=new File(testDirectory,"temp_1002B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df3 = dao.upload(uploadFileF);
        DocumentFile df4 = dao.upload(uploadFileB);

        check2.setCheckImageFront(df3);
        check2.setCheckImageBack(df4);

        //============  check 3 ==============

        CheckDocument check3 = new CheckDocument();

        check3.setCheckNumber("1002");

        uploadFileF=new File(testDirectory,"temp_1003F.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);
        uploadFileB=new File(testDirectory,"temp_1003B.jpg");
        FileUtils.writeStringToFile(uploadFileF,"asdfg", TomwStringUtils.ENCODING_ISO);

        DocumentFile df5 = dao.upload(uploadFileF);
        DocumentFile df6 = dao.upload(uploadFileB);

        check3.setCheckImageFront(df5);
        check3.setCheckImageBack(df6);

        //======================================
        dao.save(check1);
        dao.save(check2);

        Collection<CheckDocument> checks = dao.getAllCheckDocuments();

        assertEquals(2,checks.size());
        assertTrue(checks.contains(check1));
        assertTrue(checks.contains(check2));
        assertFalse(checks.contains(check3));

    }

    @Test
    public void test_Save2() throws Exception {
        System.out.println("test_Save2");

        CheckDocument check = new CheckDocument();
        check.setCheckNumber("1234");

        File uploadFile=new File(testDirectory,"temp_1234F.jpg");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile checkFront = dao.upload(uploadFile,true);

        File uploadFile2=new File(testDirectory,"temp_1234B.jpg");
        FileUtils.writeStringToFile(uploadFile2,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile2.exists());

        DocumentFile checkBack = dao.upload(uploadFile2,true);

        check.setCheckImageFront(checkFront);
        check.setCheckImageBack(checkBack);

        dao.save(checkFront);
        dao.save(checkBack);
        dao.save(check);

        CheckDocument checkOut = dao.getCheckDocument(check.getId());
        assertNotNull(checkOut);
        assertEquals(checkOut.getCheckImageBack(),check.getCheckImageBack());
        assertEquals(checkOut.getCheckImageFront(),check.getCheckImageFront());
        assertEquals(checkOut.getCheckNumber(),check.getCheckNumber());

        assertEquals(checkOut.getCheckImageFront(),dao.getDocumentFile(checkFront.getId()));
        assertEquals(checkOut.getCheckImageBack(),dao.getDocumentFile(checkBack.getId()));

        check.setCheckImageFront(null);
        dao.save(check);

        CheckDocument checkOut2 = dao.getCheckDocument(check.getId());
        assertNotNull(checkOut2);
        assertEquals(checkOut2.getCheckImageBack(),check.getCheckImageBack());
        assertEquals(checkOut2.getCheckImageFront(),check.getCheckImageFront());
        assertEquals(checkOut2.getCheckNumber(),check.getCheckNumber());

        assertEquals(checkOut2.getCheckImageBack(),dao.getDocumentFile(checkBack.getId()));
    }

    @Test
    public void test_Delete2() throws Exception {
        System.out.println("test_Delete2 Check Document");

        CheckDocument check = new CheckDocument();
        check.setCheckNumber("1234");

        File uploadFile=new File(testDirectory,"temp_1234F.jpg");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile checkFront = dao.upload(uploadFile,true);

        File uploadFile2=new File(testDirectory,"temp_1234B.jpg");
        FileUtils.writeStringToFile(uploadFile2,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile2.exists());

        DocumentFile checkBack = dao.upload(uploadFile2,true);

        check.setCheckImageFront(checkFront);
        check.setCheckImageBack(checkBack);

        dao.save(checkFront);
        dao.save(checkBack);
        dao.save(check);

        CheckDocument checkOut = dao.getCheckDocument(check.getId());
        assertNotNull(checkOut);
        assertEquals(checkOut.getCheckImageBack(),check.getCheckImageBack());
        assertEquals(checkOut.getCheckImageFront(),check.getCheckImageFront());
        assertEquals(checkOut.getCheckNumber(),check.getCheckNumber());

        boolean errorOccured=false;
        try{
            dao.delete(check);
        }catch(Exception e){
            errorOccured=true;
        }
        assertFalse(errorOccured);
        assertNull(dao.getCheckDocument(check.getId()));
        assertNull(dao.getDocumentFile(check.getCheckImageBack().getId()));
        assertNull(dao.getDocumentFile(check.getCheckImageFront().getId()));
    }

    @Test
    public void test_DeleteCheckDocument() throws Exception {
        System.out.println("test_DeleteCheckDocument");
        CheckDocument check = new CheckDocument();
        check.setCheckNumber("1234");

        File uploadFile=new File(testDirectory,"temp_1234F.jpg");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile checkFront = dao.upload(uploadFile,true);

        File uploadFile2=new File(testDirectory,"temp_1234B.jpg");
        FileUtils.writeStringToFile(uploadFile2,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile2.exists());

        DocumentFile checkBack = dao.upload(uploadFile2,true);

        check.setCheckImageFront(checkFront);
        check.setCheckImageBack(checkBack);

        dao.save(checkFront);
        dao.save(checkBack);
        dao.save(check);

        CheckDocument checkOut = dao.getCheckDocument(check.getId());
        assertNotNull(checkOut);
        assertEquals(checkOut.getCheckImageBack(),check.getCheckImageBack());
        assertEquals(checkOut.getCheckImageFront(),check.getCheckImageFront());
        assertEquals(checkOut.getCheckNumber(),check.getCheckNumber());

        boolean errorOccured=false;
        try{
            dao.deleteCheckDocument(check.getId());
        }catch(Exception e){
            errorOccured=true;
        }
        assertFalse(errorOccured);
        assertNull(dao.getCheckDocument(check.getId()));
        assertNull(dao.getDocumentFile(check.getCheckImageBack().getId()));
        assertNull(dao.getDocumentFile(check.getCheckImageFront().getId()));
    }

    @Test
    public void test_GetDocumentFile() throws Exception {
        System.out.println("test_GetDocumentFile");
        test_get_and_save_documentFile();
    }

    private void test_get_and_save_documentFile(){
        DocumentFile df1 = new DocumentFile();
        df1.setExternalFileName("delete_this_file1.txt");
        df1.setInternalFileName("delete_this_internal1.txt");
        df1.setDocumentDescription("description 1");
        df1.setDocumentTitle("title1 1");

        dao.save(df1);

        DocumentFile dfOut = dao.getDocumentFile(df1.getId());

        assertEquals(df1,dfOut);
        assertEquals(df1.getInternalFileName(),dfOut.getInternalFileName());
        assertEquals(df1.getExternalFileName(),dfOut.getExternalFileName());
        assertEquals(df1.getDocumentTitle(),dfOut.getDocumentTitle());
        assertEquals(df1.getDocumentDescription(),dfOut.getDocumentDescription());
    }

    @Test
    public void test_GetAllDocumentFiles() throws Exception {
        System.out.println("test_GetAllDocumentFiles");
        DocumentFile df1 = new DocumentFile();
        df1.setExternalFileName("delete_this_file1.txt");
        df1.setInternalFileName("delete_this_internal1.txt");
        df1.setDocumentDescription("description 1");
        df1.setDocumentTitle("title1 1");

        dao.save(df1);

        DocumentFile df2 = new DocumentFile();
        df2.setExternalFileName("delete_this_file2.txt");
        df2.setInternalFileName("delete_this_internal2.txt");
        df2.setDocumentDescription("description 2");
        df2.setDocumentTitle("title1 2");

        dao.save(df2);

        DocumentFile df3 = new DocumentFile();
        df3.setExternalFileName("delete_this_file3.txt");
        df3.setInternalFileName("delete_this_internal3.txt");
        df3.setDocumentDescription("description 3");
        df3.setDocumentTitle("title1 3");

        Collection<DocumentFile> allDocuments = dao.getAllDocumentFiles();
        assertTrue(allDocuments.contains(df1));
        assertTrue(allDocuments.contains(df2));
        assertFalse(allDocuments.contains(df3));
    }

    @Test
    public void test_GetDocumentFilesNotPersonalPictures() throws Exception {
        System.out.println("test_GetDocumentFilesNotPersonalPictures");
        DocumentFile df1 = new DocumentFile();
        df1.setExternalFileName("delete_this_file1.txt");
        df1.setInternalFileName("delete_this_internal1.txt");
        df1.setDocumentDescription("description 1");
        df1.setDocumentTitle("title1 1");
        df1.setDocumentType(DocumentFile.DOCUMENT);

        dao.save(df1);

        DocumentFile df2 = new DocumentFile();
        df2.setExternalFileName("delete_this_file2.txt");
        df2.setInternalFileName("delete_this_internal2.txt");
        df2.setDocumentDescription("description 2");
        df2.setDocumentTitle("title1 2");
        df2.setDocumentType(DocumentFile.PERSON_PICTURE);

        dao.save(df2);

        Collection<DocumentFile> result = dao.getDocumentFilesNotPersonalPictures();

        assertTrue(result.contains(df1));
        assertFalse(result.contains(df2));
    }

    @Test
    public void test_Save3() throws Exception {
        System.out.println("test_Save3");
        test_get_and_save_documentFile();
    }

    @Test
    public void test_Delete3() throws Exception {
        System.out.println("test_Delete3");
        DocumentFile df1 = new DocumentFile();
        df1.setExternalFileName("delete_this_file1.txt");
        df1.setInternalFileName("delete_this_internal1.txt");
        df1.setDocumentDescription("description 1");
        df1.setDocumentTitle("title1 1");

        dao.save(df1);

        DocumentFile df2 = new DocumentFile();
        df2.setExternalFileName("delete_this_file2.txt");
        df2.setInternalFileName("delete_this_internal2.txt");
        df2.setDocumentDescription("description 2");
        df2.setDocumentTitle("title1 2");

        dao.save(df2);

        DocumentFile df3 = new DocumentFile();
        df3.setExternalFileName("delete_this_file3.txt");
        df3.setInternalFileName("delete_this_internal3.txt");
        df3.setDocumentDescription("description 3");
        df3.setDocumentTitle("title1 3");

        Collection<DocumentFile> allDocuments = dao.getAllDocumentFiles();
        assertTrue(allDocuments.contains(df1));
        assertTrue(allDocuments.contains(df2));
        assertFalse(allDocuments.contains(df3));

        dao.delete(df2);

        assertNotNull(dao.getDocumentFile(df1.getId()));
        assertNull(dao.getDocumentFile(df2.getId()));
        assertNull(dao.getDocumentFile(df3.getId()));

    }

    @Test
    public void test_DeleteDocumentFile() throws Exception {
        System.out.println("test_DeleteDocumentFile");
        DocumentFile df1 = new DocumentFile();
        df1.setExternalFileName("delete_this_file1.txt");
        df1.setInternalFileName("delete_this_internal1.txt");
        df1.setDocumentDescription("description 1");
        df1.setDocumentTitle("title1 1");

        dao.save(df1);

        DocumentFile df2 = new DocumentFile();
        df2.setExternalFileName("delete_this_file2.txt");
        df2.setInternalFileName("delete_this_internal2.txt");
        df2.setDocumentDescription("description 2");
        df2.setDocumentTitle("title1 2");

        dao.save(df2);

        DocumentFile df3 = new DocumentFile();
        df3.setExternalFileName("delete_this_file3.txt");
        df3.setInternalFileName("delete_this_internal3.txt");
        df3.setDocumentDescription("description 3");
        df3.setDocumentTitle("title1 3");

        Collection<DocumentFile> allDocuments = dao.getAllDocumentFiles();
        assertTrue(allDocuments.contains(df1));
        assertTrue(allDocuments.contains(df2));
        assertFalse(allDocuments.contains(df3));

        dao.deleteDocumentFile(df2.getId());

        assertNotNull(dao.getDocumentFile(df1.getId()));
        assertNull(dao.getDocumentFile(df2.getId()));
        assertNull(dao.getDocumentFile(df3.getId()));
    }

    @Test
    public void test_Upload() throws Exception {
        System.out.println("test_Upload");

        File uploadFile=new File(testDirectory,"tempFile1.txt");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile df = dao.upload(uploadFile,false);

        assertTrue(uploadFile.exists());

        DocumentFile dfOut = dao.getDocumentFile(df.getId());

        assertEquals(df.getExternalFileName(),dfOut.getExternalFileName());

    }

    @Test
    public void test_Upload1() throws Exception {
        System.out.println("test_Upload1");
        File uploadFile=new File(testDirectory,"tempFile2.txt");
        FileUtils.writeStringToFile(uploadFile,"xyz", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile df = dao.upload(uploadFile,true);

        assertFalse(uploadFile.exists());

        DocumentFile dfOut = dao.getDocumentFile(df.getId());

        assertEquals(df.getExternalFileName(),dfOut.getExternalFileName());
    }

    @Test
    public void test_Download() throws Exception {
        System.out.println("test_Download");
        File uploadFile=new File(testDirectory,"tempFile2.txt");
        FileUtils.writeStringToFile(uploadFile,"xyz", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile df = dao.upload(uploadFile,true);

        assertFalse(uploadFile.exists());

        DocumentFile dfOut = dao.getDocumentFile(df.getId());

        assertEquals(df.getExternalFileName(),dfOut.getExternalFileName());

        // now download it

        dao.download(testDirectory,dfOut);

        assertTrue(uploadFile.exists());
    }

    @Test
    public void test_GetTransactionsWhereBuyerIsAccount() throws Exception {
        System.out.println("test_GetTransactionsWhereBuyerIsAccount");
        test_transactions_buyer_seller();
    }

    @Test
    public void test_GetTransactionsWhereSellerIsAccount() throws Exception {
        System.out.println("test_GetTransactionsWhereSellerIsAccount");
        test_transactions_buyer_seller();
    }

    public void test_transactions_buyer_seller(){
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        transakcja1.setSeller(konto1);
        transakcja1.setBuyer(konto2);

        transakcja2.setSeller(konto2);
        transakcja2.setBuyer(konto3);

        transakcja3.setSeller(konto3);
        transakcja3.setBuyer(konto1);

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Map<Integer,Transakcja> r1 = dao.getTransactionsWhereBuyerIsAccount(konto1);
        Map<Integer,Transakcja> r2 = dao.getTransactionsWhereBuyerIsAccount(konto2);
        Map<Integer,Transakcja> r3 = dao.getTransactionsWhereBuyerIsAccount(konto3);

        assertEquals(1,r1.size());
        assertEquals(1,r2.size());
        assertEquals(1,r3.size());

        assertTrue(r1.containsValue(transakcja3));
        assertTrue(r2.containsValue(transakcja1));
        assertTrue(r3.containsValue(transakcja2));

        Map<Integer,Transakcja> r11 = dao.getTransactionsWhereSellerIsAccount(konto1);
        Map<Integer,Transakcja> r21 = dao.getTransactionsWhereSellerIsAccount(konto2);
        Map<Integer,Transakcja> r31 = dao.getTransactionsWhereSellerIsAccount(konto3);

        assertEquals(1,r11.size());
        assertEquals(1,r21.size());
        assertEquals(1,r31.size());

        assertTrue(r11.containsValue(transakcja1));
        assertTrue(r21.containsValue(transakcja2));
        assertTrue(r31.containsValue(transakcja3));
    }

    @Test
    public void test_GetTransactionsForAccount() throws Exception {
        System.out.println("test_GetTransactionsForAccount");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        transakcja1.setSeller(konto1);
        transakcja1.setBuyer(konto2);

        transakcja2.setSeller(konto2);
        transakcja2.setBuyer(konto3);

        transakcja3.setSeller(konto3);
        transakcja3.setBuyer(konto1);

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Map<Integer,Transakcja> r1 = dao.getTransactionsForAccount(konto1);
        Map<Integer,Transakcja> r2 = dao.getTransactionsForAccount(konto2);
        Map<Integer,Transakcja> r3 = dao.getTransactionsForAccount(konto3);

        assertEquals(2,r1.size());
        assertEquals(2,r2.size());
        assertEquals(2,r3.size());

        assertTrue(r1.containsValue(transakcja1));
        assertTrue(r1.containsValue(transakcja3));

        assertTrue(r2.containsValue(transakcja1));
        assertTrue(r2.containsValue(transakcja2));

        assertTrue(r3.containsValue(transakcja2));
        assertTrue(r3.containsValue(transakcja3));
    }

    @Test
    public void test_getTransactionsBetweenAccounts(){

        System.out.println("test_getTransactionsBetweenAccounts");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        transakcja1.setSeller(konto1);
        transakcja1.setBuyer(konto2);

        transakcja2.setSeller(konto2);
        transakcja2.setBuyer(konto3);

        transakcja3.setSeller(konto3);
        transakcja3.setBuyer(konto1);

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Map<Integer,Transakcja> r1 = dao.getTransactionsBetweenAccounts(konto2,konto3);

        assertEquals(1,r1.size());

        assertTrue(r1.containsValue(transakcja2));
    }



    @Test
    public void test_GetTransactionsForAccount1() throws Exception {
        System.out.println("test_GetTransactionsForAccount1");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        transakcja1.setSeller(konto1);
        transakcja1.setBuyer(konto2);

        transakcja2.setSeller(konto2);
        transakcja2.setBuyer(konto3);

        transakcja3.setSeller(konto3);
        transakcja3.setBuyer(konto1);

        transakcja1.setTransactionDate(LocalDate.of(2018, Month.MARCH,1));
        transakcja2.setTransactionDate(LocalDate.of(2018, Month.JUNE,15));
        transakcja3.setTransactionDate(LocalDate.of(2018, Month.AUGUST,18));

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Map<Integer,Transakcja> r1 = dao.getTransactionsForAccount(konto1,LocalDate.of(2018, Month.JANUARY,1));
        Map<Integer,Transakcja> r2 = dao.getTransactionsForAccount(konto3,LocalDate.of(2018, Month.MAY,1));
        Map<Integer,Transakcja> r3 = dao.getTransactionsForAccount(konto3,LocalDate.of(2018, Month.SEPTEMBER,1));

        assertEquals(2,r1.size());
        assertEquals(2,r2.size());
        assertEquals(0,r3.size());

        assertTrue(r1.containsValue(transakcja1));
        assertFalse(r1.containsValue(transakcja2));
        assertTrue(r1.containsValue(transakcja3));

        assertTrue(r2.containsValue(transakcja2));
        assertTrue(r2.containsValue(transakcja3));
    }

    @Test
    public void test_GetTransactionsForAccount2() throws Exception {
        System.out.println("test_GetTransactionsForAccount2");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        transakcja1.setSeller(konto1);
        transakcja1.setBuyer(konto2);

        transakcja2.setSeller(konto2);
        transakcja2.setBuyer(konto3);

        transakcja3.setSeller(konto3);
        transakcja3.setBuyer(konto1);

        transakcja1.setTransactionDate(LocalDate.of(2018, Month.MARCH,1));
        transakcja2.setTransactionDate(LocalDate.of(2018, Month.JUNE,15));
        transakcja3.setTransactionDate(LocalDate.of(2018, Month.AUGUST,18));

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        Map<Integer,Transakcja> r1 = dao.getTransactionsForAccount(konto1,
                LocalDate.of(2018, Month.JANUARY,1),
                LocalDate.of(2018, Month.DECEMBER,1)  );
        Map<Integer,Transakcja> r2 = dao.getTransactionsForAccount(konto3,
                LocalDate.of(2018, Month.JULY,1),
                LocalDate.of(2018, Month.DECEMBER,1));
        Map<Integer,Transakcja> r3 = dao.getTransactionsForAccount(konto3,
                LocalDate.of(2018, Month.SEPTEMBER,1),
                LocalDate.of(2018, Month.NOVEMBER,1));

        assertEquals(2,r1.size());
        assertEquals(1,r2.size());
        assertEquals(0,r3.size());

        assertTrue(r1.containsValue(transakcja1));
        assertFalse(r1.containsValue(transakcja2));
        assertTrue(r1.containsValue(transakcja3));

        assertFalse(r2.containsValue(transakcja2));
        assertTrue(r2.containsValue(transakcja3));

    }

    @Test
    public void test_Flush() throws Exception {
        System.out.println("test_Flush");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        transakcja1.setSeller(konto1);
        transakcja1.setBuyer(konto2);

        transakcja2.setSeller(konto2);
        transakcja2.setBuyer(konto3);

        transakcja3.setSeller(konto3);
        transakcja3.setBuyer(konto1);

        transakcja1.setTransactionDate(LocalDate.of(2018, Month.MARCH,1));
        transakcja2.setTransactionDate(LocalDate.of(2018, Month.JUNE,15));
        transakcja3.setTransactionDate(LocalDate.of(2018, Month.AUGUST,18));

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        boolean errorOccured=false;
        try {
            dao.flush();
        }catch(Exception e){
            errorOccured=true;
        }
        assertFalse(errorOccured);

        assertEquals(transakcja1,dao.getTransakcja(transakcja1.getId()));
    }

    @Test
    public void test_Close() throws Exception {
        System.out.println("test_Close");
        Transakcja transakcja1 = new Transakcja();
        Transakcja transakcja2 = new Transakcja();
        Transakcja transakcja3 = new Transakcja();

        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        dao.save(konto1);
        dao.save(konto2);
        dao.save(konto3);

        transakcja1.setSeller(konto1);
        transakcja1.setBuyer(konto2);

        transakcja2.setSeller(konto2);
        transakcja2.setBuyer(konto3);

        transakcja3.setSeller(konto3);
        transakcja3.setBuyer(konto1);

        transakcja1.setTransactionDate(LocalDate.of(2018, Month.MARCH,1));
        transakcja2.setTransactionDate(LocalDate.of(2018, Month.JUNE,15));
        transakcja3.setTransactionDate(LocalDate.of(2018, Month.AUGUST,18));

        dao.save(transakcja1);
        dao.save(transakcja2);
        dao.save(transakcja3);

        boolean errorOccured=false;
        try {
            dao.close();
        }catch(Exception e){
            errorOccured=true;
        }
        assertFalse(errorOccured);

        try {
            dao.getTransakcja(transakcja1.getId());
        }catch(Exception e){
            errorOccured=true;
        }
        assertTrue(errorOccured);

    }


    @Test
    public void test_DeleteFile() throws Exception {
        System.out.println("test_DeleteFile");
        File uploadFile=new File(testDirectory,"tempFile1.txt");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile df = dao.upload(uploadFile,false);

        assertTrue(uploadFile.exists());

        DocumentFile dfOut = dao.getDocumentFile(df.getId());

        assertEquals(df.getExternalFileName(),dfOut.getExternalFileName());

        String internalFileName= dfOut.getInternalFileName();

        File internalfile = new File(rachunkiConfig.getFileStoreDirectory(),internalFileName);

        assertTrue(internalfile.exists());

        dao.delete(df);


        assertNull(dao.getDocumentFile(df.getId()));
        assertFalse(internalfile.exists());

        boolean debug=true;

    }

    @Test
    public void test_DeleteFile1() throws Exception {
        System.out.println("test_DeleteFile1");
        File uploadFile=new File(testDirectory,"tempFile1.txt");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile df = dao.upload(uploadFile,false);

        assertTrue(uploadFile.exists());

        DocumentFile dfOut = dao.getDocumentFile(df.getId());

        assertEquals(df.getExternalFileName(),dfOut.getExternalFileName());

        String internalFileName= dfOut.getInternalFileName();

        File internalfile = new File(rachunkiConfig.getFileStoreDirectory(),internalFileName);

        assertTrue(internalfile.exists());

        dao.deleteDocumentFile(df.getId());

        assertNull(dao.getDocumentFile(df.getId()));
        assertFalse(internalfile.exists());

        boolean debug=true;
    }

    @Test
    public void test_GetPathToFile() throws Exception {
        System.out.println("test_GetPathToFile");
        File uploadFile=new File(testDirectory,"tempFile1.txt");
        FileUtils.writeStringToFile(uploadFile,"asdfg", TomwStringUtils.ENCODING_ISO);

        assertTrue(uploadFile.exists());

        DocumentFile df = dao.upload(uploadFile,false);

        assertTrue(uploadFile.exists());

        DocumentFile dfOut = dao.getDocumentFile(df.getId());

        File pathToFile = dao.getPathToFile(dfOut.getInternalFileName());
        assertTrue(pathToFile.exists());

        dao.delete(dfOut);

        assertFalse(pathToFile.exists());

        File pathToFile2 = dao.getPathToFile(dfOut.getInternalFileName());

        assertFalse(pathToFile2.exists());

    }



    @Test
    public void test_GetInternalSubdirectory() throws Exception {
        System.out.println("test_GetInternalSubdirectory");
        String s = dao.getInternalSubdirectory(12345678);
        assertEquals("12345",s);
    }

    @Test
    public void test_InternalNameWithInternalDirectory() throws Exception {
        System.out.println("test_InternalNameWithInternalDirectory");
        String s = dao.internalNameWithInternalDirectory(12345678,"txt");
        assertEquals("12345/12345678.txt",s);
    }



}