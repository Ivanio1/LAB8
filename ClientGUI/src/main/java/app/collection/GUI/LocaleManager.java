package app.collection.GUI;


import java.util.Locale;

public class LocaleManager {

    public static final Locale RU_LOCALE = new Locale("ru");
    public static final Locale EN_LOCALE = new Locale("en","IN");
    public static final Locale CZ_LOCALE = new Locale("cz");
    public static final Locale SV_LOCALE = new Locale("sv");

    private static Lang currentLang;

    public static Lang getCurrentLang() {
        return currentLang;
    }

    public static void setCurrentLang(Lang currentLang) {
        LocaleManager.currentLang = currentLang;
    }
}