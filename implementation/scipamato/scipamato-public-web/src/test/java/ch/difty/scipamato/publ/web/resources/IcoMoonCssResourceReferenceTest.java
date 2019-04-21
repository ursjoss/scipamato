package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class IcoMoonCssResourceReferenceTest {
    private static final IcoMoonCssResourceReference REF = IcoMoonCssResourceReference.get();

    @Test
    public void canGetInstance() {
        assertThat(REF).isInstanceOf(IcoMoonCssResourceReference.class);
    }

    @Test
    public void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("css/IcoMoon.css");
    }
}