package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MetaOTCssResourceReferenceTest {

    private static final MetaOTCssResourceReference REF = MetaOTCssResourceReference.get();

    @Test
    public void canGetInstance() {
        assertThat(REF).isInstanceOf(MetaOTCssResourceReference.class);
    }

    @Test
    public void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("css/MetaOT.css");
    }
}
