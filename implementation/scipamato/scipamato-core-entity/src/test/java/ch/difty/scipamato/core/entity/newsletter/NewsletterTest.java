package ch.difty.scipamato.core.entity.newsletter;

import static ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.ISSUE;
import static ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.PUBLICATION_STATUS;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest;
import ch.difty.scipamato.core.entity.projection.PaperSlim;

public class NewsletterTest extends Jsr303ValidatedEntityTest<Newsletter> {

    private final NewsletterTopic topic1 = new NewsletterTopic(1, "sometopic");
    private final PaperSlim       paper1 = new PaperSlim();
    private final PaperSlim       paper2 = new PaperSlim();

    public NewsletterTest() {
        super(Newsletter.class);
    }

    @Override
    protected Newsletter newValidEntity() {
        final Newsletter nl = new Newsletter();
        nl.setId(1);
        nl.setIssue("2018-03");
        nl.setIssueDate(LocalDate.parse("2018-03-26"));
        nl.setPublicationStatus(PublicationStatus.WIP);

        paper1.setId(1l);
        paper1.setTitle("somepaper");
        nl.addPaper(paper1, topic1);

        paper2.setId(2l);
        paper2.setTitle("otherpaper");
        nl.addPaper(paper2, null);
        return nl;
    }

    @Override
    protected String getToString() {
        return "Newsletter(issue=2018-03, issueDate=2018-03-26, publicationStatus=WIP, "
               + "papersByTopic={null=[PaperSlim(number=null, firstAuthor=null, publicationYear=null, title=otherpaper)], "
               + "NewsletterTopic(title=sometopic)=[PaperSlim(number=null, firstAuthor=null, publicationYear=null, title=somepaper)]})";
    }

    @Override
    protected String getDisplayValue() {
        return newValidEntity().getIssue();
    }

    @Test
    public void get() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getId()).isEqualTo(1);
        assertThat(nl.getIssue()).isEqualTo("2018-03");
        assertThat(nl.getIssueDate()).isEqualTo(LocalDate.parse("2018-03-26"));
        assertThat(nl.getPublicationStatus()).isEqualTo(PublicationStatus.WIP);
        assertThat(nl.getPapers())
            .hasSize(2)
            .containsOnly(paper1, paper2);
        assertThat(nl.getTopics())
            .hasSize(2)
            .containsOnly(topic1, null);
    }

    @Test
    public void canGetIssueDateLegacy() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getIssueDateLegacy()).isEqualTo(Date.valueOf("2018-03-26"));
    }

    @Test
    public void canChangeIssueDateViaLegacyDate() {
        Newsletter nl = newValidEntity();
        nl.setIssueDateLegacy(Date.valueOf("2018-04-01"));
        assertThat(nl.getIssueDateLegacy()).isEqualTo(Date.valueOf("2018-04-01"));
        assertThat(nl.getIssueDate()).isEqualTo(LocalDate.parse("2018-04-01"));
    }

    @Test
    public void canGetIssueDateLegacy_withUninitializedDate() {
        Newsletter nl = new Newsletter();
        assertThat(nl.getIssueDate()).isNull();
        assertThat(nl.getIssueDateLegacy()).isNull();
    }

    @Test
    public void canSetIssueDateLegacyToNull() {
        Newsletter nl = new Newsletter();
        nl.setIssueDateLegacy(null);
        assertThat(nl.getIssueDate()).isNull();
    }

    @Test
    public void validatingNewsletter_withIssueAndPublicationStatus_succeeds() {
        verifySuccessfulValidation(newValidEntity());
    }

    @Test
    public void validatingNewsletter_withNullIssue_fails() {
        Newsletter nl = newValidEntity();
        nl.setIssue(null);
        validateAndAssertFailure(nl, ISSUE, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void validatingNewsletter_withNullPublicationStatus_fails() {
        Newsletter nl = newValidEntity();
        nl.setPublicationStatus((PublicationStatus) null);
        validateAndAssertFailure(nl, PUBLICATION_STATUS, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    public void isDeletable_delegatesToPublicationStatus() {
        Newsletter nl = newValidEntity();

        nl.setPublicationStatus(PublicationStatus.WIP);
        assertThat(nl.isDeletable()).isTrue();
        nl.setPublicationStatus(PublicationStatus.CANCELLED);
        assertThat(nl.isDeletable()).isFalse();
    }

    @Test
    public void cannotAddNullPaper() {
        assertDegenerateParameter(newValidEntity(), null, "paper");
    }

    @Test
    public void cannotAddPaperWithNullId() {
        assertDegenerateParameter(newValidEntity(), new PaperSlim(), "paper.id");
    }

    private void assertDegenerateParameter(final Newsletter nl, final PaperSlim p, final String prmName) {
        try {
            nl.addPaper(p, new NewsletterTopic(1, "t1"));
            Assertions.fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage(prmName + " must not be null.");
        }
    }

    @Test
    public void canAddAdditionalPaperToExistingTopic() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);

        PaperSlim oneMore = new PaperSlim();
        oneMore.setId(10l);
        oneMore.setTitle("foo");
        nl.addPaper(oneMore, topic1);

        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2, oneMore);
    }

    @Test
    public void canAddAdditionalPaperToNewTopic() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);

        NewsletterTopic oneMoreTopic = new NewsletterTopic(1, "t1");
        oneMoreTopic.setTitle("anotherTopic");

        PaperSlim oneMore = new PaperSlim();
        oneMore.setId(10l);
        oneMore.setTitle("foo");
        nl.addPaper(oneMore, oneMoreTopic);

        assertThat(nl.getTopics()).containsOnly(null, topic1, oneMoreTopic);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2, oneMore);
    }

    @Test
    public void canReassignAssociatedPaperFromOneTopicToAnother() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);
        assertThat(nl
            .getPapersByTopic()
            .get(null)
            .contains(paper2));

        nl.addPaper(paper2, topic1);

        assertThat(nl.getTopics()).containsOnly(topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);
        assertThat(nl
            .getPapersByTopic()
            .get(topic1)
            .contains(paper2));
    }
}
