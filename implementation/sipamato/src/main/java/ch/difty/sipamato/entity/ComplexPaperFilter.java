package ch.difty.sipamato.entity;

import static ch.difty.sipamato.entity.Paper.FLD_AUTHORS;
import static ch.difty.sipamato.entity.Paper.FLD_COMMENT;
import static ch.difty.sipamato.entity.Paper.FLD_DOI;
import static ch.difty.sipamato.entity.Paper.FLD_EXPOSURE_ASSESSMENT;
import static ch.difty.sipamato.entity.Paper.FLD_EXPOSURE_POLLUTANT;
import static ch.difty.sipamato.entity.Paper.FLD_FIRST_AUTHOR;
import static ch.difty.sipamato.entity.Paper.FLD_FIRST_AUTHOR_OVERRIDDEN;
import static ch.difty.sipamato.entity.Paper.FLD_GOALS;
import static ch.difty.sipamato.entity.Paper.FLD_ID;
import static ch.difty.sipamato.entity.Paper.FLD_INTERN;
import static ch.difty.sipamato.entity.Paper.FLD_LOCATION;
import static ch.difty.sipamato.entity.Paper.FLD_MAIN_CODE_OF_CODECLASS1;
import static ch.difty.sipamato.entity.Paper.FLD_METHODS;
import static ch.difty.sipamato.entity.Paper.FLD_METHOD_CONFOUNDERS;
import static ch.difty.sipamato.entity.Paper.FLD_METHOD_OUTCOME;
import static ch.difty.sipamato.entity.Paper.FLD_METHOD_STATISTICS;
import static ch.difty.sipamato.entity.Paper.FLD_METHOD_STUDY_DESIGN;
import static ch.difty.sipamato.entity.Paper.FLD_PMID;
import static ch.difty.sipamato.entity.Paper.FLD_POPULATION;
import static ch.difty.sipamato.entity.Paper.FLD_POPULATION_DURATION;
import static ch.difty.sipamato.entity.Paper.FLD_POPULATION_PARTICIPANTS;
import static ch.difty.sipamato.entity.Paper.FLD_POPULATION_PLACE;
import static ch.difty.sipamato.entity.Paper.FLD_PUBL_YEAR;
import static ch.difty.sipamato.entity.Paper.FLD_RESULT;
import static ch.difty.sipamato.entity.Paper.FLD_RESULT_EFFECT_ESTIMATE;
import static ch.difty.sipamato.entity.Paper.FLD_RESULT_EXPOSURE_RANGE;
import static ch.difty.sipamato.entity.Paper.FLD_TITLE;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link ComplexPaperFilter} is an instance of {@link SipamatoFilter} that provides
 * accessors for all fields present in the entity {@link Paper}, but all in String form.
 *
 * The provided String values may contain query specific meta information that can be interpreted
 * by the query infrastructure to specify e.g. ranges or wildcards.
 *
 * Internally it stores any of the fields that were explicitly set in Maps that can be accessed
 * to be evaluated by the query engine.
 *
 * @author u.joss
 *
 * TODO parse the type specific information and validate both meta information and the actual query term 
 *      (e.g. that integer search specifications are actually integer values when stripped from the meta information)
 */
public class ComplexPaperFilter extends SipamatoFilter implements CodeBoxAware {

    private static final long serialVersionUID = 1L;

    private final Map<String, StringSearchTerm> stringSearchTerms = new HashMap<>();
    private final Map<String, IntegerSearchTerm> integerSearchTerms = new HashMap<>();
    private final Map<String, BooleanSearchTerm> booleanSearchTerms = new HashMap<>();

    /**
     * @return all search terms specified for string fields in entity {@link Paper}
     */
    public Collection<StringSearchTerm> getStringSearchTerms() {
        return stringSearchTerms.values();
    }

    /**
     * @return all search terms specified for integer fields in entity {@link Paper}
     */
    public Collection<IntegerSearchTerm> getIntegerSearchTerms() {
        return integerSearchTerms.values();
    }

    /**
     * @return all search terms specified for boolean fields in entity {@link Paper}
     */
    public Collection<BooleanSearchTerm> getBooleanSearchTerms() {
        return booleanSearchTerms.values();
    }

    private final CodeBox codes = new CodeBox();

    /** {@link Paper} specific accessors */

    public String getId() {
        return getIntegerValue(FLD_ID);
    }

    public void setId(String value) {
        setIntegerValue(FLD_ID, value);
    }

    public String getDoi() {
        return getStringValue(FLD_DOI);
    }

    public void setDoi(String value) {
        setStringValue(FLD_DOI, value);
    }

    public String getPmId() {
        return getStringValue(FLD_PMID);
    }

    public void setPmId(String value) {
        setStringValue(FLD_PMID, value);
    }

    public String getAuthors() {
        return getStringValue(FLD_AUTHORS);
    }

    public void setAuthors(String value) {
        setStringValue(FLD_AUTHORS, value);
    }

    public String getFirstAuthor() {
        return getStringValue(FLD_FIRST_AUTHOR);
    }

    public void setFirstAuthor(String value) {
        setStringValue(FLD_FIRST_AUTHOR, value);
    }

    public Boolean isFirstAuthorOverridden() {
        return getBooleanRawValue(FLD_FIRST_AUTHOR_OVERRIDDEN);
    }

    public void setFirstAuthorOverridden(Boolean value) {
        setBooleanValue(FLD_FIRST_AUTHOR_OVERRIDDEN, value);
    }

    public String getTitle() {
        return getStringValue(FLD_TITLE);
    }

    public void setTitle(String value) {
        setStringValue(FLD_TITLE, value);
    }

    public String getLocation() {
        return getStringValue(FLD_LOCATION);
    }

    public void setLocation(String value) {
        setStringValue(FLD_LOCATION, value);
    }

    public String getPublicationYear() {
        return getIntegerValue(FLD_PUBL_YEAR);
    }

    public void setPublicationYear(String value) {
        setIntegerValue(FLD_PUBL_YEAR, value);
    }

    public String getGoals() {
        return getStringValue(FLD_GOALS);
    }

    public void setGoals(String value) {
        setStringValue(FLD_GOALS, value);
    }

    public String getPopulation() {
        return getStringValue(FLD_POPULATION);
    }

    public void setPopulation(String value) {
        setStringValue(FLD_POPULATION, value);
    }

    public String getMethods() {
        return getStringValue(FLD_METHODS);
    }

    public void setMethods(String value) {
        setStringValue(FLD_METHODS, value);
    }

    public String getResult() {
        return getStringValue(FLD_RESULT);
    }

    public void setResult(String value) {
        setStringValue(FLD_RESULT, value);
    }

    public String getComment() {
        return getStringValue(FLD_COMMENT);
    }

    public void setComment(String value) {
        setStringValue(FLD_COMMENT, value);
    }

    public String getIntern() {
        return getStringValue(FLD_INTERN);
    }

    public void setIntern(String value) {
        setStringValue(FLD_INTERN, value);
    }

    public String getPopulationPlace() {
        return getStringValue(FLD_POPULATION_PLACE);
    }

    public void setPopulationPlace(String value) {
        setStringValue(FLD_POPULATION_PLACE, value);
    }

    public String getPopulationParticipants() {
        return getStringValue(FLD_POPULATION_PARTICIPANTS);
    }

    public void setPopulationParticipants(String value) {
        setStringValue(FLD_POPULATION_PARTICIPANTS, value);
    }

    public String getPopulationDuration() {
        return getStringValue(FLD_POPULATION_DURATION);
    }

    public void setPopulationDuration(String value) {
        setStringValue(FLD_POPULATION_DURATION, value);
    }

    public String getExposurePollutant() {
        return getStringValue(FLD_EXPOSURE_POLLUTANT);
    }

    public void setExposurePollutant(String value) {
        setStringValue(FLD_EXPOSURE_POLLUTANT, value);
    }

    public String getExposureAssessment() {
        return getStringValue(FLD_EXPOSURE_ASSESSMENT);
    }

    public void setExposureAssessment(String value) {
        setStringValue(FLD_EXPOSURE_ASSESSMENT, value);
    }

    public String getMethodStudyDesign() {
        return getStringValue(FLD_METHOD_STUDY_DESIGN);
    }

    public void setMethodStudyDesign(String value) {
        setStringValue(FLD_METHOD_STUDY_DESIGN, value);
    }

    public String getMethodOutcome() {
        return getStringValue(FLD_METHOD_OUTCOME);
    }

    public void setMethodOutcome(String value) {
        setStringValue(FLD_METHOD_OUTCOME, value);
    }

    public String getMethodStatistics() {
        return getStringValue(FLD_METHOD_STATISTICS);
    }

    public void setMethodStatistics(String value) {
        setStringValue(FLD_METHOD_STATISTICS, value);
    }

    public String getMethodConfounders() {
        return getStringValue(FLD_METHOD_CONFOUNDERS);
    }

    public void setMethodConfounders(String value) {
        setStringValue(FLD_METHOD_CONFOUNDERS, value);
    }

    public String getResultExposureRange() {
        return getStringValue(FLD_RESULT_EXPOSURE_RANGE);
    }

    public void setResultExposureRange(String value) {
        setStringValue(FLD_RESULT_EXPOSURE_RANGE, value);
    }

    public String getResultEffectEstimate() {
        return getStringValue(FLD_RESULT_EFFECT_ESTIMATE);
    }

    public void setResultEffectEstimate(String value) {
        setStringValue(FLD_RESULT_EFFECT_ESTIMATE, value);
    }

    public String getMainCodeOfCodeclass1() {
        return getStringValue(FLD_MAIN_CODE_OF_CODECLASS1);
    }

    public void setMainCodeOfCodeclass1(String value) {
        setStringValue(FLD_MAIN_CODE_OF_CODECLASS1, value);
    }

    /** {@link CodeBoxAware} methods */

    @Override
    public void clearCodes() {
        this.codes.clear();
    }

    @Override
    public List<Code> getCodes() {
        return this.codes.getCodes();
    }

    @Override
    public List<Code> getCodesOf(CodeClassId ccId) {
        return this.codes.getCodesBy(ccId);
    }

    @Override
    public void clearCodesOf(CodeClassId ccId) {
        this.codes.clearBy(ccId);
    }

    @Override
    public void addCode(Code code) {
        this.codes.addCode(code);
    }

    @Override
    public void addCodes(List<Code> codes) {
        this.codes.addCodes(codes);
    }

    /**
     * Helper class to capture string specific search terms
     */
    public static class StringSearchTerm implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String key;
        public final String rawValue;

        private StringSearchTerm(final String key, final String value) {
            this.key = key;
            this.rawValue = value;
        }
    }

    private String getStringValue(String key) {
        final StringSearchTerm st = stringSearchTerms.get(key);
        return st != null ? st.rawValue : null;
    }

    private void setStringValue(final String key, String value) {
        if (value != null) {
            stringSearchTerms.put(key, new StringSearchTerm(key, value));
        } else {
            stringSearchTerms.remove(key);
        }
    }

    /**
     * Helper class to capture integer specific search terms
     */
    public static class IntegerSearchTerm implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String key;
        public final String rawValue;

        private IntegerSearchTerm(final String key, final String value) {
            this.key = key;
            this.rawValue = value;
        }
    }

    private String getIntegerValue(String key) {
        final IntegerSearchTerm st = integerSearchTerms.get(key);
        return st != null ? st.rawValue : null;
    }

    private void setIntegerValue(final String key, String value) {
        if (value != null) {
            integerSearchTerms.put(key, new IntegerSearchTerm(key, value));
        } else {
            integerSearchTerms.remove(key);
        }
    }

    /**
     * Helper class to capture boolean specific search terms
     */
    public static class BooleanSearchTerm implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String key;
        public final boolean rawValue;

        private BooleanSearchTerm(final String key, final boolean rawValue) {
            this.key = key;
            this.rawValue = rawValue;
        }
    }

    private Boolean getBooleanRawValue(String key) {
        final BooleanSearchTerm st = booleanSearchTerms.get(key);
        return st != null ? st.rawValue : null;
    }

    private void setBooleanValue(final String key, Boolean value) {
        if (value != null) {
            booleanSearchTerms.put(key, new BooleanSearchTerm(key, value.booleanValue()));
        } else {
            booleanSearchTerms.remove(key);
        }
    }

}
