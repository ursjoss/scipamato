package ch.difty.scipamato.common.entity

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("PrivatePropertyName", "SpellCheckingInspection", "LocalVariableName")
class AbstractDefinitionEntityTest {

    private var dt_de = TestDefinitionTranslation(1, "de", "deutsch", 10)
    private var dt_de2 = TestDefinitionTranslation(1, "de", "deutsch2", 100)
    private var dt_en = TestDefinitionTranslation(2, "en", "english", 20)
    private var dt_fr = TestDefinitionTranslation(3, "fr", "francais", 30)

    private val tde = TestDefinitionEntity("de", "mainName", 11, arrayOf(dt_de, dt_en, dt_fr, dt_de2))
    private val tde_wo_transl = TestDefinitionEntity("de", "some", 0, arrayOf())
    private val tde_wo_transl_versionNull = TestDefinitionEntity("de", "some", translations = arrayOf())

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
    ) : AbstractDefinitionEntity<DefinitionTranslation, String>(mainLanguageCode, mainName, translationArray = translations) {
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
        assertThat(tde.mainLanguageCode).isEqualTo("de")
    }

    @Test
    fun entity_canGetName() {
        assertThat(tde.name).isEqualTo("mainName")
    }

    @Test
    fun definitionEntity_canChangeName() {
        tde.name = "foo"
        assertThat(tde.name).isEqualTo("foo")
    }

    @Test
    fun entity_canGetTranslationsInLanguage_evenMultiple() {
        assertThat(tde.getTranslations("de")).containsExactly(dt_de, dt_de2)
        assertThat(tde.getTranslations("en")).containsExactly(dt_en)
        assertThat(tde.getTranslations("fr")).containsExactly(dt_fr)
    }

    @Test
    fun entity_gettingTranslationsInUndefinedLanguage_returnsEmptyList() {
        assertThat(tde.getTranslations("es")).isEmpty()
    }

    @Test
    fun entity_canGetTranslationsAsString() {
        assertThat(tde.translationsAsString).isEqualTo("DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais'")
    }

    @Test
    fun entity_canAddTranslationInLanguage() {
        tde.addTranslation("EN", TestDefinitionTranslation(20, "en", "english2", 20))
        assertThat(tde.translationsAsString).isEqualTo(
            "DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais'; EN: 'english2'"
        )
    }

    @Test
    fun entity_canRemoveTranslationInLanguage() {
        assertThat(tde.translationsAsString).isEqualTo(
            "DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais'"
        )
        tde.removeTranslation(dt_de2)
        assertThat(tde.translationsAsString).isEqualTo(
            "DE: 'deutsch'; EN: 'english'; FR: 'francais'"
        )
    }

    @Test
    fun entity_gettingTranslationsAsString_withNoTranslations_returnsNull() {
        assertThat(tde_wo_transl.translationsAsString).isNull()
    }

    @Test
    fun entity_gettingTranslationsAsString_withSingleTranslationsInMainLanguage_WithNullName_returnsNA() {
        val t = TestDefinitionTranslation(1, "de", null, 10)
        val e = TestDefinitionEntity("de", "some", 1, arrayOf(t))
        assertThat(e.translationsAsString).isEqualTo("DE: n.a.")
    }

    @Test
    fun entity_gettingTranslationsAsString_withMultipleTranslations_allButMainLanguageWithNullName_returns_NA_last() {
        val t_de = TestDefinitionTranslation(1, "de", "d", 10)
        val t_en = TestDefinitionTranslation(2, "en", null, 11)
        val t_fr = TestDefinitionTranslation(3, "fr", null, 12)
        val e = TestDefinitionEntity("de", "some", 1, arrayOf(t_de, t_en, t_fr))
        assertThat(e.translationsAsString).isEqualTo("DE: 'd'; EN: n.a.; FR: n.a.")
    }

    @Test
    fun entity_canSetNameInMainLanguage() {
        assertThat(tde.getNameInLanguage("de")).isEqualTo("deutsch")
        assertThat(tde.name).isEqualTo("mainName")
        tde.setNameInLanguage("de", "d")
        assertThat(tde.getNameInLanguage("de")).isEqualTo("d")
        assertThat(tde.name).isEqualTo("d")
    }

    @Test
    fun entity_canSetNameInOtherDefinedLanguage() {
        assertThat(tde.getNameInLanguage("en")).isEqualTo("english")
        tde.setNameInLanguage("en", "e")
        assertThat(tde.getNameInLanguage("en")).isEqualTo("e")
        assertThat(tde.name).isEqualTo("mainName")
    }

    @Test
    fun entity_settingNameInUndefinedLanguageHasNoEffect() {
        assertThat(tde.getNameInLanguage("es")).isNull()
        tde.setNameInLanguage("es", "d")
        assertThat(tde.getNameInLanguage("es")).isNull()
    }

    @Test
    fun entity_gettingNameInUndefinedLanguage_returnsNull() {
        assertThat(tde.getNameInLanguage("es")).isNull()
    }

    @Test
    fun entity_withNoTranslations_gettingNameInNormallyDefinedLanguage_returnsNull() {
        assertThat(tde_wo_transl.getNameInLanguage("es")).isNull()
    }

    @Test
    fun entity_canGetDisplayName() {
        assertThat(tde.displayValue).isEqualTo("mainName")
    }

    @Test
    fun entity_canGet_nullSafeId() {
        assertThat(tde.nullSafeId).isEqualTo("foo")
    }

    @Test
    fun entity_testingToString() {
        assertThat(tde.toString()).isEqualTo(
            "AbstractDefinitionEntity[translations=DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais', " +
                "mainLanguageCode=de, name=mainName]"
        )
    }

    @Test
    fun entity_settingVersionNull_resultsInZero() {
        assertThat(tde_wo_transl_versionNull.version).isEqualTo(0)
    }

    @Test
    fun translation_canGetId() {
        assertThat(dt_de.id).isEqualTo(1)
    }

    @Test
    fun translation_canGetLangCode() {
        assertThat(dt_de.langCode).isEqualTo("de")
    }

    @Test
    fun translation_canGetName() {
        assertThat(dt_de.name).isEqualTo("deutsch")
    }

    @Test
    fun translation_canGetDisplayValue() {
        assertThat(dt_de.displayValue).isEqualTo("de: deutsch")
    }

    @Test
    fun translation_canGetVersion() {
        assertThat(dt_de.version).isEqualTo(10)
    }

    @Test
    fun translation_settingVersionNull_resultsInZero() {
        val dt = TestDefinitionTranslation(1, "de", "deutsch")
        assertThat(dt.version).isEqualTo(0)
    }

    @Test
    fun translation_testingToString() {
        assertThat(dt_de.toString()).isEqualTo("AbstractDefinitionTranslation(id=1, langCode=de, name=deutsch)")
    }

    @Test
    fun translationFields() {
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.values()).containsExactly(
            AbstractDefinitionTranslation.DefinitionTranslationFields.ID,
            AbstractDefinitionTranslation.DefinitionTranslationFields.LANG_CODE,
            AbstractDefinitionTranslation.DefinitionTranslationFields.NAME)
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.ID.fieldName).isEqualTo("id")
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.LANG_CODE.fieldName).isEqualTo("langCode")
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.NAME.fieldName).isEqualTo("name")
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(AbstractDefinitionEntity::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(ScipamatoEntity.ScipamatoEntityFields.CREATED.fieldName,
                ScipamatoEntity.ScipamatoEntityFields.MODIFIED.fieldName)
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
