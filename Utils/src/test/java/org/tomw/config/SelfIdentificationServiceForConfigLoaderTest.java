package org.tomw.config;

import org.tomw.config.SelfIdentificationService;

/**
 * test implementation of self identification service class, for the sole purpose
 * of unit testing the ConfigLoaderJsonImpl class
 */
public class SelfIdentificationServiceForConfigLoaderTest extends SelfIdentificationService {
    @Override
    public String getApplicationName() {
        return "ConfigLoaderJsonImplTest";
    }

    @Override
    public boolean isTest() {
        return true;
    }

    @Override
    public boolean isDevel() {
        return false;
    }

    @Override
    public boolean isPreprod() {
        return false;
    }

    @Override
    public boolean isProd() {
        return false;
    }

    @Override
    public String instanceName() {
        return getApplicationName()+"@"+instanceType()+"@"+getSystemName();
    }
}
