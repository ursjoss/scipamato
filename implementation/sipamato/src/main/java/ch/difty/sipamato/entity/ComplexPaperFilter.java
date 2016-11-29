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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
 */
public class ComplexPaperFilter extends SipamatoFilter implements CodeBoxAware {

    private static final long serialVersionUID = 1L;

    private static final String JOIN_DELIMITER = " AND ";

    private final StringSearchTerms stringSearchTerms = new StringSearchTerms();
    private final IntegerSearchTerms integerSearchTerms = new IntegerSearchTerms();
    private final BooleanSearchTerms booleanSearchTerms = new BooleanSearchTerms();

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
        return getBooleanValue(FLD_FIRST_AUTHOR_OVERRIDDEN);
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

    private String getStringValue(String key) {
        final StringSearchTerm st = stringSearchTerms.get(key);
        return st != null ? st.getRawValue() : null;
    }

    private void setStringValue(final String key, String value) {
        if (value != null) {
            stringSearchTerms.put(key, new StringSearchTerm(key, value));
        } else {
            stringSearchTerms.remove(key);
        }
    }

    private String getIntegerValue(String key) {
        final IntegerSearchTerm st = integerSearchTerms.get(key);
        return st != null ? st.getRawValue() : null;
    }

    private void setIntegerValue(final String key, String value) {
        if (value != null) {
            integerSearchTerms.put(key, new IntegerSearchTerm(key, value));
        } else {
            integerSearchTerms.remove(key);
        }
    }

    private Boolean getBooleanValue(String key) {
        final BooleanSearchTerm st = booleanSearchTerms.get(key);
        return st != null ? st.getValue() : null;
    }

    private void setBooleanValue(final String key, Boolean value) {
        if (value != null) {
            booleanSearchTerms.put(key, new BooleanSearchTerm(key, value.toString()));
        } else {
            booleanSearchTerms.remove(key);
        }
    }

    @Override
    public String toString() {
        final String textString = stringSearchTerms.values().stream().map(StringSearchTerm::toString).collect(Collectors.joining(JOIN_DELIMITER));
        final String intString = integerSearchTerms.values().stream().map(IntegerSearchTerm::toString).collect(Collectors.joining(JOIN_DELIMITER));
        final String boolString = booleanSearchTerms.values().stream().map(BooleanSearchTerm::toString).collect(Collectors.joining(JOIN_DELIMITER));
        return Arrays.asList(textString, intString, boolString).stream().filter((String s) -> !s.isEmpty()).collect(Collectors.joining(JOIN_DELIMITER));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + stringSearchTerms.hashCode();
        result = prime * result + integerSearchTerms.hashCode();
        result = prime * result + booleanSearchTerms.hashCode();
//        result = prime * result + ((codes == null) ? 0 : codes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComplexPaperFilter other = (ComplexPaperFilter) obj;
        if (!booleanSearchTerms.equals(other.booleanSearchTerms))
            return false;
        if (!integerSearchTerms.equals(other.integerSearchTerms))
            return false;
        if (!stringSearchTerms.equals(other.stringSearchTerms))
            return false;
//        if (codes == null) {
//            if (other.codes != null)
//                return false;
//        } else if (!codes.equals(other.codes))
//            return false;
        return true;
    }
}
