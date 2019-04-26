package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SimplonCssResourceReferenceTest {

    private static final SimplonCssResourceReference REF = SimplonCssResourceReference.get();

    @Test
    void canGetInstance() {
        assertThat(REF).isInstanceOf(SimplonCssResourceReference.class);
    }

    @Test
    void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("css/Simplon.css");
    }
}
