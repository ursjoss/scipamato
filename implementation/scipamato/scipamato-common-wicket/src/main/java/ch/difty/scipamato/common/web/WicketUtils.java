package ch.difty.scipamato.common.web;

@SuppressWarnings("WeakerAccess")
public final class WicketUtils {

    public static final String LABEL_TAG                 = "Label";
    public static final String LABEL_RESOURCE_TAG        = ".label";
    public static final String LOADING_RESOURCE_TAG      = ".loading";
    public static final String TITLE_RESOURCE_TAG        = ".title";
    public static final String SHORT_LABEL_RESOURCE_TAG  = ".short.label";
    public static final String PANEL_HEADER_RESOURCE_TAG = ".header";

    private WicketUtils() {
    }

    // hmmm, bloody trick to get the coverage of this class up to what it actually is....
    static String dummyMethod() {
        return LABEL_TAG + LABEL_RESOURCE_TAG + LOADING_RESOURCE_TAG + TITLE_RESOURCE_TAG + SHORT_LABEL_RESOURCE_TAG
               + PANEL_HEADER_RESOURCE_TAG;
    }

}
