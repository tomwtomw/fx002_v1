package org.tomw.rachunki.ingest;

import org.tomw.rachunki.core.SelfIdentificationServiceRachunki;

/**
 * Class wwhich enforces prod instance or rachunki application in office. To be used to ingest prod
 * database with IngestMain
 */
public class SelfIdentificationServiceRachunkiOfficeProd extends SelfIdentificationServiceRachunki {
    public boolean isKnownInstance(String instance){ return true;}

    public String  getSystemName(){
            return OFFICE1;
    }

    public String getSystemType(){
       return PROD;
    }

    /**
     * is this LAPTOP1?
     * @return true if yes
     */
    public boolean isLaptop1(){
        return false;
    }

    /**
     * Is this office Pc
     * @return true if yes
     */
    public boolean isOffice1(){
        return true;
    }

    public boolean isTest() {
        return false;
    }

    public boolean isDevel() {
        return false;
    }

    public boolean isPreprod() {
        return  false;
    }

    public boolean isProd() {
        return  true;
    }

    public String instanceType(){
        return PROD;
    }
}
