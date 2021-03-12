package org.tomw.ficc.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tomw.utils.IdGenerator;
import org.tomw.utils.LocalDateUtils;
import org.tomw.utils.UniqueIdGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomw on 7/22/2017.
 */
public class FiccFactory {
    private final static Logger LOGGER = Logger.getLogger(FiccDaoJsonImpl.class.getName());

    // where parameters are stored in CSV line
    public static final String CSV_SPLIT="\t";
    public static final int CSV_DATE=0;
    public static final int CSV_TRANSACTION=1;
    public static final int CSV_NAME=2;
    public static final int CSV_MEMO=3;
    public static final int CSV_AMOUNT=4;

    private FiccDao dao;
    private final JSONParser jsonParser = new JSONParser();
    private final ObjectMapper mapper = new ObjectMapper();
    private IdGenerator idGenerator = new UniqueIdGenerator();

    public static final String EOL = "\n";

    public FiccFactory(){}

    public FiccFactory(FiccDao daoIn){
        dao=daoIn;
    }

    public void setDao(FiccDao dao) {
        this.dao = dao;
    }

    /**
     * Parse content of csv file, interpret lines as transactions
     * @param csv string with csv lines
     * @return list of FiccTransactions
     */
    public List<FiccTransaction> fromCsvString(String csv){
        boolean firstLine = true;
        List<FiccTransaction> list = new ArrayList<>();
        for(String line : csv.split(EOL)){
            if(!firstLine) {
                list.add(fromCsvLine(line));
            }else{
                firstLine=false;
            }
        }
        return list;
    }

    /**
     * unpack a single line in csv, interpret it as Ficc Transactions
     * @param csvLine
     * @return
     */
    public FiccTransaction fromCsvLine(String csvLine){
        String[] parameters = csvLine.split(CSV_SPLIT);
        FiccTransaction transaction = new FiccTransaction();
        transaction.setId(idGenerator.generate());
        transaction.setDate(LocalDateUtils.fromString(parameters[CSV_DATE]));

        FiccCounterParty cp = cptyFromName(parameters[CSV_NAME]);
        dao.add(cp);
        transaction.setCounterparty(cp);

        transaction.setMemo(parameters[CSV_MEMO]);
        transaction.setTransaction(parameters[CSV_TRANSACTION]);
        transaction.setAmount(Double.parseDouble(parameters[CSV_AMOUNT]));
        return transaction;
    }


    /**
     * get counterparty by name
     * @param name
     * @return
     */
    public FiccCounterParty cptyFromName(String name){
        FiccCounterParty result = dao.getFiccCounterPartyByName(name);
        if(result!=null){
            return result;
        }else{
            result = new FiccCounterParty(name);
            result.setId(idGenerator.generate());
            return result;
        }
    }

    /**
     * unpack transaction from json string
     * @param jsonString
     * @return
     * @throws ParseException if string cannot be parsed to json
     * @throws IOException if something is wrong
     */
    public FiccTransaction fromJsonString(String jsonString) throws ParseException, IOException {
        JSONObject json = (JSONObject)jsonParser.parse(jsonString);
        return fromJsonObject(json);
    }

    /**
     * Unpack Transaction form json object
     * @param json object
     * @return FiccTransaction
     * @throws IOException if something is wrong
     */
    public FiccTransaction fromJsonObject(JSONObject json) throws IOException {

        FiccTransaction transaction = new FiccTransaction();
        transaction.setId((String)json.get(FiccTransaction.ID));
        transaction.setDate(LocalDateUtils.fromString((String)json.get(FiccTransaction.DATE)));
        FiccCounterParty cp = dao.getFiccCounterParty((String)json.get(FiccTransaction.COUNTERPARTY_ID));
        transaction.setCounterparty(cp);
        transaction.setTransaction((String)json.get(FiccTransaction.TRANSACTION));
        transaction.setMemo((String)json.get(FiccTransaction.MEMO));
        transaction.setAmount((Double)json.get(FiccTransaction.AMOUNT));
        transaction.setComment((String)json.get(FiccTransaction.COMMENT));
        return transaction;
    }

    /**
     * unpack FiccCounterParty from json object
     * @param json
     * @return
     * @throws IOException
     */
    public FiccCounterParty ficcCptyfromJsonObject(JSONObject json) throws IOException {
        FiccCounterParty cp = mapper.readValue(json.toString(),FiccCounterParty.class);
        return cp;
    }

    /**
     * Build list of Ficc transactions from JsonArray
     * @param jsonArray input list
     * @return List<FiccTransaction> of transactions
     */
    public List<FiccTransaction> fromJsonArray(JSONArray jsonArray) throws IOException {
        List<FiccTransaction> list = new ArrayList<>();
        for(Object o : jsonArray){
            JSONObject json = (JSONObject)o;
            FiccTransaction transaction = fromJsonObject(json);
            list.add(transaction);
        }
        return list;
    }

    /**
     * build counterparty object from cpty name
     * @param cptyName
     * @return
     */
    public FiccCounterParty fromCptyName(String cptyName){
        FiccCounterParty cp = dao.getFiccCounterPartyByName(cptyName);
        if(cp==null){
            cp = new FiccCounterParty(cptyName);
            cp.setId(idGenerator.generate());
        }
        return cp;
    }
}
