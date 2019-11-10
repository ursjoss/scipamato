package ch.difty.scipamato.core.entity.search;

import static ch.difty.scipamato.core.entity.IdScipamatoEntity.IdScipamatoEntityFields.ID;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.core.NewsletterAware;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeBox;
import ch.difty.scipamato.core.entity.CodeBoxAware;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

/**
 * The {@link SearchCondition} is an instance of {@link ScipamatoFilter} that
 * provides accessors for all fields present in the entity {@link Paper}, but
 * all in String form.
 * <p>
 * The provided String values may contain query specific meta information that
 * can be interpreted by the query infrastructure to specify e.g. ranges or
 * wildcards.
 * <p>
 * Internally it stores any of the fields that were explicitly set in Maps that
 * can be accessed to be evaluated by the query engine.
 * <p>
 * <b>Note:</b> the actual ID of the {@link SearchCondition} is called
 * {@code searchConditionId} due to the name clash with its search condition id,
 * which holds the search term for the paper id.
 * <p>
 *
 * @author u.joss
 */
@SuppressWarnings({ "SameParameterValue" })
public class SearchCondition extends ScipamatoFilter implements CodeBoxAware, NewsletterAware {

    private static final long serialVersionUID = 1L;

    private static final String JOIN_DELIMITER = " AND ";

    private Long    searchConditionId;
    // not identifying and therefore not used for equals or hashcode
    private String  newsletterHeadline;
    private Integer newsletterTopicId;
    private String  newsletterIssue;
    // only used for the display value - not identifying and therefore not used for equals or hashcode
    private String  newsletterTopicTitle;

    private final StringSearchTerms  stringSearchTerms  = new StringSearchTerms();
    private final IntegerSearchTerms integerSearchTerms = new IntegerSearchTerms();
    private final BooleanSearchTerms booleanSearchTerms = new BooleanSearchTerms();
    private final AuditSearchTerms   auditSearchTerms   = new AuditSearchTerms();
    private final CodeBox            codes              = new SearchConditionCodeBox();
    private final Set<String>        removedKeys        = new HashSet<>();

    public SearchCondition() {
        // default constructor
    }

    public SearchCondition(@Nullable final Long searchConditionId) {
        setSearchConditionId(searchConditionId);
    }

    @Nullable
    public Long getSearchConditionId() {
        return searchConditionId;
    }

    public void setSearchConditionId(@Nullable final Long searchConditionId) {
        this.searchConditionId = searchConditionId;
    }

    public void addSearchTerm(@NotNull final SearchTerm searchTerm) {
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
        case UNSUPPORTED:
        default:
            throw new AssertionError("SearchTermType." + searchTerm.getSearchTermType() + " is not supported");
        }
    }

    /**
     * @return all search terms specified for string fields in entity {@link Paper}
     */
    @NotNull
    public Collection<StringSearchTerm> getStringSearchTerms() {
        return stringSearchTerms.values();
    }

    /**
     * @return all search terms specified for integer fields in entity {@link Paper}
     */
    @NotNull
    public Collection<IntegerSearchTerm> getIntegerSearchTerms() {
        return integerSearchTerms.values();
    }

    /**
     * @return all search terms specified for boolean fields in entity {@link Paper}
     */
    @NotNull
    public Collection<BooleanSearchTerm> getBooleanSearchTerms() {
        return booleanSearchTerms.values();
    }

    /**
     * @return all search terms specified for audit fields in entity {@link Paper}
     */
    @NotNull
    public Collection<AuditSearchTerm> getAuditSearchTerms() {
        return auditSearchTerms.values();
    }

    /* {@link Paper} specific accessors */

    /**
     * @return id
     */
    @Nullable
    public String getId() {
        return getIntegerValue(ID);
    }

    public void setId(final String value) {
        setIntegerValue(value, ID);
    }

    @Nullable
    public String getNumber() {
        return getIntegerValue(NUMBER);
    }

    public void setNumber(final String value) {
        setIntegerValue(value, NUMBER);
    }

    @Nullable
    public String getDoi() {
        return getStringValue(DOI);
    }

    public void setDoi(final String value) {
        setStringValue(value, DOI);
    }

    @Nullable
    public String getPmId() {
        return getStringValue(PMID);
    }

    public void setPmId(final String value) {
        setStringValue(value, PMID);
    }

    @Nullable
    public String getAuthors() {
        return getStringValue(AUTHORS);
    }

    public void setAuthors(final String value) {
        setStringValue(value, AUTHORS);
    }

    @Nullable
    public String getFirstAuthor() {
        return getStringValue(FIRST_AUTHOR);
    }

    public void setFirstAuthor(final String value) {
        setStringValue(value, FIRST_AUTHOR);
    }

    @Nullable
    public Boolean isFirstAuthorOverridden() {
        return getBooleanValue(FIRST_AUTHOR_OVERRIDDEN);
    }

    public void setFirstAuthorOverridden(final Boolean value) {
        setBooleanValue(value, FIRST_AUTHOR_OVERRIDDEN);
    }

    @Nullable
    public String getTitle() {
        return getStringValue(TITLE);
    }

    public void setTitle(final String value) {
        setStringValue(value, TITLE);
    }

    @Nullable
    public String getLocation() {
        return getStringValue(LOCATION);
    }

    public void setLocation(final String value) {
        setStringValue(value, LOCATION);
    }

    @Nullable
    public String getPublicationYear() {
        return getIntegerValue(PUBL_YEAR);
    }

    public void setPublicationYear(final String value) {
        setIntegerValue(value, PUBL_YEAR);
    }

    @Nullable
    public String getGoals() {
        return getStringValue(GOALS);
    }

    public void setGoals(final String value) {
        setStringValue(value, GOALS);
    }

    @Nullable
    public String getPopulation() {
        return getStringValue(POPULATION);
    }

    public void setPopulation(final String value) {
        setStringValue(value, POPULATION);
    }

    @Nullable
    public String getMethods() {
        return getStringValue(METHODS);
    }

    public void setMethods(final String value) {
        setStringValue(value, METHODS);
    }

    @Nullable
    public String getResult() {
        return getStringValue(RESULT);
    }

    public void setResult(final String value) {
        setStringValue(value, RESULT);
    }

    @Nullable
    public String getComment() {
        return getStringValue(COMMENT);
    }

    public void setComment(final String value) {
        setStringValue(value, COMMENT);
    }

    @Nullable
    public String getIntern() {
        return getStringValue(INTERN);
    }

    public void setIntern(final String value) {
        setStringValue(value, INTERN);
    }

    @Nullable
    public String getOriginalAbstract() {
        return getStringValue(ORIGINAL_ABSTRACT);
    }

    public void setOriginalAbstract(final String value) {
        setStringValue(value, ORIGINAL_ABSTRACT);
    }

    @Nullable
    public String getPopulationPlace() {
        return getStringValue(POPULATION_PLACE);
    }

    public void setPopulationPlace(final String value) {
        setStringValue(value, POPULATION_PLACE);
    }

    @Nullable
    public String getPopulationParticipants() {
        return getStringValue(POPULATION_PARTICIPANTS);
    }

    public void setPopulationParticipants(final String value) {
        setStringValue(value, POPULATION_PARTICIPANTS);
    }

    @Nullable
    public String getPopulationDuration() {
        return getStringValue(POPULATION_DURATION);
    }

    public void setPopulationDuration(final String value) {
        setStringValue(value, POPULATION_DURATION);
    }

    @Nullable
    public String getExposurePollutant() {
        return getStringValue(EXPOSURE_POLLUTANT);
    }

    public void setExposurePollutant(final String value) {
        setStringValue(value, EXPOSURE_POLLUTANT);
    }

    @Nullable
    public String getExposureAssessment() {
        return getStringValue(EXPOSURE_ASSESSMENT);
    }

    public void setExposureAssessment(final String value) {
        setStringValue(value, EXPOSURE_ASSESSMENT);
    }

    @Nullable
    public String getMethodStudyDesign() {
        return getStringValue(METHOD_STUDY_DESIGN);
    }

    public void setMethodStudyDesign(final String value) {
        setStringValue(value, METHOD_STUDY_DESIGN);
    }

    @Nullable
    public String getMethodOutcome() {
        return getStringValue(METHOD_OUTCOME);
    }

    public void setMethodOutcome(final String value) {
        setStringValue(value, METHOD_OUTCOME);
    }

    @Nullable
    public String getMethodStatistics() {
        return getStringValue(METHOD_STATISTICS);
    }

    public void setMethodStatistics(final String value) {
        setStringValue(value, METHOD_STATISTICS);
    }

    @Nullable
    public String getMethodConfounders() {
        return getStringValue(METHOD_CONFOUNDERS);
    }

    public void setMethodConfounders(final String value) {
        setStringValue(value, METHOD_CONFOUNDERS);
    }

    @Nullable
    public String getResultExposureRange() {
        return getStringValue(RESULT_EXPOSURE_RANGE);
    }

    public void setResultExposureRange(final String value) {
        setStringValue(value, RESULT_EXPOSURE_RANGE);
    }

    @Nullable
    public String getResultEffectEstimate() {
        return getStringValue(RESULT_EFFECT_ESTIMATE);
    }

    public void setResultEffectEstimate(final String value) {
        setStringValue(value, RESULT_EFFECT_ESTIMATE);
    }

    @Nullable
    public String getConclusion() {
        return getStringValue(CONCLUSION);
    }

    public void setConclusion(final String value) {
        setStringValue(value, CONCLUSION);
    }

    @Nullable
    public String getResultMeasuredOutcome() {
        return getStringValue(RESULT_MEASURED_OUTCOME);
    }

    public void setResultMeasuredOutcome(final String value) {
        setStringValue(value, RESULT_MEASURED_OUTCOME);
    }

    @Nullable
    public String getMainCodeOfCodeclass1() {
        return getStringValue(MAIN_CODE_OF_CODECLASS1);
    }

    public void setMainCodeOfCodeclass1(final String value) {
        setStringValue(value, MAIN_CODE_OF_CODECLASS1);
    }

    @Nullable
    public String getCreatedDisplayValue() {
        return getAuditValue(CREATED_BY);
    }

    public void setCreatedDisplayValue(final String value) {
        setAuditValue(value, CREATED_BY, CREATED);
    }

    @Nullable
    public String getModifiedDisplayValue() {
        return getAuditValue(LAST_MOD_BY);
    }

    public void setModifiedDisplayValue(final String value) {
        setAuditValue(value, LAST_MOD_BY, LAST_MOD);
    }

    @Nullable
    public String getCreated() {
        return getAuditValue(CREATED);
    }

    public String getCreatedBy() {
        return getAuditValue(CREATED_BY);
    }

    @Nullable
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

    @NotNull
    @Override
    public List<Code> getCodes() {
        return this.codes.getCodes();
    }

    @NotNull
    @Override
    public List<Code> getCodesOf(@NotNull final CodeClassId ccId) {
        return this.codes.getCodesBy(ccId);
    }

    @Override
    public void clearCodesOf(@NotNull final CodeClassId ccId) {
        this.codes.clearBy(ccId);
    }

    @Override
    public void addCode(@NotNull final Code code) {
        this.codes.addCode(code);
    }

    @Override
    public void addCodes(@NotNull final List<Code> codes) {
        this.codes.addCodes(codes);
    }

    private String getStringValue(final FieldEnumType fieldType) {
        final StringSearchTerm st = stringSearchTerms.get(fieldType.getFieldName());
        return st != null ? st.getRawSearchTerm() : null;
    }

    private void setStringValue(final String value, final FieldEnumType fieldType) {
        final String key = fieldType.getFieldName();
        if (value != null) {
            stringSearchTerms.put(key, SearchTerm.newStringSearchTerm(key, value));
            getRemovedKeys().remove(key);
        } else {
            getRemovedKeys().add(key);
            stringSearchTerms.remove(key);
        }
    }

    private String getIntegerValue(final FieldEnumType fieldType) {
        final IntegerSearchTerm st = integerSearchTerms.get(fieldType.getFieldName());
        return st != null ? st.getRawSearchTerm() : null;
    }

    private void setIntegerValue(final String value, final FieldEnumType fieldType) {
        final String key = fieldType.getFieldName();
        if (value != null) {
            integerSearchTerms.put(key, SearchTerm.newIntegerSearchTerm(key, value));
            getRemovedKeys().remove(key);
        } else {
            getRemovedKeys().add(key);
            integerSearchTerms.remove(key);
        }
    }

    private Boolean getBooleanValue(final FieldEnumType fieldType) {
        final BooleanSearchTerm st = booleanSearchTerms.get(fieldType.getFieldName());
        return st != null ? st.getValue() : null;
    }

    private void setBooleanValue(final Boolean value, final FieldEnumType fieldType) {
        final String key = fieldType.getFieldName();
        if (value != null) {
            booleanSearchTerms.put(key, SearchTerm.newBooleanSearchTerm(key, value.toString()));
            getRemovedKeys().remove(key);
        } else {
            getRemovedKeys().add(key);
            booleanSearchTerms.remove(key);
        }
    }

    private String getAuditValue(final FieldEnumType fieldType) {
        final AuditSearchTerm st = auditSearchTerms.get(fieldType.getFieldName());
        return st != null ? st.getRawSearchTerm() : null;
    }

    /**
     * Here we allow multiple keys (i.e. fields)
     */
    private void setAuditValue(final String value, final FieldEnumType... fieldTypes) {
        for (final FieldEnumType fieldType : fieldTypes) {
            final String key = fieldType.getFieldName();
            if (value != null) {
                auditSearchTerms.put(key, SearchTerm.newAuditSearchTerm(key, value));
                getRemovedKeys().remove(key);
            } else {
                getRemovedKeys().add(key);
                auditSearchTerms.remove(key);
            }
        }
    }

    @NotNull
    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        final String textString = stringSearchTerms
            .values()
            .stream()
            .map(StringSearchTerm::getDisplayValue)
            .collect(Collectors.joining(JOIN_DELIMITER));
        final String intString = integerSearchTerms
            .values()
            .stream()
            .map(IntegerSearchTerm::getDisplayValue)
            .collect(Collectors.joining(JOIN_DELIMITER));
        final String boolString = booleanSearchTerms
            .values()
            .stream()
            .map(BooleanSearchTerm::getDisplayValue)
            .collect(Collectors.joining(JOIN_DELIMITER));
        final String auditString = auditSearchTerms
            .values()
            .stream()
            .map(AuditSearchTerm::getDisplayValue)
            .distinct()
            .collect(Collectors.joining(JOIN_DELIMITER));
        sb.append(Stream
            .of(textString, intString, boolString, auditString)
            .filter((final String s) -> !s.isEmpty())
            .collect(Collectors.joining(JOIN_DELIMITER)));
        if (!codes.isEmpty()) {
            if (sb.length() > 0)
                sb.append(JOIN_DELIMITER);
            sb.append(codes.toString());
        }
        if (newsletterIssue != null) {
            if (sb.length() > 0)
                sb.append(JOIN_DELIMITER);
            sb
                .append("issue=")
                .append(newsletterIssue);
        }
        if (newsletterHeadline != null) {
            if (sb.length() > 0)
                sb.append(JOIN_DELIMITER);
            sb
                .append("headline=")
                .append(newsletterHeadline);
        }
        if (newsletterTopicId != null) {
            if (sb.length() > 0)
                sb.append(JOIN_DELIMITER);
            sb
                .append("topic=")
                .append(newsletterTopicTitle);
        }
        return sb.toString();
    }

    @NotNull
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
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SearchCondition other = (SearchCondition) obj;
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
        return codes.equals(other.codes);
    }

    @Nullable
    @Override
    public Integer getNewsletterTopicId() {
        return newsletterTopicId;
    }

    @Override
    public void setNewsletterTopic(@Nullable final NewsletterTopic newsletterTopic) {
        if (newsletterTopic == null) {
            this.newsletterTopicId = null;
            this.newsletterTopicTitle = null;
        } else {
            this.newsletterTopicId = newsletterTopic.getId();
            this.newsletterTopicTitle = newsletterTopic.getTitle();
        }
    }

    @Override
    public void setNewsletterHeadline(@Nullable final String newsletterHeadline) {
        this.newsletterHeadline = newsletterHeadline;
    }

    @Nullable
    @Override
    public String getNewsletterHeadline() {
        return newsletterHeadline;
    }

    @Override
    public void setNewsletterIssue(@Nullable final String newsletterIssue) {
        this.newsletterIssue = newsletterIssue;
    }

    @Nullable
    @Override
    public String getNewsletterIssue() {
        return newsletterIssue;
    }
}
