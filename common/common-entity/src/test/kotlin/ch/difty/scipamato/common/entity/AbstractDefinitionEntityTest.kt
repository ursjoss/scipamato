package ch.difty.scipamato.common.entity

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

@Suppress("PrivatePropertyName", "SpellCheckingInspection", "LocalVariableName")
class AbstractDefinitionEntityTest {

    private var dt_de = TestDefinitionTranslation(1, "de", "deutsch", 10)
    private var dt_de2 = TestDefinitionTranslation(1, "de", "deutsch2", 100)
    private var dt_en = TestDefinitionTranslation(2, "en", "english", 20)
    private var dt_fr = TestDefinitionTranslation(3, "fr", "francais", 30)

    private val tde = TestDefinitionEntity(
        mainLanguageCode = "de",
        mainName = "mainName",
        version = 11,
        translations = arrayOf(dt_de, dt_en, dt_fr, dt_de2)
    )
    private val tde_wo_transl = TestDefinitionEntity(
        mainLanguageCode = "de",
        mainName = "some",
        version = 0,
        translations = arrayOf()
    )
    private val tde_wo_transl_versionNull = TestDefinitionEntity(
        mainLanguageCode = "de",
        mainName = "some",
        translations = arrayOf()
    )

    private inner class TestDefinitionEntity internal constructor(
        mainLanguageCode: String,
        mainName: String,
        version: Int? = 0,
        translations: Array<AbstractDefinitionTranslation>
    ) : AbstractDefinitionEntity<DefinitionTranslation, String>(mainLanguageCode, mainName, version, translations) {
        override val nullSafeId: String? get() = "foo"
    }

    private inner class TestDefinitionEntity2 internal constructor(
        mainLanguageCode: String,
        mainName: String,
        translations: Array<AbstractDefinitionTranslation>
    ) : AbstractDefinitionEntity<DefinitionTranslation, String>(
        mainLanguageCode,
        mainName,
        translationArray = translations
    ) {
        override val nullSafeId: String? get() = "foo"
    }

    private inner class TestDefinitionTranslation(
        id: Int?,
        langCode: String,
        name: String?,
        version: Int? = 0
    ) : AbstractDefinitionTranslation(id, langCode, name, version)

    private inner class TestDefinitionTranslation2(
        id: Int?,
        langCode: String,
        name: String?
    ) : AbstractDefinitionTranslation(id, langCode, name)

    @Test
    fun entity_canGetMainLanguageCode() {
        tde.mainLanguageCode shouldBeEqualTo "de"
    }

    @Test
    fun entity_canGetName() {
        tde.name shouldBeEqualTo "mainName"
    }

    @Test
    fun definitionEntity_canChangeName() {
        tde.name = "foo"
        tde.name shouldBeEqualTo "foo"
    }

    @Test
    fun entity_canGetTranslationsInLanguage_evenMultiple() {
        tde.getTranslations("de") shouldContainSame listOf(dt_de, dt_de2)
        tde.getTranslations("en") shouldContainSame listOf(dt_en)
        tde.getTranslations("fr") shouldContainSame listOf(dt_fr)
    }

    @Test
    fun entity_gettingTranslationsInUndefinedLanguage_returnsEmptyList() {
        tde.getTranslations("es").shouldBeEmpty()
    }

    @Test
    fun entity_canGetTranslationsAsString() {
        tde.translationsAsString shouldBeEqualTo "DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais'"
    }

    @Test
    fun entity_canAddTranslationInLanguage() {
        tde.addTranslation("EN", TestDefinitionTranslation(20, "en", "english2", 20))
        tde.translationsAsString shouldBeEqualTo "DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais'; EN: 'english2'"
    }

    @Test
    fun entity_canRemoveTranslationInLanguage() {
        tde.translationsAsString shouldBeEqualTo "DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais'"
        tde.removeTranslation(dt_de2)
        tde.translationsAsString shouldBeEqualTo "DE: 'deutsch'; EN: 'english'; FR: 'francais'"
    }

    @Test
    fun entity_gettingTranslationsAsString_withNoTranslations_returnsNull() {
        tde_wo_transl.translationsAsString.shouldBeNull()
    }

    @Test
    fun entity_gettingTranslationsAsString_withSingleTranslationsInMainLanguage_WithNullName_returnsNA() {
        val t = TestDefinitionTranslation(1, "de", null, 10)
        val e = TestDefinitionEntity("de", "some", 1, arrayOf(t))
        e.translationsAsString shouldBeEqualTo "DE: n.a."
    }

    @Test
    fun entity_gettingTranslationsAsString_withMultipleTranslations_allButMainLanguageWithNullName_returns_NA_last() {
        val t_de = TestDefinitionTranslation(1, "de", "d", 10)
        val t_en = TestDefinitionTranslation(2, "en", null, 11)
        val t_fr = TestDefinitionTranslation(3, "fr", null, 12)
        val e = TestDefinitionEntity("de", "some", 1, arrayOf(t_de, t_en, t_fr))
        e.translationsAsString shouldBeEqualTo "DE: 'd'; EN: n.a.; FR: n.a."
    }

    @Test
    fun entity_canSetNameInMainLanguage() {
        tde.getNameInLanguage("de") shouldBeEqualTo "deutsch"
        tde.name shouldBeEqualTo "mainName"
        tde.setNameInLanguage("de", "d")
        tde.getNameInLanguage("de") shouldBeEqualTo "d"
        tde.name shouldBeEqualTo "d"
    }

    @Test
    fun entity_canSetNameInOtherDefinedLanguage() {
        tde.getNameInLanguage("en") shouldBeEqualTo "english"
        tde.setNameInLanguage("en", "e")
        tde.getNameInLanguage("en") shouldBeEqualTo "e"
        tde.name shouldBeEqualTo "mainName"
    }

    @Test
    fun entity_settingNameInUndefinedLanguageHasNoEffect() {
        tde.getNameInLanguage("es").shouldBeNull()
        tde.setNameInLanguage("es", "d")
        tde.getNameInLanguage("es").shouldBeNull()
    }

    @Test
    fun entity_gettingNameInUndefinedLanguage_returnsNull() {
        tde.getNameInLanguage("es").shouldBeNull()
    }

    @Test
    fun entity_withNoTranslations_gettingNameInNormallyDefinedLanguage_returnsNull() {
        tde_wo_transl.getNameInLanguage("es").shouldBeNull()
    }

    @Test
    fun entity_canGetDisplayName() {
        tde.displayValue shouldBeEqualTo "mainName"
    }

    @Test
    fun entity_canGet_nullSafeId() {
        tde.nullSafeId shouldBeEqualTo "foo"
    }

    @Test
    fun entity_testingToString() {
        tde.toString() shouldBeEqualTo
            "AbstractDefinitionEntity[translations=DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais', " +
            "mainLanguageCode=de, name=mainName]"
    }

    @Test
    fun entity_settingVersionNull_resultsInZero() {
        tde_wo_transl_versionNull.version shouldBeEqualTo 0
    }

    @Test
    fun translation_canGetId() {
        dt_de.id shouldBeEqualTo 1
    }

    @Test
    fun translation_canGetLangCode() {
        dt_de.langCode shouldBeEqualTo "de"
    }

    @Test
    fun translation_canGetName() {
        dt_de.name shouldBeEqualTo "deutsch"
    }

    @Test
    fun translation_canGetDisplayValue() {
        dt_de.displayValue shouldBeEqualTo "de: deutsch"
    }

    @Test
    fun translation_canGetVersion() {
        dt_de.version shouldBeEqualTo 10
    }

    @Test
    fun translation_settingVersionNull_resultsInZero() {
        val dt = TestDefinitionTranslation(1, "de", "deutsch")
        dt.version shouldBeEqualTo 0
    }

    @Test
    fun translation_testingToString() {
        dt_de.toString() shouldBeEqualTo "AbstractDefinitionTranslation(id=1, langCode=de, name=deutsch)"
    }

    @Test
    fun translationFields() {
        AbstractDefinitionTranslation.DefinitionTranslationFields.values() shouldContainSame listOf(
            AbstractDefinitionTranslation.DefinitionTranslationFields.ID,
            AbstractDefinitionTranslation.DefinitionTranslationFields.LANG_CODE,
            AbstractDefinitionTranslation.DefinitionTranslationFields.NAME
        )
        AbstractDefinitionTranslation.DefinitionTranslationFields.ID.fieldName shouldBeEqualTo "id"
        AbstractDefinitionTranslation.DefinitionTranslationFields.LANG_CODE.fieldName shouldBeEqualTo "langCode"
        AbstractDefinitionTranslation.DefinitionTranslationFields.NAME.fieldName shouldBeEqualTo "name"
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(AbstractDefinitionEntity::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(
                ScipamatoEntity.ScipamatoEntityFields.CREATED.fieldName,
                ScipamatoEntity.ScipamatoEntityFields.MODIFIED.fieldName
            )
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun testDefinitionEntity_withNullVersion_hasVersionZero() {
        TestDefinitionEntity(
            mainLanguageCode = "de",
            mainName = "mainName",
            version = null,
            translations = arrayOf(dt_de, dt_en, dt_fr, dt_de2)
        ).version shouldBeEqualTo 0
    }

    @Test
    fun testDefinitionEntity_withoutVersion_hasVersionZero() {
        TestDefinitionEntity2(
            mainLanguageCode = "de",
            mainName = "mainName",
            translations = arrayOf(dt_de, dt_en, dt_fr, dt_de2)
        ).version shouldBeEqualTo 0
    }

    @Test
    fun testDefinitionTranslation_withNullVersion_hasVersionZero() {
        TestDefinitionTranslation(
            id = 4,
            langCode = "de",
            name = "deutsch3",
            version = null
        ).version shouldBeEqualTo 0
    }

    @Test
    fun testDefinitionTranslation_withoutVersion_hasVersionZero() {
        TestDefinitionTranslation2(
            id = 4,
            langCode = "de",
            name = "deutsch3"
        ).version shouldBeEqualTo 0
    }
}
