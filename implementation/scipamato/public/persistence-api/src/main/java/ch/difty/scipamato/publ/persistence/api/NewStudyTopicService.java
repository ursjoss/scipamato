package ch.difty.scipamato.publ.persistence.api;

import java.util.List;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    List<NewStudyTopic> findMostRecentNewStudyTopics(@NotNull String languageCode);

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
    @NotNull
    List<NewStudyTopic> findNewStudyTopicsForNewsletterIssue(@NotNull String issue, @NotNull String languageCode);

    /**
     * Returns a list with the most recent newsletters.
     *
     * @param newsletterCount
     *     the number of newsletters that shall be offered.
     * @param languageCode
     *     the two character languageCode (e.g. 'en')
     * @return a list of {@link Newsletter}s
     */
    @NotNull
    List<Newsletter> findArchivedNewsletters(int newsletterCount, @NotNull String languageCode);

    /**
     * Returns a list with links to display on the new study page.
     *
     * @param languageCode
     *     the two character languageCode (e.g. 'en')
     * @return a list of {@link NewStudyPageLink}s
     */
    @NotNull
    List<NewStudyPageLink> findNewStudyPageLinks(@NotNull String languageCode);
}
