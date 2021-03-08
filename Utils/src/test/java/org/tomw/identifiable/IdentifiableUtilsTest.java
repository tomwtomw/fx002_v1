package org.tomw.identifiable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class IdentifiableUtilsTest {
    private  Identifiable id1;
    private  Identifiable id2;
    private  Identifiable id3;

    @Before
    public void setUp() throws Exception {
        id1 = new Identifiable(){
            @Override
            public int getId() {
                return 1;
            }
        };
        id2 = new Identifiable(){
            @Override
            public int getId() {
                return 2;
            }
        };
        id3 = new Identifiable(){
            @Override
            public int getId() {
                return 3;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void test_getObjectsAdded() throws Exception {

        List<Identifiable> listA = new ArrayList<>();
        List<Identifiable> listB = new ArrayList<>();

        listA.add(id1);
        listA.add(id2);

        listB.add(id2);
        listB.add(id3);

        Set added = IdentifiableUtils.getObjectsAdded(listA,listB);

        assertTrue(added.size()==1);
        assertTrue(added.contains(id3));

    }

    @Test
    public void test_getObjectsRemoved() throws Exception {

        List<Identifiable> listA = new ArrayList<>();
        List<Identifiable> listB = new ArrayList<>();

        listA.add(id1);
        listA.add(id2);

        listB.add(id2);
        listB.add(id3);

        Set removed = IdentifiableUtils.getObjectsRemoved(listA,listB);

        assertTrue(removed.size()==1);
        assertTrue(removed.contains(id1));

    }


    @Test
    public void test_collectionsAreIdentical() throws Exception {


        List<Identifiable> listA = new ArrayList<>();
        List<Identifiable> listB = new ArrayList<>();

        listA.add(id1);
        listA.add(id2);
        listA.add(id3);

        listB.add(id1);
        listB.add(id2);
        listB.add(id3);

        assertTrue(IdentifiableUtils.collectionsAreIdentical(listA, listB));

        List<Identifiable> listC = new ArrayList<>();
        List<Identifiable> listD = new ArrayList<>();

        listC.add(id1);
        listC.add(id2);

        listD.add(id2);
        listD.add(id3);

        assertFalse(IdentifiableUtils.collectionsAreIdentical(listC, listD));

    }

    @Test
    public void test_combineCollections(){
        Identifiable id1 = new Identifiable() {
            @Override
            public int getId() {
                return 1;
            }
        };
        Identifiable id2a = new Identifiable() {
            @Override
            public int getId() {
                return 2;
            }
        };
        Identifiable id2b = new Identifiable() {
            @Override
            public int getId() {
                return 2;
            }
        };
        Identifiable id3 = new Identifiable() {
            @Override
            public int getId() {
                return 3;
            }
        };

        Collection<Identifiable> col1 = new ArrayList<>();
        col1.add(id1);
        col1.add(id2a);
        Collection<Identifiable> col2 = new ArrayList<>();
        col2.add(id2b);
        col2.add(id3);

        Collection col3 = IdentifiableUtils.combineCollections(col1,col2);

        assertEquals(3,col3.size());
        assertTrue(col3.contains(id1));
        assertTrue(col3.contains(id3));
        assertTrue(col3.contains(id2a) || col3.contains(id2b));

    }

}