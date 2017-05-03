package ch.difty.sipamato.entity.filter;

import static ch.difty.sipamato.entity.IdSipamatoEntity.ID;
import static ch.difty.sipamato.entity.Paper.AUTHORS;
import static ch.difty.sipamato.entity.Paper.COMMENT;
import static ch.difty.sipamato.entity.Paper.CREATED;
import static ch.difty.sipamato.entity.Paper.CREATED_BY;
import static ch.difty.sipamato.entity.Paper.DOI;
import static ch.difty.sipamato.entity.Paper.EXPOSURE_ASSESSMENT;
import static ch.difty.sipamato.entity.Paper.EXPOSURE_POLLUTANT;
import static ch.difty.sipamato.entity.Paper.FIRST_AUTHOR;
import static ch.difty.sipamato.entity.Paper.FIRST_AUTHOR_OVERRIDDEN;
import static ch.difty.sipamato.entity.Paper.GOALS;
import static ch.difty.sipamato.entity.Paper.INTERN;
import static ch.difty.sipamato.entity.Paper.LAST_MOD;
import static ch.difty.sipamato.entity.Paper.LAST_MOD_BY;
import static ch.difty.sipamato.entity.Paper.LOCATION;
import static ch.difty.sipamato.entity.Paper.MAIN_CODE_OF_CODECLASS1;
import static ch.difty.sipamato.entity.Paper.METHODS;
import static ch.difty.sipamato.entity.Paper.METHOD_CONFOUNDERS;
import static ch.difty.sipamato.entity.Paper.METHOD_OUTCOME;
import static ch.difty.sipamato.entity.Paper.METHOD_STATISTICS;
import static ch.difty.sipamato.entity.Paper.METHOD_STUDY_DESIGN;
import static ch.difty.sipamato.entity.Paper.NUMBER;
import static ch.difty.sipamato.entity.Paper.ORIGINAL_ABSTRACT;
import static ch.difty.sipamato.entity.Paper.PMID;
import static ch.difty.sipamato.entity.Paper.POPULATION;
import static ch.difty.sipamato.entity.Paper.POPULATION_DURATION;
import static ch.difty.sipamato.entity.Paper.POPULATION_PARTICIPANTS;
import static ch.difty.sipamato.entity.Paper.POPULATION_PLACE;
import static ch.difty.sipamato.entity.Paper.PUBL_YEAR;
import static ch.difty.sipamato.entity.Paper.RESULT;
import static ch.difty.sipamato.entity.Paper.RESULT_EFFECT_ESTIMATE;
import static ch.difty.sipamato.entity.Paper.RESULT_EXPOSURE_RANGE;
import static ch.difty.sipamato.entity.Paper.RESULT_MEASURED_OUTCOME;
import static ch.difty.sipamato.entity.Paper.TITLE;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeBox;
import ch.difty.sipamato.entity.CodeBoxAware;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchConditionCodeBox;

/**
 * The {@link SearchCondition} is an instance of {@link SipamatoFilter} that provides
 * accessors for all fields present in the entity {@link Paper}, but all in String form.<p/>
 *
 * The provided String values may contain query specific meta information that can be interpreted
 * by the query infrastructure to specify e.g. ranges or wildcards.<p/>
 *
 * Internally it stores any of the fields that were explicitly set in Maps that can be accessed
 * to be evaluated by the query engine.<p/>
 *
 * <b>Note:</b> the actual ID of the {@link SearchCondition} is called <code>searchConditionId</code>
 * due to the name clash with its search condition id, which holds the search term for the paper id.<p/>
 *
 * @author u.joss
 */
public class SearchCondition extends SipamatoFilter implements CodeBoxAware {

    private static final long serialVersionUID = 1L;

    private static final String JOIN_DELIMITER = " AND ";

    private Long searchConditionId;

    private final StringSearchTerms stringSearchTerms = new StringSearchTerms();
    private final IntegerSearchTerms integerSearchTerms = new IntegerSearchTerms();
    private final BooleanSearchTerms booleanSearchTerms = new BooleanSearchTerms();
    private final AuditSearchTerms auditSearchTerms = new AuditSearchTerms();
    private final CodeBox codes = new SearchConditionCodeBox();
    private final Set<String> removedKeys = new HashSet<>();

    public SearchCondition() {
        // default constructor
    }

    public SearchCondition(Long searchConditionId) {
        setSearchConditionId(searchConditionId);
    }

    public Long getSearchConditionId() {
        return searchConditionId;
    }

    public void setSearchConditionId(Long searchConditionId) {
        this.searchConditionId = searchConditionId;
    }

    public void addSearchTerm(final SearchTerm searchTerm) {
        switch (searchTerm.getSearchTermType()) {
        case BOOLEAN:
            final BooleanSearchTerm bst = (BooleanSearchTerm) searchTerm;
            booleanSearchTerms.put(bst.getFieldName(), bst);
            break;
        case INTEGER:
            final IntegerSearchTerm ist = (IntegerSearchTerm) searchTerm;
            integerSearchTerms.put(ist.getFieldName(), ist);
            break;
        case STRING:
            final StringSearchTerm sst = (StringSearchTerm) searchTerm;
            stringSearchTerms.put(sst.getFieldName(), sst);
            break;
        case AUDIT:
            final AuditSearchTerm ast = (AuditSearchTerm) searchTerm;
            auditSearchTerms.put(ast.getFieldName(), ast);
            break;
        default:
            throw new UnsupportedOperationException("SearchTermType." + searchTerm.getSearchTermType() + " is not supported");
        }
    }

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

    /**
     * @return all search terms specified for audit fields in entity {@link Paper}
     */
    public Collection<AuditSearchTerm> getAuditSearchTerms() {
        return auditSearchTerms.values();
    }

    /** {@link Paper} specific accessors */

    public String getId() {
        return getIntegerValue(ID);
    }

    public void setId(String value) {
        setIntegerValue(value, ID);
    }

    public String getNumber() {
        return getIntegerValue(NUMBER);
    }

    public void setNumber(String value) {
        setIntegerValue(value, NUMBER);
    }

    public String getDoi() {
        return getStringValue(DOI);
    }

    public void setDoi(String value) {
        setStringValue(value, DOI);
    }

    public String getPmId() {
        return getStringValue(PMID);
    }

    public void setPmId(String value) {
        setStringValue(value, PMID);
    }

    public String getAuthors() {
        return getStringValue(AUTHORS);
    }

    public void setAuthors(String value) {
        setStringValue(value, AUTHORS);
    }

    public String getFirstAuthor() {
        return getStringValue(FIRST_AUTHOR);
    }

    public void setFirstAuthor(String value) {
        setStringValue(value, FIRST_AUTHOR);
    }

    public Boolean isFirstAuthorOverridden() {
        return getBooleanValue(FIRST_AUTHOR_OVERRIDDEN);
    }

    public void setFirstAuthorOverridden(Boolean value) {
        setBooleanValue(FIRST_AUTHOR_OVERRIDDEN, value);
    }

    public String getTitle() {
        return getStringValue(TITLE);
    }

    public void setTitle(String value) {
        setStringValue(value, TITLE);
    }

    public String getLocation() {
        return getStringValue(LOCATION);
    }

    public void setLocation(String value) {
        setStringValue(value, LOCATION);
    }

    public String getPublicationYear() {
        return getIntegerValue(PUBL_YEAR);
    }

    public void setPublicationYear(String value) {
        setIntegerValue(value, PUBL_YEAR);
    }

    public String getGoals() {
        return getStringValue(GOALS);
    }

    public void setGoals(String value) {
        setStringValue(value, GOALS);
    }

    public String getPopulation() {
        return getStringValue(POPULATION);
    }

    public void setPopulation(String value) {
        setStringValue(value, POPULATION);
    }

    public String getMethods() {
        return getStringValue(METHODS);
    }

    public void setMethods(String value) {
        setStringValue(value, METHODS);
    }

    public String getResult() {
        return getStringValue(RESULT);
    }

    public void setResult(String value) {
        setStringValue(value, RESULT);
    }

    public String getComment() {
        return getStringValue(COMMENT);
    }

    public void setComment(String value) {
        setStringValue(value, COMMENT);
    }

    public String getIntern() {
        return getStringValue(INTERN);
    }

    public void setIntern(String value) {
        setStringValue(value, INTERN);
    }

    public String getOriginalAbstract() {
        return getStringValue(ORIGINAL_ABSTRACT);
    }

    public void setOriginalAbstract(String value) {
        setStringValue(value, ORIGINAL_ABSTRACT);
    }

    public String getPopulationPlace() {
        return getStringValue(POPULATION_PLACE);
    }

    public void setPopulationPlace(String value) {
        setStringValue(value, POPULATION_PLACE);
    }

    public String getPopulationParticipants() {
        return getStringValue(POPULATION_PARTICIPANTS);
    }

    public void setPopulationParticipants(String value) {
        setStringValue(value, POPULATION_PARTICIPANTS);
    }

    public String getPopulationDuration() {
        return getStringValue(POPULATION_DURATION);
    }

    public void setPopulationDuration(String value) {
        setStringValue(value, POPULATION_DURATION);
    }

    public String getExposurePollutant() {
        return getStringValue(EXPOSURE_POLLUTANT);
    }

    public void setExposurePollutant(String value) {
        setStringValue(value, EXPOSURE_POLLUTANT);
    }

    public String getExposureAssessment() {
        return getStringValue(EXPOSURE_ASSESSMENT);
    }

    public void setExposureAssessment(String value) {
        setStringValue(value, EXPOSURE_ASSESSMENT);
    }

    public String getMethodStudyDesign() {
        return getStringValue(METHOD_STUDY_DESIGN);
    }

    public void setMethodStudyDesign(String value) {
        setStringValue(value, METHOD_STUDY_DESIGN);
    }

    public String getMethodOutcome() {
        return getStringValue(METHOD_OUTCOME);
    }

    public void setMethodOutcome(String value) {
        setStringValue(value, METHOD_OUTCOME);
    }

    public String getMethodStatistics() {
        return getStringValue(METHOD_STATISTICS);
    }

    public void setMethodStatistics(String value) {
        setStringValue(value, METHOD_STATISTICS);
    }

    public String getMethodConfounders() {
        return getStringValue(METHOD_CONFOUNDERS);
    }

    public void setMethodConfounders(String value) {
        setStringValue(value, METHOD_CONFOUNDERS);
    }

    public String getResultExposureRange() {
        return getStringValue(RESULT_EXPOSURE_RANGE);
    }

    public void setResultExposureRange(String value) {
        setStringValue(value, RESULT_EXPOSURE_RANGE);
    }

    public String getResultEffectEstimate() {
        return getStringValue(RESULT_EFFECT_ESTIMATE);
    }

    public void setResultEffectEstimate(String value) {
        setStringValue(value, RESULT_EFFECT_ESTIMATE);
    }

    public String getResultMeasuredOutcome() {
        return getStringValue(RESULT_MEASURED_OUTCOME);
    }

    public void setResultMeasuredOutcome(String value) {
        setStringValue(value, RESULT_MEASURED_OUTCOME);
    }

    public String getMainCodeOfCodeclass1() {
        return getStringValue(MAIN_CODE_OF_CODECLASS1);
    }

    public void setMainCodeOfCodeclass1(String value) {
        setStringValue(value, MAIN_CODE_OF_CODECLASS1);
    }

    public String getCreatedDisplayValue() {
        return getAuditValue(CREATED_BY);
    }

    public void setCreatedDisplayValue(String value) {
        setAuditValue(value, CREATED_BY, CREATED);
    }

    public String getModifiedDisplayValue() {
        return getAuditValue(LAST_MOD_BY);
    }

    public void setModifiedDisplayValue(String value) {
        setAuditValue(value, LAST_MOD_BY, LAST_MOD);
    }

    public String getCreated() {
        return getAuditValue(CREATED);
    }

    public String getCreatedBy() {
        return getAuditValue(CREATED_BY);
    }

    public String getLastModified() {
        return getAuditValue(LAST_MOD);
    }

    public String getLastModifiedBy() {
        return getAuditValue(LAST_MOD_BY);
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
        return st != null ? st.getRawSearchTerm() : null;
    }

    private void setStringValue(String value, final String key) {
        if (value != null) {
            stringSearchTerms.put(key, new StringSearchTerm(key, value));
            getRemovedKeys().remove(key);
        } else {
            getRemovedKeys().add(key);
            stringSearchTerms.remove(key);
        }
    }

    private String getIntegerValue(String key) {
        final IntegerSearchTerm st = integerSearchTerms.get(key);
        return st != null ? st.getRawSearchTerm() : null;
    }

    private void setIntegerValue(String value, final String key) {
        if (value != null) {
            integerSearchTerms.put(key, new IntegerSearchTerm(key, value));
            getRemovedKeys().remove(key);
        } else {
            getRemovedKeys().add(key);
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
            getRemovedKeys().remove(key);
        } else {
            getRemovedKeys().add(key);
            booleanSearchTerms.remove(key);
        }
    }

    private String getAuditValue(String key) {
        final AuditSearchTerm st = auditSearchTerms.get(key);
        return st != null ? st.getRawSearchTerm() : null;
    }

    /**
     * Here we allow multiple keys (i.e. fields)
     */
    private void setAuditValue(String value, final String... keys) {
        if (keys.length == 0)
            throw new IllegalArgumentException("You must provide at least one key");
        for (String key : keys) {
            if (value != null) {
                auditSearchTerms.put(key, new AuditSearchTerm(key, value));
                getRemovedKeys().remove(key);
            } else {
                getRemovedKeys().add(key);
                auditSearchTerms.remove(key);
            }
        }
    }

    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        final String textString = stringSearchTerms.values().stream().map(StringSearchTerm::getDisplayValue).collect(Collectors.joining(JOIN_DELIMITER));
        final String intString = integerSearchTerms.values().stream().map(IntegerSearchTerm::getDisplayValue).collect(Collectors.joining(JOIN_DELIMITER));
        final String boolString = booleanSearchTerms.values().stream().map(BooleanSearchTerm::getDisplayValue).collect(Collectors.joining(JOIN_DELIMITER));
        final String auditString = auditSearchTerms.values().stream().map(AuditSearchTerm::getDisplayValue).distinct().collect(Collectors.joining(JOIN_DELIMITER));
        sb.append(Arrays.asList(textString, intString, boolString, auditString).stream().filter((String s) -> !s.isEmpty()).collect(Collectors.joining(JOIN_DELIMITER)));
        if (!codes.isEmpty()) {
            if (sb.length() > 0)
                sb.append(" AND ");
            sb.append(codes.toString());
        }
        return sb.toString();
    }

    public Set<String> getRemovedKeys() {
        return removedKeys;
    }

    public void clearRemovedKeys() {
        removedKeys.clear();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (searchConditionId == null ? 0 : searchConditionId.hashCode());
        result = prime * result + stringSearchTerms.hashCode();
        result = prime * result + integerSearchTerms.hashCode();
        result = prime * result + booleanSearchTerms.hashCode();
        result = prime * result + auditSearchTerms.hashCode();
        result = prime * result + codes.hashCode();
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
        SearchCondition other = (SearchCondition) obj;
        if (searchConditionId == null) {
            if (other.searchConditionId != null)
                return false;
        } else if (!searchConditionId.equals(other.searchConditionId))
            return false;
        if (!booleanSearchTerms.equals(other.booleanSearchTerms))
            return false;
        if (!integerSearchTerms.equals(other.integerSearchTerms))
            return false;
        if (!stringSearchTerms.equals(other.stringSearchTerms))
            return false;
        if (!auditSearchTerms.equals(other.auditSearchTerms))
            return false;
        if (!codes.equals(other.codes))
            return false;
        return true;
    }

}
