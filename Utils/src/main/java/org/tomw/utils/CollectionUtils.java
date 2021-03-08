package org.tomw.utils;

import java.util.Collection;

import static org.tomw.utils.TomwStringUtils.BLANK;

/**
 * utility methods to handle collections
 */
public class CollectionUtils {

    /**
     * return string representing colleciton size. If collection is null return "0"
     * @param collection colleciton
     * @return collection size as string
     */
    public static String size2String(Collection collection){
        if(collection==null){
            return "0";
        }else{
            return BLANK+collection.size();
        }
    }
}
