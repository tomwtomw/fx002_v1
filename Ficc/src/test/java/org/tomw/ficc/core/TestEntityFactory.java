package org.tomw.ficc.core;

import org.tomw.ficc.core.FiccCounterParty;
import org.tomw.ficc.core.FiccTransaction;

import java.time.LocalDate;

/**
 * Created by tomw on 7/28/2017.
 */
public class TestEntityFactory {
    public static FiccTransaction getTransaction1() {
        FiccTransaction t = new FiccTransaction();
        t.setMemo("memo1");
        t.setTransaction("DEBIT");
        t.setId("id9999999999999999999");
        t.setDate(LocalDate.of(2017, 7, 11));
        t.setAmount(5.0);
        t.setCounterparty(getCounterParty1());
        t.setComment("Comment transaction 1");
        return t;
    }

    public static FiccTransaction getTransaction2() {
        FiccTransaction t = new FiccTransaction();
        t.setMemo("memo2");
        t.setTransaction("CREDIT");
        t.setId("id888888888888888");
        t.setDate(LocalDate.of(2016, 6, 1));
        t.setAmount(-25.0);
        t.setCounterparty(getCounterParty2());
        return t;
    }

    public static FiccTransaction getTransaction3() {
        FiccTransaction t = new FiccTransaction();
        t.setMemo("memo3");
        t.setTransaction("DEBIT");
        t.setId("id33333333333333");
        t.setDate(LocalDate.of(2015, 4, 19));
        t.setAmount(75.0);
        t.setCounterparty(getCounterParty3());
        return t;
    }

    public static FiccCounterParty getCounterParty3() {
        FiccCounterParty c = new FiccCounterParty("name3");
        c.setId("id33333333333333333333");
        return c;
    }

    public static FiccCounterParty getCounterParty2() {
        FiccCounterParty c = new FiccCounterParty("name2");
        c.setId("id4444444444444444444444");
        c.setComment("Comment cpty 2");
        return c;
    }

    public static FiccCounterParty getCounterParty1() {
        FiccCounterParty c = new FiccCounterParty("name1");
        c.setId("id11111111111111111111");
        return c;
    }
}
