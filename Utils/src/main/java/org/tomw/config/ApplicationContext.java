package org.tomw.config;

public interface ApplicationContext {
    /**
     * Load context info
     */
    void load();

    /**
     * persist context info
     */
    void persist();

    /**
     * save key, value pair into context
     */
    public void setContext(String key, String value);

    /**
     * Load parameter from context
     * @param key parameter to be loaded
     * @return value
     */
    public String getContext(String key);
}
