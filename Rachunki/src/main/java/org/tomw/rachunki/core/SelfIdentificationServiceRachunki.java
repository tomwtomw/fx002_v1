package org.tomw.rachunki.core;

import org.tomw.config.SelfIdentificationService;

import java.util.HashMap;
import java.util.Map;


public class SelfIdentificationServiceRachunki extends SelfIdentificationService {
    public final String APPLICATION_NAME = "Rachunki";

    public  final String DEVEL_DIR_OFFICE1 = "C:\\Users\\tomw\\IdeaProjects\\fx001_v2";
    public  final String TEST_DIR_OFFICE1 = "C:\\Users\\tomw\\IdeaProjects\\fx001_v2\\Rachunki";

    public  final String DEVEL_DIR_LAPTOP1 = "D:\\tomw2\\IdeaProjects\\fx001_v2";
    public  final String TEST_DIR_LAPTOP1 = "D:\\tomw2\\IdeaProjects\\fx001_v2\\Rachunki";

    public SelfIdentificationServiceRachunki( ){
        initMap();
    }

    void initMap(){
        getMap().put(OFFICE1,new HashMap<>());
        getMap().get(OFFICE1).put(DEVEL,DEVEL_DIR_OFFICE1);
        getMap().get(OFFICE1).put(TEST,TEST_DIR_OFFICE1);

        getMap().put(LAPTOP1,new HashMap<>());
        getMap().get(LAPTOP1).put(DEVEL,DEVEL_DIR_LAPTOP1);
        getMap().get(LAPTOP1).put(TEST,TEST_DIR_LAPTOP1);
    }

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }
}
