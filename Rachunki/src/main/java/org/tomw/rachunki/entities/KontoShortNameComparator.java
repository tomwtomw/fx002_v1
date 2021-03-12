package org.tomw.rachunki.entities;

import org.tomw.utils.TomwStringUtils;

import java.util.Comparator;

public class KontoShortNameComparator implements Comparator<Konto> {
    @Override
    public int compare(Konto t1, Konto t2) {
        if (t1==null && t2==null){
            return 0;
        }else{
            if(t1!=null && t2==null){
                return 1;
            }else{
                if(t1==null && t2!=null){
                    return -1;
                }else{
                    // both are not null
                    return TomwStringUtils.compareStrings(
                            t1.getShortName(),
                            t2.getShortName()
                    );
                }
            }
        }
    }
}
