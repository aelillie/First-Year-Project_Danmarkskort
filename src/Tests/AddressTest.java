package Tests;

import Model.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by woozy_000 on 25-03-2015.
 */
public class AddressTest{
    @Test
    public void testDash(){
        String input = "Harkos-vej 57, 2860 Egekilde";
        Address addr = Address.parse(input);
        assertEquals("street of " + input, "Harkos-vej", addr.street());
        assertEquals("house of " + input, "57", addr.house());
        assertEquals("postcode of " + input, "2860", addr.postcode());
        assertEquals("city of " + input, "Egekilde", addr.city());
    }

    @Test
    public void testLongAdress(){
        String input = "Karl Gjellerups Alle 19, 5. tv 2860 Søborg";
        Address addr = Address.parse(input);
        assertEquals("street of " + input, "Karl Gjellerups Alle", addr.street());
        assertEquals("house of " + input, "19", addr.house());
        assertEquals("floor of " + input, "5", addr.floor());
        assertEquals("side of " + input, "tv", addr.side());
        assertEquals("postcode of " + input, "2860", addr.postcode());
        assertEquals("city of " + input, "Søborg", addr.city());
    }

    @Test
    public void PostalFirst(){
        String input = "2900 Hellerup, Rigmandsvej 8";
        Address addr = Address.parse(input);
        assertEquals("street of " + input, "Rigmandsvej", addr.street());
        assertEquals("house of " + input, "8", addr.house());
        assertEquals("postcode of " + input, "2900", addr.postcode());
        assertEquals("city of " + input, "Hellerup", addr.city());
    }

    @Test
    public void testAccents(){
        String input = "Ourevej Allé 19, 5. tv 2860 Søborg";
        Address addr = Address.parse(input);
        assertEquals("street of " + input, "Ourevej Allé", addr.street());
        assertEquals("house of " + input, "19", addr.house());
        assertEquals("floor of " + input, "5", addr.floor());
        assertEquals("side of " + input, "tv", addr.side());
        assertEquals("postcode of " + input, "2860", addr.postcode());
        assertEquals("city of " + input, "Søborg", addr.city());
    }

    @Test
    public void testNoSide(){
        String input = "Langgaardsvej 28, 3. Helsingør";
        Address addr = Address.parse(input);
        assertEquals("street of " + input, "Langgaardsvej", addr.street());
        assertEquals("house of " + input, "28", addr.house());
        assertEquals("floor of " + input, "3", addr.floor());
        assertEquals("city of " + input, "Helsingør", addr.city());
    }
/*
    @Test
    public void testWeirdAddress(){
        String input = "5. Junivej 29, 2400 København";
        Address addr = Address.parse(input);
        assertEquals("street of" + input, "5. Junivej", addr.street());
        assertEquals("house of" + input, "29", addr.house());
        assertEquals("postcode of" + input, "2400", addr.postcode());
        assertEquals("city of" + input, "København", addr.city());
    }
    */

}
