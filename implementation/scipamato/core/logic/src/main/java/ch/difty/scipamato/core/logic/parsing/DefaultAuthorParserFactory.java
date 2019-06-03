package ch.difty.scipamato.core.logic.parsing;

import ch.difty.scipamato.common.AssertAs;

class DefaultAuthorParserFactory implements AuthorParserFactory {

    private final AuthorParserStrategy authorParserStrategy;

    DefaultAuthorParserFactory(final AuthorParserStrategy authorParserStrategy) {
        this.authorParserStrategy = AssertAs.notNull(authorParserStrategy, "authorParserStrategy");
    }

    @Override
    public AuthorParser createParser(final String authorString) {
        AssertAs.notNull(authorString, "authorString");

        switch (authorParserStrategy) {
        case PUBMED:
        default:
            return new PubmedAuthorParser(authorString);
        }
    }
}
