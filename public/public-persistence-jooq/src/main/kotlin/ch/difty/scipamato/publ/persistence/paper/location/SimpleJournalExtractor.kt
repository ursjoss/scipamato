package ch.difty.scipamato.publ.persistence.paper.location

import ch.difty.scipamato.publ.persistence.paper.JournalExtractor
import org.springframework.stereotype.Component

/**
 * Simple implementation of [JournalExtractor].
 *
 *
 *  1. if the location contains a period before the end of the string, we're
 * taking everything up to and excluding the period.
 *  1. otherwise if the location contains a '(' (actually a ' ('), we're using
 * everything up to (and excluding) the ' ('.
 *  1. otherwise use the entire location string
 *
 * @author Urs Joss
 */
@Component
class SimpleJournalExtractor : JournalExtractor {

    override fun extractJournal(location: String?): String {
        val firstPeriodIndex = location?.indexOf('.') ?: return ""
        return if (hasIntermediatePeriod(firstPeriodIndex, location.length))
            location.substring(0, firstPeriodIndex)
        else location.substringBefore(" (")
    }

    private fun hasIntermediatePeriod(firstPeriodIndex: Int, length: Int): Boolean =
        firstPeriodIndex > 0 && firstPeriodIndex < length - 1
}
