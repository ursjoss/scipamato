package ch.difty.scipamato.core.web.paper.entry

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.pubmed.AHEAD_OF_PRINT_TAG
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade

/**
 * Ahead of print if first author and title match, location starts with same journal and ends with ahead of print tag.
 */
fun isAheadOfPrint(p: Paper, a: PubmedArticleFacade): Boolean =
    p.firstAuthor == a.firstAuthor &&
        p.title == a.title &&
        p.location?.endsWith(AHEAD_OF_PRINT_TAG) ?: false &&
        p.location?.substringBefore('.') == a.location.substringBefore('.')
