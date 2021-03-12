/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.util.Comparator;

/**
 *
 * @author tomw
 */
public class CounterpartiesNameComparator implements Comparator<CounterParty> {

    @Override
    public int compare(CounterParty c1, CounterParty c2) {
        return c1.getShortName().compareTo(c2.getShortName());
    }
    
}
