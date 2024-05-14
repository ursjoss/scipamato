package ch.difty.scipamato.publ.persistence.newstudies

import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService
import org.springframework.stereotype.Service

/**
 * Currently only stubbed. Implement from table
 */
@Service
class JooqNewStudyTopicService(private val repo: NewStudyRepository) : NewStudyTopicService {

    override fun findMostRecentNewStudyTopics(languageCode: String): List<NewStudyTopic> =
        repo.findMostRecentNewsletterId()?.let {
            repo.findNewStudyTopicsForNewsletter(it, languageCode)
        } ?: emptyList()

    override fun findNewStudyTopicsForNewsletterIssue(
        issue: String,
        languageCode: String,
    ): List<NewStudyTopic> = repo.findIdOfNewsletterWithIssue(issue)?.let {
        repo.findNewStudyTopicsForNewsletter(it, languageCode)
    } ?: emptyList()

    override fun findArchivedNewsletters(newsletterCount: Int, languageCode: String): List<Newsletter> =
        repo.findArchivedNewsletters(newsletterCount, languageCode)

    override fun findNewStudyPageLinks(languageCode: String): List<NewStudyPageLink> =
        repo.findNewStudyPageLinks(languageCode)
}
