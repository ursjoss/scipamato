package ch.difty.scipamato.core.entity.code;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import ch.difty.scipamato.core.entity.CodeClass;

public class CodeFilterTest {

    private final CodeFilter f  = new CodeFilter();
    private final CodeClass  cc = new CodeClass(1, "cc1", "");

    @Test
    public void getAndSet() {
        f.setCodeClass(cc);
        f.setNameMask("nameMask");
        f.setCommentMask("commentMask");
        f.setInternal(true);

        assertThat(f.getCodeClass()).isEqualTo(cc);
        assertThat(f.getNameMask()).isEqualTo("nameMask");
        assertThat(f.getCommentMask()).isEqualTo("commentMask");
        assertThat(f.getInternal()).isTrue();

        assertThat(f.toString()).isEqualTo(
            "CodeFilter(codeClass=CodeClass[id=1], nameMask=nameMask, commentMask=commentMask, internal=true)");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(CodeFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(CodeFilter.CodeFilterFields.values())
            .extracting("name")
            .containsExactly("codeClass", "nameMask", "commentMask", "internal");
    }

}