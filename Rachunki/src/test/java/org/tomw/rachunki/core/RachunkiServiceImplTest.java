package org.tomw.rachunki.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tomw.config.SelfIdentificationService;
import org.tomw.daoutils.DataIntegrityException;
import org.tomw.documentfile.DocumentFile;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.config.RachunkiConfigurationImpl;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RachunkiServiceImplTest {

    private RachunkiService service;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File testDirectory;

    @Before
    public void setUp() throws Exception {

        testDirectory = folder.newFolder("testDirectory");

        SelfIdentificationService selfIdentificationService = new SelfIdentificationServiceRachunki();

        RachunkiConfiguration rachunkiConfig = new RachunkiConfigurationImpl(selfIdentificationService);

        TomwFileUtils.deleteDirectoryWithFiles(rachunkiConfig.getFileStoreDirectory());

        RachunkiDao dao = RachunkiDaoHibernateTestUtils.createDao(rachunkiConfig);

        service = new RachunkiServiceImpl(dao, rachunkiConfig);

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void test_GetCheckDocument() throws Exception {
        System.out.println("test_GetCheckDocument");
        test_createCheck_delete();
    }

    @Test
    public void test_createCheck() throws IOException {
        System.out.println("test_createCheck");

        test_createCheck_delete();
    }

    private File createTemporaryFile(String name) throws IOException {
        File file = new File(testDirectory, name);
        FileUtils.writeStringToFile(file, "temporary file " + name, TomwStringUtils.ENCODING_ISO);
        return file;
    }

    private void test_createCheck_delete() throws IOException {
        String checkNumber = "1234";
        String checkComment = "check comment";
        String memo = "check memo";
        boolean deleteSourceFiles = true;

        File imageFront = createTemporaryFile("temp_" + checkNumber + "F.jpg");
        assertTrue(imageFront.exists());

        File imageBack = createTemporaryFile("temp_" + checkNumber + "B.jpg");
        assertTrue(imageBack.exists());

        CheckDocument check = service.createCheck(
                checkNumber,
                checkComment,
                memo,
                imageFront,
                imageBack, deleteSourceFiles);

        CheckDocument checkOut = service.getCheckDocument(check.getId());
        assertEquals(check, checkOut);

        assertFalse(imageFront.exists());
        assertFalse(imageBack.exists());

        service.delete(check);

        assertNull(service.getCheckDocument(check.getId()));

    }

    @Test
    public void test_GetAllCheckDocuments() throws Exception {
        System.out.println("test_GetAllCheckDocuments");
        assertEquals(0, service.getAllCheckDocuments().size());

        CheckDocument check1 = service.createCheck(
                "1001",
                "comment 1",
                "memo 1",
                createTemporaryFile("1001F.jpg"),
                createTemporaryFile("1001B.jpg"),
                true);
        CheckDocument check2 = service.createCheck(
                "1002",
                "comment 2",
                "memo 2",
                createTemporaryFile("1002F.jpg"),
                createTemporaryFile("1002B.jpg"),
                true);
        CheckDocument check3 = service.createCheck(
                "1003",
                "comment 3",
                "memo 3",
                createTemporaryFile("1003F.jpg"),
                createTemporaryFile("1003B.jpg"),
                true);
        service.save(check1);
        service.save(check2);
        service.save(check3);

        ObservableList<CheckDocument> result = service.getAllCheckDocuments();
        assertEquals(3, result.size());
        assertTrue(result.contains(check1));
        assertTrue(result.contains(check2));
        assertTrue(result.contains(check3));

        service.delete(check1);

        ObservableList<CheckDocument> result2 = service.getAllCheckDocuments();
        assertEquals(2, result2.size());
        assertFalse(result2.contains(check1));
        assertTrue(result2.contains(check2));
        assertTrue(result2.contains(check3));
    }

    @Test
    public void test_GetAllCheckDocumentsWithCheckNumber() throws Exception {
        System.out.println("test_GetAllCheckDocumentsWithCheckNumber");
        assertEquals(0, service.getAllCheckDocuments().size());

        CheckDocument check1 = service.createCheck(
                "1001",
                "comment 1",
                "memo 1",
                createTemporaryFile("1001F.jpg"),
                createTemporaryFile("1001B.jpg"),
                true);
        CheckDocument check2 = service.createCheck(
                "1002",
                "comment 2",
                "memo 2",
                createTemporaryFile("1002F.jpg"),
                createTemporaryFile("1002B.jpg"),
                true);
        CheckDocument check3 = service.createCheck(
                "1003",
                "comment 3",
                "memo 3",
                createTemporaryFile("1003F.jpg"),
                createTemporaryFile("1003B.jpg"),
                true);
        // the same check number as check 3
        CheckDocument check4 = service.createCheck(
                "1003",
                "comment 4",
                "memo 4",
                createTemporaryFile("2-1003F.jpg"),
                createTemporaryFile("2-1003B.jpg"),
                true);
        service.save(check1);
        service.save(check2);
        service.save(check3);
        service.save(check4);

        ObservableList<CheckDocument> result = service.getAllCheckDocumentsWithCheckNumber("1003");
        assertEquals(2, result.size());
        assertFalse(result.contains(check1));
        assertFalse(result.contains(check2));
        assertTrue(result.contains(check3));
        assertTrue(result.contains(check4));
    }

    @Test
    public void test_GetCheckDocuments() throws Exception {
        System.out.println("test_GetCheckDocuments");
        // create checks
        CheckDocument check1 = service.createCheck(
                "1001",
                "comment 1",
                "memo 1",
                createTemporaryFile("1001F.jpg"),
                createTemporaryFile("1001B.jpg"),
                true);
        CheckDocument check2 = service.createCheck(
                "1002",
                "comment 2",
                "memo 2",
                createTemporaryFile("1002F.jpg"),
                createTemporaryFile("1002B.jpg"),
                true);
        CheckDocument check3 = service.createCheck(
                "1003",
                "comment 3",
                "memo 3",
                createTemporaryFile("1003F.jpg"),
                createTemporaryFile("1003B.jpg"),
                true);
        // the same check number as check 3
        CheckDocument check4 = service.createCheck(
                "1003",
                "comment 4",
                "memo 4",
                createTemporaryFile("2-1003F.jpg"),
                createTemporaryFile("2-1003B.jpg"),
                true);

        // create transactions
        Transakcja transaction1 = new Transakcja();
        Transakcja transaction2 = new Transakcja();
        Transakcja transaction3 = new Transakcja();
        Transakcja transaction4 = new Transakcja();
        Transakcja transaction5 = new Transakcja();

        transaction1.setCheckDocument(check1);
        transaction2.setCheckDocument(check2);
        transaction3.setCheckDocument(check3);
        transaction4.setCheckDocument(check4);

        Konto konto1 = new Konto();
        konto1.setShortName("konto1");
        Konto konto2 = new Konto();
        konto2.setShortName("konto2");
        Konto konto3 = new Konto();
        konto3.setShortName("konto3");

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        transaction1.setSeller(konto1);
        transaction1.setBuyer(konto2);

        transaction2.setSeller(konto2);
        transaction2.setBuyer(konto3);

        transaction3.setSeller(konto3);
        transaction3.setBuyer(konto1);

        transaction4.setSeller(konto1);
        transaction4.setBuyer(konto2);

        transaction1.setTransactionDate(LocalDate.now());
        transaction2.setTransactionDate(LocalDate.now());
        transaction3.setTransactionDate(LocalDate.now());
        transaction4.setTransactionDate(LocalDate.now());
        transaction5.setTransactionDate(LocalDate.now());

        Konto seller = new Konto();
        Konto buyer = new Konto();
        transaction5.setBuyerAndSeller(buyer, seller);

        service.save(transaction1);
        service.save(transaction2);
        service.save(transaction3);
        service.save(transaction4);
        service.save(transaction5);

        Collection<CheckDocument> result = service.getCheckDocuments(konto1);
        assertEquals(3, result.size());
        assertTrue(result.contains(check1));
        assertTrue(result.contains(check3));
        assertTrue(result.contains(check4));

    }

    @Test
    public void test_DisplayCheckDocuments() throws Exception {
        System.out.println("test_DisplayCheckDocuments");
        test_displaycheckDocumentsOperations();
    }

    @Test
    public void test_DisplayCheckDocuments1() throws Exception {
        System.out.println("test_DisplayCheckDocuments1");
        test_displaycheckDocumentsOperations();
    }

    private void test_displaycheckDocumentsOperations() throws IOException {
        CheckDocument check1 = service.createCheck(
                "1001",
                "comment 1",
                "memo 1",
                createTemporaryFile("1001F.jpg"),
                createTemporaryFile("1001B.jpg"),
                true);
        CheckDocument check2 = service.createCheck(
                "1002",
                "comment 2",
                "memo 2",
                createTemporaryFile("1002F.jpg"),
                createTemporaryFile("1002B.jpg"),
                true);
        CheckDocument check3 = service.createCheck(
                "1003",
                "comment 3",
                "memo 3",
                createTemporaryFile("1003F.jpg"),
                createTemporaryFile("1003B.jpg"),
                true);
        // the same check number as check 3
        CheckDocument check4 = service.createCheck(
                "1003",
                "comment 4",
                "memo 4",
                createTemporaryFile("2-1003F.jpg"),
                createTemporaryFile("2-1003B.jpg"),
                true);

        service.save(check1);
        service.save(check2);
        service.save(check3);
        service.save(check4);

        assertEquals(0, service.getDisplayedCheckDocuments().size());

        service.displayAllCheckDocuments();
        assertEquals(4, service.getDisplayedCheckDocuments().size());

        service.clearDisplayedCheckDocuments();
        assertEquals(0, service.getDisplayedCheckDocuments().size());

        Collection<CheckDocument> forDisplay = new ArrayList<>();
        forDisplay.add(check1);
        forDisplay.add(check3);

        service.displayCheckDocuments(forDisplay);
        assertEquals(2, service.getDisplayedCheckDocuments().size());
        assertTrue(service.getDisplayedCheckDocuments().contains(check1));
        assertTrue(service.getDisplayedCheckDocuments().contains(check3));

    }

    @Test
    public void test_DisplayAllCheckDocuments() throws Exception {
        System.out.println("test_DisplayAllCheckDocuments");
        test_displaycheckDocumentsOperations();
    }

    @Test
    public void test_ClearDisplayedCheckDocuments() throws Exception {
        System.out.println("test_ClearDisplayedCheckDocuments");
        test_displaycheckDocumentsOperations();
    }

    @Test
    public void test_GetDocuments() throws Exception {
        System.out.println("test_GetDocuments");
        DocumentFile df1 = new DocumentFile();
        DocumentFile df2 = new DocumentFile();

        // the same thing for accounts

        Konto konto = new Konto();
        konto.getDocuments().add(df1);
        konto.getDocuments().add(df2);
        service.save(konto);
        Collection<DocumentFile> result2 = service.getDocuments(konto);
        assertEquals(konto.getDocuments().size(), result2.size());
        for (DocumentFile df : konto.getDocuments()) {
            assertTrue(result2.contains(df));
        }

    }

    @Test
    public void test_GetDocuments1() throws Exception {
        System.out.println("test_GetDocuments1");

        DocumentFile df1 = new DocumentFile();
        DocumentFile df2 = new DocumentFile();
        Transakcja t1 = new Transakcja();

        t1.getDocuments().add(df1);
        t1.getDocuments().add(df2);

        t1.setTransactionDate(LocalDate.now());

        Konto seller = new Konto();
        Konto buyer = new Konto();

        t1.setBuyerAndSeller(buyer, seller);

        service.save(t1);

        Collection<DocumentFile> result = service.getDocuments(t1);
        assertEquals(t1.getDocuments().size(), result.size());
        for (DocumentFile df : t1.getDocuments()) {
            assertTrue(result.contains(df));
        }

    }

    @Test
    // test Transakcja getTransaction(CheckDocument checkDocument);
    public void test_GetTransaction() throws Exception {
        System.out.println("test_GetTransaction");
        // create checks
        CheckDocument check1 = service.createCheck(
                "1001",
                "comment 1",
                "memo 1",
                createTemporaryFile("1001F.jpg"),
                createTemporaryFile("1001B.jpg"),
                true);
        CheckDocument check2 = service.createCheck(
                "1002",
                "comment 2",
                "memo 2",
                createTemporaryFile("1002F.jpg"),
                createTemporaryFile("1002B.jpg"),
                true);
        CheckDocument check3 = service.createCheck(
                "1003",
                "comment 3",
                "memo 3",
                createTemporaryFile("1003F.jpg"),
                createTemporaryFile("1003B.jpg"),
                true);
        // the same check number as check 3
        CheckDocument check4 = service.createCheck(
                "1003",
                "comment 4",
                "memo 4",
                createTemporaryFile("2-1003F.jpg"),
                createTemporaryFile("2-1003B.jpg"),
                true);

        // create transactions
        Transakcja transaction1 = new Transakcja();
        Transakcja transaction2 = new Transakcja();
        Transakcja transaction3 = new Transakcja();
        Transakcja transaction4 = new Transakcja();
        Transakcja transaction5 = new Transakcja();

        transaction1.setCheckDocument(check1);
        transaction2.setCheckDocument(check2);
        transaction3.setCheckDocument(check3);
        transaction4.setCheckDocument(check4);

        transaction1.setTransactionDate(LocalDate.now());
        transaction2.setTransactionDate(LocalDate.now());
        transaction3.setTransactionDate(LocalDate.now());
        transaction4.setTransactionDate(LocalDate.now());
        transaction5.setTransactionDate(LocalDate.now());

        Konto seller = new Konto();
        Konto buyer = new Konto();

        transaction1.setBuyerAndSeller(buyer, seller);
        transaction2.setBuyerAndSeller(buyer, seller);
        transaction3.setBuyerAndSeller(buyer, seller);
        transaction4.setBuyerAndSeller(buyer, seller);
        transaction5.setBuyerAndSeller(buyer, seller);

        service.save(transaction1);
        service.save(transaction2);
        service.save(transaction3);
        service.save(transaction4);
        service.save(transaction5);

        Transakcja result = service.getTransaction(check1);
        assertEquals(transaction1, result);

        Transakcja result2 = service.getTransaction(null);
        assertEquals(null, result2);

    }

    /**
     * test save method
     * @throws Exception if something goes wrong
     */
    @Test
    public void test_Save() throws Exception {
        System.out.println("test_Save");
        String checkNumber = "8888";
        CheckDocument check = new CheckDocument();
        check.setCheckNumber(checkNumber);
        service.save(check);
        assertEquals(check, service.getCheckDocument(check.getId()));
        service.delete(check);
        assertNull(service.getCheckDocument(check.getId()));
    }

    @Test
    public void test_Delete() throws Exception {
        System.out.println("test_Delete");
        test_createCheck_delete();
    }

    @Test
    public void test_DeleteCheckDocument() throws Exception {
        System.out.println("test_DeleteCheckDocument");
        String checkNumber = "2222";
        CheckDocument check = new CheckDocument();
        check.setCheckNumber(checkNumber);
        service.save(check);
        assertEquals(check, service.getCheckDocument(check.getId()));
        service.deleteCheckDocument(check.getId());
        assertNull(service.getCheckDocument(check.getId()));
    }

    @Test
    public void test_GetAccount() throws Exception {
        System.out.println("test_GetAccount");
        test_save_get_delete_konto();
    }

    private void test_save_get_delete_konto() throws RachunkiException {
        Konto konto = new Konto();
        konto.setShortName("short name");
        konto.setFullName("full name");

        service.save(konto);
        assertEquals(konto, service.getAccount(konto.getId()));

        service.delete(konto);

        assertNull(service.getAccount(konto.getId()));
    }

    @Test
    public void test_GetAllAccounts() throws Exception {
        System.out.println("test_GetAllAccounts");
        Konto konto1 = new Konto();
        konto1.setShortName("short name1");
        Konto konto2 = new Konto();
        konto2.setShortName("short name2");
        Konto konto3 = new Konto();
        konto3.setShortName("short name3");
        Konto konto4 = new Konto();
        konto4.setShortName("short name4");

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        Collection<Konto> result = service.getAllAccounts();
        assertEquals(3, result.size());
        assertTrue(result.contains(konto1));
        assertTrue(result.contains(konto2));
        assertTrue(result.contains(konto3));
        assertFalse(result.contains(konto4));
    }

    @Test
    public void test_GetAllActiveAccounts() throws Exception {
        System.out.println("test_GetAllActiveAccounts");
        Konto konto1 = new Konto();
        konto1.setShortName("short name1");
        Konto konto2 = new Konto();
        konto2.setShortName("short name2");
        Konto konto3 = new Konto();
        konto3.setShortName("short name3");
        Konto konto4 = new Konto();
        konto4.setShortName("short name4");

        konto1.setAccountActive(true);
        konto2.setAccountActive(true);
        konto3.setAccountActive(false);
        konto4.setAccountActive(true);

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        Collection<Konto> result = service.getAllActiveAccounts();
        assertEquals(2, result.size());
        assertTrue(result.contains(konto1));
        assertTrue(result.contains(konto2));
        assertFalse(result.contains(konto3));
        assertFalse(result.contains(konto4));
    }

    @Test
    public void test_GetAllLocalAccounts() throws Exception {
        System.out.println("test_GetAllLocalAccounts");

        Konto konto1 = new Konto();
        konto1.setShortName("short name1");
        Konto konto2 = new Konto();
        konto2.setShortName("short name2");
        Konto konto3 = new Konto();
        konto3.setShortName("short name3");
        Konto konto4 = new Konto();
        konto4.setShortName("short name4");

        konto1.setAccountLocal(true);
        konto2.setAccountLocal(true);
        konto3.setAccountLocal(false);
        konto4.setAccountLocal(true);

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        Collection<Konto> result = service.getAllLocalAccounts();
        assertEquals(2, result.size());
        assertTrue(result.contains(konto1));
        assertTrue(result.contains(konto2));
        assertFalse(result.contains(konto3));
        assertFalse(result.contains(konto4));
    }

    @Test
    public void test_GetAllActiveLocalAccounts() throws Exception {
        System.out.println("test_GetAllActiveLocalAccounts");
        Konto konto1 = new Konto();
        konto1.setShortName("short name1");
        Konto konto2 = new Konto();
        konto2.setShortName("short name2");
        Konto konto3 = new Konto();
        konto3.setShortName("short name3");
        Konto konto4 = new Konto();
        konto4.setShortName("short name4");

        konto1.setAccountLocal(true);
        konto2.setAccountLocal(true);
        konto3.setAccountLocal(false);
        konto4.setAccountLocal(true);

        konto1.setAccountActive(true);
        konto2.setAccountActive(false);
        konto3.setAccountActive(false);
        konto4.setAccountActive(true);

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        Collection<Konto> result = service.getAllActiveLocalAccounts();
        assertEquals(1, result.size());
        assertTrue(result.contains(konto1));
        assertFalse(result.contains(konto2));
        assertFalse(result.contains(konto3));
        assertFalse(result.contains(konto4));
    }

    @Test
    public void test_DisplayAllAccounts() throws Exception {
        System.out.println("test_DisplayAllAccounts");
        test_display_clear_accounts();
    }

    @Test
    public void test_DisplayAccounts() throws Exception {
        System.out.println("test_DisplayAccounts");
        test_display_clear_accounts();
    }

    @Test
    public void test_DisplayAccounts1() throws Exception {
        System.out.println("test_DisplayAccounts1");
        test_display_clear_accounts();
    }

    @Test
    public void test_ClearDisplayedAccounts() throws Exception {
        System.out.println("test_ClearDisplayedAccounts");

        test_display_clear_accounts();
    }

    /**
     * Test the following methods:
     * displayAllAccounts();
     * displayAccounts(Collection<Konto> accounts);
     * displayAccounts(ObservableList<Konto> accounts);
     * clearDisplayedAccounts();
     *
     * @throws RachunkiException if something is wrong
     */
    private void test_display_clear_accounts() throws RachunkiException {
        Konto konto1 = new Konto();
        konto1.setShortName("short name1");
        Konto konto2 = new Konto();
        konto2.setShortName("short name2");
        Konto konto3 = new Konto();
        konto3.setShortName("short name3");
        Konto konto4 = new Konto();
        konto4.setShortName("short name4");

        List<Konto> allAccounts = new ArrayList<>();
        allAccounts.add(konto1);
        allAccounts.add(konto2);
        allAccounts.add(konto3);
        allAccounts.add(konto4);

        List<Konto> threeAccounts = new ArrayList<>();
        threeAccounts.add(konto1);
        threeAccounts.add(konto2);
        threeAccounts.add(konto3);

        service.displayAllAccounts();
        assertEquals(0, service.getDisplayedAccounts().size());

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);
        service.save(konto4);

        service.displayAllAccounts();
        assertEquals(4, service.getDisplayedAccounts().size());
        assertTrue(service.getDisplayedAccounts().contains(konto1));
        assertTrue(service.getDisplayedAccounts().contains(konto2));
        assertTrue(service.getDisplayedAccounts().contains(konto3));
        assertTrue(service.getDisplayedAccounts().contains(konto4));

        service.delete(konto1);

        service.displayAllAccounts();
        assertEquals(3, service.getDisplayedAccounts().size());
        assertFalse(service.getDisplayedAccounts().contains(konto1));
        assertTrue(service.getDisplayedAccounts().contains(konto2));
        assertTrue(service.getDisplayedAccounts().contains(konto3));
        assertTrue(service.getDisplayedAccounts().contains(konto4));

        service.clearDisplayedAccounts();
        assertEquals(0, service.getDisplayedAccounts().size());

        service.displayAccounts(threeAccounts);
        assertEquals(3, service.getDisplayedAccounts().size());
        assertTrue(service.getDisplayedAccounts().contains(konto1));
        assertTrue(service.getDisplayedAccounts().contains(konto2));
        assertTrue(service.getDisplayedAccounts().contains(konto3));
        assertFalse(service.getDisplayedAccounts().contains(konto4));

        service.clearDisplayedAccounts();
        assertEquals(0, service.getDisplayedAccounts().size());

        ObservableList<Konto> observableList = FXCollections.observableArrayList();
        observableList.addAll(threeAccounts);

        service.displayAccounts(observableList);
        assertEquals(3, service.getDisplayedAccounts().size());
        assertTrue(service.getDisplayedAccounts().contains(konto1));
        assertTrue(service.getDisplayedAccounts().contains(konto2));
        assertTrue(service.getDisplayedAccounts().contains(konto3));
        assertFalse(service.getDisplayedAccounts().contains(konto4));
    }

    @Test
    public void test_GetTransactionsforAccount() throws Exception {
        System.out.println("test_GetTransactionsforAccount");
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();

        Konto k1 = new Konto();
        Konto k2 = new Konto();
        Konto k3 = new Konto();

        t1.setBuyer(k1);
        t1.setSeller(k2);

        t2.setBuyer(k2);
        t2.setSeller(k3);

        t3.setBuyer(k3);
        t3.setSeller(k1);

        t1.setTransactionDate(LocalDate.now());
        t2.setTransactionDate(LocalDate.now());
        t3.setTransactionDate(LocalDate.now());

        service.save(t1);
        service.save(t2);
        service.save(t3);

        Collection<Transakcja> transactionsForK1 = service.getTransactionsForAccount(k1);

        assertEquals(2, transactionsForK1.size());
        assertTrue(transactionsForK1.contains(t1));
        assertTrue(transactionsForK1.contains(t3));

    }

    @Test
    public void test_DisplayTransactionsForAccount() throws Exception {
        System.out.println("test_DisplayTransactionsforAccount");
        testDisplayingTransactions();
    }

    @Test
    public void test_ClearDisplayedTransactions() throws Exception {
        System.out.println("test_ClearDisplayedTransactions");
        testDisplayingTransactions();
    }

    private void testDisplayingTransactions() {
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();

        Konto k1 = new Konto();
        Konto k2 = new Konto();
        Konto k3 = new Konto();

        t1.setBuyer(k1);
        t1.setSeller(k2);

        t2.setBuyer(k2);
        t2.setSeller(k3);

        t3.setBuyer(k3);
        t3.setSeller(k1);

        t1.setTransactionDate(LocalDate.now());
        t2.setTransactionDate(LocalDate.now());
        t3.setTransactionDate(LocalDate.now());

        service.save(t1);
        service.save(t2);
        service.save(t3);

        assertEquals(0, service.getDisplayedTransactions().size());

        service.displayTransactionsForAccount(k1);
        assertEquals(2, service.getDisplayedTransactions().size());
        assertTrue(service.getDisplayedTransactions().contains(t1));
        assertTrue(service.getDisplayedTransactions().contains(t3));

        service.clearDisplayedTransactions();
        assertEquals(0, service.getDisplayedTransactions().size());

        Collection<Transakcja> list1 = new ArrayList<>();
        list1.add(t1);
        service.displayTransactions(list1);
        assertEquals(1, service.getDisplayedTransactions().size());
        assertTrue(service.getDisplayedTransactions().contains(t1));

        service.clearDisplayedTransactions();
        assertEquals(0, service.getDisplayedTransactions().size());

        ObservableList<Transakcja> list2 = FXCollections.observableArrayList();
        list2.add(t2);
        service.displayTransactions(list2);
        assertEquals(1, service.getDisplayedTransactions().size());
        assertTrue(service.getDisplayedTransactions().contains(t2));

    }

    @Test
    public void test_Save1() throws Exception {
        System.out.println("test_Save Konto");
        test_save_get_delete_konto();
    }

    @Test
    public void test_GetTransactionsforAccount1() throws Exception {
        System.out.println("test_GetTransactionsforAccount1");
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();

        Konto k1 = new Konto();
        Konto k2 = new Konto();
        Konto k3 = new Konto();

        t1.setBuyer(k1);
        t1.setSeller(k2);
        t1.setTransactionDate(LocalDate.of(2018, 10, 15));

        t2.setBuyer(k2);
        t2.setSeller(k3);
        t2.setTransactionDate(LocalDate.of(2018, 11, 15));

        t3.setBuyer(k3);
        t3.setSeller(k1);
        t3.setTransactionDate(LocalDate.of(2018, 12, 15));

        service.save(t1);
        service.save(t2);
        service.save(t3);

        Collection<Transakcja> result = service.getTransactionsForAccount(
                k1,
                LocalDate.of(2018, 11, 1));
        assertEquals(1, result.size());
        assertTrue(result.contains(t3));
    }

    @Test
    public void test_DisplayTransactionsforAccount1() throws Exception {
        System.out.println("test_DisplayTransactionsforAccount1");
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();
        Transakcja t4 = new Transakcja();

        Konto k1 = new Konto();
        Konto k2 = new Konto();
        Konto k3 = new Konto();

        t1.setBuyer(k1);
        t1.setSeller(k2);

        t2.setBuyer(k2);
        t2.setSeller(k3);

        t3.setBuyer(k3);
        t3.setSeller(k1);

        t4.setBuyer(k3);
        t4.setSeller(k2);

        t1.setTransactionDate(LocalDate.of(2018, 1, 15));
        t2.setTransactionDate(LocalDate.of(2018, 2, 15));
        t3.setTransactionDate(LocalDate.of(2018, 3, 15));
        t4.setTransactionDate(LocalDate.of(2018, 4, 15));

        service.save(t1);
        service.save(t2);
        service.save(t3);
        service.save(t4);

        assertEquals(0, service.getDisplayedTransactions().size());

        service.displayTransactionsForAccount(k1);
        assertEquals(2, service.getDisplayedTransactions().size());
        assertTrue(service.getDisplayedTransactions().contains(t1));
        assertTrue(service.getDisplayedTransactions().contains(t3));

        service.clearDisplayedTransactions();
        assertEquals(0, service.getDisplayedTransactions().size());

        service.displayTransactionsForAccount(k1, LocalDate.of(2018, 2, 1));
        assertEquals(1, service.getDisplayedTransactions().size());
        assertFalse(service.getDisplayedTransactions().contains(t1));
        assertTrue(service.getDisplayedTransactions().contains(t3));

        service.clearDisplayedTransactions();
        assertEquals(0, service.getDisplayedTransactions().size());

        service.displayTransactionsForAccount(
                k3,
                LocalDate.of(2018, 2, 1),
                LocalDate.of(2018, 4, 1)
        );
        assertEquals(2, service.getDisplayedTransactions().size());
        assertFalse(service.getDisplayedTransactions().contains(t1));
        assertTrue(service.getDisplayedTransactions().contains(t2));
        assertTrue(service.getDisplayedTransactions().contains(t3));
        assertFalse(service.getDisplayedTransactions().contains(t4));
    }

    @Test
    public void test_DisplayTransactions() throws Exception {
        System.out.println("test_DisplayTransactions");
        testDisplayingTransactions();
    }

    @Test
    public void test_DisplayTransactions1() throws Exception {
        System.out.println("test_DisplayTransactions1");
        testDisplayingTransactions();
    }

    @Test
    public void test_GetTransactionsforAccount2() throws Exception {
        System.out.println("test_GetTransactionsforAccount2");
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();
        Transakcja t4 = new Transakcja();

        Konto k1 = new Konto();
        Konto k2 = new Konto();
        Konto k3 = new Konto();

        t1.setBuyer(k1);
        t1.setSeller(k2);
        t1.setTransactionDate(LocalDate.of(2018, 9, 15));

        t2.setBuyer(k2);
        t2.setSeller(k3);
        t2.setTransactionDate(LocalDate.of(2018, 10, 15));

        t3.setBuyer(k3);
        t3.setSeller(k1);
        t3.setTransactionDate(LocalDate.of(2018, 11, 15));

        t4.setBuyer(k1);
        t4.setSeller(k3);
        t4.setTransactionDate(LocalDate.of(2018, 12, 15));

        service.save(t1);
        service.save(t2);
        service.save(t3);

        Collection<Transakcja> result = service.getTransactionsForAccount(
                k1,
                LocalDate.of(2018, 11, 1),
                LocalDate.of(2018, 12, 1));
        assertEquals(1, result.size());
        assertTrue(result.contains(t3));
    }


    @Test
    public void test_GetAccounts() throws Exception {
        System.out.println("test_GetAccounts");
        Konto konto1 = new Konto();
        konto1.setShortName("short name1");
        Konto konto2 = new Konto();
        konto2.setShortName("short name2");
        Konto konto3 = new Konto();
        konto3.setShortName("short name3");
        Konto konto4 = new Konto();
        konto4.setShortName("short name4");

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        Collection<Integer> ids = new ArrayList<>();
        ids.add(konto1.getId());
        ids.add(konto2.getId());
        ids.add(konto3.getId());

        Collection<Konto> result = service.getAccounts(ids);
        assertEquals(3, result.size());
        assertTrue(result.contains(konto1));
        assertTrue(result.contains(konto2));
        assertTrue(result.contains(konto3));
        assertFalse(result.contains(konto4));
    }

    /**
     * test service.getTransakcja(int) and service.delete(Transakcja)
     */
    @Test
    public void test_GetTransakcja() throws Exception {
        System.out.println("test_GetTransakcja");
        Transakcja t1 = new Transakcja(LocalDate.now(), new Konto(), new Konto());
        Transakcja t2 = new Transakcja(LocalDate.now(), new Konto(), new Konto());
        Transakcja t3 = new Transakcja(LocalDate.now(), new Konto(), new Konto());

        service.save(t1);
        service.save(t2);
        service.save(t3);

        assertEquals(t1, service.getTransakcja(t1.getId()));
        assertEquals(t2, service.getTransakcja(t2.getId()));
        assertEquals(t3, service.getTransakcja(t3.getId()));

        service.delete(t1);
        assertNull(service.getTransakcja(t1.getId()));
        assertEquals(t2, service.getTransakcja(t2.getId()));
        assertEquals(t3, service.getTransakcja(t3.getId()));
    }

    /**
     * test service.getTransakcjaByOldId(string)
     */
    @Test
    public void test_GetTransakcjaByOldId() throws Exception {
        System.out.println("test_GetTransakcjaByOldId");
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();

        t1.setTransactionDate(LocalDate.now());
        t2.setTransactionDate(LocalDate.now());
        t3.setTransactionDate(LocalDate.now());

        Konto seller = new Konto();
        Konto buyer = new Konto();

        t1.setBuyerAndSeller(buyer, seller);
        t2.setBuyerAndSeller(buyer, seller);
        t3.setBuyerAndSeller(buyer, seller);

        List<String> oldIds1 = new ArrayList<>();
        oldIds1.add("AAAA");
        oldIds1.add("BBBB");
        oldIds1.add("CCCC");

        List<String> oldIds2 = new ArrayList<>();
        oldIds2.add("XXXX");

        List<String> oldIds3 = new ArrayList<>();

        t1.setOldIds(oldIds1);
        t2.setOldIds(oldIds2);
        t3.setOldIds(oldIds3);

        t1.setTransactionDate(LocalDate.now());
        t2.setTransactionDate(LocalDate.now());
        t3.setTransactionDate(LocalDate.now());

        service.save(t1);
        service.save(t2);
        service.save(t3);

        assertEquals(t1, service.getTransakcjaByOldId("AAAA"));
        assertEquals(t1, service.getTransakcjaByOldId("BBBB"));
        assertEquals(t2, service.getTransakcjaByOldId("XXXX"));

        service.delete(t1);
        assertNull(service.getTransakcjaByOldId("AAAA"));
        assertNull(service.getTransakcjaByOldId("BBBB"));
        assertEquals(t2, service.getTransakcjaByOldId("XXXX"));
    }

    @Test
    public void test_GetTransakcja1() throws Exception {
        System.out.println("test_GetTransakcja1");
        Transakcja transaction1 = new Transakcja(LocalDate.now(), new Konto(), new Konto());
        transaction1.setComment("comment for transaction 1");
        service.save(transaction1);
        Transakcja tout = service.getTransakcja(transaction1.getId());
        assertEquals(transaction1, tout);
    }

    @Test
    public void test_GetTransactionsForCheckNumber() throws Exception {
        System.out.println("test_GetTransactionsForCheckNumber");

        // create checks
        CheckDocument check1 = service.createCheck(
                "1001",
                "comment 1",
                "memo 1",
                createTemporaryFile("1001F.jpg"),
                createTemporaryFile("1001B.jpg"),
                true);
        CheckDocument check2 = service.createCheck(
                "1002",
                "comment 2",
                "memo 2",
                createTemporaryFile("1002F.jpg"),
                createTemporaryFile("1002B.jpg"),
                true);
        CheckDocument check3 = service.createCheck(
                "1003",
                "comment 3",
                "memo 3",
                createTemporaryFile("1003F.jpg"),
                createTemporaryFile("1003B.jpg"),
                true);
        // the same check number as check 3
        CheckDocument check4 = service.createCheck(
                "1003",
                "comment 4",
                "memo 4",
                createTemporaryFile("2-1003F.jpg"),
                createTemporaryFile("2-1003B.jpg"),
                true);

        // create transactions
        Transakcja transaction1 = new Transakcja();
        Transakcja transaction2 = new Transakcja();
        Transakcja transaction3 = new Transakcja();
        Transakcja transaction4 = new Transakcja();
        Transakcja transaction5 = new Transakcja();

        transaction1.setCheckDocument(check1);
        transaction2.setCheckDocument(check2);
        transaction3.setCheckDocument(check3);
        transaction4.setCheckDocument(check4);

        transaction1.setTransactionDate(LocalDate.now());
        transaction2.setTransactionDate(LocalDate.now());
        transaction3.setTransactionDate(LocalDate.now());
        transaction4.setTransactionDate(LocalDate.now());
        transaction5.setTransactionDate(LocalDate.now());

        Konto seller = new Konto();
        Konto buyer = new Konto();

        transaction1.setBuyerAndSeller(buyer, seller);
        transaction2.setBuyerAndSeller(buyer, seller);
        transaction3.setBuyerAndSeller(buyer, seller);
        transaction4.setBuyerAndSeller(buyer, seller);
        transaction5.setBuyerAndSeller(buyer, seller);

        service.save(transaction1);
        service.save(transaction2);
        service.save(transaction3);
        service.save(transaction4);
        service.save(transaction5);

        ObservableList<Transakcja> result = service.getTransactionsForCheckNumber("1003");
        assertEquals(2, result.size());
        assertFalse(result.contains(transaction1));
        assertFalse(result.contains(transaction2));
        assertTrue(result.contains(transaction3));
        assertTrue(result.contains(transaction4));
        assertFalse(result.contains(transaction5));
    }

    @Test
    public void test_Delete1() throws Exception {
        System.out.println("test_Delete1 Konto");
        test_save_get_delete_konto();
    }

    @Test
    public void test_DeleteTransakcjaById() throws Exception {
        System.out.println("test_DeleteTransakcjaById");
        Transakcja t1 = new Transakcja(LocalDate.now(), new Konto(), new Konto());
        service.save(t1);
        assertEquals(t1, service.getTransakcja(t1.getId()));

        service.deleteTransakcjaById(t1.getId());
        assertNull(service.getTransakcja(t1.getId()));
    }

    @Test
    public void test_Delete2() throws Exception {
        System.out.println("test_Delete2");
        Transakcja t1 = new Transakcja(LocalDate.now(), new Konto(), new Konto());
        Transakcja t2 = new Transakcja(LocalDate.now(), new Konto(), new Konto());

        service.save(t1);
        service.save(t2);

        assertEquals(t1, service.getTransakcja(t1.getId()));
        assertEquals(t2, service.getTransakcja(t2.getId()));

        service.delete(t1);
        assertNull(service.getTransakcja(t1.getId()));
        assertEquals(t2, service.getTransakcja(t2.getId()));
    }

    @Test
    public void test_DeleteKontoById() throws Exception {
        System.out.println("test_DeleteKontoById");
        Konto k1 = new Konto();
        Konto k2 = new Konto();

        service.save(k1);
        service.save(k2);

        assertEquals(k1, service.getAccount(k1.getId()));
        assertEquals(k2, service.getAccount(k2.getId()));

        service.deleteKontoById(k1.getId());

        assertNull(service.getAccount(k1.getId()));
        assertEquals(k2, service.getAccount(k2.getId()));

    }

    @Test
    public void test_GetKontoWithOldId() throws Exception {
        System.out.println("test_GetKontoWithOldId");
        System.out.println("test_DeleteKontoById");
        Konto k1 = new Konto();
        Konto k2 = new Konto();

        k1.getOldIds().add("asdf");
        k1.getOldIds().add("dfgh");
        k2.getOldIds().add("abcd");

        service.save(k1);
        service.save(k2);

        assertEquals(k1, service.getKontoWithOldId("asdf"));
        assertEquals(k2, service.getKontoWithOldId("abcd"));
        assertNull(service.getKontoWithOldId("xxxxxxxx"));

    }

    @Test
    public void test_Delete3() throws Exception {
        System.out.println("test_Delete3");
        test_save_get_delete_konto();
    }

    @Test
    public void test_GetImage() throws Exception {
        System.out.println("test_GetImage");
        //TODO implement
    }

    @Test
    public void test_CanBeDisplayed() throws Exception {
        System.out.println("test_CanBeDisplayed");

        DocumentFile df1 = new DocumentFile();
        File file1 = createTemporaryFile("1003F.jpg");
        service.uploadFile(df1, file1, true);
        assertTrue(service.canBeDisplayed(df1));

        DocumentFile df2 = new DocumentFile();
        File file2 = createTemporaryFile("1003F.txt");
        service.uploadFile(df2, file2, true);
        assertFalse(service.canBeDisplayed(df2));
    }

    @Test
    public void test_DisplayAllDocuments() throws Exception {
        System.out.println("test_DisplayAllDocuments");
        test_displayDocuments();
    }

    @Test
    public void test_DisplayAllDocumentsNotPersonalPicture() throws Exception {
        System.out.println("test_DisplayAllDocumentsNotPersonalPicture");
        test_displayDocuments();
    }

    @Test
    public void test_DisplayOnly() throws Exception {
        System.out.println("test_DisplayOnly");
        test_displayDocuments();
    }

    @Test
    public void test_DisplayDocumentFiles() throws Exception {
        System.out.println("test_DisplayDocumentFiles");
        test_displayDocuments();
    }

    @Test
    public void test_GetListOfDisplayedDocuments() throws Exception {
        System.out.println("test_GetListOfDisplayedDocuments");
        test_displayDocuments();
    }

    @Test
    public void test_RemoveFromDisplay() throws Exception {
        System.out.println("test_RemoveFromDisplay");
        test_displayDocuments();
    }

    @Test
    public void test_ClearDisplayedDocuments() throws Exception {
        System.out.println("test_ClearDisplayedDocuments");
        test_displayDocuments();
    }

    /**
     * tests the following methods:
     * x void displayAllDocuments();
     * x void displayAllDocumentsNotPersonalPicture();
     * x void displayOnly(Collection<DocumentFile> documents);
     * x void displayDocumentFiles(Collection<DocumentFile> documents);
     * x ObservableList<DocumentFile> getListOfDisplayedDocuments();
     * x void save(DocumentFile documentFile);
     * x void removeFromDisplay(DocumentFile currentEntity);
     * x void clearDisplayedDocuments();
     */
    private void test_displayDocuments() throws DataIntegrityException {

        DocumentFile df1 = new DocumentFile();
        DocumentFile df2 = new DocumentFile();
        DocumentFile df3 = new DocumentFile();
        DocumentFile df4 = new DocumentFile();

        df1.setDocumentType(DocumentFile.PERSON_PICTURE);
        df4.setDocumentType(DocumentFile.PERSON_PICTURE);

        assertEquals(0, service.getListOfDisplayedDocuments().size());

        service.save(df1);
        service.save(df2);
        service.save(df3);
        service.save(df4);

        service.displayAllDocuments();
        Collection<DocumentFile> result1 = service.getListOfDisplayedDocuments();
        assertEquals(4, result1.size());
        assertTrue(result1.contains(df1));
        assertTrue(result1.contains(df2));
        assertTrue(result1.contains(df3));
        assertTrue(result1.contains(df4));

        service.clearDisplayedDocuments();
        assertEquals(0, service.getListOfDisplayedDocuments().size());

        service.delete(df2);
        service.displayAllDocuments();
        Collection<DocumentFile> result2 = service.getListOfDisplayedDocuments();
        assertEquals(3, result2.size());
        assertTrue(result2.contains(df1));
        assertFalse(result2.contains(df2));
        assertTrue(result2.contains(df3));
        assertTrue(result2.contains(df4));

        Collection<DocumentFile> twoDocuments = new ArrayList<>();
        twoDocuments.add(df1);
        twoDocuments.add(df2);
        service.displayOnly(twoDocuments);
        Collection<DocumentFile> result3 = service.getListOfDisplayedDocuments();
        assertEquals(2, result3.size());
        assertTrue(result3.contains(df1));
        assertTrue(result3.contains(df2));
        assertFalse(result3.contains(df3));
        assertFalse(result3.contains(df4));

        service.removeFromDisplay(df1);
        Collection<DocumentFile> result4 = service.getListOfDisplayedDocuments();
        assertEquals(1, result4.size());
        assertFalse(result4.contains(df1));
        assertTrue(result4.contains(df2));
        assertFalse(result4.contains(df3));
        assertFalse(result4.contains(df4));
        // remove from display does not remove from storage
        assertEquals(df1, service.getDocumentFile(df1.getId()));

        service.displayDocumentFiles(twoDocuments);
        Collection<DocumentFile> result5 = service.getListOfDisplayedDocuments();
        assertEquals(2, result5.size());
        assertTrue(result5.contains(df1));
        assertTrue(result5.contains(df2));
        assertFalse(result5.contains(df3));
        assertFalse(result5.contains(df4));

        service.save(df1);
        df2 = new DocumentFile();

        service.save(df2);
        service.save(df3);
        service.save(df4);

        service.displayAllDocumentsNotPersonalPicture();
        Collection<DocumentFile> result6 = service.getListOfDisplayedDocuments();
        assertEquals(2, result6.size());
        assertFalse(result6.contains(df1));
        assertTrue(result6.contains(df2));
        assertTrue(result6.contains(df3));
        assertFalse(result6.contains(df4));

        service.removeFromDisplay(df2);
        Collection<DocumentFile> result8 = service.getListOfDisplayedDocuments();
        assertEquals(1, result6.size());
        assertFalse(result6.contains(df1));
        assertFalse(result6.contains(df2));
        assertTrue(result6.contains(df3));
        assertFalse(result6.contains(df4));

        service.clearDisplayedDocuments();
        Collection<DocumentFile> result7 = service.getListOfDisplayedDocuments();
        assertEquals(0, result7.size());

        Collection<DocumentFile> displayOnlu = new ArrayList<>();
        DocumentFile lastDocumet = new DocumentFile();
        displayOnlu.add(lastDocumet);
        service.displayOnly(displayOnlu);
        Collection<DocumentFile> result9 = service.getListOfDisplayedDocuments();
        assertEquals(1, result9.size());
        assertFalse(result9.contains(df1));
        assertFalse(result9.contains(df2));
        assertTrue(result9.contains(lastDocumet));
        assertFalse(result9.contains(df4));
    }

    @Test
    public void test_UploadFile() throws Exception {
        System.out.println("test_UploadFile");
        test_uploadFile_and_deleteFile();
    }

    @Test
    public void test_UploadFile1() throws Exception {
        System.out.println("test_UploadFile1");
        test_uploadFile_and_deleteFile();
    }

    @Test
    public void test_UploadFile2() throws Exception {
        System.out.println("test_UploadFile2");
        test_uploadFile_and_deleteFile();
    }

    @Test
    public void test_DownloadFile() throws Exception {
        System.out.println("test_DownloadFile");
        DocumentFile df = new DocumentFile();
        File tobeUploaded = createTemporaryFile("tempssssssssssssssss.txt");
        long fileSize = tobeUploaded.length();
        assertTrue(tobeUploaded.exists());
        File internalFile = service.uploadFile(df, tobeUploaded, true);

        assertFalse(tobeUploaded.exists());
        assertTrue(internalFile.exists());

        File downloaddirectory = new File(testDirectory, "download");
        downloaddirectory.mkdirs();
        File downloadedfile = service.downloadFile(df, downloaddirectory);
        assertTrue(downloadedfile.exists());
        assertEquals(fileSize, downloadedfile.length());
    }

    @Test
    public void test_DeleteFile() throws Exception {
        System.out.println("test_DeleteFile");
        test_uploadFile_and_deleteFile();
    }

    private void test_uploadFile_and_deleteFile() throws IOException {
        DocumentFile df = new DocumentFile();
        File tobeUploaded = createTemporaryFile("temp.txt");
        assertTrue(tobeUploaded.exists());
        File internalFile = service.uploadFile(df, tobeUploaded, true);

        assertFalse(tobeUploaded.exists());
        assertTrue(internalFile.exists());

        String internalFileid = df.getInternalFileName();
        service.deleteFile(internalFileid);
        assertFalse(internalFile.exists());
    }

    @Test
    public void test_DeleteFileFromDocument() throws Exception {
        System.out.println("test_DeleteFileFromDocument");
        DocumentFile df = new DocumentFile();
        File tobeUploaded = createTemporaryFile("temp.txt");
        assertTrue(tobeUploaded.exists());
        File internalFile = service.uploadFile(df, tobeUploaded, true);

        assertFalse(tobeUploaded.exists());
        assertTrue(internalFile.exists());

        service.deleteFileFromDocument(df);
        assertFalse(internalFile.exists());
        String s = df.getInternalFileName();
        assertNull(s);
        assertNull(df.getExternalFileName());
    }

    @Test
    public void test_getNumberOfTransactionsForAccount() {
        System.out.println("test_getNumberOfTransactionsForAccount");
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();

        t1.setTransactionDate(LocalDate.now());
        t2.setTransactionDate(LocalDate.now());
        t3.setTransactionDate(LocalDate.now());

        Konto k1 = new Konto();
        Konto k2 = new Konto();
        Konto k3 = new Konto();

        Konto k0 = new Konto();

        service.save(k1);
        service.save(k2);
        service.save(k3);
        service.save(k0);

        t1.setBuyer(k1);
        t1.setSeller(k2);

        t2.setBuyer(k2);
        t2.setSeller(k3);

        t3.setBuyer(k3);
        t3.setSeller(k1);

        service.save(t1);
        service.save(t2);
        service.save(t3);

        service.save(k0);

        Integer numberOfTransactionsForK1 = service.getNumberOfTransactionsForAccount(k1);
        Integer numberOfTransactionsForK0 = service.getNumberOfTransactionsForAccount(k0);

        assertEquals(2, numberOfTransactionsForK1.intValue());
        assertEquals(0, numberOfTransactionsForK0.intValue());

    }


    @Test
    public void test_getAccountsWhichContain() {
        Konto k1 = new Konto();
        Konto k2 = new Konto();
        Konto k3 = new Konto();
        Konto k0 = new Konto();

        DocumentFile dc1_1 = new DocumentFile();
        dc1_1.setDocumentTitle("dc1_1");
        service.save(dc1_1);
        DocumentFile dc2_1 = new DocumentFile();
        dc2_1.setDocumentTitle("dc2_1");
        service.save(dc2_1);
        DocumentFile dc2_2 = new DocumentFile();
        dc2_2.setDocumentTitle("dc2_2");
        service.save(dc2_2);
        DocumentFile dc3_1 = new DocumentFile();
        dc3_1.setDocumentTitle("dc3_1");
        service.save(dc3_1);
        DocumentFile dc0_1 = new DocumentFile();
        dc0_1.setDocumentTitle("dc0_1");
        service.save(dc0_1);

        k0.getDocuments().add(dc0_1);
        k1.getDocuments().add(dc1_1);
        k2.getDocuments().add(dc2_1);
        k2.getDocuments().add(dc2_2);
        k3.getDocuments().add(dc3_1);

        service.save(k1);
        service.save(k2);
        service.save(k3);
        service.save(k0);

        Collection<Konto> documentsWhichContain_dc2_2 = service.getAccountsWhichContain(dc2_2);

        assertEquals(1, documentsWhichContain_dc2_2.size());
        assertTrue(documentsWhichContain_dc2_2.contains(k2));
    }

    @Test
    public void test_getTransactionsWhichContain() {
        Konto k1 = new Konto();
        Konto k2 = new Konto();

        service.save(k1);
        service.save(k2);

        DocumentFile dc1_1 = new DocumentFile();
        dc1_1.setDocumentTitle("dc1_1");
        service.save(dc1_1);
        DocumentFile dc2_1 = new DocumentFile();
        dc2_1.setDocumentTitle("dc2_1");
        service.save(dc2_1);
        DocumentFile dc2_2 = new DocumentFile();
        dc2_2.setDocumentTitle("dc2_2");
        service.save(dc2_2);
        DocumentFile dc3_1 = new DocumentFile();
        dc3_1.setDocumentTitle("dc3_1");
        service.save(dc3_1);
        DocumentFile dc0_1 = new DocumentFile();
        dc0_1.setDocumentTitle("dc0_1");
        service.save(dc0_1);

        Transakcja t1 = new Transakcja(LocalDate.now(), k1, k2);
        Transakcja t2 = new Transakcja(LocalDate.now(), k1, k2);
        Transakcja t3 = new Transakcja(LocalDate.now(), k1, k2);

        t1.getDocuments().add(dc1_1);
        t2.getDocuments().add(dc2_1);
        t2.getDocuments().add(dc2_2);
        t3.getDocuments().add(dc3_1);

        service.save(t1);
        service.save(t2);
        service.save(t3);

        Collection<Transakcja> transactionsWhichContain_dc2_2 = service.getTransactionsWhichContain(dc2_2);

        assertEquals(1, transactionsWhichContain_dc2_2.size());
        assertTrue(transactionsWhichContain_dc2_2.contains(t2));
    }

    @Test
    public void test_getTransactionsForAccountHavingCheckNumberAndCpty(){
        Konto buyer1 = new Konto();
        Konto buyer2 = new Konto();
        Konto buyer3 = new Konto();
        Konto buyer4 = new Konto();

        Konto seller1 = new Konto();
        Konto seller2 = new Konto();
        Konto seller3 = new Konto();
        Konto seller4 = new Konto();

        buyer1.setShortName("buyer1");
        buyer2.setShortName("buyer2");
        buyer3.setShortName("buyer3");
        buyer4.setShortName("buyer4");

        seller1.setShortName("seller1");
        seller2.setShortName("seller2");
        seller3.setShortName("seller3");
        seller4.setShortName("seller4");

        service.save(buyer1);
        service.save(buyer2);
        service.save(buyer3);
        service.save(buyer4);

        service.save(seller1);
        service.save(seller2);
        service.save(seller3);
        service.save(seller4);

        Transakcja t4 = new Transakcja();
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();

        t4.setTransactionDate(LocalDate.now());
        t1.setTransactionDate(LocalDate.now());
        t2.setTransactionDate(LocalDate.now());
        t3.setTransactionDate(LocalDate.now());

        t1.setBuyer(buyer1);
        t1.setSeller(seller2);
        t1.setCheckNumber("1111");

        t2.setBuyer(buyer1);
        t2.setSeller(seller1);
        t2.setCheckNumber("2222");

        t3.setBuyer(buyer2);
        t3.setSeller(seller2);
        t3.setCheckNumber("1111");

        t4.setBuyer(buyer3);
        t4.setSeller(seller3);

        service.save(t1);
        service.save(t2);
        service.save(t3);
        service.save(t4);

        Collection<Transakcja> result = service.getTransactionsForAccountHavingCheckNumberAndCpty(buyer1,"11", "er1");

        assertEquals(1,result.size());
        assertTrue(result.contains(t1));
    }

    @Test
    public void test_combineDocuments() throws IOException {
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();
        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        DocumentFile df1 = new DocumentFile();
        File tobeUploaded = createTemporaryFile("temp1.txt");
        assertTrue(tobeUploaded.exists());
        File internalFile = service.uploadFile(df1, tobeUploaded, true);
        service.save(df1);

        DocumentFile df2 = new DocumentFile();
        File tobeUploaded2 = createTemporaryFile("temp2.txt");
        assertTrue(tobeUploaded2.exists());
        File internalFile2 = service.uploadFile(df2, tobeUploaded2, true);
        service.save(df2);

        DocumentFile df3 = new DocumentFile();
        File tobeUploaded3 = createTemporaryFile("temp3.txt");
        assertTrue(tobeUploaded3.exists());
        File internalFile3 = service.uploadFile(df3, tobeUploaded3, true);
        service.save(df3);

        konto1.getDocuments().add(df1);

        konto2.getDocuments().add(df2);
        konto2.getDocuments().add(df3);

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        assertTrue(konto3.getDocuments().isEmpty());

        service.combineDocuments(konto1, konto2, konto3);

        assertTrue(konto1.getDocuments().isEmpty());
        assertTrue(konto2.getDocuments().isEmpty());
        assertEquals(3,konto3.getDocuments().size());

        assertEquals(df1,service.getDocumentFile(df1.getId()));
        assertEquals(df2,service.getDocumentFile(df2.getId()));
        assertEquals(df3,service.getDocumentFile(df3.getId()));
    }

    @Test
    public void test_GetAccountsWithShortName() throws Exception {
        System.out.println("test_GetAccountsWithShortName");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setShortName("konto1");
        konto2.setShortName("konto2");
        konto3.setShortName("konto3");

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        ObservableList<Konto> result = service.getAccountsWithShortName("konto2");
        assertEquals(1,result.size());
        assertTrue(result.contains(konto2));

        ObservableList<Konto> result2 = service.getAccountsWithShortName(" \tkonto2\n");
        assertEquals(1,result2.size());
        assertTrue(result2.contains(konto2));

        assertTrue(service.getAccountsWithShortName("konto22").isEmpty());
    }

    @Test
    public void test_GetAccountsWithFullName() throws Exception {
        System.out.println("test_GetAccountsWithFullName");
        Konto konto1 = new Konto();
        Konto konto2 = new Konto();
        Konto konto3 = new Konto();

        konto1.setFullName("konto1");
        konto2.setFullName("konto2");
        konto3.setFullName("konto3");

        service.save(konto1);
        service.save(konto2);
        service.save(konto3);

        ObservableList<Konto> result = service.getAccountsWithFullName("konto2");
        assertEquals(1,result.size());
        assertTrue(result.contains(konto2));

        ObservableList<Konto> result2 = service.getAccountsWithFullName(" \tkonto2\n");
        assertEquals(1,result2.size());
        assertTrue(result2.contains(konto2));

        assertTrue(service.getAccountsWithFullName("konto22").isEmpty());
    }

    @Test
    public void test_mergeAccounts(){
        Konto buyer1 = new Konto();
        Konto buyer2 = new Konto();
        Konto buyer3 = new Konto();

        Konto newAccount = new Konto();

        Konto seller1 = new Konto();
        Konto seller2 = new Konto();
        Konto seller3 = new Konto();
        Konto seller4 = new Konto();

        buyer1.setShortName("buyer1");
        buyer2.setShortName("buyer2");
        buyer3.setShortName("buyer3");
        newAccount.setShortName("newAccount");

        seller1.setShortName("seller1");
        seller2.setShortName("seller2");
        seller3.setShortName("seller3");
        seller4.setShortName("seller4");

        service.save(buyer1);
        service.save(buyer2);
        service.save(buyer3);
        service.save(newAccount);

        service.save(seller1);
        service.save(seller2);
        service.save(seller3);
        service.save(seller4);

        Transakcja t4 = new Transakcja();
        Transakcja t1 = new Transakcja();
        Transakcja t2 = new Transakcja();
        Transakcja t3 = new Transakcja();

        t4.setTransactionDate(LocalDate.now());
        t1.setTransactionDate(LocalDate.now());
        t2.setTransactionDate(LocalDate.now());
        t3.setTransactionDate(LocalDate.now());

        t1.setBuyer(buyer1);
        t1.setSeller(seller2);
        t1.setCheckNumber("1111");

        t2.setBuyer(buyer1);
        t2.setSeller(seller1);
        t2.setCheckNumber("2222");

        t3.setBuyer(buyer2);
        t3.setSeller(seller2);
        t3.setCheckNumber("1111");

        t4.setBuyer(buyer3);
        t4.setSeller(seller3);

        DocumentFile df1 = new DocumentFile();
        df1.setDocumentTitle("document1");
        DocumentFile df2 = new DocumentFile();
        df2.setDocumentTitle("document2");
        DocumentFile df3 = new DocumentFile();
        df3.setDocumentTitle("document3");

        buyer2.getDocuments().add(df1);
        buyer2.getDocuments().add(df2);
        seller1.getDocuments().add(df3);

        service.save(t1);
        service.save(t2);
        service.save(t3);
        service.save(t4);

        // we will merge buyer2 and seller1
        service.mergeAccounts(buyer2,seller1,newAccount);

        assertNull(service.getAccount(buyer2.getId()));
        assertNull(service.getAccount(seller1.getId()));
        assertEquals(newAccount,service.getAccount(newAccount.getId()));

        assertEquals(3,newAccount.getDocuments().size());
        //TODO make test if the documents are cloned copies of originals
    }

    @Test
    public void test_internalFileExists() throws IOException {
        DocumentFile df1 = new DocumentFile();
        service.save(df1);
        assertFalse(service.internalFileExists(df1));
        File tobeUploaded = createTemporaryFile("temp1.txt");
        assertTrue(tobeUploaded.exists());
        File internalFile = service.uploadFile(df1, tobeUploaded, true);
        service.save(df1);
        assertTrue(service.internalFileExists(df1));
    }

    @Test
    public void test_internalFileSize() throws IOException {
        DocumentFile df1 = new DocumentFile();
        File tobeUploaded = createTemporaryFile("temp1.txt");
        assertTrue(tobeUploaded.exists());
        long expectedFilesize = tobeUploaded.length();

        File internalFile = service.uploadFile(df1, tobeUploaded, true);
        service.save(df1);
        assertTrue(internalFile.exists());

        assertEquals(expectedFilesize,service.internalFileSize(df1));
        internalFile.delete();
        assertEquals(-1L,service.internalFileSize(df1));
    }

    @Test
    public void test_clone() throws IOException {
        DocumentFile df1 = new DocumentFile();
        File tobeUploaded = createTemporaryFile("temp1.txt");
        assertTrue(tobeUploaded.exists());
        File internalFile = service.uploadFile(df1, tobeUploaded, true);
        service.save(df1);
        assertTrue(internalFile.exists());

        DocumentFile df2 = new DocumentFile();
        File tobeUploaded2 = createTemporaryFile("temp2xxxxx.txt");
        assertTrue(tobeUploaded2.exists());
        File internalFile2 = service.uploadFile(df2, tobeUploaded2, true);
        service.save(df2);
        assertTrue(internalFile2.exists());

        // now clone df1
        DocumentFile clonedDocument = service.clone(df1);
        assertEquals(clonedDocument.getExternalFileName(),df1.getExternalFileName());
        assertEquals(clonedDocument.getDocumentDescription(),df1.getDocumentDescription());
        assertEquals(clonedDocument.getDocumentTitle(),df1.getDocumentTitle());
        assertEquals(clonedDocument.getComment(),df1.getComment());
        assertEquals(clonedDocument.getDocumentType(),df1.getDocumentType());
        assertTrue(service.internalFileExists(clonedDocument));
        assertEquals(service.internalFileSize(clonedDocument),service.internalFileSize(df1));
    }

    @Test
    public void test_getAllAccountsWhichAreTradingWith(){
        Konto buyer1 = new Konto();
        Konto buyer2 = new Konto();
        Konto buyer3 = new Konto();
        Konto buyer4 = new Konto();

        service.save(buyer1);
        service.save(buyer2);
        service.save(buyer3);
        service.save(buyer4);

        Konto newAccount = new Konto();
        service.save(newAccount);

        Konto seller1 = new Konto();
        Konto seller2 = new Konto();
        Konto seller3 = new Konto();
        Konto seller4 = new Konto();

        service.save(seller1);
        service.save(seller2);
        service.save(seller3);
        service.save(seller4);

        LocalDate now = LocalDate.now();

        Transakcja t11 = new Transakcja(now, buyer1, seller1);
        Transakcja t12 = new Transakcja(now, buyer1, seller2);

        service.save(t11);
        service.save(t12);

        Transakcja t23 = new Transakcja(now, buyer2, seller3);
        Transakcja t24 = new Transakcja(now, buyer2, seller4);
        service.save(t23);
        service.save(t24);

        Transakcja t41 = new Transakcja(now, buyer4, seller1);
        Transakcja t43 = new Transakcja(now, buyer4, seller3);
        service.save(t41);
        service.save(t43);

        Transakcja t1 = new Transakcja(now, newAccount, seller1);
        Transakcja t2 = new Transakcja(now, newAccount, seller2);
        Transakcja t3 = new Transakcja(now, buyer3, newAccount);
        Transakcja t4 = new Transakcja(now, buyer4, newAccount);
        Transakcja t5 = new Transakcja(now, seller1, newAccount);
        service.save(t1);
        service.save(t2);
        service.save(t3);
        service.save(t4);
        service.save(t5);

        ObservableList<Konto> accountsWhichTradeWithNewAccount = service.getAllAccountsWhichAreTradingWith(newAccount);

        assertEquals(4,accountsWhichTradeWithNewAccount.size());
        assertTrue(accountsWhichTradeWithNewAccount.contains(seller1));
        assertTrue(accountsWhichTradeWithNewAccount.contains(seller2));
        assertFalse(accountsWhichTradeWithNewAccount.contains(seller3));
        assertFalse(accountsWhichTradeWithNewAccount.contains(seller4));

        assertFalse(accountsWhichTradeWithNewAccount.contains(buyer1));
        assertFalse(accountsWhichTradeWithNewAccount.contains(buyer2));
        assertTrue(accountsWhichTradeWithNewAccount.contains(buyer3));
        assertTrue(accountsWhichTradeWithNewAccount.contains(buyer4));

    }
}