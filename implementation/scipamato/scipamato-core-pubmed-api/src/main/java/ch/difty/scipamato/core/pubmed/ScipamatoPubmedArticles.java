package ch.difty.scipamato.core.pubmed;

import ch.difty.scipamato.core.pubmed.api.PubmedArticle;
import ch.difty.scipamato.core.pubmed.api.PubmedBookArticle;

public final class ScipamatoPubmedArticles {

    private ScipamatoPubmedArticles() {
    }

    /**
     * Instantiate an instance of {@link ScipamatoPubmedArticle} if the provided
     * object is an instance of {@link PubmedArticle} or an instance of
     * {@link ScipamatoPubmedBookArticle} if the provided object is an instance of
     * {@link PubmedBookArticle}.
     *
     * @param pubmedArticleOrPubmedBookArticle
     *            and object that should either be an instance of
     *            {@link PubmedArticle} or {@link PumbedBookArticle}
     * @return a derivative of {@link PubmedArticleFacade}
     * @throws IllegalArgumentException
     *             - if the parameter is of any other class than one of the two
     *             managed ones.
     */
    public static PubmedArticleFacade newPubmedArticleFrom(final java.lang.Object pubmedArticleOrPubmedBookArticle) {
        if (pubmedArticleOrPubmedBookArticle instanceof PubmedArticle)
            return new ScipamatoPubmedArticle((PubmedArticle) pubmedArticleOrPubmedBookArticle);
        else if (pubmedArticleOrPubmedBookArticle instanceof PubmedBookArticle)
            return new ScipamatoPubmedBookArticle((PubmedBookArticle) pubmedArticleOrPubmedBookArticle);
        throw new IllegalArgumentException("Cannot instantiate ScipamatoArticle from provided object "
                + pubmedArticleOrPubmedBookArticle.toString());
    }
}
