package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SimplonCssResourceReferenceTest {

    private static final SimplonCssResourceReference REF = SimplonCssResourceReference.get();

    @Test
    public void canGetInstance() {
        assertThat(REF).isInstanceOf(SimplonCssResourceReference.class);
    }

    @Test
    public void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("css/Simplon.css");
    }
}
