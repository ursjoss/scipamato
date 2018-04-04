package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MainCssResourceReferenceTest {

    private static final MainCssResourceReference REF = MainCssResourceReference.get();

    @Test
    public void canGetInstance() {
        assertThat(REF).isInstanceOf(MainCssResourceReference.class);
    }

    @Test
    public void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("css/main.css");
    }
}
