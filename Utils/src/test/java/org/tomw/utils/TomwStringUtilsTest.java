package org.tomw.utils;


import org.apache.log4j.Logger;
import org.junit.*;

import static org.junit.Assert.*;
import static org.tomw.utils.TomwStringUtils.BLANK;

/**
 * Test class for time utilities
 * Created by tomw on 5/20/2017.
 */
public class TomwStringUtilsTest {

    private final static Logger LOGGER = Logger.getLogger(TomwStringUtilsTest.class.getName());

    public TomwStringUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of removeDoubleSpaces method, of class TomwStringUtils.
     */
    @Test
    public void testRemoveDoubleSpaces() {
        LOGGER.info("removeDoubleSpaces");
        String input = "abcd  efgh ij   ";
        String expResult = "abcd efgh ij ";
        String result = TomwStringUtils.removeDoubleSpaces(input);
        assertEquals(expResult, result);
    }

    @Test
    public void testFilterCommentLines(){
        LOGGER.info("testFilterCommentLines");

        String s1="abcd\n";
        String s2="#efgh\n";
        String s3="xyz\n";

        String expected=s1+s3;
        String actual= TomwStringUtils.filterCommentLines(s1+s2+s3);

        assertEquals(expected,actual);
    }

    @Test
    public void testFilterEmptyLines(){
        LOGGER.info("testFilterEmptyLines");

        String s1="abcd\n";
        String s2="\n";
        String s3="xyz\n";
        String s4="    \n";
        String s5="xyz\n";

        String expected=s1+s3+s5;
        String actual= TomwStringUtils.filterEmptyLines(s1+s2+s3+s4+s5);

        assertEquals(expected,actual);
    }

    @Test
    public void test_stringToBoolean(){
        assertTrue(TomwStringUtils.stringToBoolean("y"));
        assertTrue(TomwStringUtils.stringToBoolean("Y"));
        assertTrue(TomwStringUtils.stringToBoolean("Yes"));
        assertTrue(TomwStringUtils.stringToBoolean("t"));
        assertTrue(TomwStringUtils.stringToBoolean("True"));
        assertTrue(TomwStringUtils.stringToBoolean("1"));

        assertFalse(TomwStringUtils.stringToBoolean(""));
        assertFalse(TomwStringUtils.stringToBoolean("n"));
        assertFalse(TomwStringUtils.stringToBoolean("No"));
        assertFalse(TomwStringUtils.stringToBoolean("False"));
        assertFalse(TomwStringUtils.stringToBoolean("f"));
        assertFalse(TomwStringUtils.stringToBoolean("0"));

        boolean thrown=false;
        try{
            boolean result = TomwStringUtils.stringToBoolean("Noo");
        }catch(Exception e){
            thrown=true;
        }
        assertTrue(thrown);
    }


    @Test
    public void test_bytes2String(){
        LOGGER.info("test_bytes2String");

        long n1 = 11230698631L;
        String actual= TomwStringUtils.bytes2String(n1);
        assertEquals("11.2 Gb",actual);

        long n2 = 11230698L;
        assertEquals("11.2 Mb",TomwStringUtils.bytes2String(n2));

        long n3 = 11230L;
        assertEquals("11.2 Kb",TomwStringUtils.bytes2String(n3));

        long n4 = 112L;
        assertEquals("112 bytes",TomwStringUtils.bytes2String(n4));
    }

    @Test
    public void test_removeLastComa(){
        String s="abcd,xyz,78";
        String expected="abcd,xyz78";
        assertEquals(expected,TomwStringUtils.removeLastComa(s));

        String s2="abcd";
        String expected2="abcd";
        assertEquals(expected2,TomwStringUtils.removeLastComa(s2));
    }

    @Test
    public void test_formatMoney(){
        String expected="123.14";
        String actual = TomwStringUtils.formatMoney(123.141596);
        assertEquals(expected, actual);
    }

    @Test
    public void backslash2slash(){
        String s="abcd\\efgh\\xyz";
        String expected="abcd/efgh/xyz";
        String actual = TomwStringUtils.backslash2slash(s);
        assertEquals(expected, actual);
    }

    @Test
    public void test_String2IntIncludingBlank() throws Exception {
        System.out.println("test_String2IntIncludingBlank");
        assertEquals(1,TomwStringUtils.string2IntIncludingBlank("1"));
        assertEquals(1,TomwStringUtils.string2IntIncludingBlank(" 1"));
        assertEquals(0,TomwStringUtils.string2IntIncludingBlank(""));
        assertEquals(0,TomwStringUtils.string2IntIncludingBlank(" "));
        assertEquals(0,TomwStringUtils.string2IntIncludingBlank(" \t"));
        assertEquals(0,TomwStringUtils.string2IntIncludingBlank(" \t\n"));
        boolean exceptionTrown=false;
        try{
            int result = TomwStringUtils.string2IntIncludingBlank(" \t\n.");
        }catch(Exception e){
            exceptionTrown=true;
        }
        assertTrue(exceptionTrown);
    }

    @Test
    public void test_StringNullCompare() throws Exception {
        assertTrue(TomwStringUtils.stringNullEquals("abc","abc"));
        assertFalse(TomwStringUtils.stringNullEquals("abc","abcx"));
        assertFalse(TomwStringUtils.stringNullEquals("abc",null));
        assertFalse(TomwStringUtils.stringNullEquals(null,"abcx"));
        assertTrue(TomwStringUtils.stringNullEquals(null,null));
    }

    @Test
    public void test_cutString(){
        String in = "abcdefghijkl";
        String expected1=in;
        String expected2="abc...";

        assertEquals(expected1,TomwStringUtils.cutString(in,100));
        assertEquals(expected2,TomwStringUtils.cutString(in,3));
    }

    @Test
    public void test_stringNullCompareOrBothEmpty(){
        String text1=null;
        String text2="";
        String text3="A";
        String text4=new String("A");

        assertTrue(TomwStringUtils.stringNullCompareOrBothEmpty(text1,text2));
        assertFalse(TomwStringUtils.stringNullCompareOrBothEmpty(text1,text3));
        assertFalse(TomwStringUtils.stringNullCompareOrBothEmpty(text2,text3));
        assertTrue(TomwStringUtils.stringNullCompareOrBothEmpty(text3,text4));

    }

    @Test
    public void test_representsInt(){
        assertTrue(TomwStringUtils.representsInt("123"));
        assertTrue(TomwStringUtils.representsInt("-123"));
        assertTrue(TomwStringUtils.representsInt("0"));
        assertTrue(TomwStringUtils.representsInt("00"));

        assertFalse(TomwStringUtils.representsInt("0.0"));
        assertFalse(TomwStringUtils.representsInt("123."));
        assertFalse(TomwStringUtils.representsInt("-123."));
        assertFalse(TomwStringUtils.representsInt("-123.6"));
        assertFalse(TomwStringUtils.representsInt("123.6"));

        assertFalse(TomwStringUtils.representsInt("123A"));
    }

    @Test
    public void parseToInt(){
        assertEquals(123,TomwStringUtils.parseToInt("123"));
        assertEquals(-123,TomwStringUtils.parseToInt("-123"));
        assertEquals(0,TomwStringUtils.parseToInt("0"));
        assertEquals(0,TomwStringUtils.parseToInt("-0"));
        assertEquals(0,TomwStringUtils.parseToInt("00"));
    }

    @Test
    public void test_firstLine(){
        assertEquals("abcd",TomwStringUtils.firstLine("abcd\nxyz\ndfg"));
        assertEquals("abcd",TomwStringUtils.firstLine("\n\t   abcd\nxyz\ndfg"));
        assertEquals(BLANK,TomwStringUtils.firstLine(null));
        assertEquals(BLANK,TomwStringUtils.firstLine(BLANK));
    }

    @Test
    public void test_compareStrings(){
        assertEquals(0,TomwStringUtils.compareStrings(null,null));
        assertEquals(-1,TomwStringUtils.compareStrings(null,"a"));
        assertEquals(1,TomwStringUtils.compareStrings("a",null));

        assertEquals(0,TomwStringUtils.compareStrings("a","a"));
        assertTrue(TomwStringUtils.compareStrings("a","z")<0);
        assertTrue(TomwStringUtils.compareStrings("z","a")>0);
    }

    @Test
    public void test_incrementCheckNumber(){
        assertEquals("AA",TomwStringUtils.incrementCheckNumber("AA"));
        assertEquals("13",TomwStringUtils.incrementCheckNumber("12"));
        assertEquals("-12",TomwStringUtils.incrementCheckNumber("-12"));
    }

}
