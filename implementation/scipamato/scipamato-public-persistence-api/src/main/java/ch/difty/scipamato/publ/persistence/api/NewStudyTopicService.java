package ch.difty.scipamato.publ.persistence.api;

import java.util.List;

import ch.difty.scipamato.publ.entity.NewStudyPageLink;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;

public interface NewStudyTopicService {

    /**
     * Finds the all {@link NewStudyTopic}s associated with the newest newsletter.
     * The Topics are translated into the language specified with the languageCode.
     *
     * @param languageCode
     *     the two character language code (used to translate the topics)
     * @return list of new study topics, will not be null.
     */
    List<NewStudyTopic> findMostRecentNewStudyTopics(String languageCode);

    /**
     * Finds the all {@link NewStudyTopic}s associated with the newsletter with the specified issue.
     * The Topics are translated into the language specified with the languageCode.
     *
     * @param issue
     *     the issue of the newsletter (e.g. 2018/06)
     * @param languageCode
     *     the two character language code (used to translate the topics)
     * @return list of new study topics, will not be null
     */
    List<NewStudyTopic> findNewStudyTopicsForNewsletterIssue(String issue, String languageCode);

    /**
     * Returns a list with the most recent newsletters.
     *
     * @param languageCode
     *     the two character languageCode (e.g. 'en')
     * @return a list of {@link Newsletter}s
     */
    List<Newsletter> findArchivedNewsletters(String languageCode);

    /**
     * Returns a list with links to display on the new study page.
     *
     * @param languageCode
     *     the two character languageCode (e.g. 'en')
     * @return a list of {@link NewStudyPageLink}s
     */
    List<NewStudyPageLink> findNewStudyPageLinks(String languageCode);
}
