package ch.difty.sipamato.entity.xml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;

public class SipamatoPubmedArticleTest {

    @Test
    public void degenerateConstruction() {
        try {
            new SipamatoPubmedArticle(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle must not be null.");
        }
    }
}
