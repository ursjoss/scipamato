package ch.difty.scipamato.public_.entity;

import static org.assertj.core.api.Assertions.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class CodeClassTest extends PublicEntityTest<CodeClass> {

    @Override
    protected CodeClass newEntity() {
        return CodeClass.builder().codeClassId(1).langCode("lc").name("name").description("description").build();
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
            .withIgnoredFields(CodeClass.CREATED, CodeClass.MODIFIED)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
