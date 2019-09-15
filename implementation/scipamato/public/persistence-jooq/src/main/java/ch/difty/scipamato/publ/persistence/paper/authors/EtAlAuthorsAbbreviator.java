package ch.difty.scipamato.publ.persistence.paper.authors;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.persistence.paper.AuthorsAbbreviator;

/**
 * Implementation of {@link AuthorsAbbreviator} truncating author strings longer
 * than 80 characters and adding ' et al.'.
 *
 * @author Urs Joss
 */
@Component
class EtAlAuthorsAbbreviator implements AuthorsAbbreviator {

    private static final String ET_AL    = " et al.";
    private static final String ELLIPSIS = "...";

    private final int authorsAbbreviatedMaxLength;
    private final int authorsAbbreviatedCutOff;
    private final int authorsTruncatedCutOff;

    /**
     * Instantiates the abbreviator with a specific maximum length for the
     * abbreviated author string.
     *
     * @param properties
     *     the public scipamato properties with the
     *     authorsAbbreviatedMaxLength setting.
     */
    EtAlAuthorsAbbreviator(final ApplicationPublicProperties properties) {
        AssertAs.INSTANCE.notNull(properties, "properties");
        this.authorsAbbreviatedMaxLength = properties.getAuthorsAbbreviatedMaxLength();
        this.authorsAbbreviatedCutOff = authorsAbbreviatedMaxLength - ET_AL.length();
        this.authorsTruncatedCutOff = authorsAbbreviatedMaxLength - ELLIPSIS.length();
    }

    @Override
    public String abbreviate(final String authors) {
        if (authors == null)
            return "";
        if (noNeedToAbbreviate(authors))
            return authors;

        final String authorsHead = authors.substring(0, authorsAbbreviatedCutOff);
        if (oneOrMoreEntireAuthorsTruncated(authors)) {
            return authorsHead + ET_AL;
        } else {
            return fixAuthorsHead(authors, authorsHead.lastIndexOf(','));
        }
    }

    private boolean noNeedToAbbreviate(final String authors) {
        return authors.length() <= authorsAbbreviatedMaxLength || authorsAbbreviatedMaxLength == 0;
    }

    /**
     * The character after the truncated part is a ',' -> the truncated string is a
     * full author. No need to skip the partially cut-off last author.
     */
    private boolean oneOrMoreEntireAuthorsTruncated(final String authors) {
        return authors.charAt(authorsAbbreviatedCutOff) == ',';
    }

    /**
     * If there are multiple authors, skip last partially cut off author and add '
     * et al.' If however only one very long author is present (no comma), truncate
     * its name and add ellipsis.
     */
    private String fixAuthorsHead(final String authors, final int lastComma) {
        if (lastComma > 0)
            return authors.substring(0, lastComma) + ET_AL;
        else
            return authors.substring(0, authorsTruncatedCutOff) + ELLIPSIS;
    }
}
