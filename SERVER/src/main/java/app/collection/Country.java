package app.collection;

import java.io.Serializable;

/**
 * Enumeration with country name constants.
 */
public enum Country implements Serializable {
    USA("USA",0),
    GERMANY("GERMANY",1),
    INDIA("INDIA",2),
    VATICAN("VATICAN",3),
    SOUTH_KOREA("SOUTH_KOREA",4);
    private String Name;
    final private int number;
    Country(String name,int number){
        this.Name = name;
        this.number = number;
    }
    /**
     * Generates a list of enum values.
     * @return String with all enum values splitted by comma.
     */
    public static String nameList() {
        String nameList = "";
        for (Country Type : values()) {      //синтаксис ForEach
            nameList += Type.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2); //обрезание запятой и пробела
    }

    public String getName() {
        return Name;
    }
    public static Country StringNameToObj(String name){
        if (name == null)                               return null;
        if (name.equals(USA.Name))  return USA;
        if (name.equals(GERMANY.Name))  return GERMANY;
        if (name.equals(INDIA.Name))  return INDIA;
        if (name.equals(VATICAN.Name))  return VATICAN;
        if (name.equals(SOUTH_KOREA.Name))  return SOUTH_KOREA;

        else {
            return null;
        }
    }
}
