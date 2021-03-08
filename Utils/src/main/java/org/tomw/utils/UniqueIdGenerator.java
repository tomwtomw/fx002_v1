package org.tomw.utils;

import java.util.UUID;

/**
 * generate unique id string
 * Created by tomw on 7/29/2017.
 */
public class UniqueIdGenerator implements IdGenerator{

    @Override
    public String generate(){
        return UUID.randomUUID().toString();
    }
}
