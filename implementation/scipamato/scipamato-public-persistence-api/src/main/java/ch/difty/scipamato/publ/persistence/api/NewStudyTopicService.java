package ch.difty.scipamato.publ.persistence.api;

import java.util.List;

import ch.difty.scipamato.publ.entity.NewStudyTopic;

public interface NewStudyTopicService {

    /**
     * Finds the all {@NewStudyTopic}s associated with the newest newsletter.
     * The Topics are translated into the language specified with the languageCode.
     *
     * @param languageCode
     *     the two character language code (used to translate the topics)
     * @return list of new study topics, will not be null.
     */
    List<NewStudyTopic> findMostRecentNewStudyTopics(String languageCode);

    /**
     * Finds the all {@NewStudyTopic}s associated with the newsletter with the specified issue.
     * The Topics are translated into the language specified with the languageCode.
     *
     * @param issue
     *     the issue of the newsletter (e.g. 2018/06)
     * @param languageCode
     *     the two character language code (used to translate the topics)
     * @return list of new study topics, will not be null
     */
    List<NewStudyTopic> findNewStudyTopicsForNewsletterIssue(String issue, String languageCode);
}
