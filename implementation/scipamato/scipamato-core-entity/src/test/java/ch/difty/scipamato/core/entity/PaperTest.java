package ch.difty.scipamato.core.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolation;

import org.junit.Before;
import org.junit.Test;

import ch.difty.scipamato.common.entity.CodeClassId;

public class PaperTest extends Jsr303ValidatedEntityTest<Paper> {

    private static final String VALID_AUTHORS                 = "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
    private static final String VALID_AUTHORS_WITH_COLLECTIVE = "Mehta AJ, Thun GA, Imboden M, Ferrarotti I, Keidel D, Künzli N, Kromhout H, Miedinger D, Phuleria H, Rochat T, Russi EW, Schindler C, Schwartz J, Vermeulen R, Luisetti M, Probst-Hensch N; SAPALDIA team.";
    private static final String FIRST_AUTHOR                  = "Turner MC";
    private static final String TITLE                         = "Title";
    private static final String VALID_DOI                     = "10.1093/aje/kwu275";
    private static final String NON_NULL_STRING               = "foo";

    private final Paper p = new Paper();

    @Override
    @Before
    public void setUp() {
        super.setUp();

        p.setId(1l);
        p.setNumber(2l);
        p.setDoi(VALID_DOI);
        p.setPmId(1000);
        p.setAuthors(VALID_AUTHORS);
        p.setFirstAuthor(FIRST_AUTHOR);
        p.setFirstAuthorOverridden(false);
        p.setTitle(TITLE);
        p.setLocation(NON_NULL_STRING);
        p.setPublicationYear(2016);
        p.setGoals(NON_NULL_STRING);

        p.setCreated(LocalDateTime.parse("2017-01-01T22:15:13.111"));
        p.setCreatedBy(10);
        p.setCreatedByName("creator");
        p.setCreatedByFullName("creator full name");
        p.setLastModified(LocalDateTime.parse("2017-01-10T22:15:13.111"));
        p.setLastModifiedBy(20);
        p.setLastModifiedByName("modifier");
        p.setVersion(10);
    }

    private void verifySuccessfulValidation() {
        validate(p);
        assertThat(getViolations()).isEmpty();
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstname_withPeriod_succeeds() {
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withNullNumber_fails() {
        p.setNumber(null);
        validateAndAssertFailure(Paper.NUMBER, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withNegativeNumber_fails() {
        p.setNumber(-1l);
        validateAndAssertFailure(Paper.NUMBER, -1L, "{javax.validation.constraints.Min.message}");
    }

    @Test
    public void validatingPaper_withNullTitle_fails() {
        p.setTitle(null);
        validateAndAssertFailure(Paper.TITLE, null, "{javax.validation.constraints.NotNull.message}");
    }

    private void validateAndAssertFailure(final String field, final Object invalidValue, final String msg) {
        validate(p);

        assertThat(getViolations()).isNotEmpty()
            .hasSize(1);
        ConstraintViolation<Paper> violation = getViolations().iterator()
            .next();
        assertThat(violation.getMessageTemplate()).isEqualTo(msg);
        assertThat(violation.getInvalidValue()).isEqualTo(invalidValue);
        assertThat(violation.getPropertyPath()
            .toString()).isEqualTo(field);
    }

    private void verifyFailedAuthorValidation(final String invalidValue) {
        validateAndAssertFailure(Paper.AUTHORS, invalidValue, "{paper.invalidAuthor}");
    }

    @Test
    public void validatingPaper_withBlankAuthor_fails() {
        final String invalidValue = "";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withoutPeriod_fails() {
        final String invalidValue = "Turner";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withPeriod_succeeds() {
        final String validValue = "Turner.";
        p.setAuthors(validValue);
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withAuthorsPlusCollectiveAuthor_succeeds() {
        p.setAuthors(VALID_AUTHORS_WITH_COLLECTIVE);
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withoutPeriod_fails() {
        p.setAuthors("Turner MC");
        verifyFailedAuthorValidation("Turner MC");
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withPeriod_succeeds() {
        p.setAuthors("Turner MC.");
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstnames_withoutPeriod_fails() {
        final String invalidValue = "Turner MC, Cohan A";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstname_withPeriod_succeeds() {
        p.setAuthors("Turner MC, Cohen A.");
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstnames_withoutMissingSpace_fails() {
        final String invalidValue = "Turner MC,Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(invalidValue);
    }

    @Test
    public void validatingPaper_withNullFirstAuthor_fails() {
        p.setFirstAuthor(null);
        validateAndAssertFailure(Paper.FIRST_AUTHOR, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withAuthorWithDashInName_succeeds() {
        p.setAuthors("Alpha-Beta G.");
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withAuthorWithTickInName_succeeds() {
        p.setAuthors("d'Alpha G.");
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withNullLocation_fails() {
        p.setLocation(null);
        validateAndAssertFailure(Paper.LOCATION, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withTooSmallPublicationYear_fails() {
        final int tooEarly = 1499;
        p.setPublicationYear(tooEarly);
        validate(p);
        validateAndAssertFailure(Paper.PUBL_YEAR, tooEarly, "{paper.invalidPublicationYear}");
    }

    @Test
    public void validatingPaper_withOkPublicationYear_succeeds() {
        p.setPublicationYear(1500);
        verifySuccessfulValidation();

        p.setPublicationYear(2016);
        verifySuccessfulValidation();

        p.setPublicationYear(2100);
        verifySuccessfulValidation();
    }

    @Test
    public void validatingPaper_withTooLargePublicationYear_fails() {
        final int tooLate = 2101;
        p.setPublicationYear(tooLate);
        validate(p);
        validateAndAssertFailure(Paper.PUBL_YEAR, tooLate, "{paper.invalidPublicationYear}");
    }

    @Test
    public void validatingPaper_withInvalidDoi_fails() {
        final String invalidDoi = "abc";
        p.setDoi(invalidDoi);
        validate(p);
        validateAndAssertFailure(Paper.DOI, invalidDoi, "{paper.invalidDOI}");
    }

    @Test
    public void validatingPaper_withNullGoals_fails() {
        p.setGoals(null);
        validateAndAssertFailure(Paper.GOALS, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withNonAsciiChars_passes() {
        final String valueWithUmlaut = "ÄäÖöÜüéèàêç A.";
        p.setAuthors(valueWithUmlaut);
        verifySuccessfulValidation();
    }

    @Test
    public void testingToString_withoutCodeClasses() {
     // @formatter:off
        assertThat(p.toString()).isEqualTo(
            "Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000"
            + ",authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.,firstAuthor=Turner MC,firstAuthorOverridden=false"
            + ",title=Title,location=foo,publicationYear=2016,goals=foo,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>"
            + ",exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,methodStatistics=<null>"
            + ",methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>,resultMeasuredOutcome=<null>,comment=<null>,intern=<null>,originalAbstract=<null>"
            + ",mainCodeOfCodeclass1=<null>,attachments=[],codes=[],id=1,createdBy=10,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]");
     // @formatter:on
    }

    @Test
    public void testingToString_withCodeClassesAndMainCodeOfClass1() {
        p.addCode(makeCode(1, "D"));
        p.addCode(makeCode(1, "E"));
        p.addCode(makeCode(5, "A"));
        p.setMainCodeOfCodeclass1("1D");
        // @formatter:off
        assertThat(p.toString()).isEqualTo(
            "Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000"
            + ",authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.,firstAuthor=Turner MC,firstAuthorOverridden=false"
            + ",title=Title,location=foo,publicationYear=2016,goals=foo,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>"
            + ",exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,methodStatistics=<null>"
            + ",methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>,resultMeasuredOutcome=<null>,comment=<null>,intern=<null>,originalAbstract=<null>"
            + ",mainCodeOfCodeclass1=1D,attachments=[],codes=[codesOfClass1=[Code[code=1D,name=code 1D,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]]"
            + ",codesOfClass1=[Code[code=1E,name=code 1E,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]]"
            + ",codesOfClass5=[Code[code=5A,name=code 5A,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]]]"
            + ",id=1,createdBy=10,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]");
        // @formatter:on
    }

    private Code makeCode(int codeClassId, String codePart) {
        String code = codeClassId + codePart;
        return new Code(code, "code " + code, null, false, codeClassId, "cc" + code, "code class " + code, 1);
    }

    @Test
    public void testingToString_withAttachments() {
        List<PaperAttachment> attachments = new ArrayList<>();
        attachments.add(newAttachment(1, 1, "p1"));
        attachments.add(newAttachment(2, 1, "p2"));
        p.setAttachments(attachments);
     // @formatter:off
        assertThat(p.toString()).isEqualTo(
            "Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000"
            + ",authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.,firstAuthor=Turner MC,firstAuthorOverridden=false"
            + ",title=Title,location=foo,publicationYear=2016,goals=foo,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>"
            + ",exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,methodStatistics=<null>"
            + ",methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>,resultMeasuredOutcome=<null>,comment=<null>,intern=<null>,originalAbstract=<null>"
            + ",mainCodeOfCodeclass1=<null>,attachments=[PaperAttachment[paperId=1,name=p1,id=1], PaperAttachment[paperId=1,name=p2,id=2]"
            + "],codes=[],id=1,createdBy=10,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]");
     // @formatter:on
    }

    private PaperAttachment newAttachment(int id, long paperId, String name) {
        PaperAttachment pa = new PaperAttachment();
        pa.setId(id);
        pa.setPaperId(paperId);
        pa.setName(name);
        pa.setContent(name.getBytes());
        pa.setContentType("ct");
        pa.setSize((long) name.length());
        return pa;
    }

    @Test
    public void addingCode_addsItAndAllowsToRetrieveIt() {
        assertThat(p.getCodes()).isNotNull()
            .isEmpty();
        p.addCode(makeCode(1, "C"));

        assertThat(extractProperty(Code.CODE).from(p.getCodes())).containsExactly("1C");
        assertThat(extractProperty(Code.CODE).from(p.getCodesOf(CodeClassId.CC1))).containsExactly("1C");
        assertThat(extractProperty(Code.CODE).from(p.getCodesOf(CodeClassId.CC2))).isEmpty();
    }

    @Test
    public void addingCodes() {
        p.addCode(makeCode(1, "C"));
        Code c1D = makeCode(1, "D");
        Code c2A = makeCode(2, "A");
        p.addCodes(Arrays.asList(c1D, c2A));

        assertThat(extractProperty(Code.CODE).from(p.getCodes())).containsExactly("1C", "1D", "2A");

        p.clearCodes();
        assertThat(p.getCodes()).isNotNull()
            .isEmpty();
    }

    @Test
    public void addingCodesOfSeveralClasses_allowsToRetrieveThemByClass() {
        Code c1D = makeCode(1, "D");
        Code c2F = makeCode(2, "F");
        Code c2A = makeCode(2, "A");

        p.addCodes(Arrays.asList(c1D, c2F, c2A));

        assertThat(p.getCodesOf(CodeClassId.CC1)).containsExactly(c1D);
        assertThat(p.getCodesOf(CodeClassId.CC2)).containsExactly(c2F, c2A);
        assertThat(p.getCodesOf(CodeClassId.CC3)).isEmpty();
    }

    @Test
    public void clearingCode_delegatesClearingToCode() {
        assertThat(p.getCodes()).isNotNull()
            .isEmpty();
        p.addCode(makeCode(CodeClassId.CC1.getId(), "C"));
        assertThat(p.getCodes()).isNotEmpty();
        p.clearCodesOf(CodeClassId.CC1);
        assertThat(p.getCodes()).isNotNull()
            .isEmpty();
    }

    @Test
    public void testingMainCodeOfCodeClass1() {
        Code c1D = makeCode(1, "D");
        Code c1E = makeCode(1, "E");
        Code c5A = makeCode(5, "A");
        p.addCodes(Arrays.asList(c1E, c1D, c5A));
        p.setMainCodeOfCodeclass1(c1E.getCode());

        assertThat(p.getCodesOf(CodeClassId.CC1)).containsExactly(c1E, c1D);
        assertThat(p.getCodesOf(CodeClassId.CC2)).isEmpty();
        assertThat(p.getCodesOf(CodeClassId.CC5)).containsExactly(c5A);
        assertThat(p.getMainCodeOfCodeclass1()).isEqualTo("1E");
    }

    @Test
    public void displayValue() {
        assertThat(p.getDisplayValue()).isEqualTo("Turner MC (2016): Title.");
    }

    @Test
    public void createdDisplayValue() {
        assertThat(p.getCreatedDisplayValue()).isEqualTo("creator (2017-01-01 22:15:13)");
    }

    @Test
    public void modifiedDisplayValue() {
        assertThat(p.getModifiedDisplayValue()).isEqualTo("modifier (2017-01-10 22:15:13)");
    }

    @Test
    public void assertNotAvailableValueForAuthors() {
        assertThat(Paper.NA_AUTHORS).isEqualTo("N A.");
    }

    @Test
    public void assertNotAvailableValueForOtherStringFields() {
        assertThat(Paper.NA_STRING).isEqualTo("n.a.");
    }

    @Test
    public void assertNotAvailableValueForPublicationYear() {
        assertThat(Paper.NA_PUBL_YEAR).isEqualTo(1500);
    }

    @Test
    public void newPaper_hasNonNullButEmptyAttachments() {
        assertThat(p.getAttachments()).isNotNull()
            .isEmpty();
    }

    @Test
    public void cannotAddAttachment_viaGetter() {
        p.getAttachments()
            .add(new PaperAttachment());
        assertThat(p.getAttachments()).isEmpty();
    }

    @Test
    public void canSetAttachments() {
        p.setAttachments(Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        assertThat(p.getAttachments()).hasSize(2);
    }

    @Test
    public void cannotModifyAttachmentsAfterSettig() {
        List<PaperAttachment> attachments = new ArrayList<>(
                Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        p.setAttachments(attachments);
        attachments.add(new PaperAttachment());
        assertThat(p.getAttachments()
            .size()).isLessThan(attachments.size());
    }

    @Test
    public void canUnsetAttachments_withNullParameter() {
        List<PaperAttachment> attachments = new ArrayList<>(
                Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        p.setAttachments(attachments);
        assertThat(p.getAttachments()).hasSize(2);
        p.setAttachments(null);
        assertThat(p.getAttachments()).isEmpty();
    }

    @Test
    public void canUnsetAttachments_withEmptyListParameter() {
        List<PaperAttachment> attachments = new ArrayList<>(
                Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        p.setAttachments(attachments);
        assertThat(p.getAttachments()).hasSize(2);
        p.setAttachments(new ArrayList<PaperAttachment>());
        assertThat(p.getAttachments()).isEmpty();
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    // Note: Did not get this to run with equalsverifier due to 'Abstract
    // delegation: Paper's hashCode method delegates to an abstract method' on codes
    public void equalityAndHashCode() {
        Paper p1 = new Paper();
        p1.setId(1l);

        assertThat(p1.equals(p1)).isTrue();
        assertThat(p1.equals(null)).isFalse();
        assertThat(p1.equals(Integer.valueOf(1))).isFalse();

        Paper p2 = new Paper();
        p2.setId(1l);
        assertThat(p1.equals(p2)).isTrue();
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        p2.setId(2l);
        assertThat(p1.equals(p2)).isFalse();
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode());
        p2.setId(1l);
        p2.setPublicationYear(2017);
        assertThat(p1.equals(p2)).isFalse();
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode());
    }

}
