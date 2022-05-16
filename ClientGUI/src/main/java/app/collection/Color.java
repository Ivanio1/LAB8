package app.collection;

import java.io.Serializable;

/**
 * Enumeration with color name constants.
 */
public enum Color implements Serializable {
    GREEN("GREEN", 0),
    RED("RED", 1),
    BLACK("BLACK", 2),
    ORANGE("ORANGE", 3),
    BROWN("BROWN", 4);
    private String Name;
    final private int number;

    Color(String name, int number) {
        this.Name = name;
        this.number = number;
    }

    /**
     * Generates a list of enum values.
     *
     * @return String with all enum values splitted by comma.
     */
    public static String nameList() {
        String nameList = "";
        for (Color colorType : values()) {      //синтаксис ForEach
            nameList += colorType.name() + ", ";
        }
        return nameList.substring(0, nameList.length() - 2); //обрезание запятой и пробела
    }

    public String getName() {
        return Name;
    }

    public static Color StringNameToObj(String name) {
        if (name == null) return null;
        if (name.equals(GREEN.Name)) return GREEN;
        if (name.equals(BLACK.Name)) return BLACK;
        if (name.equals(RED.Name)) return RED;
        if (name.equals(ORANGE.Name)) return ORANGE;
        if (name.equals(BROWN.Name)) return BROWN;

        else {
            return null;
        }
    }
}
