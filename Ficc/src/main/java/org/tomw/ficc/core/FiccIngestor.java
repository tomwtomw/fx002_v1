package org.tomw.ficc.core;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomw on 7/29/2017.
 */
public class FiccIngestor {
    private final static Logger LOGGER = Logger.getLogger(FiccIngestor.class.getName());
    private FiccDao dao;
    private FiccFactory factory;

    public void setDao(FiccDao dao) {
        this.dao = dao;
        this.factory = new FiccFactory(dao);
    }

    public FiccIngestor(){}

    public FiccIngestor(FiccDao daoIn){
        dao=daoIn;
        this.factory = new FiccFactory(dao);
    }

    /**
     * ingest transactions from multiple csv files
     * @param listOfcsvFiles
     */
    public void ingestFromMultipleCsvFiles(List<File> listOfcsvFiles){
        for(File f : listOfcsvFiles){
            try {
                ingestFromCsvFile(f);
            } catch (IOException e) {
                LOGGER.error("failed to ingest from file "+f,e);
            }
        }
    }

    /**
     * inget content of csv file
     * @param file
     */
    public void ingestFromCsvFile(File file) throws IOException {
        LOGGER.info("Ingest csv file "+file);

        String csvFileContent;

        // first try to read from a regular file
        if(file.exists()) {
            csvFileContent = FileUtils.readFileToString(file, TomwFileUtils.ENCODING);
        }else {
            // otherwise try to read from respurces
            csvFileContent = TomwFileUtils.readTextFileFromResources(file);
        }

        List<FiccTransaction> list = factory.fromCsvString(csvFileContent);
        List<FiccTransaction> listToIngest = new ArrayList<>();
        for(FiccTransaction t : list){
            if(!dao.containsFiccTransactionWithMemo(t.getMemo())){
                listToIngest.add(t);
            }
        }
        ingestList(listToIngest);
    }

    /**
     * Ingest transactions from a list
     * @param list
     */
    public void ingestList(List<FiccTransaction> list){
        for(FiccTransaction t : list){
            LOGGER.info("add transaction "+t);
            dao.add(t);
        }
    }
}
