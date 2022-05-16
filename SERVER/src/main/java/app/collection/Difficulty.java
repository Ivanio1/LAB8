package app.collection;

import java.io.Serializable;

/**
 * Enumeration with difficulty constants.
 */
public enum Difficulty implements Serializable {
    EASY("EASY",0),
    HARD("HARD",1),
    VERY_HARD("VERY_HARD",2),
    HOPELESS("HOPELESS",3);


    private String Name;
    final private int number;
    Difficulty(String name,int number){
        this.Name = name;
        this.number = number;
    }
    /**
     * Generates a list of enum values.
     * @return String with all enum values splitted by comma.
     */
    public static String nameList() {
        String nameList = "";
        for (Difficulty Type : values()) {      //синтаксис ForEach
            nameList += Type.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2); //обрезание запятой и пробела
    }

    public String getName() {
        return Name;
    }
    public static Difficulty StringNameToObj(String name){
        if (name == null)                               return null;
        if (name.equals(EASY.Name))  return EASY;
        if (name.equals(HARD.Name))  return HARD;
        if (name.equals(VERY_HARD.Name))  return VERY_HARD;
        if (name.equals(HOPELESS.Name))  return HOPELESS;


        else {
            return null;
        }
    }
}
