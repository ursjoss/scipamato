package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PymJavaScriptResourceReferenceTest {

    private static final PymJavaScriptResourceReference REF = PymJavaScriptResourceReference.get();

    @Test
    public void canGetInstance() {
        assertThat(REF).isInstanceOf(PymJavaScriptResourceReference.class);
    }

    @Test
    public void assertResourceName() {
        assertThat(REF.getName()).isEqualTo("js/pym.v1.js");
    }
}
