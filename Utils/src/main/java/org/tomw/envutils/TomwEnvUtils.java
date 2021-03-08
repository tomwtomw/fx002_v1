package org.tomw.envutils;

import java.io.File;

public class TomwEnvUtils {
    public static final String HOME="homePc";
    public static final String LAPTOP="laptop";
    public static final String UNKNOWN="Unknown";

    public static final String HOME_USER="tomw";
    public static final String LAPTOP_USER="tomw2";

    public static final String NETBEANS_ENV="Netbeans";
    public static final String INTELIJ_ENV="Intelij";
    public static final String PRODUCTION_ENV="Production";

    public static final String NETBEANS_PROJECTS = "NetBeansProjects";
    public static final String IDEA_PROJECTS = "IdeaProjects";

    public static final String NETBEANS = "NetBeans";

    //=== new style environments
    public static final String HOME_INTELLIJ = "Home_Intellij";
    public static final String HOME_PROD = "Home_Prod";


    public static File getApplicationDirectory(){
        return new File(System.getProperty("user.dir"));
    }

    public static String getSystemName(){
        return getSystemName(getApplicationDirectory());
    }

    public static String getSystemName(File dir) {
        return getSystemName(dir.toString());
    }

    public static String getSystemName(String s) {
        if(s.contains(LAPTOP_USER)){
            return LAPTOP;
        }
        if(s.contains(HOME_USER)){
            return HOME;
        }
        return UNKNOWN;
    }

    public static boolean isLaptop(){
        return LAPTOP.equals(getSystemName());
    }

    public static boolean isHome(){
        return HOME.equals(getSystemName());
    }
    public static boolean isUnknown(){
        return UNKNOWN.equals(getSystemName());
    }

    /**
     * determine environment. To be used in the newer, config file based env setups
     */
    public static String getEnvironment(){
        File applicationDirectory = getApplicationDirectory();

        if(isHome() && isIntellijEnv()) return HOME_INTELLIJ;
        if(isHome() && isProdEnv()) return HOME_PROD;
        return UNKNOWN;
    }

    public static String getEnv(){
        File applicationDirectory = getApplicationDirectory();
        if(applicationDirectory.toString().contains(NETBEANS_PROJECTS)){
            return NETBEANS_ENV;
        }
        if(applicationDirectory.toString().contains(IDEA_PROJECTS)){
            return INTELIJ_ENV;
        }
        return PRODUCTION_ENV;
    }

    public static boolean isNetbeansEnv(){
        return getEnv().equals(NETBEANS_ENV);
    }

    public static boolean isIntellijEnv(){
        return getEnv().equals(INTELIJ_ENV);
    }

    public static boolean isProdEnv(){
        return getEnv().equals(PRODUCTION_ENV);
    }

    public static boolean isDeploymentInstance(File applicationDirectory) {
        return !applicationDirectory.toString().contains(NETBEANS) &&
                !applicationDirectory.toString().contains(IDEA_PROJECTS);
    }

}
