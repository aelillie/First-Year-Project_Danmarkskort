package Model;



import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An address object containing information about the address
 * specifications and place on the map
 */
public class Address implements Comparable<Address>, Serializable {
    private String street, house, floor, side, postcode, city;
    static private List<Pattern> patternList = new ArrayList<>();
    private OSMNode addressLocation;
    private Path2D boundaryLocation;

    private Address(String _street, String _house, String _floor, String _side, String _postcode, String _city) {
        street = _street;
        house = _house;
        floor = _floor;
        side = _side;
        postcode = _postcode;
        city = _city;
    }

    public static Address newAddress(String streetName, String houseNumber, String postcode, String city){
        Builder b = new Builder();

        streetName = streetName.intern();
        houseNumber = houseNumber.intern();
        postcode = postcode.intern();
        city = city.intern();

        b.street(streetName);
        b.house(houseNumber);
        b.postcode(postcode);
        b.city(city);
        return b.build();

    }

    public static Address newStreet(String street){
        Builder b = new Builder();
        street = street.intern(); //Using string pool
        b.street(street);
        return b.build();
    }

    public static Address newTown(String city){
        Builder b = new Builder();
        city = city.intern(); //Using string pool
        b.city(city);
        return b.build();
    }

    @Override
    public String toString(){
        if ((!floor.equals("")) && !side.equals("")) {
            System.out.println(floor()+" og "+side());
            String s = String.format("%s %s, %s. %s, %s %s", Address.capitalize(street()), Address.capitalize(house()),floor(), side(),postcode(),Address.capitalize(city)).trim().intern();
            return s;
        } else if(!house.equals("")){
            return String.format("%s %s, %s %s", Address.capitalize(street()), Address.capitalize(house()), postcode(), Address.capitalize(city())).trim().intern();
        } else if(!street.equals("")){
            return String.format("%s", Address.capitalize(street())).trim();
        } else {
            return String.format("%s", Address.capitalize(city())).trim();
        }
    }

    /**
     * Specifies the string representation of an address object.
     * @return the string representation of an address object.
     */
    public String toStringForSort(){
        String s = street.trim() + " " + house.trim() + " " + floor.trim() + " " + side.trim()+" " + postcode.trim() + " " + city.trim();
        s = s.replaceAll(" +", " ");
        s = s.trim();
        s = s.toLowerCase().intern();
        return s;
    }


    /**
     * Compare this address object with the specified address object for order.
     * @param addr The address to be compared to.
     * @return A value which is negative if this object is less than the other, zero if they are equal or positive if this object is larger than the other.
     */
    @Override
    public int compareTo(Address addr) {
        return this.toStringForSort().compareTo(addr.toStringForSort());
    }


    //Type of compare: either 1 = startsWith compare else equality compare
    public int searchCompare(Address addr, int type){
       if(type == 1) {
           if (this.toStringForSort().startsWith(addr.toStringForSort())) return 0;
           else return this.toStringForSort().compareTo(addr.toStringForSort());
       } else {
           if(this.toStringForSort().equals(addr.toStringForSort())) return 0;
           else return this.toStringForSort().compareTo(addr.toStringForSort());
       }
    }


    public static class Builder {
        private String street = "".intern(), house = "".intern(), floor = "".intern(),
                side = "".intern(), postcode = "".intern(), city = "".intern();

        public Builder street(String _street) {
            street = _street;
            return this;
        }

        public Builder house(String _house) {
            house = _house;
            return this;
        }

        public Builder floor(String _floor) {
            floor = _floor;
            return this;
        }

        public Builder side(String _side) {
            side = _side;
            return this;
        }

        public Builder postcode(String _postcode) {
            postcode = _postcode;
            return this;
        }

        public Builder city(String _city) {
            city = _city;
            return this;
        }

        public Address build() {
            return new Address(street, house, floor, side, postcode, city);
        }
    }

    public String street() {
        return street;
    }

    public String house() { return house; }

    public String floor() { return floor; }

    public String side() {
        return side;
    }

    public String postcode() {
        return postcode;
    }

    public String city() {
        return city;
    }


    static String st = "([a-zæøåéäöëüA-ZÆØÅÉÄÖËÜ -./]*)";
    static String ho = "([0-9a-zæøåéA-ZÆØÅÉ]{0,4})";
    static String fl = "([0-9]{0,2})";
    static String si = "([\\w*]{0,3})";
    static String pc = "([0-9]{4})";
    static String ci = "([a-zæøåA-ZÆØÅ .]*)";
    static String filler = "[ ,.-]*";

    /**
     * Sets up patterns to be used for parsing addresses.
     */
    public static void addPatterns() {
        patternList.add(Pattern.compile(st + "" + filler)); //only street name
        patternList.add(Pattern.compile(st + " " + filler + ci + filler)); //street + city
        patternList.add(Pattern.compile(pc + filler + ci + filler)); //postal and city
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + ci + filler));//no postal code
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + pc + filler));//no city name
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + fl + filler + ci + filler)); //no side
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + ci + filler + pc + filler));//no floor and side
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + fl + filler + si + filler + pc + filler + ci + filler));//Street first - postcode, city
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + fl + filler + si + filler + ci + filler + pc + filler)); //Street first - city, postcode
        patternList.add(Pattern.compile(pc + filler + ci + filler + st + " " + filler + ho + filler)); //postcode > city > street > house
        patternList.add(Pattern.compile(ci + filler + pc + filler + st + " " + filler + ho + filler + fl + filler + si + filler)); //city first - postcode, street
        patternList.add(Pattern.compile(pc + filler + ci + filler + st + " " + filler + ho + filler + fl + filler + si + filler)); //postcode first - city, street
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + fl + filler + si + filler + pc + filler)); //street first - postcode
        patternList.add(Pattern.compile(st + " " + filler + ho + filler + fl + filler + si + filler + ci + filler)); //street first - city
        patternList.add(Pattern.compile(ci + filler + st + " " + filler + ho + filler + fl + filler + si + filler)); //city first - street
        patternList.add(Pattern.compile(pc + filler + st + " " + filler + ho + filler + fl + filler + si + filler)); //postcode first - street
    }


    /**
     * This function tries to match an input String with different patterns. If a pattern matches it will create
     * an Address object with the information given.
     * @param s String, Address
     * @return Address object.
     */
    public static Address parse(String s) {
        addPatterns();
        Builder b = new Builder();
        boolean noMatch = false;
        for(int i = 0; i<patternList.size(); i++){
            Matcher m = patternList.get(i).matcher(s);

            if(m.matches() && i==0){
                b.street(m.group(1));
                break;
            }
            else if(m.matches() && i==1){
                b.street(m.group(1));
                b.city(m.group(2));
                break;
            }else if(m.matches() && i==2) {
                b.postcode(m.group(1));
                b.city(m.group(2));
                break;
            }else if(m.matches() && i== 3){
                b.street(m.group(1));
                b.house(m.group(2));
                b.city(m.group(3));
                break;
            }else if (m.matches() && i==4){
                b.street(m.group(1));
                b.house(m.group(2));
                b.postcode(m.group(3));
                break;
            }else if(m.matches() && i==5) {
                b.street(m.group(1));
                b.house(m.group(2));
                b.floor(m.group(3));
                b.city(m.group(4));
                break;
            }else if (m.matches()&& i==6) {
                b.street(m.group(1));
                b.house(m.group(2));
                b.postcode(m.group(4));
                b.city(m.group(3));
                break;
            } else if (m.matches()&& i==7) {
                b.street(m.group(1));
                b.house(m.group(2));
                b.floor(m.group(3));
                b.side(m.group(4));
                b.postcode(m.group(5));
                b.city(m.group(6));
                break;
            } else if (m.matches()&& i==8) {
                b.street(m.group(1));
                b.house(m.group(2));
                b.floor(m.group(3));
                b.side(m.group(4));
                b.postcode(m.group(6));
                b.city(m.group(5));
                break;
            }else if(m.matches() && i==9) {
                b.postcode(m.group(1));
                b.city(m.group(2));
                b.street(m.group(3));
                b.house(m.group(4));
                break;
            }else if (m.matches()&& i==10) {
                b.street(m.group(3));
                b.house(m.group(4));
                b.floor(m.group(5));
                b.side(m.group(6));
                b.postcode(m.group(2));
                b.city(m.group(1));
                break;
            } else if (m.matches()&& i==11) {
                b.street(m.group(3));
                b.house(m.group(4));
                b.floor(m.group(5));
                b.side(m.group(6));
                b.postcode(m.group(1));
                b.city(m.group(2));
                break;
            } else if (m.matches()&& i==12) {
                b.street(m.group(1));
                b.house(m.group(2));
                b.floor(m.group(3));
                b.side(m.group(4));
                b.postcode(m.group(5));
                break;
            } else if (m.matches()&& i==13) {
                b.street(m.group(1));
                b.house(m.group(2));
                b.floor(m.group(3));
                b.side(m.group(4));
                b.city(m.group(5));
                break;
            } else if (m.matches()&& i==14) {
                b.street(m.group(2));
                b.house(m.group(3));
                b.floor(m.group(4));
                b.side(m.group(5));
                b.city(m.group(1));
                break;
            } else if (m.matches() && i==15) {
                b.street(m.group(2));
                b.house(m.group(3));
                b.floor(m.group(4));
                b.side(m.group(5));
                b.postcode(m.group(1));
                break;
            }else if (i==patternList.size()-1){
                noMatch = true;
                break;
            }
        }
        if(noMatch)return null;
        else return b.build();

    }

    /**
     * Capitalizes a String
     * @param string the string to be capitalized
     * @return the capitalized string
     */
    public static String capitalize(String string) {
        char[] chars = string.toCharArray();
        boolean capitalize = true;

        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (capitalize) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
                capitalize = false;
            } else {
                if(Character.isWhitespace(chars[i])){
                    capitalize = true;
                } else if (Character.isDigit(chars[i])){
                    capitalize = true;
                }
            }
            if(chars[i] == '.'){
                capitalize = true;
            }
        }
        return new String(chars).intern();
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Address)) return false;
        if (obj == this) return true;
        Address addr = (Address) obj;
        return this.toStringForSort().equals(addr.toStringForSort());
    }

    @Override
    public int hashCode(){
        return Objects.hash(street,house,floor,side,postcode,city);
    }

    public void setAddressLocation(OSMNode addressLocation){this.addressLocation = addressLocation;}

    public OSMNode getAddressLocation() {return addressLocation;}

    public Path2D getBoundaryLocation() {return boundaryLocation;}

}