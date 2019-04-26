package ch.difty.scipamato.publ.web.pym;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class PymScriptsTest {

    @Test
    void testValues() {
        assertThat(PymScripts.values()).containsExactly(PymScripts.INSTANTIATE, PymScripts.RESIZE);
    }

    @Test
    void instantiating() {
        assertThat(PymScripts.INSTANTIATE.id).isEqualTo("pymChild");
        assertThat(PymScripts.INSTANTIATE.script).isEqualTo(
            "var pymChild = new pym.Child({ id: 'scipamato-public' });");
    }

    @Test
    void resizing() {
        assertThat(PymScripts.RESIZE.id).isEqualTo("pymResize");
        assertThat(PymScripts.RESIZE.script).isEqualTo("pymChild.sendHeight();");
    }
}