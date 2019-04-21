package ch.difty.scipamato.core.entity.newsletter;

import static ch.difty.scipamato.core.entity.newsletter.NewsletterTopic.NewsletterTopicFields.TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest;

public class NewsletterTopicTest extends Jsr303ValidatedEntityTest<NewsletterTopic> {

    public NewsletterTopicTest() {
        super(NewsletterTopic.class);
    }

    @Override
    protected NewsletterTopic newValidEntity() {
        return new NewsletterTopic(10, "sometopic");
    }

    @Override
    protected String getToString() {
        return "NewsletterTopic(title=sometopic)";
    }

    @Override
    protected String getDisplayValue() {
        return newValidEntity().getTitle();
    }

    @Test
    public void get() {
        NewsletterTopic nt = newValidEntity();
        assertThat(nt.getId()).isEqualTo(10);
        assertThat(nt.getTitle()).isEqualTo("sometopic");
    }

    @Test
    public void validatingNewsletterTopic_withCompleteInformation_succeeds() {
        verifySuccessfulValidation(newValidEntity());
    }

    @Test
    public void validatingNewsletter_withNullIssue_fails() {
        NewsletterTopic nt = newValidEntity();
        nt.setTitle(null);
        validateAndAssertFailure(nt, TITLE, null, "{javax.validation.constraints.NotNull.message}");
    }

}
