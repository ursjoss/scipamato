package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.TestUtils;

public class NewsletterTopicDefinitionTest {

    private final NewsletterTopicTranslation ntt_de = new NewsletterTopicTranslation(10, "de", "thema2");
    private final NewsletterTopicTranslation ntt_en = new NewsletterTopicTranslation(11, "en", "topic2");
    private final NewsletterTopicTranslation ntt_fr = new NewsletterTopicTranslation(12, "fr", "sujet2");

    @Test
    public void degenerateConstruction_withNullLanguageCode_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new NewsletterTopicDefinition(1, null), "mainLanguageCode");
    }

    @Test
    public void withNoTranslations_unableToEstablishMainTitle() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(1, "de");
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getTitle()).isEqualTo("n.a.");
        assertThat(ntd.getDisplayValue()).isEqualTo("n.a.");
        assertThat(ntd.getTranslations()).isEmpty();
    }

    @Test
    public void withTranslations() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", ntt_de, ntt_en, ntt_fr);
        assertThat(ntd.getId()).isEqualTo(2);
        assertThat(ntd.getTitle()).isEqualTo("thema2");
        assertThat(ntd.getDisplayValue()).isEqualTo("thema2");
        assertThat(ntd.getTranslations()).hasSize(3);
        assertThat(ntd
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<NewsletterTopicTranslation> trs = ntd
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(NewsletterTopicTranslation.NewsletterTopicTranslationFields.TITLE.getName())
            .containsOnly("thema2", "topic2", "sujet2");
        final Iterator<NewsletterTopicTranslation> it = trs.iterator();
        while (it.hasNext())
            assertThat(it
                .next()
                .getLastModified()).isNull();
    }

    @Test
    public void canGetTranslationsAsString_withTanslationsIncludingMainTranslation() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", ntt_de, ntt_en, ntt_fr);
        assertThat(ntd.getTranslationsAsString()).isEqualTo("DE: 'thema2'; EN: 'topic2'; FR: 'sujet2'");
    }

    @Test
    public void canGetTranslationsAsString_withTanslationsIncludingMainTranslation_withPartialTranslation() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", ntt_de, ntt_en,
            new NewsletterTopicTranslation(12, "fr", null));
        assertThat(ntd.getTranslationsAsString()).isEqualTo("DE: 'thema2'; EN: 'topic2'; FR: n.a.");
    }

    @Test
    public void canGetTranslationsAsString_withNoTanslations() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de");
        assertThat(ntd.getTranslationsAsString()).isNull();
    }

    @Test
    public void modifyingTranlsation_withNullLanguageCode_throws() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", ntt_de, ntt_en, ntt_fr);
        try {
            ntd.setTitleInLanguage(null, "foo");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("langCode must not be null.");
        }
    }

    @Test
    public void modifyTranslation_withMainLanguageTranslationModfied_changesMainTitle_translationTitle_andSetsModifiedTimestamp() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", ntt_de, ntt_en, ntt_fr);
        ntd.setTitleInLanguage("de", "thema 2");
        assertThat(ntd.getTitle()).isEqualTo("thema 2");
        assertThat(ntd
            .getTranslations()
            .get("de")
            .getTitle()).isEqualTo("thema 2");
        assertThat(ntd
            .getTranslations()
            .get("de")
            .getLastModified()).isNotNull();
        assertThat(ntd
            .getTranslations()
            .get("en")
            .getLastModified()).isNull();
        assertThat(ntd
            .getTranslations()
            .get("fr")
            .getLastModified()).isNull();
    }

    @Test
    public void modifyTranslation_withNonMainLanguageTranslationModfied_keepsMainTitle_changesTranslationTitle_andSetsModifiedTimestamp() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", ntt_de, ntt_en, ntt_fr);
        ntd.setTitleInLanguage("fr", "bar");
        assertThat(ntd.getTitle()).isEqualTo("thema2");
        assertThat(ntd
            .getTranslations()
            .get("fr")
            .getTitle()).isEqualTo("bar");
        assertThat(ntd
            .getTranslations()
            .get("de")
            .getLastModified()).isNull();
        assertThat(ntd
            .getTranslations()
            .get("en")
            .getLastModified()).isNull();
        assertThat(ntd
            .getTranslations()
            .get("fr")
            .getLastModified()).isNotNull();
    }
}