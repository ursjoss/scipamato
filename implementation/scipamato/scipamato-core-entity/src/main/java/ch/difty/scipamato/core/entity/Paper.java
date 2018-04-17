package ch.difty.scipamato.core.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.FieldEnumType;

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
public class Paper extends IdScipamatoEntity<Long> implements CodeBoxAware {

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
     * Regex verifying the correctness of an Author string. Comprises of:
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
    private static final String AUTHOR_REGEX =
        "^" + RE_WW + "(" + RE_S_WW + ")*(," + RE_S_WW + "(" + RE_S_WW + ")*)*(;" + RE_S_WW + "(" + RE_S_WW
        + ")*)?\\.$";

    /**
     * Regex to validate DOIs. Does not capture the full range of possible DOIs, but
     * nearly all of the likely ones.
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
     *     "http://blog.crossref.org/2015/08/doi-regular-expressions.html">
     *     http://blog.crossref.org/2015/08/doi-regular-expressions.html</a>
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
        ORIGINAL_ABSTRACT("originalAbstract"),
        ATTACHMENTS("attachments"),
        MAIN_CODE_OF_CODECLASS1("mainCodeOfCodeclass1"),
        CODES("codes"),
        CREATED("paper.created"),
        CREATED_BY("paper.created_by"),
        LAST_MOD("paper.last_modified"),
        LAST_MOD_BY("paper.last_modified_by");

        private final String name;

        PaperFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @NotNull
    @Min(0)
    private Long number;

    /**
     * Digital Object Identifier (see http://www.doi.org)
     * <p>
     * /^10.\d{4,9}/[-._;()/:A-Z0-9]+$/i
     */
    @Pattern(regexp = DOI_REGEX, flags = { Flag.CASE_INSENSITIVE }, message = "{paper.invalidDOI}")
    private String doi;

    private Integer pmId;

    @NotNull
    @Pattern(regexp = AUTHOR_REGEX, message = "{paper.invalidAuthor}")
    private String authors;

    @NotNull
    private String  firstAuthor;
    private boolean firstAuthorOverridden;
    @NotNull
    private String  title;
    @NotNull
    private String  location;

    @NotNull
    @Min(value = 1500, message = "{paper.invalidPublicationYear}")
    @Max(value = 2100, message = "{paper.invalidPublicationYear}")
    private Integer publicationYear;

    @NotNull
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
    private String comment;
    private String intern;
    private String originalAbstract;
    private String mainCodeOfCodeclass1;

    // Note: Attachments are not persisted with the Paper in the repo
    private final List<PaperAttachment> attachments = new ArrayList<>();

    private final CodeBox codes = new PaperCodeBox();

    public List<PaperAttachment> getAttachments() {
        return new ArrayList<>(attachments);
    }

    public void setAttachments(final List<PaperAttachment> attachments) {
        this.attachments.clear();
        if (attachments != null)
            this.attachments.addAll(attachments);
    }

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

    @Override
    public String getDisplayValue() {
        return firstAuthor + " (" + publicationYear + "): " + title + ".";
    }

}
