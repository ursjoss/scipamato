package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MainCssResourceReferenceTest {

    private static final MainCssResourceReference REF = MainCssResourceReference.get();

    @Test
    void canGetInstance() {
        assertThat(REF).isInstanceOf(MainCssResourceReference.class);
    }

    @Test
    void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("css/main.css");
    }
}
