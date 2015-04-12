package Tests;

import Model.Address;
import Model.AddressSearcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nicoline on 09-04-2015.
 */
public class AddressSearcherTest {
    private List<Address> testList;
    private List<Address> otherTestlist;
    private List<Address> otherTestlist2;
    private Address addr1;
    private Address addr2;
    private Address addr3;
    private Address addr4;

    @Before
    public void setUp(){
        testList = new ArrayList<>();
        addr1 = Address.newAddress("Testvej","01","1010","Testby");
        testList.add(addr1);
        addr2 = Address.newStreet("Lærkevej");
        testList.add(addr2);
        addr3 = Address.newAddress("Nicoline vej", "2", "8000", "Roskilde");
        testList.add(addr3);
        addr4 = Address.newTown("Andeby");
        testList.add(addr4);
        testList.add(Address.newStreet("Lyndbyvej"));
        testList.add(Address.newAddress("Lyndbyvej", "3", "1000", "Lyndby"));
        testList.add(Address.newAddress("Lyndbyvej","2","1000","Lyndby"));
        testList.add(Address.newAddress("Lyndbyvej","0","1000","Lyndby"));
        Collections.sort(testList);
        //Sorted: Andeby- Lyndbyvej - Lyndbyvej 0 - Lyndbyvej 2 - Lyndbyvej 3 - Lærkevej - Nicolinevej - Testvej

        otherTestlist = new ArrayList<>();
        otherTestlist.add(Address.newTown("Aarhus"));
        otherTestlist.add(Address.newTown("Chicago"));
        otherTestlist.add(Address.newStreet("Death Valley"));
        otherTestlist.add(Address.newAddress("Infinite loop", "2", "2320", "Appleby"));
        otherTestlist.add(Address.newTown("Las Vegas"));
        otherTestlist.add(Address.newStreet("Nicevej"));
        otherTestlist.add(Address.newTown("Sedona"));
        Collections.sort(otherTestlist);

        otherTestlist2 = new ArrayList<>();
        otherTestlist2.add(Address.newTown("Aarhus"));
        otherTestlist2.add(Address.newTown("Chicago"));
        otherTestlist2.add(Address.newStreet("Infinite loop"));
        otherTestlist2.add(Address.newAddress("Infinite loop", "2", "2320", "Appleby"));
        otherTestlist2.add(Address.newAddress("Infinite loop", "3", "2320", "Appleby"));
        otherTestlist2.add(Address.newStreet("Nicevej"));
        otherTestlist2.add(Address.newTown("Sedona"));
        Collections.sort(otherTestlist);
    }

    /*==============================================================================================================
    Test of the binSearch method, types: either 1 = startsWith compare, else equality compare */

    @Test
    public void testBinSearchStartsWithExists(){
        try {
            //Tests for newStreet
            int index = callBinSearch(Address.parse("Lærke"), 1);
            Address result = testList.get(index);
            assertEquals(result, addr2);

            //Tests for newAddress
            index = callBinSearch(Address.parse("Nico"), 1);
            result = testList.get(index);
            assertEquals(result, addr3);

            //Tests for newStreet
            index = callBinSearch(Address.parse("And"), 1);
            result = testList.get(index);
            assertEquals(result, addr4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBinSearchStartsWithDoesNotExist(){
        try {
            int index = callBinSearch(Address.parse("Lærkl"), 1);
            assertEquals(index, -1);

            index = callBinSearch(Address.parse("And 1"), 1);
            assertEquals(index, -1);

            index = callBinSearch(Address.parse("Random 1 1234 Randomby"),1);
            assertEquals(index,-1);

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testBinSearchEqualsExists(){
        try {
            //Tests for newStreet
            int index = callBinSearch(Address.parse("Lærkevej"), 2);
            Address result = testList.get(index);
            assertEquals(result, addr2);

            //Tests for newAddress
            index = callBinSearch(Address.parse("Testvej 01 1010 Testby"), 2);
            result = testList.get(index);
            assertEquals(result, addr1);

            //Tests for newStreet
            index = callBinSearch(Address.parse("Andeby"), 2);
            result = testList.get(index);
            assertEquals(result, addr4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBinSearchEqualsDoesNotExist(){
        try {
            int index = callBinSearch(Address.parse("Lærkevejl"), 2);
            assertEquals(index, -1);

            index = callBinSearch(Address.parse("Andeby 1"), 2);
            assertEquals(index, -1);

            index = callBinSearch(Address.parse("Testvej 01 1012 Testby"),2);
            assertEquals(index,-1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public int callBinSearch(Address addr, int type) throws Exception {
        AddressSearcher addressSearcher = new AddressSearcher();
        Method method = addressSearcher.getClass().getDeclaredMethod("binSearch", testList.getClass(), Address.class, int.class, int.class, int.class);
        method.setAccessible(true);
        return (int)method.invoke(null, testList,addr,0,testList.size()-1,type);
    }

    //==============================================================================================================
    //Test of the multiple entries search

    @Test
    public void testMultipleEntriesSearchOneIteration(){
        try{
            int[] results = callMultipleEntriesSearch(Address.parse("Infinite"),otherTestlist,1);
            assertEquals(results[0],3);
            assertEquals(results[1],3);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleEntriesSearchTwoIterations(){
        try {
            int[] results = callMultipleEntriesSearch(Address.parse("Infinite"),otherTestlist2, 1);
            assertEquals(results[0],2);
            assertEquals(results[1],4);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleEntriesSearchMultipleIterations(){
        //Two iterations in lowerbound and upperbound
        try {
            int[] results = callMultipleEntriesSearch(Address.parse("Lyndby"),testList, 1);
            assertEquals(results[0],1);
            assertEquals(results[1],4);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleSearchIndexNegative(){
        try{
             int[] results = callMultipleEntriesSearch(Address.parse("Non-existant"),testList,1);
             assertEquals(results,null);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public int[] callMultipleEntriesSearch(Address addr, List<Address> list, int type) throws Exception {
        AddressSearcher addressSearcher = new AddressSearcher();
        Method method = addressSearcher.getClass().getDeclaredMethod("multipleEntriesSearch", Address.class, testList.getClass(), int.class);
        method.setAccessible(true);
        return (int[]) method.invoke(null, addr, list,type);
    }

    //==============================================================================================================

}
