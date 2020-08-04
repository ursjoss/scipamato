package ch.difty.scipamato.core.web.paper.entry

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.pubmed.AHEAD_OF_PRINT_TAG
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade

/**
 * Ahead of print if first author and title match, location starts with same journal and ends with ahead of print tag.
 *
 * Case is ignored when comparing.
 */
fun isAheadOfPrint(p: Paper, a: PubmedArticleFacade): Boolean =
    p.firstAuthor.equals(a.firstAuthor, ignoreCase = true) &&
        p.title.equals(a.title, ignoreCase = true) &&
        p.location?.endsWith(AHEAD_OF_PRINT_TAG, ignoreCase = true) ?: false &&
        p.location?.substringBefore('.').equals(a.location.substringBefore('.'), ignoreCase = true)
