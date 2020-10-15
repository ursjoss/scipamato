package ch.difty.scipamato.publ.persistence.api

import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter

interface NewStudyTopicService {

    /**
     * Finds the all [NewStudyTopic]s associated with the newest newsletter.
     * The Topics are translated into the language specified with the provided [languageCode].
     */
    fun findMostRecentNewStudyTopics(languageCode: String): List<NewStudyTopic>

    /**
     * Finds the all [NewStudyTopic]s associated with the newsletter with the specified [issue] (e.g. 2018/06).
     * The Topics are translated into the language specified with the [languageCode].
     */
    fun findNewStudyTopicsForNewsletterIssue(issue: String, languageCode: String): List<NewStudyTopic>

    /**
     * Returns a list with the most recent newsletters.
     *
     * @param newsletterCount the number of newsletters that shall be offered.
     * @param languageCode the two character languageCode (e.g. 'en')
     */
    fun findArchivedNewsletters(newsletterCount: Int, languageCode: String): List<Newsletter>

    /**
     * Returns a list with links to display on the new study page, according to the provided [languageCode].
     */
    fun findNewStudyPageLinks(languageCode: String): List<NewStudyPageLink>
}
