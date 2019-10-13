package ch.difty.scipamato.core.persistence.newsletter;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.persistence.EntityRepository;

public interface NewsletterRepository extends EntityRepository<Newsletter, Integer, NewsletterFilter> {

    /**
     * Returns the newsletter that is in status 'work in progress'
     *
     * @return the newsletter as optional - or an empty optional if none has the status.
     */
    @NotNull
    Optional<Newsletter> getNewsletterInStatusWorkInProgress();

    /**
     * Assigns the paper to the newsletter with the given topic by either adding a new
     * association or updating an existing one.
     *
     * @param newsletterId
     *     the id of the newsletter to assign the paper to
     * @param paperId
     *     the id of the paper to assign
     * @param newsletterTopicId
     *     the id of the newsletter topic, may be null.
     * @param languageCode
     *     the 2 character languageCode, e.g. 'en' or 'de'
     * @return the count of records that were inserted or updated (expected to be 1 all the time)
     */
    @NotNull
    Optional<Paper.NewsletterLink> mergePaperIntoNewsletter(int newsletterId, long paperId,
        @Nullable Integer newsletterTopicId, @NotNull String languageCode);

    /**
     * Removes the paper with the specified id from the newsletter with the given id.
     *
     * @param newsletterId
     *     the id of the newsletter to assign the paper to
     * @param paperId
     *     the id of the paper to assign
     * @return the count of records that were removed
     */
    int removePaperFromNewsletter(int newsletterId, long paperId);
}
