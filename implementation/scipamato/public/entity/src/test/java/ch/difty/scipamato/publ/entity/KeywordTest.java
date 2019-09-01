package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class KeywordTest extends PublicEntityTest<Keyword> {

    @Override
    protected Keyword newEntity() {
        return Keyword
            .builder()
            .id(1)
            .keywordId(2)
            .langCode("lc")
            .name("name")
            .searchOverride("n")
            .build();
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getId()).isEqualTo(1);
        assertThat(getEntity().getKeywordId()).isEqualTo(2);
        assertThat(getEntity().getLangCode()).isEqualTo("lc");
        assertThat(getEntity().getName()).isEqualTo("name");
        assertThat(getEntity().getSearchOverride()).isEqualTo("n");
    }

    @Override
    protected String getToString() {
        return "Keyword(id=1, keywordId=2, langCode=lc, name=name, searchOverride=n)";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier
            .forClass(Keyword.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getFieldName(), MODIFIED.getFieldName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void displayValue() {
        assertThat(getEntity().getDisplayValue()).isEqualTo("name");
    }

    @Test
    void assertEnumFields() {
        assertThat(Keyword.KeywordFields.values())
            .extracting("name")
            .containsExactly("id", "keywordId", "langCode", "name", "searchOverride", "displayValue");
    }
}
