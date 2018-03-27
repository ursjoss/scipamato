package ch.difty.scipamato.core.entity.newsletter;

import static ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.ISSUE;
import static ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.PUBLICATION_STATUS;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Test;

import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest;

public class NewsletterTest extends Jsr303ValidatedEntityTest<Newsletter> {

    private NewsletterTopic topic = new NewsletterTopic();

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

        topic.setTitle("sometopic");
        nl.setTopics(Arrays.asList(topic, topic));
        return nl;
    }

    @Override
    protected String getToString() {
        return "Newsletter(issue=2018-03, issueDate=2018-03-26, publicationStatus=WIP, topics=[NewsletterTopic(title=sometopic), NewsletterTopic(title=sometopic)])";
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
        assertThat(nl.getTopics())
            .hasSize(2)
            .containsOnly(topic);
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

}
