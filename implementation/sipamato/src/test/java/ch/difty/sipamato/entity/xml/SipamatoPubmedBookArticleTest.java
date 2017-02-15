package ch.difty.sipamato.entity.xml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.pubmed.PubmedBookArticle;

public class SipamatoPubmedBookArticleTest {

    @Test
    public void degenerateConstruction() {
        try {
            new SipamatoPubmedBookArticle(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedBookArticle must not be null.");
        }
    }

    @Test(expected = NotImplementedException.class)
    public void construct() {
        new SipamatoPubmedBookArticle(new PubmedBookArticle());
    }
}
