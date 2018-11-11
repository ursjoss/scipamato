package ch.difty.scipamato.core.entity.code_class;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class CodeClassFilterTest {

    private final CodeClassFilter f = new CodeClassFilter();

    @Test
    public void getAndSet() {
        f.setNameMask("nameMask");
        f.setDescriptionMask("descriptionMask");

        assertThat(f.getNameMask()).isEqualTo("nameMask");
        assertThat(f.getDescriptionMask()).isEqualTo("descriptionMask");

        assertThat(f.toString()).isEqualTo("CodeClassFilter(nameMask=nameMask, descriptionMask=descriptionMask)");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(CodeClassFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(CodeClassFilter.CodeClassFilterFields.values())
            .extracting("name")
            .containsExactly("nameMask", "descriptionMask");
    }

}