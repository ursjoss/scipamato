package ch.difty.scipamato.core.entity.newsletter

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("PrivatePropertyName", "SpellCheckingInspection")
internal class NewsletterTopicDefinitionTest {

    private val ntt_de = NewsletterTopicTranslation(10, "de", "thema2", 1)
    private val ntt_en = NewsletterTopicTranslation(11, "en", "topic2", 1)
    private val ntt_fr = NewsletterTopicTranslation(12, "fr", "sujet2", 1)

    @Test
    fun withNoTranslations_unableToEstablishMainTitle() {
        val ntd = NewsletterTopicDefinition(1, "de", 1)
        ntd.id shouldBeEqualTo 1
        ntd.name shouldBeEqualTo "n.a."
        ntd.displayValue shouldBeEqualTo "n.a."
        ntd.getTranslations().shouldBeEmpty()
    }

    @Test
    fun withTranslations() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.id shouldBeEqualTo 2
        ntd.name shouldBeEqualTo "thema2"
        ntd.displayValue shouldBeEqualTo "thema2"
        val trs = ntd.getTranslations()
        trs.map { it.name } shouldContainSame listOf("thema2", "topic2", "sujet2")
        for (tr in trs)
            tr.lastModified.shouldBeNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.translationsAsString shouldBeEqualTo "DE: 'thema2'; EN: 'topic2'; FR: 'sujet2'"
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val ntd = NewsletterTopicDefinition(
            2, "de", 1, ntt_de, ntt_en,
            NewsletterTopicTranslation(12, "fr", null, 1)
        )
        ntd.translationsAsString shouldBeEqualTo "DE: 'thema2'; EN: 'topic2'; FR: n.a."
    }

    @Test
    fun modifyTransl_withMainLanguageTranslationModified_changesMainTitle_translationTitle_andSetsModifiedTimestamp() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.setNameInLanguage("de", "thema 2")
        ntd.name shouldBeEqualTo "thema 2"
        ntd.getTranslations("de")[0]?.name shouldBeEqualTo "thema 2"
        ntd.getTranslations("de")[0]?.lastModified.shouldNotBeNull()
        ntd.getTranslations("en")[0]?.lastModified.shouldBeNull()
        ntd.getTranslations("fr")[0]?.lastModified.shouldBeNull()
    }

    @Test
    fun modifyTransl_withNonMainLangTranslModified_keepsMainTitle_changesTranslationTitle_andSetsModifiedTimestamp() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.setNameInLanguage("fr", "bar")
        ntd.name shouldBeEqualTo "thema2"
        ntd.getTranslations("fr")[0]?.name shouldBeEqualTo "bar"
        ntd.getTranslations("de")[0]?.lastModified.shouldBeNull()
        ntd.getTranslations("en")[0]?.lastModified.shouldBeNull()
        ntd.getTranslations("fr")[0]?.lastModified.shouldNotBeNull()
    }

    @Test
    fun gettingNullSafeId_withNonNullId() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.nullSafeId shouldBeEqualTo 2
    }

    @Test
    fun gettingNullSafeId_withNullId() {
        val ntd = NewsletterTopicDefinition(null, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.nullSafeId shouldBeEqualTo 0
    }

    @Test
    fun titleIsAliasForName() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.title shouldBeEqualTo ntd.name
        ntd.getTitleInLanguage("de") shouldBeEqualTo ntd.getNameInLanguage("de")
        ntd.title = "foo"
        ntd.name shouldBeEqualTo "foo"
        ntd.setTitleInLanguage("de", "bar")
        ntd.name shouldBeEqualTo "bar"
    }
}
