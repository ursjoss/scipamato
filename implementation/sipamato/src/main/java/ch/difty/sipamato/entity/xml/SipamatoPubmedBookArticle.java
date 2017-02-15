package ch.difty.sipamato.entity.xml;

import org.apache.commons.lang3.NotImplementedException;

import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.pubmed.PubmedBookArticle;

/**
 * Derives from {@link PubmedArticleFacade} wrapping an instance of {@link PubmedBookArticle}.
 * 
 * @author u.joss
 */
public class SipamatoPubmedBookArticle extends PubmedArticleFacade {

    protected SipamatoPubmedBookArticle(final PubmedBookArticle pubmedBookArticle) {
        AssertAs.notNull(pubmedBookArticle, "pubmedBookArticle");
        throw new NotImplementedException("parsing pubmedBookArticles is not yet implemented");
    }
}
