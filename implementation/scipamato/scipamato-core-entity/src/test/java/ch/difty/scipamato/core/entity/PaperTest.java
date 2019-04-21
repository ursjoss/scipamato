package ch.difty.scipamato.core.entity;

import static ch.difty.scipamato.core.entity.Code.CodeFields.CODE;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

@SuppressWarnings({ "SameParameterValue", "SpellCheckingInspection" })
public class PaperTest extends Jsr303ValidatedEntityTest<Paper> {

    private static final String VALID_AUTHORS      = "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
    private static final String VALID_COLLECTIVE   = "Mehta AJ, Thun GA, Imboden M, Ferrarotti I, Keidel D, Künzli N, Kromhout H, Miedinger D, Phuleria H, Rochat T, Russi EW, Schindler C, Schwartz J, Vermeulen R, Luisetti M, Probst-Hensch N; SAPALDIA team.";
    private static final String VALID_FIRST_AUTHOR = "Turner MC";
    private static final String VALID_TITLE        = "Title";
    private static final String VALID_DOI          = "10.1093/aje/kwu275";
    private static final String NON_NULL_STRING    = "foo";

    public PaperTest() {
        super(Paper.class);
    }

    @Override
    protected Paper newValidEntity() {
        final Paper p = new Paper();
        p.setId(1L);
        p.setNumber(2L);
        p.setDoi(VALID_DOI);
        p.setPmId(1000);
        p.setAuthors(VALID_AUTHORS);
        p.setFirstAuthor(VALID_FIRST_AUTHOR);
        p.setFirstAuthorOverridden(false);
        p.setTitle(VALID_TITLE);
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
        return p;
    }

    @Override
    protected String getToString() {
        return "Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000"
               + ",authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.,firstAuthor=Turner MC,firstAuthorOverridden=false"
               + ",title=Title,location=foo,publicationYear=2016,goals=foo,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>"
               + ",exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,methodStatistics=<null>"
               + ",methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>,resultMeasuredOutcome=<null>,conclusion=<null>,comment=<null>,intern=<null>,originalAbstract=<null>"
               + ",mainCodeOfCodeclass1=<null>,newsletterLink=<null>,attachments=[],codes=[],id=1,createdBy=10,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]";
    }

    @Override
    protected String getDisplayValue() {
        return "Turner MC (2016): Title.";
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstname_withPeriod_succeeds() {
        final Paper p = newValidEntity();
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withNullNumber_fails() {
        final Paper p = newValidEntity();
        p.setNumber(null);
        validateAndAssertFailure(p, NUMBER, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withNegativeNumber_fails() {
        final Paper p = newValidEntity();
        p.setNumber(-1L);
        validateAndAssertFailure(p, NUMBER, -1L, "{javax.validation.constraints.Min.message}");
    }

    @Test
    public void validatingPaper_withNullTitle_fails() {
        final Paper p = newValidEntity();
        p.setTitle(null);
        validateAndAssertFailure(p, TITLE, null, "{javax.validation.constraints.NotNull.message}");
    }

    private void verifyFailedAuthorValidation(final Paper p, final String invalidValue) {
        validateAndAssertFailure(p, AUTHORS, invalidValue, "{paper.invalidAuthor}");
    }

    @Test
    public void validatingPaper_withBlankAuthor_fails() {
        final String invalidValue = "";
        final Paper p = newValidEntity();
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(p, invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withoutPeriod_fails() {
        final String invalidValue = "Turner";
        final Paper p = newValidEntity();
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(p, invalidValue);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithoutFirstname_withPeriod_succeeds() {
        final String validValue = "Turner.";
        final Paper p = newValidEntity();
        p.setAuthors(validValue);
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withAuthorsPlusCollectiveAuthor_succeeds() {
        final Paper p = newValidEntity();
        p.setAuthors(VALID_COLLECTIVE);
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withoutPeriod_fails() {
        final Paper p = newValidEntity();
        p.setAuthors("Turner MC");
        verifyFailedAuthorValidation(p, "Turner MC");
    }

    @Test
    public void validatingPaper_withSingleAuthorWithFirstname_withPeriod_succeeds() {
        final Paper p = newValidEntity();
        p.setAuthors("Turner MC.");
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstnames_withoutPeriod_fails() {
        final String invalidValue = "Turner MC, Cohan A";
        final Paper p = newValidEntity();
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(p, invalidValue);
    }

    @Test
    public void validatingPaper_withTwoAuthorsWithFirstname_withPeriod_succeeds() {
        final Paper p = newValidEntity();
        p.setAuthors("Turner MC, Cohen A.");
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withMultipleAuthorsWithFirstnames_withoutMissingSpace_fails() {
        final String invalidValue = "Turner MC,Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.";
        final Paper p = newValidEntity();
        p.setAuthors(invalidValue);
        verifyFailedAuthorValidation(p, invalidValue);
    }

    @Test
    public void validatingPaper_withNullFirstAuthor_fails() {
        final Paper p = newValidEntity();
        p.setFirstAuthor(null);
        validateAndAssertFailure(p, FIRST_AUTHOR, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withAuthorWithDashInName_succeeds() {
        final Paper p = newValidEntity();
        p.setAuthors("Alpha-Beta G.");
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withAuthorWithTickInName_succeeds() {
        final Paper p = newValidEntity();
        p.setAuthors("d'Alpha G.");
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withNullLocation_fails() {
        final Paper p = newValidEntity();
        p.setLocation(null);
        validateAndAssertFailure(p, LOCATION, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withTooSmallPublicationYear_fails() {
        final int tooEarly = 1499;
        final Paper p = newValidEntity();
        p.setPublicationYear(tooEarly);
        validateAndAssertFailure(p, PUBL_YEAR, tooEarly, "{paper.invalidPublicationYear}");
    }

    @Test
    public void validatingPaper_withOkPublicationYear_succeeds() {
        final Paper p = newValidEntity();
        p.setPublicationYear(1500);
        verifySuccessfulValidation(p);

        p.setPublicationYear(2016);
        verifySuccessfulValidation(p);

        p.setPublicationYear(2100);
        verifySuccessfulValidation(p);
    }

    @Test
    public void validatingPaper_withTooLargePublicationYear_fails() {
        final int tooLate = 2101;
        final Paper p = newValidEntity();
        p.setPublicationYear(tooLate);
        validateAndAssertFailure(p, PUBL_YEAR, tooLate, "{paper.invalidPublicationYear}");
    }

    @Test
    public void validatingPaper_withInvalidDoi_fails() {
        final String invalidDoi = "abc";
        final Paper p = newValidEntity();
        p.setDoi(invalidDoi);
        validateAndAssertFailure(p, DOI, invalidDoi, "{paper.invalidDOI}");
    }

    @Test
    public void validatingPaper_withNullGoals_fails() {
        final Paper p = newValidEntity();
        p.setGoals(null);
        validateAndAssertFailure(p, GOALS, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingPaper_withNonAsciiChars_passes() {
        final String valueWithUmlaut = "ÄäÖöÜüéèàêç A.";
        final Paper p = newValidEntity();
        p.setAuthors(valueWithUmlaut);
        verifySuccessfulValidation(p);
    }

    @Test
    public void testingToString_withCodeClassesAndMainCodeOfClass1() {
        final Paper p = newValidEntity();
        p.addCode(makeCode(1, "D"));
        p.addCode(makeCode(1, "E"));
        p.addCode(makeCode(5, "A"));
        p.setMainCodeOfCodeclass1("1D");
        assertThat(p.toString()).isEqualTo("Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000"
                                           + ",authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.,firstAuthor=Turner MC,firstAuthorOverridden=false"
                                           + ",title=Title,location=foo,publicationYear=2016,goals=foo,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>"
                                           + ",exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,methodStatistics=<null>"
                                           + ",methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>,resultMeasuredOutcome=<null>,conclusion=<null>,comment=<null>,intern=<null>,originalAbstract=<null>"
                                           + ",mainCodeOfCodeclass1=1D,newsletterLink=<null>,attachments=[],codes=[codesOfClass1=[Code[code=1D,name=code 1D,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]]"
                                           + ",codesOfClass1=[Code[code=1E,name=code 1E,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]]"
                                           + ",codesOfClass5=[Code[code=5A,name=code 5A,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]]]"
                                           + ",id=1,createdBy=10,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]");
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
        final Paper p = newValidEntity();
        p.setAttachments(attachments);
        assertThat(p.toString()).isEqualTo("Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000"
                                           + ",authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.,firstAuthor=Turner MC,firstAuthorOverridden=false"
                                           + ",title=Title,location=foo,publicationYear=2016,goals=foo,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>"
                                           + ",exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,methodStatistics=<null>"
                                           + ",methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>,resultMeasuredOutcome=<null>,conclusion=<null>,comment=<null>,intern=<null>,originalAbstract=<null>"
                                           + ",mainCodeOfCodeclass1=<null>,newsletterLink=<null>,attachments=[PaperAttachment[paperId=1,name=p1,id=1], PaperAttachment[paperId=1,name=p2,id=2]"
                                           + "],codes=[],id=1,createdBy=10,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]");
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
        final Paper p = newValidEntity();
        assertThat(p.getCodes())
            .isNotNull()
            .isEmpty();
        p.addCode(makeCode(1, "C"));

        assertThat(extractProperty(CODE.getName()).from(p.getCodes())).containsExactly("1C");
        assertThat(extractProperty(CODE.getName()).from(p.getCodesOf(CodeClassId.CC1))).containsExactly("1C");
        assertThat(extractProperty(CODE.getName()).from(p.getCodesOf(CodeClassId.CC2))).isEmpty();
    }

    @Test
    public void addingCodes() {
        final Paper p = newValidEntity();
        p.addCode(makeCode(1, "C"));
        Code c1D = makeCode(1, "D");
        Code c2A = makeCode(2, "A");
        p.addCodes(Arrays.asList(c1D, c2A));

        assertThat(extractProperty(CODE.getName()).from(p.getCodes())).containsExactly("1C", "1D", "2A");

        p.clearCodes();
        assertThat(p.getCodes())
            .isNotNull()
            .isEmpty();
    }

    @Test
    public void addingCodesOfSeveralClasses_allowsToRetrieveThemByClass() {
        Code c1D = makeCode(1, "D");
        Code c2F = makeCode(2, "F");
        Code c2A = makeCode(2, "A");

        final Paper p = newValidEntity();
        p.addCodes(Arrays.asList(c1D, c2F, c2A));

        assertThat(p.getCodesOf(CodeClassId.CC1)).containsExactly(c1D);
        assertThat(p.getCodesOf(CodeClassId.CC2)).containsExactly(c2F, c2A);
        assertThat(p.getCodesOf(CodeClassId.CC3)).isEmpty();
    }

    @Test
    public void clearingCode_delegatesClearingToCode() {
        final Paper p = newValidEntity();
        assertThat(p.getCodes())
            .isNotNull()
            .isEmpty();
        p.addCode(makeCode(CodeClassId.CC1.getId(), "C"));
        assertThat(p.getCodes()).isNotEmpty();
        p.clearCodesOf(CodeClassId.CC1);
        assertThat(p.getCodes())
            .isNotNull()
            .isEmpty();
    }

    @Test
    public void testingMainCodeOfCodeClass1() {
        Code c1D = makeCode(1, "D");
        Code c1E = makeCode(1, "E");
        Code c5A = makeCode(5, "A");
        final Paper p = newValidEntity();
        p.addCodes(Arrays.asList(c1E, c1D, c5A));
        p.setMainCodeOfCodeclass1(c1E.getCode());

        assertThat(p.getCodesOf(CodeClassId.CC1)).containsExactly(c1E, c1D);
        assertThat(p.getCodesOf(CodeClassId.CC2)).isEmpty();
        assertThat(p.getCodesOf(CodeClassId.CC5)).containsExactly(c5A);
        assertThat(p.getMainCodeOfCodeclass1()).isEqualTo("1E");
    }

    @Test
    public void createdDisplayValue() {
        final Paper p = newValidEntity();
        assertThat(p.getCreatedDisplayValue()).isEqualTo("creator (2017-01-01 22:15:13)");
    }

    @Test
    public void modifiedDisplayValue() {
        final Paper p = newValidEntity();
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
        final Paper p = newValidEntity();
        assertThat(p.getAttachments())
            .isNotNull()
            .isEmpty();
    }

    @Test
    public void cannotAddAttachment_viaGetter() {
        final Paper p = newValidEntity();
        p
            .getAttachments()
            .add(new PaperAttachment());
        assertThat(p.getAttachments()).isEmpty();
    }

    @Test
    public void canSetAttachments() {
        final Paper p = newValidEntity();
        p.setAttachments(Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        assertThat(p.getAttachments()).hasSize(2);
    }

    @Test
    public void cannotModifyAttachmentsAfterSettig() {
        List<PaperAttachment> attachments = new ArrayList<>(
            Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        final Paper p = newValidEntity();
        p.setAttachments(attachments);
        attachments.add(new PaperAttachment());
        assertThat(p
            .getAttachments()
            .size()).isLessThan(attachments.size());
    }

    @Test
    public void canUnsetAttachments_withNullParameter() {
        List<PaperAttachment> attachments = new ArrayList<>(
            Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        final Paper p = newValidEntity();
        p.setAttachments(attachments);
        assertThat(p.getAttachments()).hasSize(2);
        p.setAttachments(null);
        assertThat(p.getAttachments()).isEmpty();
    }

    @Test
    public void canUnsetAttachments_withEmptyListParameter() {
        List<PaperAttachment> attachments = new ArrayList<>(
            Arrays.asList(new PaperAttachment(), new PaperAttachment()));
        final Paper p = newValidEntity();
        p.setAttachments(attachments);
        assertThat(p.getAttachments()).hasSize(2);
        p.setAttachments(new ArrayList<>());
        assertThat(p.getAttachments()).isEmpty();
    }

    @SuppressWarnings({ "unlikely-arg-type", "EqualsBetweenInconvertibleTypes", "ConstantConditions",
        "EqualsWithItself", "UnnecessaryBoxing" })
    @Test
    // Note: Did not get this to run with equalsverifier due to 'Abstract
    // delegation: Paper's hashCode method delegates to an abstract method' on codes
    public void equalityAndHashCode() {
        Paper p1 = newValidEntity();
        p1.setId(1L);

        assertThat(p1.equals(p1)).isTrue();
        assertThat(p1.equals(null)).isFalse();
        assertThat(p1.equals(Integer.valueOf(1))).isFalse();

        Paper p2 = newValidEntity();
        p2.setId(1L);
        assertThat(p1.equals(p2)).isTrue();
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        p2.setId(2L);
        assertThat(p1.equals(p2)).isFalse();
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode());
        p2.setId(1L);
        p2.setPublicationYear(2017);
        assertThat(p1.equals(p2)).isFalse();
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode());
    }

    @Override
    public void verifyEquals() {
        // no-op - see comment on previous test equalityAndHashCode
    }

    @Test
    public void canSetAndGetNewsletterLinkAsObject() {
        Paper p = newValidEntity();
        assertThat(p.getNewsletterLink()).isNull();

        Paper.NewsletterLink nl = new Paper.NewsletterLink(1, "issue", 0, null, null, null);

        p.setNewsletterLink(nl);

        assertThat(p.getNewsletterLink()).isEqualTo(nl);
    }

    @Test
    public void canSetAndGetNewsletterLinkAsFields() {
        Paper p = newValidEntity();
        assertThat(p.getNewsletterLink()).isNull();

        p.setNewsletterLink(1, "issue", 0, null, null, null);

        assertThat(p.getNewsletterLink()).isEqualTo(new Paper.NewsletterLink(1, "issue", 0, null, null, null));
    }

    @Test
    public void settingNewsletterAssociation() {
        Paper p = newValidEntity();
        assertThat(p.getNewsletterLink()).isNull();

        p.setNewsletterLink(
            new Paper.NewsletterLink(1, "1806", PublicationStatus.WIP.getId(), 1, "mytopic", "headline"));

        assertThat(p.getNewsletterLink()).isNotNull();
        validateNewsletterLink(p.getNewsletterLink(), 1, "1806", PublicationStatus.WIP, 1, "mytopic", "headline");
        assertThat(p.getNewsletterHeadline()).isEqualTo("headline");
        assertThat(p.getNewsletterTopicId()).isEqualTo(1);

        p.setNewsletterHeadline("otherHeadline");
        validateNewsletterLink(p.getNewsletterLink(), 1, "1806", PublicationStatus.WIP, 1, "mytopic", "otherHeadline");

        p.setNewsletterTopic(new NewsletterTopic(10, "someothertopic"));
        validateNewsletterLink(p.getNewsletterLink(), 1, "1806", PublicationStatus.WIP, 10, "someothertopic",
            "otherHeadline");

        p.setNewsletterTopic(null);
        validateNewsletterLink(p.getNewsletterLink(), 1, "1806", PublicationStatus.WIP, null, null, "otherHeadline");

        p.setNewsletterHeadline(null);
        validateNewsletterLink(p.getNewsletterLink(), 1, "1806", PublicationStatus.WIP, null, null, null);

        assertThat(p.getNewsletterIssue()).isEqualTo("1806");
        assertThat(p.getNewsletterLink()).isNotNull();
        assertThat(p
            .getNewsletterLink()
            .getIssue()).isEqualTo("1806");

    }

    private void validateNewsletterLink(final Paper.NewsletterLink newsletterLink, final Integer newsletterId,
        final String issue, final PublicationStatus status, final Integer topicId, final String topic,
        final String headline) {
        assertThat(newsletterLink.getNewsletterId()).isEqualTo(newsletterId);
        assertThat(newsletterLink.getIssue()).isEqualTo(issue);
        assertThat(newsletterLink.getPublicationStatusId()).isEqualTo(status.getId());
        assertThat(newsletterLink.getTopicId()).isEqualTo(topicId);
        assertThat(newsletterLink.getTopic()).isEqualTo(topic);
        assertThat(newsletterLink.getHeadline()).isEqualTo(headline);
    }

    @Test
    public void settingNewsletterLinkFields_whileNewsletterLinkIsNull_doesNothing() {
        Paper p = newValidEntity();
        assertThat(p.getNewsletterLink()).isNull();

        p.setNewsletterTopic(new NewsletterTopic(10, "someothertopic"));
        p.setNewsletterHeadline("foo");

        assertThat(p.getNewsletterLink()).isNull();
        assertThat(p.getNewsletterTopicId()).isNull();
        assertThat(p.getNewsletterHeadline()).isNull();
    }

    @Test
    public void settingNewsletterIssue_isNoOp() {
        Paper p = newValidEntity();
        assertThat(p.getNewsletterIssue()).isNull();
        p.setNewsletterIssue("whatever");
        assertThat(p.getNewsletterIssue()).isNull();
    }
}
