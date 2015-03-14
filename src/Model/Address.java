package Model;

import java.util.*;
import java.util.regex.*;

public class Address implements Comparable<Address> {
    private String street, house, floor, side, postcode, city;
    static private List<Pattern> patternList = new ArrayList<>();



    private Address(String _street, String _house, String _floor, String _side, String _postcode, String _city) {
        street = _street;
        house = _house;
        floor = _floor;
        side = _side;
        postcode = _postcode;
        city = _city;
    }


    /**
     * Specifies the string representation of an address object.
     * @return the string representation of an address object.
     */
    @Override
    public String toString(){
        String s = street.trim() + " " + house.trim() + " " + floor.trim() + " " + side.trim()+" " + postcode.trim() + " " + city.trim();
        s = s.replaceAll(" +", " ");
        return s;
    }


    /**
     * Compare this address object with the specified address object for order.
     * @param addr The address to be compared to.
     * @return A value which is negative if this object is less than the other, zero if they are equal or positive if this object is larger than the other.
     */
    @Override
    public int compareTo(Address addr) {
        return this.toString().compareTo(addr.toString()); //TODO: modify according to whether things like city is null

    }


    public static class Builder {
        private String street = "", house = "", floor = "",
                side = "", postcode = "", city = "";

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
    static public void addPatterns() {
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
     * an Model.Address object with the information given.
     * @param s String, Model.Address
     * @return Model.Address object.
     */
    public static Address parse(String s) {
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
        if(noMatch){
            return null;
        }else {
            return b.build();
        }
    }
}