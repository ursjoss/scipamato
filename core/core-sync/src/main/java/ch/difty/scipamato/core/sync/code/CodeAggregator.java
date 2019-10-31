package ch.difty.scipamato.core.sync.code;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface CodeAggregator {

    /**
     * @param internalCodes
     *     Sets the internal codes as list of strings ('1A', '1B'...)
     */
    void setInternalCodes(@NotNull List<String> internalCodes);

    /**
     * @param codes
     *     loads the codes to be aggregated into the Bean
     */
    void load(@NotNull String[] codes);

    /**
     * @return the aggregated codes as array of strings
     */
    @NotNull
    String[] getAggregatedCodes();

    /**
     * @return the codesPopulation as arrays of shorts
     */
    @NotNull
    Short[] getCodesPopulation();

    /**
     * @return the codesStudyDesign as arrays of shorts
     */
    @NotNull
    Short[] getCodesStudyDesign();
}
