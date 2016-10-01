package ch.difty.sipamato.config;

/**
 * Parses a property value to define the {@link SaveMode} to be used for saving models from wicket pages.
 *
 * @author u.joss
 */
public enum SaveMode {

    /**
     * Saving automatically using ajax, kicked off using a timer event 
     */
    AUTO,

    /**
     * Saving normally using submit.
     */
    SUBMIT;

    public static SaveMode fromProperty(final String propertyValue) {
        return PropertyUtils.fromProperty(propertyValue, values(), SUBMIT, ApplicationProperties.PAPER_SAVE_MODE);
    }

}
