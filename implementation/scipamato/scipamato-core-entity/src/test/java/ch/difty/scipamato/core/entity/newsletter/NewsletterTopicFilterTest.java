package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class NewsletterTopicFilterTest {

    private final NewsletterTopicFilter f = new NewsletterTopicFilter();

    @Test
    public void getAndSet() {
        f.setTitleMask("titleMask");

        assertThat(f.getTitleMask()).isEqualTo("titleMask");

        assertThat(f.toString()).isEqualTo("NewsletterTopicFilter(titleMask=titleMask)");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(NewsletterTopicFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(NewsletterTopicFilter.NewsletterTopicFilterFields.values())
            .extracting("name")
            .containsExactly("titleMask");
    }

}