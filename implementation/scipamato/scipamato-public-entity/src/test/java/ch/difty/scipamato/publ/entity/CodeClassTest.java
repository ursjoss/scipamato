package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class CodeClassTest extends PublicEntityTest<CodeClass> {

    @Override
    protected CodeClass newEntity() {
        return CodeClass
            .builder()
            .codeClassId(1)
            .langCode("lc")
            .name("name")
            .description("description")
            .build();
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getCodeClassId()).isEqualTo(1);
        assertThat(getEntity().getLangCode()).isEqualTo("lc");
        assertThat(getEntity().getName()).isEqualTo("name");
        assertThat(getEntity().getDescription()).isEqualTo("description");
    }

    @Override
    protected String getToString() {
        return "CodeClass(codeClassId=1, langCode=lc, name=name, description=description)";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier
            .forClass(CodeClass.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), MODIFIED.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(CodeClass.CodeClassFields.values())
            .extracting("name")
            .containsExactly("codeClassId", "langCode", "name", "description");
    }
}
