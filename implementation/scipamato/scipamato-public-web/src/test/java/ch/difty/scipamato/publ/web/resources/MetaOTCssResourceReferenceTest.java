package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MetaOTCssResourceReferenceTest {

    private static final MetaOTCssResourceReference REF = MetaOTCssResourceReference.get();

    @Test
    void canGetInstance() {
        assertThat(REF).isInstanceOf(MetaOTCssResourceReference.class);
    }

    @Test
    void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("css/MetaOT.css");
    }
}
