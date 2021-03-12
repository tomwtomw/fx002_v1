package org.tomw.rachunki.ingest;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.tomw.config.SelfIdentificationService;
import org.tomw.czeki.Account;
import org.tomw.czeki.AccountsDaoJsonImpl;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;
import org.tomw.envutils.TomwEnvUtils;
import org.tomw.filestoredao.FileDao;
import org.tomw.filestoredao.FileDaoDirImpl;
import org.tomw.filestoredao.FileDaoDrirImplConfiguration;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.config.RachunkiConfigurationImpl;
import org.tomw.rachunki.core.*;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;


public class IngestMain {
    private final static Logger LOGGER = Logger.getLogger(IngestMain.class.getName());

    private static final String NETBEANS = "NetBeans";

    public static final String TFCU_ID = "dbe41458-743d-41b2-808e-5477e1cc1ef0";
    public static final String BOACAR_ID = "6726edd9-6e37-428b-9664-9ebeb1d28d03";
    public static final String TFCU_SAVINGS_ID = "6a6188e0-f297-4962-92a4-865c5ffc9eb3";
    public static final String MINISTRIES_APPEAL_ID = "c4e3b260-d2f8-4e47-88bb-441e0f6d289a";
    public static final String SAMS_CLUB_ID = "164c0688-2803-4d91-8de4-96f5eeb27fc0";
    public static final String BOACC_ID = "3dcbc124-ffda-4eab-ac24-4fd95e82fe43";
    public static final String HDCC_ID = "4421648c-750b-4190-87c0-7db885ec0597";
    public static final String AMAZON_ID = "489baa6f-9c27-4ed5-acf8-4c161cd06647";
    public static final String BOA40_ID = "52f04fc8-09f0-4631-b2c3-4785cf93f66f";
    public static final String ST_JUDE_CC_ID = "7f30edf7-cb93-41ab-b171-239c5619ae37";
    public static final String FICC_ID = "91627656-c004-4382-bda8-84fe67a2e31e";
    public static final String PSFCU_CHECKING_ID = "2343604b-5463-4ae0-8a4e-b0915029aac1";
    public static final String PSFCU_SAVINGS_ID = "5851f3e8-7d7d-4eb2-8c56-fa0faf5f81ab";
    public static final String BOA50_ID = "b8d5dd23-3ab6-4d64-8db6-702cd60edfe9";
    public static final String GEICO_ID = "9c29bbfa-49a8-417d-b065-a49d8291a14b";
    public static final String CMP_ID = "d21ce247-cd97-4afe-86ac-60b82c38e9b1";
    public static final String CENLAR_ID = "25628674-0fd7-4b22-bc7c-394be95b7897";
    public static final String CENLAR_ESCROW_ID = "fede4b2d-0722-4df6-84a2-12f939f5539e";
    public static final String TFCU_VAC_ID = "9a7f426f-3127-453f-b6c8-753904105f54";
    public static final String GR_ID = "dd3b286d-b1b9-4d66-b9ca-dc5e3c8bec80";



    //==== ralational ids
    public static final String PSFCU_SAVINGS_IN_PSFCU_CHECKING="18f70d76-fb90-4f8d-bf2f-ab1ec1b2f6fa";

    // id of tfcu counterparty in BoaCar Account
    public static final String BOACAR_TFCU_ID = "d0bd780f-1c98-4383-b14a-35ecd2f47897";

    private static Map<String, Konto> mapCpToKonto = new HashMap<>();

    public static Comparator<CounterParty> cpComparator = new Comparator<CounterParty>() {

        @Override
        public int compare(CounterParty o1, CounterParty o2) {
            return o1.getShortName().compareTo(o2.getShortName());
        }
    };


    /**
     * Initialize application data in CzekiRegistry
     *
     * @throws FileNotFoundException if something goes wrong
     */
    private static void initDirectories() throws FileNotFoundException {
        CzekiRegistry.applicationDirectory = new File(System.getProperty("user.dir"));
        //CzekiRegistry.applicationDirectory = new File("C:\\Users\\tomw2\\Documents\\IdeaProjectsData\\Czeki");
        LOGGER.info("Application directory =" + CzekiRegistry.applicationDirectory);

        if (isDeploymentInstance()) {
            LOGGER.info("redirecting output to directory " + CzekiRegistry.applicationDirectory);
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
            System.setErr(out);
        }

        CzekiRegistry.dataDirectory = selectDataDirectory();

        LOGGER.info("Data directory = " + CzekiRegistry.dataDirectory);
        if (!CzekiRegistry.dataDirectory.exists()) {
            LOGGER.info("Data directory does not exist, create...");
            CzekiRegistry.dataDirectory.mkdirs();
        }

        CzekiRegistry.defaultImageDirectory = new File(CzekiRegistry.dataDirectory, "images");
        if (!CzekiRegistry.defaultImageDirectory.exists()) {
            CzekiRegistry.defaultImageDirectory.mkdirs();
        }

        CzekiRegistry.initialDirectory = CzekiRegistry.dataDirectory;
        LOGGER.info("Start directory =" + CzekiRegistry.initialDirectory);

        CzekiRegistry.accountsDao = new AccountsDaoJsonImpl();
        LOGGER.info("Accounts dao loaded: " + CzekiRegistry.accountsDao.toString());

        LOGGER.info("Load context ...");
        CzekiRegistry.context.load();

        LOGGER.info("Account =" + CzekiRegistry.currentAccount);

        for (Account account : CzekiRegistry.accountsDao.getAllAccounts().values()) {
            LOGGER.info(account);
        }

        boolean stop = true;
    }

    /**
     * location of the data directory varies, depending if we are in devel or
     * production
     *
     * @return location of the data directory
     */
    public static File selectDataDirectory() {
        //return new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data_for_ingest");
        //return new File("C:\\Users\\tomw2\\Documents\\accounts\\data");
        if (!isDeploymentInstance()) {
            if (TomwEnvUtils.isLaptop()) {
                return new File("C:\\Users\\tomw2\\Documents\\IdeaProjectsData\\Czeki\\data");
            } else {
                return new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data");
            }

        } else {
            return new File(CzekiRegistry.applicationDirectory, "data");
        }
    }

    /**
     * decide if this is deployment instance or development
     *
     * @return true if this is deployment instance
     */
    public static boolean isDeploymentInstance() {
        return !CzekiRegistry.applicationDirectory.toString().contains(NETBEANS) &&
                !CzekiRegistry.applicationDirectory.toString().contains("IdeaProjects");
    }

    public static void main(String[] args) {
        LOGGER.info("Ingest starts...");

        try {
            initDirectories();
        } catch (FileNotFoundException ex) {
            LOGGER.error("File not found", ex);
        }

        SelfIdentificationService selfIdentificationService = new SelfIdentificationServiceRachunki();

        // determine if we want to ingest devel database of prod database
        if(args.length>0 && SelfIdentificationService.PROD.equalsIgnoreCase(args[0])){
            LOGGER.error("Ingest to prod is disabled");
            System.exit(0);
            //selfIdentificationService = new SelfIdentificationServiceRachunkiOfficeProd();
        }else if(args.length>0 && SelfIdentificationService.PREPROD.equalsIgnoreCase(args[0])){
            LOGGER.error("Ingest to preprod is disabled");
            System.exit(0);
            //selfIdentificationService = new SelfIdentificationServiceRachunkiOfficePreProd();
        }else{
            selfIdentificationService = new SelfIdentificationServiceRachunki();
        }

        LOGGER.info("selfId=" + selfIdentificationService);

        RachunkiConfiguration rachunkiConfig = new RachunkiConfigurationImpl(selfIdentificationService);

        FileDao fileDao = new FileDaoDirImpl(rachunkiConfig);

        RachunkiDao rachunkiDao = new RachunkiDaoHibernateImpl(rachunkiConfig, fileDao);
        RachunkiService rachunkiService = new RachunkiServiceImpl(rachunkiDao, rachunkiConfig);

        TransactionsConverter converter = new TransactionsConverter(rachunkiService, CzekiRegistry.accountsDao);

        // init mapper
        CpToAccountMapper mapper = new CpToAccountMapper("tfcu_mapping.txt", rachunkiService);

        //printCounterPartiesForAccount(PSFCU_CHECKING_ID);
        //printCounterPartiesForAccount(TFCU_ID);
        //printCounterPartiesForAccount(CENLAR_ID);


        deleteEverything(rachunkiService);

        createTfcu(rachunkiService, converter, mapper);

        createGr(rachunkiService, converter, mapper);

        createTfcuVac(rachunkiService, converter, mapper);

        createCenlar104(rachunkiService, converter, mapper);

        createCmp(rachunkiService, converter, mapper);

        createGeico(rachunkiService, converter, mapper);

        createBoa50(rachunkiService, converter, mapper);

        createPsfcuChecking(rachunkiService, converter, mapper);

        createFiCC(rachunkiService, converter, mapper);

        createStJudeCC(rachunkiService, converter, mapper);

        createBoa40(rachunkiService, converter, mapper);

        createAmazon(rachunkiService, converter, mapper);

        createHdcc(rachunkiService, converter, mapper);

        createBoacc(rachunkiService, converter, mapper);

        createSamsClub(rachunkiService, converter, mapper);

        createMinistriesAppeal(rachunkiService, converter, mapper);

        createBoaCar(rachunkiService, converter, mapper);

        createTfcuSavings(rachunkiService, converter, mapper);


        LOGGER.info("End of ingest");
    }

    private static void createGr(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                GR_ID,
                "tfcu",
                "tfcu",
                "gr"
        );
    }

    private static void createTfcuVac(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                TFCU_VAC_ID,
                "tfcu",
                "tfcu",
                "tfcu_vac"
        );
    }


    private static void createCenlar104(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                CENLAR_ID,
                "tfcu",
                "tfcu",
                "cenlar104"
        );
        CpToAccountMapper mapperCenlar104 = new CpToAccountMapper(
                "cenlar104_mapping.txt",
                rachunkiService
        );
        ingestAccount(
                rachunkiService,
                converter,
                mapperCenlar104,
                CENLAR_ESCROW_ID,
                "cenlar104",
                "cenlar104",
                "cenlar_escrow"
        );
    }

    private static void createCmp(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                CMP_ID,
                "tfcu",
                "tfcu",
                "compass"
        );
    }


    private static void createGeico(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                GEICO_ID,
                "tfcu",
                "tfcu",
                "geico"
        );
    }

    private static void createBoa50(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                BOA50_ID,
                "tfcu",
                "tfcu",
                "boa50"
        );
    }

    private static void createPsfcuChecking(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                PSFCU_CHECKING_ID,
                "tfcu",
                "tfcu",
                "psfcu_c"
        );

        CpToAccountMapper mapperPsfcuChecking = new CpToAccountMapper(
                "psfcu_checking_mapping.txt",
                rachunkiService
        );
        ingestAccount(
                rachunkiService,
                converter,
                mapperPsfcuChecking,
                PSFCU_SAVINGS_ID,
                "psfcu_c",
                "psfcu checking",
                "psfcu_saving"
        );
    }

    private static void createFiCC(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                FICC_ID,
                "tfcu",
                "tfcu",
                "ficc"
        );
    }

    private static void createStJudeCC(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                ST_JUDE_CC_ID,
                "tfcu",
                "tfcu",
                "St Jude Capital Campaign"
        );
    }

    private static void createBoa40(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                BOA40_ID,
                "tfcu",
                "tfcu",
                "boa40"
        );
    }

    private static void createAmazon(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                AMAZON_ID,
                "tfcu",
                "tfcu",
                "amazon"
        );
    }

    private static void createHdcc(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                HDCC_ID,
                "tfcu",
                "tfcu",
                "hdcc"
        );
    }

    private static void createBoacc(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                BOACC_ID,
                "tfcu",
                "tfcu",
                "boacc"
        );
    }

    private static void createSamsClub(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        ingestAccount(
                rachunkiService,
                converter,
                mapper,
                SAMS_CLUB_ID,
                "tfcu",
                "tfcu",
                "sams club"
        );

    }

    private static void ingestAccount(
            RachunkiService rachunkiService,
            TransactionsConverter converter,
            CpToAccountMapper mapper,
            String oldAccountId,
            String shortNameOfJoinerKontoInNewSystem, /* usually this refers to tfcu*/
            String shortNameOfJoinerInOldDao, /* how the joiner account is called in old dao */
            String ingestAccountInNewSystemShorrtName /* how the ingested account is refered to in new system */
    ) {

        for(Konto k : rachunkiService.getAllAccounts()){
            LOGGER.info("K="+k+" "+k.getId());
        }

        Collection<Konto> knownKonto = rachunkiService.getAllAccounts();
        Collection<Transakcja> allTransactions = rachunkiService.getAllTransactions();
        for(Transakcja t : allTransactions){
            LOGGER.info("T="+t+" "+t.getId());
        }
        Konto kontoMinistriesAppeal = getKontoByShortname(ingestAccountInNewSystemShorrtName, knownKonto);
        Konto kontoTfcu = getKontoByShortname(shortNameOfJoinerKontoInNewSystem, knownKonto);

        Collection<Transakcja> knownTransactions = rachunkiService.getTransactionsForAccount(kontoMinistriesAppeal);

        Account accountDaoOldSystem = CzekiRegistry.accountsDao.getAccount(oldAccountId);

        String tfcuShortnameInMinistriesDao = shortNameOfJoinerInOldDao;
        CounterParty counterpartyInOldSystem = getCounterpartybyShortname(tfcuShortnameInMinistriesDao, accountDaoOldSystem.getDao().getCounterParties());


        Map<CounterParty, Konto> cpToKontoMap = new HashMap<>();
        cpToKontoMap.put(counterpartyInOldSystem, kontoMinistriesAppeal);

        List<Transaction> transactionsWhichFailedToMatch = new ArrayList<>();

        Collection<Transaction> oldTransactions = accountDaoOldSystem.getDao().getTransactions().values();
        for (Transaction oldTransaction : oldTransactions) {
            LOGGER.info("oldTransaction =" + oldTransaction.toShortString());
        }
        for (Transaction oldTransaction : oldTransactions) {
            LOGGER.info("oldTransaction =" + oldTransaction);
            if (!cpToKontoMap.containsKey(oldTransaction.getCounterParty())) {
                Transakcja newTransaction = converter.convert(
                        rachunkiService, oldTransaction, kontoMinistriesAppeal, cpToKontoMap
                );
                rachunkiService.save(newTransaction);
                LOGGER.info("converted=" + newTransaction);
            } else {
                Transakcja matchedTransaction = matchTransaction(oldTransaction, knownTransactions, kontoTfcu);
                if (matchedTransaction == null) {
                    LOGGER.error("Failed to match " + oldTransaction);
                    transactionsWhichFailedToMatch.add(oldTransaction);
                } else {
                    try {
                        rachunkiService.save(matchedTransaction);
                        LOGGER.info("matched=" + matchedTransaction);
                    } catch (Exception e) {
                        LOGGER.error("Failed to match " + oldTransaction);
                        transactionsWhichFailedToMatch.add(oldTransaction);
                    }
                }
            }
        }
        if (!transactionsWhichFailedToMatch.isEmpty()) {
            LOGGER.error("Some transactions failed to match");
            for (Transaction oldTransaction : transactionsWhichFailedToMatch) {
                LOGGER.info(oldTransaction);
            }
            LOGGER.error("There were errors");
        }
    }

    private static void createMinistriesAppeal(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        String ministtriesAppealShortname = "ministries appeal";

        Collection<Konto> knownKonto = rachunkiService.getAllAccounts();
        Konto kontoMinistriesAppeal = getKontoByShortname(ministtriesAppealShortname, knownKonto);
        Konto kontoTfcu = getKontoByShortname("tfcu", knownKonto);

        Collection<Transakcja> knownTransactions = rachunkiService.getTransactionsForAccount(kontoMinistriesAppeal);

        Account accountDaoOldSystem = CzekiRegistry.accountsDao.getAccount(MINISTRIES_APPEAL_ID);

        String tfcuShortnameInMinistriesDao = "tfcu";
        CounterParty counterpartyInOldSystem = getCounterpartybyShortname(tfcuShortnameInMinistriesDao, accountDaoOldSystem.getDao().getCounterParties());


        Map<CounterParty, Konto> cpToKontoMap = new HashMap<>();
        cpToKontoMap.put(counterpartyInOldSystem, kontoMinistriesAppeal);

        for (Transaction oldTransaction : accountDaoOldSystem.getDao().getTransactions().values()) {
            LOGGER.info("oldTransaction =" + oldTransaction);
            if (!cpToKontoMap.containsKey(oldTransaction.getCounterParty())) {
                Transakcja newTransaction = converter.convert(
                        rachunkiService, oldTransaction, kontoMinistriesAppeal, cpToKontoMap
                );
                rachunkiService.save(newTransaction);
                LOGGER.info("converted=" + newTransaction);
            } else {
                Transakcja matchedTransaction = matchTransaction(oldTransaction, knownTransactions, kontoTfcu);
                rachunkiService.save(matchedTransaction);
                LOGGER.info("matched=" + matchedTransaction);
            }
        }
    }

    private static void createTfcuSavings(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        Collection<Konto> knownKonto = rachunkiService.getAllAccounts();
        Konto kontoTfcuSaving = getKontoByShortname("tfcu_saving", knownKonto);
        Konto kontoTfcu = getKontoByShortname("tfcu", knownKonto);

        Collection<Transakcja> knownTransactions = rachunkiService.getTransactionsForAccount(kontoTfcuSaving);

        Account tfcuSavingsChecking = CzekiRegistry.accountsDao.getAccount(TFCU_SAVINGS_ID);

        CounterParty tfcuSavingsCounterparty = getCounterpartybyShortname("tfcu checking", tfcuSavingsChecking.getDao().getCounterParties());

        Konto tfcuSavingsKonto = getKontoByShortname("tfcu_saving", knownKonto);

        Map<CounterParty, Konto> cpToKontoMap = new HashMap<>();
        cpToKontoMap.put(tfcuSavingsCounterparty, tfcuSavingsKonto);

        for (Transaction oldTransaction : tfcuSavingsChecking.getDao().getTransactions().values()) {
            LOGGER.info("tfcu savings t=" + oldTransaction);
            if (!cpToKontoMap.containsKey(oldTransaction.getCounterParty())) {
                Transakcja newTransaction = converter.convert(
                        rachunkiService, oldTransaction, tfcuSavingsKonto, cpToKontoMap
                );
                rachunkiService.save(newTransaction);
                LOGGER.info("converted=" + newTransaction);
            } else {
                Transakcja matchedTransaction = matchTransaction(oldTransaction, knownTransactions, kontoTfcu);
                rachunkiService.save(matchedTransaction);
                LOGGER.info("matched=" + matchedTransaction);
            }
        }
    }

    private static void createBoaCar(RachunkiService rachunkiService, TransactionsConverter converter, CpToAccountMapper mapper) {
        Collection<Konto> knownKonto = rachunkiService.getAllAccounts();
        Konto kontoTfcu = getKontoByShortname("tfcu", knownKonto);

        Collection<Transakcja> knownTransactions = rachunkiService.getTransactionsForAccount(kontoTfcu);

        Account boaCarChecking = CzekiRegistry.accountsDao.getAccount(BOACAR_ID);

        CounterParty tfcuCounterparty = getCounterpartybyShortname("tfcu", boaCarChecking.getDao().getCounterParties());

        Konto boacarKonto = getKontoByShortname("boacar", knownKonto);

        Map<CounterParty, Konto> cpToKontoMap = new HashMap<>();
        cpToKontoMap.put(tfcuCounterparty, kontoTfcu);

        for (Transaction oldTransaction : boaCarChecking.getDao().getTransactions().values()) {
            LOGGER.info("boacar t=" + oldTransaction);
            if (!cpToKontoMap.containsKey(oldTransaction.getCounterParty())) {
                Transakcja newTransaction = converter.convert(
                        rachunkiService, oldTransaction, boacarKonto, cpToKontoMap
                );
                rachunkiService.save(newTransaction);
                LOGGER.info("converted=" + newTransaction);
            } else {
                Transakcja matchedTransaction = matchTransaction(oldTransaction, knownTransactions, kontoTfcu);
                rachunkiService.save(matchedTransaction);
                LOGGER.info("matched=" + matchedTransaction);
            }
        }
    }

    private static Transakcja matchTransaction(Transaction oldTransaction,
                                               Collection<Transakcja> transactionsForAccount,
                                               Konto kontoInTransactions) {
        for (Transakcja transakcja : transactionsForAccount) {
            if (theyMatch(oldTransaction, transakcja)) {
                updateClearedFlag(oldTransaction, transakcja, kontoInTransactions);
                return transakcja;
            }
        }
        return null;
    }

    //TODO add unit test for this
    public static void updateClearedFlag(Transaction oldTransaction, Transakcja transakcja, Konto kontoInTransakcja) {
        if (transakcja.getSeller().equals(kontoInTransakcja)) {
            boolean buyerTransactionCleared = oldTransaction.isCleared();
            transakcja.setClearedBuyer(buyerTransactionCleared);
        }
        if (transakcja.getBuyer().equals(kontoInTransakcja)) {
            boolean sellerTransactionCleared = oldTransaction.isCleared();
            transakcja.setClearedSeller(sellerTransactionCleared);
        }
    }

    private static boolean theyMatch(Transaction oldTransaction, Transakcja transakcja) {
        boolean amountsMatch = oldTransaction.getTransactionAmount() + transakcja.getTransactionAmount() == 0.;
        boolean checkNumbersMatch = TomwStringUtils.stringNullCompareOrBothEmpty(
                oldTransaction.getCheckNumber(),
                transakcja.getCheckNumber());
        boolean datesmatch = oldTransaction.getTransactionDate().equals(transakcja.getTransactionDate());

        boolean result = amountsMatch && checkNumbersMatch && datesmatch;
        return result;
    }
//    private boolean theyMatch(Transaction oldTransaction, Transakcja transakcja) {
//        boolean accountsmatch = doAccountsMatch(oldTransaction,transakcja);
//        boolean amountsMatch = oldTransaction.getTransactionAmount()+transakcja.getTransactionAmount()==0.;
//        boolean checkNumbersMatch = TomwStringUtils.stringNullCompare(
//                oldTransaction.getCheckNumber(),
//                transakcja.getCheckNumber());
//        boolean datesmatch = oldTransaction.getTransactionDate().equals(transakcja.getTransactionDate());
//
//        boolean result = accountsmatch && amountsMatch && checkNumbersMatch && datesmatch;
//        return result;
//    }

    private boolean doAccountsMatch(Transakcja transakcja1, Transakcja transakcja2) {
        if (transakcja1.getBuyer().equals(transakcja2.getSeller()) &&
                transakcja1.getSeller().equals(transakcja2.getBuyer())) {
            return true;
        } else {
            return false;
        }
    }


    private static void createTfcu(RachunkiService rachunkiService,
                                   TransactionsConverter converter,
                                   CpToAccountMapper mapper) {

        List<Konto> knownAccounts = new ArrayList<>();
        int numberOfAccounts = 0;
        Collection<Account> accounts = CzekiRegistry.accountsDao.getAllAccounts().values();
        for (Account account : accounts) {
            numberOfAccounts = numberOfAccounts + 1;
            Konto konto = converter.convert(account);
            rachunkiService.save(konto);
            knownAccounts.add(konto);
            LOGGER.info("account " + numberOfAccounts + " " + account.getId() + " " + account.getShortName() + " konto=" + konto);
        }
        LOGGER.info("I have found " + numberOfAccounts + " accounts");

        Account tfcuChecking = CzekiRegistry.accountsDao.getAccount(TFCU_ID);

        LOGGER.info("TFCU as in Czeki application=" + tfcuChecking);

        Konto kontoTfcu = rachunkiService.getKontoWithOldId(tfcuChecking.getId());
        LOGGER.info("Konto TFCU=" + kontoTfcu);

        LOGGER.info("counterparties for " + kontoTfcu.getShortName());

        Map<CounterParty, Konto> cpToKontoMap = new HashMap<>();

        List<CounterParty> listOfCounteParties = new ArrayList<>(tfcuChecking.getDao().getCounterParties().values());
        Collections.sort(listOfCounteParties, cpComparator);
        int nCpTfcu = 0;
        for (CounterParty counterParty : listOfCounteParties) {
            nCpTfcu = nCpTfcu + 1;
            Konto konto = mapper.getTfcuMapping(counterParty.getId());
            if (konto == null) {
                LOGGER.info("cp " + nCpTfcu + "=" + counterParty.getShortName() + " " + counterParty.getName());
            } else {
                cpToKontoMap.put(counterParty, konto);
                LOGGER.info("cp " + nCpTfcu + "=" + counterParty.getShortName() + " " + counterParty.getName() + " =====>>> " + konto);
            }
        }

        LOGGER.info("Load transactions for TFCU");

        int nTotal = tfcuChecking.getDao().getTransactions().values().size();
        int counter = 0;
        for (Transaction transaction : tfcuChecking.getDao().getTransactions().values()) {
            counter = counter + 1;
            //LOGGER.info("count="+counter+"/"+nTotal+" Transakcja old="+transaction);
            Transakcja tr = converter.convert(rachunkiService, transaction, kontoTfcu, cpToKontoMap);

            rachunkiService.save(tr);
            LOGGER.info("count=" + counter + "/" + nTotal + " Transakcja new=" + tr);
        }

    }

    private static void printCounterPartiesForAccount(String accountId) {
        Account account = CzekiRegistry.accountsDao.getAccount(accountId);
        LOGGER.info("counterparties for " + account.getShortName());
        List<CounterParty> listOfCounteParties = new ArrayList<>(account.getDao().getCounterParties().values());
        Collections.sort(listOfCounteParties, cpComparator);
        int nCpTfcu = 0;
        for (CounterParty counterParty : listOfCounteParties) {
            nCpTfcu = nCpTfcu + 1;
            LOGGER.info("cp " + nCpTfcu + "=" + counterParty.getShortName() + " " + counterParty.getName() + " " + counterParty.getId());
        }
    }

    private static Konto getKontoByShortname(String shortName, Collection<Konto> knownKonto) {
        for (Konto konto : knownKonto) {
            if (konto.getShortName().equals(shortName)) {
                return konto;
            }
        }
        return null;
    }

    /**
     * from map of old countrparties get one with given short name
     *
     * @param shortname
     * @param counterParties
     * @return
     */
    private static CounterParty getCounterpartybyShortname(String shortname, Map<String, CounterParty> counterParties) {
        for (CounterParty cp : counterParties.values()) {
            if (cp.getShortName().equals(shortname)) {
                return cp;
            }
        }
        return null;
    }

    private static Collection<Transakcja> filterTransactionsInvolving(Konto konto, Collection<Transakcja> knownTransactions) {
        List<Transakcja> result = new ArrayList<>();
        for (Transakcja t : knownTransactions) {
            if (konto.equals(t.getBuyer()) || konto.equals(t.getSeller())) {
                result.add(t);
            }
        }
        return result;
    }


    private static Konto getkontoWithShortId(String shortName, Collection<Konto> knownKonto) {
        for (Konto konto : knownKonto) {
            if (shortName.equals(konto.getShortName())) {
                return konto;
            }
        }
        return null;
    }

    private static void deleteEverything(RachunkiService rachunkiService) {
        deleteTransactions(rachunkiService);
        deleteAccounts(rachunkiService);
    }

    private static void deleteTransactions(RachunkiService rachunkiService) {
        int nTransactionsDeleted = 0;
        for (Konto konto : rachunkiService.getAllAccounts()) {
            for (Transakcja tr : rachunkiService.getTransactionsForAccount(konto)) {
                LOGGER.debug("Delete transaction " + tr);
                rachunkiService.delete(tr);
                nTransactionsDeleted = nTransactionsDeleted + 1;

            }
        }
        LOGGER.info("Deleted " + nTransactionsDeleted + " transactions");
    }

    private static void deleteAccounts(RachunkiService rachunkiService) {
        int nAccountsDeleted = 0;
        for (Konto konto : rachunkiService.getAllAccounts()) {
            try {
                LOGGER.info("Delete account " + konto);
                rachunkiService.delete(konto);
                nAccountsDeleted = nAccountsDeleted + 1;
            } catch (RachunkiException e) {
                LOGGER.error("Failed to delete " + konto, e);
            }
        }
        LOGGER.info("Deleted " + nAccountsDeleted + " accounts");
    }
}
