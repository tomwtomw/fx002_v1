package org.tomw.rachunki.ingest;

import org.tomw.fileutils.TomwFileUtils;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.entities.Konto;
import org.tomw.utils.TomwStringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.tomw.utils.TomwStringUtils.EOL;

public class CpToAccountMapper {
    private RachunkiService rachunkiService;

    private Map<String,String> tfcuMapping = new HashMap<>();

    public CpToAccountMapper(String mappingFileName, RachunkiService rachunkiService){
        this.rachunkiService=rachunkiService;
        init(mappingFileName);
    }

    private void init(String mappingFileName){
        loadTfcuMapping(mappingFileName);
    }

    public Konto getTfcuMapping(String cpId){
        if (tfcuMapping.containsKey(cpId)) {
            String accountid = tfcuMapping.get(cpId);
            Konto konto = rachunkiService.getKontoWithOldId(accountid);
            return konto;
        }else{
            return null;
        }
    }

    private void loadTfcuMapping(String mappingFileName) {
        String tfcuMappingString = TomwFileUtils.readTextFileFromResources(mappingFileName);
        tfcuMappingString = TomwStringUtils.filterCommentLines(tfcuMappingString);
        for (String line : tfcuMappingString.split(EOL)){
            if( line.contains("@")){
                //System.out.println(line);
                String cpId = line.split("@")[1].trim();
                String accountid = line.split("@")[0].trim().split(" ")[1].trim();
                //System.out.println(accountid+" "+cpId);
                tfcuMapping.put(cpId,accountid);
            }
        }
    }
}
