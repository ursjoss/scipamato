package ch.difty.scipamato.core.entity;

import static java.util.Collections.emptyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.AttachmentAware;
import ch.difty.scipamato.core.NewsletterAware;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

/**
 * The main entity of SciPaMaTo. A Paper is the representation of a scientific
 * study within the application. Within SciPaMaTo-Core the full range of fields
 * is maintained (as opposed to the more restricted fields exposed for a paper
 * in SciPaMaTo-Public).
 * <p>
 * Most fields are represented as simple strings or numeric values. Exceptions
 * to this are attachments (stored as list of {@link PaperAttachment} and
 * especially {@link Code}s managed in a {@link CodeBox} implementation (hence
 * implementing {@link CodeBoxAware}).
 *
 * @author u.joss
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Paper extends IdScipamatoEntity<Long> implements CodeBoxAware, NewsletterAware, AttachmentAware {

    private static final long serialVersionUID = 1L;

    public static final String NA_AUTHORS   = "N A.";
    public static final String NA_STRING    = "n.a.";
    public static final int    NA_PUBL_YEAR = 1500;

    /**
     * One or more of the extended word characters including {@literal -} and
     * {@literal '} following a space.
     *
     * @see CoreEntity#RE_WW
     */
    private static final String RE_S_WW = "\\s" + RE_WW;

    /**
     * Regex verifying the correctness of an Author string. Consists of:
     * <ol>
     * <li>a single author, made up of one or more "name words", each made up of
     * <ul>
     * <li>any ASCII character out of the word character class \w ([a-zA-Z_0-9])
     * </li>
     * <li>additional unicode characters (\u00C0-\u024f)</li>
     * <li>dashes ({@literal -})</li>
     * <li>hyphens ({@literal '})</li>
     * </ul>
     * <li>then optionally followed by one or more of such authors, separated by a
     * comma ({@literal ,})</li>
     * <li>then optionally followed by one collective author, made up of the same
     * characters as normal authors, but separated by a semicolon
     * ({@literal ;})</li>
     * </ol>
     * <p>
     * The resulting regex:
     * <p>
     * {@code ^[\w\u00C0-\u024f-']+(\s[\w\u00C0-\u024f-']+){0,}(,\s[\w\u00C0-\u024f-']+(\s[\w\u00C0-\u024f-']+){0,}){0,}(;\s[\w\u00C0-\u024f-']+(\s[\w\u00C0-\u024f-']+){0,}){0,1}\.$}
     *
     * <b>Beware:</b> The validation of the author field should actually be
     * functionally dependent on the selected configurable author parser strategy
     * (see ScipamatoCoreProperties#getAuthorParserStrategy()). Making this static
     * on this entity is a shortcut that only works as long as we only have a single
     * strategy.
     * <p>
     * See also PubmedAuthorParser
     */
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    private static final String AUTHOR_REGEX =
        "^" + RE_WW + "(" + RE_S_WW + ")*(," + RE_S_WW + "(" + RE_S_WW + ")*)*(;" + RE_S_WW + "(" + RE_S_WW + ")*)?\\.$";

    /**
     * Regex to validate DOIs. Does not capture the full range of possible DOIs, but
     * nearly all the likely ones.
     *
     * <ol>
     * <li>starting with {@literal 10} followed by a period</li>
     * <li>continuing with 4 to 9 digits</li>
     * <li>followed by a dash ({@literal /})</li>
     * <li>followed by one or more ASCII characters, numbers or special characters
     * ({@literal -._;()/:})</li>
     * </ol>
     * <p>
     * The resulting regex:
     * <p>
     * {@code ^10\.\d{4,9}/[-._;()/:A-Z0-9]+$}
     * <p>
     * The validation pattern is simplified and seems to catch roughly 74.4M out of
     * 74.9M DOIs. The uncaught ones seem to be old and hopefully don't turn up
     * within SciPaMaTo. Otherwise additional regex patterns catching more of the
     * remaining ones can be found in the blog post (thanks to Andrew Gilmartin)
     * referenced below:
     *
     * @see <a href=
     *     "https://www.crossref.org/blog/dois-and-matching-regular-expressions/">
     *     https://www.crossref.org/blog/dois-and-matching-regular-expressions/</a>
     */
    private static final String DOI_REGEX = "^10\\.\\d{4,9}/[-._;()/:A-Z0-9]+$";

    public enum PaperFields implements FieldEnumType {
        NUMBER("number"),
        DOI("doi"),
        PMID("pmId"),
        AUTHORS("authors"),
        FIRST_AUTHOR("firstAuthor"),
        FIRST_AUTHOR_OVERRIDDEN("firstAuthorOverridden"),
        TITLE("title"),
        LOCATION("location"),
        PUBL_YEAR("publicationYear"),
        GOALS("goals"),
        POPULATION("population"),
        METHODS("methods"),
        POPULATION_PLACE("populationPlace"),
        POPULATION_PARTICIPANTS("populationParticipants"),
        POPULATION_DURATION("populationDuration"),
        EXPOSURE_POLLUTANT("exposurePollutant"),
        EXPOSURE_ASSESSMENT("exposureAssessment"),
        METHOD_STUDY_DESIGN("methodStudyDesign"),
        METHOD_OUTCOME("methodOutcome"),
        METHOD_STATISTICS("methodStatistics"),
        METHOD_CONFOUNDERS("methodConfounders"),
        RESULT("result"),
        COMMENT("comment"),
        INTERN("intern"),
        RESULT_EXPOSURE_RANGE("resultExposureRange"),
        RESULT_EFFECT_ESTIMATE("resultEffectEstimate"),
        RESULT_MEASURED_OUTCOME("resultMeasuredOutcome"),
        CONCLUSION("conclusion"),
        ORIGINAL_ABSTRACT("originalAbstract"),
        ATTACHMENTS("attachments"),
        MAIN_CODE_OF_CODECLASS1("mainCodeOfCodeclass1"),
        CODES("codes"),
        NEWSLETTER_LINK("newsletterLink"),
        NEWSLETTER_HEADLINE("newsletterHeadline"),
        NEWSLETTER_ISSUE("newsletterIssue"),
        HAS_ATTACHMENTS("hasAttachments"),
        ATTACHMENT_NAME_MASK("attachmentNameMask"),
        CODES_EXCLUDED("codesExcluded"),
        CREATED("paper.created"),
        CREATED_BY("paper.created_by"),
        LAST_MOD("paper.last_modified"),
        LAST_MOD_BY("paper.last_modified_by");

        private final String name;

        PaperFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Min(0)
    private Long number;

    /**
     * Digital Object Identifier (see <a href="https://www.doi.org">DOI Homepage</a>)
     * <p>
     * /^10.\d{4,9}/[-._;()/:A-Z0-9]+$/i
     */
    @jakarta.validation.constraints.Pattern(regexp = DOI_REGEX, flags = {
        jakarta.validation.constraints.Pattern.Flag.CASE_INSENSITIVE }, message = "{paper.invalidDOI}")
    private String doi;

    private Integer pmId;

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Pattern(regexp = AUTHOR_REGEX, message = "{paper.invalidAuthor}")
    private String authors;

    @jakarta.validation.constraints.NotNull
    private String  firstAuthor;
    private boolean firstAuthorOverridden;
    @jakarta.validation.constraints.NotNull
    private String  title;
    @jakarta.validation.constraints.NotNull
    private String  location;

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Min(value = 1500, message = "{paper.invalidPublicationYear}")
    @jakarta.validation.constraints.Max(value = 2100, message = "{paper.invalidPublicationYear}")
    private Integer publicationYear;

    @jakarta.validation.constraints.NotNull
    private String goals;
    private String population;
    private String populationPlace;
    private String populationParticipants;
    private String populationDuration;
    private String exposurePollutant;
    private String exposureAssessment;
    private String methods;
    private String methodStudyDesign;
    private String methodOutcome;
    private String methodStatistics;
    private String methodConfounders;

    private String result;
    private String resultExposureRange;
    private String resultEffectEstimate;
    private String resultMeasuredOutcome;
    private String conclusion;
    private String comment;
    private String intern;
    private String originalAbstract;
    private String mainCodeOfCodeclass1;

    private NewsletterLink newsletterLink;

    // Note: Attachments are not persisted with the Paper in the repo
    private final List<PaperAttachment> attachments = new ArrayList<>();

    private final CodeBox codes = new PaperCodeBox();

    // @NotNull breaks the tests with java/kotlin interoperability. But conceptually should be non-nullable
    public List<PaperAttachment> getAttachments() {
        return new ArrayList<>(attachments);
    }

    public void setAttachments(@Nullable final List<PaperAttachment> attachments) {
        this.attachments.clear();
        if (attachments != null)
            this.attachments.addAll(attachments);
    }

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
    public void addCode(@Nullable final Code code) {
        this.codes.addCode(code);
    }

    @Override
    public void addCodes(@NotNull final List<Code> codes) {
        this.codes.addCodes(codes);
    }

    @Override
    public void setCodesExcluded(@Nullable final String codesExcluded) {
        // no-op - only used for searching by excluded codes
    }

    @Nullable
    @Override
    public String getCodesExcluded() {
        return null;
    }

    @NotNull
    @Override
    public List<String> getExcludedCodeCodes() {
        return emptyList();
    }

    @NotNull
    @Override
    public String getDisplayValue() {
        return firstAuthor + " (" + publicationYear + "): " + title + ".";
    }

    @Value
    public static class NewsletterLink implements Serializable {
        private static final long serialVersionUID = 1L;

        Integer newsletterId;
        String  issue;
        Integer publicationStatusId;
        @Nullable
        Integer topicId;
        @Nullable
        String  topic;
        @Nullable
        String  headline;
    }

    @Override
    public void setNewsletterTopic(@Nullable final NewsletterTopic newsletterTopic) {
        if (newsletterLink != null) {
            final NewsletterLink nl = newsletterLink;
            if (newsletterTopic == null)
                setNewsletterLink(nl.getNewsletterId(), nl.getIssue(), nl.getPublicationStatusId(), null, null, nl.getHeadline());
            else
                setNewsletterLink(nl.getNewsletterId(), nl.getIssue(), nl.getPublicationStatusId(), newsletterTopic.getId(),
                    newsletterTopic.getTitle(), nl.getHeadline());
        }
    }

    @Nullable
    @Override
    public Integer getNewsletterTopicId() {
        return newsletterLink != null ? newsletterLink.getTopicId() : null;
    }

    @Override
    public void setNewsletterHeadline(@Nullable final String headline) {
        final NewsletterLink nl = newsletterLink;
        if (nl != null)
            setNewsletterLink(nl.getNewsletterId(), nl.getIssue(), nl.getPublicationStatusId(), nl.getTopicId(), nl.getTopic(), headline);
    }

    /**
     * The application sets the entire link via repo method into the database. So we don't need to expose this method
     * through the interface.
     *
     * @param newsletterId
     *     the id of the Newsletter the paper is associated with
     * @param issue
     *     the issue of the newsletter the paper is associated with
     * @param publicationStatusId
     *     the publication status the newsletter is associated with
     * @param topicId
     *     the topic id of the paper newsletter association
     * @param topic
     *     the (localized) topic title of the paper newsletter association (functionally dependent on the topicId)
     * @param headline
     *     the headline of the paper newsletter association
     */
    public void setNewsletterLink(@Nullable final Integer newsletterId, @Nullable final String issue, @Nullable final Integer publicationStatusId,
        @Nullable final Integer topicId, @Nullable final String topic, @Nullable final String headline) {
        this.newsletterLink = new NewsletterLink(newsletterId, issue, publicationStatusId, topicId, topic, headline);
    }

    @Nullable
    @Override
    public String getNewsletterHeadline() {
        return newsletterLink != null ? newsletterLink.getHeadline() : null;
    }

    @Nullable
    @Override
    public String getNewsletterIssue() {
        return newsletterLink != null ? newsletterLink.getIssue() : null;
    }

    @Override
    public void setNewsletterIssue(@Nullable final String issue) {
        // no-op - only used for searching by newsletter issue
    }

    @Nullable
    @Override
    public Boolean getHasAttachments() {
        return null;
    }

    @Override
    public void setHasAttachments(@Nullable final Boolean hasAttachments) {
        // no-op - only used for searching whether the paper has attachment or not
    }

    @Override
    public void setAttachmentNameMask(@Nullable final String attachmentName) {
        // no-op - only used for searching by attachment name
    }

    @Nullable
    @Override
    public String getAttachmentNameMask() {
        return null;
    }
}
