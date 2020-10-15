package ch.difty.scipamato.publ.persistence.newstudies

import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter

interface NewStudyRepository {

    /**
     * Find all [NewStudyTopic] entries for the newsletter with the provided [newsletterId],
     * translated according to the provided [languageCode].
     */
    fun findNewStudyTopicsForNewsletter(newsletterId: Int, languageCode: String): List<NewStudyTopic>

    /**
     * Identifies the most recent newsletter (based on issue date) and returns an its id - or null.
     */
    fun findMostRecentNewsletterId(): Int?

    /**
     * Returns an optional of the id of the newsletter with the specified [issue].
     */
    fun findIdOfNewsletterWithIssue(issue: String): Int?

    /**
     * Returns a list with the [newsletterCount] items of the most recent newsletters,
     * according to the provided [languageCode].
     */
    fun findArchivedNewsletters(newsletterCount: Int, languageCode: String): List<Newsletter>

    /**
     * Returns a list with links to display on the new study page in the provided [languageCode]
     */
    fun findNewStudyPageLinks(languageCode: String): List<NewStudyPageLink>
}
