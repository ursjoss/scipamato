package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class NewStudyPageLinkTest extends PublicEntityTest<NewStudyPageLink> {

    @Override
    protected NewStudyPageLink newEntity() {
        return new NewStudyPageLink("en", 1, "foo", "https://bar.org");
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getLangCode()).isEqualTo("en");
        assertThat(getEntity().getSort()).isEqualTo(1);
        assertThat(getEntity().getTitle()).isEqualTo("foo");
        assertThat(getEntity().getUrl()).isEqualTo("https://bar.org");
    }

    @Override
    protected String getToString() {
        return "NewStudyPageLink(langCode=en, sort=1, title=foo, url=https://bar.org)";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier
            .forClass(NewStudy.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getFieldName(), MODIFIED.getFieldName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void assertEnumFields() {
        assertThat(NewStudyPageLink.NewStudyPageLinkFields.values())
            .extracting("name")
            .containsExactly("langCode", "sort", "title", "url");
    }
}