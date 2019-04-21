package ch.difty.scipamato.common.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.TestUtils;

@SuppressWarnings("unchecked")
public class AbstractDefinitionEntityTest {

    private class TestDefinitionEntity extends AbstractDefinitionEntity {
        TestDefinitionEntity(final String mainLanguageCode, final String mainName, final Integer version,
            final DefinitionTranslation[] translations) {
            super(mainLanguageCode, mainName, version, translations);
        }

        @Override
        public String getNullSafeId() {
            return "foo";
        }
    }

    private class TestDefinitionTranslation extends AbstractDefinitionTranslation {
        TestDefinitionTranslation(final Integer id, final String langCode, final String name, final Integer version) {
            super(id, langCode, name, version);
        }
    }

    private AbstractDefinitionEntity      tde;
    private AbstractDefinitionTranslation dt_de, dt_de2, dt_en, dt_fr;

    private final TestDefinitionEntity tde_wo_transl = new TestDefinitionEntity("de", "some", 0,
        new DefinitionTranslation[] {});

    private final TestDefinitionEntity tde_wo_transl_versionNull = new TestDefinitionEntity("de", "some", null,
        new DefinitionTranslation[] {});

    @BeforeEach
    public void setUp() {
        dt_de = new TestDefinitionTranslation(1, "de", "deutsch", 10);
        dt_de2 = new TestDefinitionTranslation(1, "de", "deutsch2", 100);
        dt_en = new TestDefinitionTranslation(2, "en", "english", 20);
        dt_fr = new TestDefinitionTranslation(3, "fr", "francais", 30);
        final DefinitionTranslation[] translations = { dt_de, dt_en, dt_fr, dt_de2 };
        tde = new TestDefinitionEntity("de", "mainName", 11, translations);
    }

    @Test
    public void degenerateConstruction_ofEntityDefinition_withNullLanguageCode_throws() {
        TestUtils.assertDegenerateSupplierParameter(
            () -> new TestDefinitionEntity(null, "some", 1, new DefinitionTranslation[] {}), "mainLanguageCode");
    }

    @Test
    public void degenerateConstruction_ofEntityDefinition_withNullTranslationArray_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new TestDefinitionEntity("de", "some", 1, null),
            "translations");
    }

    @Test
    public void degenerateConstruction_ofTranslation_withNullLanguageCode_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new TestDefinitionTranslation(1, null, "some", 1),
            "langCode");
    }

    @Test
    public void entity_canGetMainLanguageCode() {
        assertThat(tde.getMainLanguageCode()).isEqualTo("de");
    }

    @Test
    public void entity_canGetName() {
        assertThat(tde.getName()).isEqualTo("mainName");
    }

    @Test
    public void definitionEntity_canChangeName() {
        tde.setName("foo");
        assertThat(tde.getName()).isEqualTo("foo");
    }

    @Test
    public void entity_canGetTranslationsInLanguage_evenMultiple() {
        assertThat(tde
            .getTranslations()
            .get("de")).containsExactly(dt_de, dt_de2);
        assertThat(tde
            .getTranslations()
            .get("en")).containsExactly(dt_en);
        assertThat(tde
            .getTranslations()
            .get("fr")).containsExactly(dt_fr);
    }

    @Test
    public void entity_gettingTranslationsInUndefinedLanguage_returnsEmpty() {
        assertThat(tde
            .getTranslations()
            .get("es")).isEmpty();
    }

    @Test
    public void entity_canGetTranslationsAsString() {
        assertThat(tde.getTranslationsAsString()).isEqualTo("DE: 'deutsch','deutsch2'; EN: 'english'; FR: 'francais'");
    }

    @Test
    public void entity_gettingTranslationsAsString_withNoTranslations_returnsNull() {
        assertThat(tde_wo_transl.getTranslationsAsString()).isNull();
    }

    @Test
    public void entity_gettingTranslationsAsString_withSingleTranslationsInMainLanguage_WithNullName_returnsNA() {
        AbstractDefinitionTranslation t = new TestDefinitionTranslation(1, "de", null, 10);
        AbstractDefinitionEntity e = new TestDefinitionEntity("de", "some", 1, new DefinitionTranslation[] { t });
        assertThat(e.getTranslationsAsString()).isEqualTo("DE: n.a.");
    }

    @Test
    public void entity_gettingTranslationsAsString_withMultipleTranslations_allButMainLanguageWithNullName_returns_NA_last() {
        AbstractDefinitionTranslation t_de = new TestDefinitionTranslation(1, "de", "d", 10);
        AbstractDefinitionTranslation t_en = new TestDefinitionTranslation(2, "en", null, 11);
        AbstractDefinitionTranslation t_fr = new TestDefinitionTranslation(3, "fr", null, 12);
        AbstractDefinitionEntity e = new TestDefinitionEntity("de", "some", 1,
            new DefinitionTranslation[] { t_de, t_en, t_fr });
        assertThat(e.getTranslationsAsString()).isEqualTo("DE: 'd'; EN: n.a.; FR: n.a.");
    }

    @Test
    public void entity_canSetNameInMainLanguage() {
        assertThat(tde.getNameInLanguage("de")).isEqualTo("deutsch");
        assertThat(tde.getName()).isEqualTo("mainName");
        tde.setNameInLanguage("de", "d");
        assertThat(tde.getNameInLanguage("de")).isEqualTo("d");
        assertThat(tde.getName()).isEqualTo("d");
    }

    @Test
    public void entity_canSetNameInOtherDefinedLanguage() {
        assertThat(tde.getNameInLanguage("en")).isEqualTo("english");
        tde.setNameInLanguage("en", "e");
        assertThat(tde.getNameInLanguage("en")).isEqualTo("e");
        assertThat(tde.getName()).isEqualTo("mainName");
    }

    @Test
    public void entity_settingNameInUndefinedLanguageHasNoEffect() {
        assertThat(tde.getNameInLanguage("es")).isNull();
        tde.setNameInLanguage("es", "d");
        assertThat(tde.getNameInLanguage("es")).isNull();
    }

    @Test
    public void modifyingTranslation_withNullLanguageCode_throws() {
        try {
            tde.setNameInLanguage(null, "foo");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("langCode must not be null.");
        }
    }

    @Test
    public void entity_gettingNameInUndefinedLanguage_returnsNull() {
        assertThat(tde.getNameInLanguage("es")).isNull();
    }

    @Test
    public void entity_withNoTranslations_gettingNameInNormallyDefinedLanguage_returnsNull() {
        assertThat(tde_wo_transl.getNameInLanguage("es")).isNull();
    }

    @Test
    public void entity_canGetDisplayName() {
        assertThat(tde.getDisplayValue()).isEqualTo("mainName");
    }

    @Test
    public void entity_canGet_nullSafeId() {
        assertThat(tde.getNullSafeId()).isEqualTo("foo");
    }

    @Test
    public void entity_testingToString() {
        assertThat(tde.toString()).isEqualTo(
            "AbstractDefinitionEntity(translations={de=[AbstractDefinitionTranslation(id=1, langCode=de, name=deutsch), "
            + "AbstractDefinitionTranslation(id=1, langCode=de, name=deutsch2)], en=[AbstractDefinitionTranslation(id=2, "
            + "langCode=en, name=english)], fr=[AbstractDefinitionTranslation(id=3, langCode=fr, name=francais)]}, mainLanguageCode=de, name=mainName)");
    }

    @Test
    public void entity_settingVersionNull_resultsInZero() {
        assertThat(tde_wo_transl_versionNull.getVersion()).isEqualTo(0);
    }

    @Test
    public void translation_canGetId() {
        assertThat(dt_de.getId()).isEqualTo(1);
    }

    @Test
    public void translation_canGetLangCode() {
        assertThat(dt_de.getLangCode()).isEqualTo("de");
    }

    @Test
    public void translation_canGetName() {
        assertThat(dt_de.getName()).isEqualTo("deutsch");
    }

    @Test
    public void translation_canGetDisplayValue() {
        assertThat(dt_de.getDisplayValue()).isEqualTo("de: deutsch");
    }

    @Test
    public void translation_canGetVersion() {
        assertThat(dt_de.getVersion()).isEqualTo(10);
    }

    @Test
    public void translation_settingVersionNull_resultsInZero() {
        TestDefinitionTranslation dt = new TestDefinitionTranslation(1, "de", "deutsch", null);
        assertThat(dt.getVersion()).isEqualTo(0);
    }

    @Test
    public void translation_testingToString() {
        assertThat(dt_de.toString()).isEqualTo("AbstractDefinitionTranslation(id=1, langCode=de, name=deutsch)");
    }

    @Test
    public void translationFields() {
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.values()).containsExactly(
            AbstractDefinitionTranslation.DefinitionTranslationFields.ID,
            AbstractDefinitionTranslation.DefinitionTranslationFields.LANG_CODE,
            AbstractDefinitionTranslation.DefinitionTranslationFields.NAME);
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.ID.getName()).isEqualTo("id");
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.LANG_CODE.getName()).isEqualTo("langCode");
        assertThat(AbstractDefinitionTranslation.DefinitionTranslationFields.NAME.getName()).isEqualTo("name");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(AbstractDefinitionEntity.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), MODIFIED.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}