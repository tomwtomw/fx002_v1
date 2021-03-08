package org.tomw.bidirectionallist;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BiListTest {
    BiList<String> biList;
    List<String> list;
    @Before
    public void setUp() throws Exception {
        list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        biList = new BiList<>(list);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_GetContent() throws Exception {
        System.out.println("test_GetContent");
        assertEquals(list,biList.getContent());
    }

    @Test
    public void test_Add() throws Exception {
        System.out.println("test_Add");
        assertEquals("A",biList.getCurrent());
        assertEquals(4,biList.getContent().size());
        assertEquals(0,biList.getCurrentIndex());

        biList.add("E");
        assertEquals(5,biList.getContent().size());
        assertEquals("E",biList.getCurrent());
        assertEquals(1,biList.getCurrentIndex());
    }

    @Test
    public void test_GetCurrent() throws Exception {
        System.out.println("test_GetCurrent");
        assertEquals("A",biList.getCurrent());
        biList.moveRight();
        biList.moveRight();
        assertEquals("C",biList.getCurrent());
        biList.moveLeft();
        assertEquals("B",biList.getCurrent());
    }

    @Test
    public void test_RemoveCurrent() throws Exception {
        System.out.println("test_RemoveCurrent");
        int size = biList.getContent().size();
        assertEquals("A",biList.getCurrent());
        biList.moveRight();
        assertEquals("B",biList.getCurrent());
        String removed = biList.removeCurrent();
        assertEquals("B",removed);
        assertEquals("C",biList.getCurrent());
        assertEquals(size-1,biList.getContent().size());
    }

    @Test
    public void test_IsEmpty() throws Exception {
        System.out.println("test_IsEmpty");
        assertFalse(biList.isEmpty());
        biList.clear();
        assertTrue(biList.isEmpty());
    }

    @Test
    public void test_CanMoveToRight() throws Exception {
        System.out.println("test_CanMoveToRight");
        assertTrue(biList.canMoveToRight());
        biList.moveRight();
        biList.moveRight();
        assertTrue(biList.canMoveToRight());
        biList.moveRight();
        assertFalse(biList.canMoveToRight());
        biList.moveLeft();
        assertTrue(biList.canMoveToRight());
        biList.clear();
        assertFalse(biList.canMoveToRight());
    }

    @Test
    public void test_IsLast() throws Exception {
        System.out.println("test_IsLast");
        assertFalse(biList.isLast());

        biList.moveRight();
        assertFalse(biList.isLast());

        biList.moveRight();
        assertFalse(biList.isLast());

        biList.moveRight();
        assertTrue(biList.isLast());

        biList.moveLeft();
        assertFalse(biList.isLast());

        biList.clear();
        assertFalse(biList.isLast());
    }

    @Test
    public void test_CanMoveToLeft() throws Exception {
        System.out.println("test_CanMoveToLeft");
        assertFalse(biList.canMoveToLeft());

        biList.moveRight();
        biList.moveRight();
        assertTrue(biList.canMoveToLeft());

        biList.moveLeft();
        biList.moveLeft();
        biList.moveLeft();
        assertFalse(biList.canMoveToLeft());

        biList.clear();
        assertFalse(biList.canMoveToLeft());
    }

    @Test
    public void test_IsFirst() throws Exception {
        System.out.println("test_IsFirst");
        assertTrue(biList.isFirst());
        biList.moveLeft();
        assertTrue(biList.isFirst());

        biList.moveRight();
        assertFalse(biList.isFirst());

        biList.moveLeft();
        assertTrue(biList.isFirst());

        biList.clear();
        assertFalse(biList.isFirst());
    }

    @Test
    public void test_MoveRight() throws Exception {
        System.out.println("test_MoveRight");
        assertEquals("A",biList.getCurrent());

        biList.moveRight();
        assertEquals("B",biList.getCurrent());

        biList.moveRight();
        assertEquals("C",biList.getCurrent());
        biList.moveRight();
        assertEquals("D",biList.getCurrent());
        biList.moveRight();
        assertEquals("D",biList.getCurrent());

        biList.clear();
        biList.moveRight();
        assertEquals(null,biList.getCurrent());
    }

    @Test
    public void test_MoveLeft() throws Exception {
        System.out.println("test_MoveLeft");
        assertEquals("A",biList.getCurrent());

        biList.moveLeft();
        assertEquals("A",biList.getCurrent());

        biList.moveRight();
        biList.moveRight();
        biList.moveRight();
        assertEquals("D",biList.getCurrent());
        biList.moveLeft();
        assertEquals("C",biList.getCurrent());

    }

    @Test
    public void test_Clear() throws Exception {
        System.out.println("test_Clear");
        assertFalse(biList.isEmpty());
        assertEquals(0,biList.getCurrentIndex());
        biList.clear();
        assertTrue(biList.isEmpty());
        assertEquals(-1,biList.getCurrentIndex());
    }

}