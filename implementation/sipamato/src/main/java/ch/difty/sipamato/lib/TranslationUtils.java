package ch.difty.sipamato.lib;

public final class TranslationUtils {

    public static final String NOT_TRANSL = "not translated";

    private TranslationUtils() {
    }

    public static String trimLanguageCode(String languageCode) {
        String lc = AssertAs.notNull(languageCode, "languageCode");
        if (lc.length() > 2)
            return lc.substring(0, 2);
        return lc;
    }
}
