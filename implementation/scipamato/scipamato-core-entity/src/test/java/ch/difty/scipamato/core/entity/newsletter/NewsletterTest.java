package ch.difty.scipamato.core.entity.newsletter;

import static ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.ISSUE;
import static ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.PUBLICATION_STATUS;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest;
import ch.difty.scipamato.core.entity.projection.PaperSlim;

class NewsletterTest extends Jsr303ValidatedEntityTest<Newsletter> {

    private final NewsletterTopic topic1 = new NewsletterTopic(1, "sometopic");
    private final PaperSlim       paper1 = new PaperSlim();
    private final PaperSlim       paper2 = new PaperSlim();

    NewsletterTest() {
        super(Newsletter.class);
    }

    @Override
    protected Newsletter newValidEntity() {
        final Newsletter nl = new Newsletter();
        nl.setId(1);
        nl.setIssue("2018-03");
        nl.setIssueDate(LocalDate.parse("2018-03-26"));
        nl.setPublicationStatus(PublicationStatus.WIP);

        paper1.setId(1L);
        paper1.setTitle("somepaper");
        nl.addPaper(paper1, topic1);

        paper2.setId(2L);
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
    void get() {
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
    void validatingNewsletter_withIssueAndPublicationStatus_succeeds() {
        verifySuccessfulValidation(newValidEntity());
    }

    @Test
    void validatingNewsletter_withNullIssue_fails() {
        Newsletter nl = newValidEntity();
        nl.setIssue(null);
        validateAndAssertFailure(nl, ISSUE, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    void validatingNewsletter_withNullPublicationStatus_fails() {
        Newsletter nl = newValidEntity();
        nl.setPublicationStatus(null);
        validateAndAssertFailure(nl, PUBLICATION_STATUS, null, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    void isDeletable_delegatesToPublicationStatus() {
        Newsletter nl = newValidEntity();

        nl.setPublicationStatus(PublicationStatus.WIP);
        assertThat(nl.isDeletable()).isTrue();
        nl.setPublicationStatus(PublicationStatus.CANCELLED);
        assertThat(nl.isDeletable()).isFalse();
    }

    @Test
    void cannotAddNullPaper() {
        assertDegenerateParameter(newValidEntity(), null, "paper");
    }

    @Test
    void cannotAddPaperWithNullId() {
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
    void canAddAdditionalPaperToExistingTopic() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);

        PaperSlim oneMore = new PaperSlim();
        oneMore.setId(10L);
        oneMore.setTitle("foo");
        nl.addPaper(oneMore, topic1);

        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2, oneMore);
    }

    @Test
    void canAddAdditionalPaperToNewTopic() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);

        NewsletterTopic oneMoreTopic = new NewsletterTopic(1, "t1");
        oneMoreTopic.setTitle("anotherTopic");

        PaperSlim oneMore = new PaperSlim();
        oneMore.setId(10L);
        oneMore.setTitle("foo");
        nl.addPaper(oneMore, oneMoreTopic);

        assertThat(nl.getTopics()).containsOnly(null, topic1, oneMoreTopic);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2, oneMore);
    }

    @Test
    void canAddPaperWithNullTopic_evenIfItWasAddedWithTopicBefore() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);

        nl.addPaper(paper1, null);

        assertThat(nl.getTopics()).hasSize(1);
        assertThat(nl.getTopics()).containsNull();
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);
    }

    @Test
    void canAddPaperWithTopic_evenIfItWasAddedWithNullTopicBefore() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);

        nl.addPaper(paper2, topic1);

        assertThat(nl.getTopics()).containsOnly(topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);
    }

    @Test
    void canAddPaperWithTopic_evenIfItWasAddedWithOtherTopicBefore() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);

        NewsletterTopic oneMoreTopic = new NewsletterTopic(1, "t1");
        oneMoreTopic.setTitle("anotherTopic");

        nl.addPaper(paper2, oneMoreTopic);

        assertThat(nl.getTopics()).containsOnly(topic1, oneMoreTopic);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);
    }

    @Test
    void canAddNullTopic_hasNoEffectEffect() {
        final Newsletter nl = new Newsletter();
        nl.setId(1);
        nl.setIssue("2018-03");
        nl.setIssueDate(LocalDate.parse("2018-03-26"));
        nl.setPublicationStatus(PublicationStatus.WIP);

        paper1.setId(1L);
        paper1.setTitle("somepaper");
        nl.addPaper(paper1, topic1);

        assertThat(nl.getTopics()).containsOnly(topic1);
        assertThat(nl.getPapers()).containsOnly(paper1);

        PaperSlim oneMore = new PaperSlim();
        oneMore.setId(10L);
        oneMore.setTitle("foo");

        nl.addPaper(oneMore, null);

        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, oneMore);
    }

    @Test
    void canReassignAssociatedPaperFromOneTopicToAnother() {
        Newsletter nl = newValidEntity();
        assertThat(nl.getTopics()).containsOnly(null, topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);
        assertThat(nl
            .getPapersByTopic()
            .get(null)).contains(paper2);

        nl.addPaper(paper2, topic1);

        assertThat(nl.getTopics()).containsOnly(topic1);
        assertThat(nl.getPapers()).containsOnly(paper1, paper2);
        assertThat(nl
            .getPapersByTopic()
            .get(topic1)).contains(paper2);
    }
}
