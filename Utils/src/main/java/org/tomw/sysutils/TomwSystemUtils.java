package org.tomw.sysutils;

import org.apache.log4j.Logger;
import org.tomw.config.SelfIdentificationService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

/**
 * utility methods for manipulating system tools
 */
public class TomwSystemUtils {
    private final static Logger LOGGER = Logger.getLogger(TomwSystemUtils.class.getName());

    /**
     * edirect application standard output and standard error to file, determined by application
     * directory
     * @param selfIdentificationService
     */
    public static void redirectStdOutStdErr(SelfIdentificationService selfIdentificationService){
        // redirect standard output and standard error
        if (selfIdentificationService.isPreprod() || selfIdentificationService.isProd()) {
            LOGGER.info("redirecting output to directory ");
            PrintStream out = null;
            try {
                String outputFileName = "output."+ LocalDate.now().toString()+".txt";
                out = new PrintStream(new FileOutputStream(outputFileName));
            } catch (FileNotFoundException e) {
                String message = "failed to redirect standard output";
                System.out.println(message);
                e.printStackTrace();
                LOGGER.error(message,e);
            }
            System.setOut(out);
            System.setErr(out);
        }
    }

}
