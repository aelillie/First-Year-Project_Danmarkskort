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
        Collections.sort(testList);
    }

    //Type of compare: either 1 = startsWith compare, 2 = equality compare and else contains compare

    @Test
    public void testBinSearchStartsWithExists(){
        //Tests for newStreet
        int index = callBinSearch(Address.parse("Lærke"),1);
        Address result = testList.get(index);
        assertEquals(result,addr2);

        //Tests for newAddress
        index = callBinSearch(Address.parse("Nico"),1);
        result = testList.get(index);
        assertEquals(result,addr3);

        //Tests for newStreet
        index = callBinSearch(Address.parse("And"),1);
        result = testList.get(index);
        assertEquals(result,addr4);
    }

    /*@Test
    public void testBinSearchStartsWithDoesNotExist(){
        int index = callBinSearch(Address.parse("Lærkl"),1);
        assertEquals(index,-1);

        index = callBinSearch(Address.parse("Thisisnotsupposedtoexist"),1);
        assertEquals(index,-1);
    }*/


    public int callBinSearch(Address addr, int type){
        AddressSearcher addressSearcher = new AddressSearcher();
        try {
            Method method = addressSearcher.getClass().getDeclaredMethod("binSearch", testList.getClass(), Address.class, int.class, int.class, int.class);
            method.setAccessible(true);
            return (int)method.invoke(null, testList,addr,0,testList.size(),type);

        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}
