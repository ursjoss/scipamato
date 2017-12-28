package ch.difty.scipamato.public_.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class CodeTest extends PublicEntityTest<Code> {

    @Override
    protected Code newEntity() {
        return Code.builder()
            .codeClassId(1)
            .code("code")
            .langCode("lc")
            .name("name")
            .comment("comment")
            .sort(3)
            .build();
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getCodeClassId()).isEqualTo(1);
        assertThat(getEntity().getCode()).isEqualTo("code");
        assertThat(getEntity().getLangCode()).isEqualTo("lc");
        assertThat(getEntity().getName()).isEqualTo("name");
        assertThat(getEntity().getComment()).isEqualTo("comment");
        assertThat(getEntity().getSort()).isEqualTo(3);
    }

    @Override
    protected String getToString() {
        return "Code(codeClassId=1, code=code, langCode=lc, name=name, comment=comment, sort=3)";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier.forClass(Code.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(Code.CREATED, Code.MODIFIED)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void displayValue() {
        assertThat(getEntity().getDisplayValue()).isEqualTo("name");
    }
}
