package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;

public class NewsletterTopicDefinitionTest {

    private final NewsletterTopicTranslation ntt_de = new NewsletterTopicTranslation(10, "de", "thema2", 1);
    private final NewsletterTopicTranslation ntt_en = new NewsletterTopicTranslation(11, "en", "topic2", 1);
    private final NewsletterTopicTranslation ntt_fr = new NewsletterTopicTranslation(12, "fr", "sujet2", 1);

    @Test
    public void withNoTranslations_unableToEstablishMainTitle() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(1, "de", 1);
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getName()).isEqualTo("n.a.");
        assertThat(ntd.getDisplayValue()).isEqualTo("n.a.");
        assertThat(ntd
            .getTranslations()
            .asMap()).isEmpty();
    }

    @Test
    public void withTranslations() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr);
        assertThat(ntd.getId()).isEqualTo(2);
        assertThat(ntd.getName()).isEqualTo("thema2");
        assertThat(ntd.getDisplayValue()).isEqualTo("thema2");
        assertThat(ntd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(ntd
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<NewsletterTopicTranslation> trs = ntd
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(NewsletterTopicTranslation.DefinitionTranslationFields.NAME.getName())
            .containsOnly("thema2", "topic2", "sujet2");
        for (final NewsletterTopicTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr);
        assertThat(ntd.getTranslationsAsString()).isEqualTo("DE: 'thema2'; EN: 'topic2'; FR: 'sujet2'");
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en,
            new NewsletterTopicTranslation(12, "fr", null, 1));
        assertThat(ntd.getTranslationsAsString()).isEqualTo("DE: 'thema2'; EN: 'topic2'; FR: n.a.");
    }

    @Test
    public void modifyTranslation_withMainLanguageTranslationModified_changesMainTitle_translationTitle_andSetsModifiedTimestamp() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr);
        ntd.setNameInLanguage("de", "thema 2");
        assertThat(ntd.getName()).isEqualTo("thema 2");
        assertThat(ntd
            .getTranslations()
            .get("de")
            .get(0)
            .getName()).isEqualTo("thema 2");
        assertThat(ntd
            .getTranslations()
            .get("de")
            .get(0)
            .getLastModified()).isNotNull();
        assertThat(ntd
            .getTranslations()
            .get("en")
            .get(0)
            .getLastModified()).isNull();
        assertThat(ntd
            .getTranslations()
            .get("fr")
            .get(0)
            .getLastModified()).isNull();
    }

    @Test
    public void modifyTranslation_withNonMainLanguageTranslationModified_keepsMainTitle_changesTranslationTitle_andSetsModifiedTimestamp() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr);
        ntd.setNameInLanguage("fr", "bar");
        assertThat(ntd.getName()).isEqualTo("thema2");
        assertThat(ntd
            .getTranslations()
            .get("fr")
            .get(0)
            .getName()).isEqualTo("bar");
        assertThat(ntd
            .getTranslations()
            .get("de")
            .get(0)
            .getLastModified()).isNull();
        assertThat(ntd
            .getTranslations()
            .get("en")
            .get(0)
            .getLastModified()).isNull();
        assertThat(ntd
            .getTranslations()
            .get("fr")
            .get(0)
            .getLastModified()).isNotNull();
    }

    @Test
    public void gettingNullSafeId_withNonNullId() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr);
        assertThat(ntd.getNullSafeId()).isEqualTo(2);
    }

    @Test
    public void gettingNullSafeId_withNullId() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(null, "de", 1, ntt_de, ntt_en, ntt_fr);
        assertThat(ntd.getNullSafeId()).isEqualTo(0);
    }

    @Test
    public void titleIsAliasForName() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr);
        assertThat(ntd.getTitle()).isEqualTo(ntd.getName());
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo(ntd.getNameInLanguage("de"));
        ntd.setTitle("foo");
        assertThat(ntd.getName()).isEqualTo("foo");
        ntd.setTitleInLanguage("de", "bar");
        assertThat(ntd.getName()).isEqualTo("bar");
    }
}