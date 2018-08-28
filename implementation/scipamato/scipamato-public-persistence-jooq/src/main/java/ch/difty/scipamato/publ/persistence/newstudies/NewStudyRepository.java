package ch.difty.scipamato.publ.persistence.newstudies;

import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;

public interface NewStudyRepository {

    /**
     * Find all {@link NewStudyTopic} entries for the newsletter with theprovided id.
     *
     * @param newsletterId
     *     the id of the newsletter
     * @param languageCode
     *     the language code, e.g. 'en' or 'de' - must not be null
     * @return list of topics, including associated new studies.
     */
    List<NewStudyTopic> findNewStudyTopicsForNewsletter(int newsletterId, String languageCode);

    /**
     * Identifies the most recent newsletter (based on issue date) and returns an optional of its id.
     *
     * @return optional of the id of the most recent newsletter
     */
    Optional<Integer> findMostRecentNewsletterId();

    /**
     * Returns an optional of the id of the newsletter with the specified issue.
     *
     * @param issue
     *     the unique issue identifying the newsletter
     * @return optional of the id of the most recent newsletter
     */
    Optional<Integer> findIdOfNewsletterWithIssue(String issue);

    /**
     * Returns a list with the most recent newsletters.
     *
     * @param languageCode
     *     the two character languageCode (e.g. 'en')
     * @return a list of {@link Newsletter}s
     */
    List<Newsletter> findArchivedNewsletters(String languageCode);
}
