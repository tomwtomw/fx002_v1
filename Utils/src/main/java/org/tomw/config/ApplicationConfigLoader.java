package org.tomw.config;

public interface ApplicationConfigLoader {
    public void load();
    public String getValue(String key);

    public SelfIdentificationService getSelfIdentificationService();
}
