package ch.difty.scipamato.core.persistence;

import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;

/**
 * Service Interface as gateway to manage persisting {@link Newsletter} instances.
 *
 * @author Urs Joss
 */
public interface NewsletterService extends EntityService<Integer, Newsletter, NewsletterFilter> {

    /**
     * Checks if a new newsletter may be created.
     *
     * @return true if a new newsletter may be created, false if it is not allowed.
     */
    boolean canCreateNewsletterInProgress();

    /**
     * Ensures the specified paper is associated with the (single) newsletter that is in status 'work in progress',
     * with given topic.
     * <p>
     * If the paper had previously been assigned to the same newsletter with no or a different topic,
     * it will be reassigned with the new topic instead.
     *
     * @param paperId
     *     the id of the paper to assign to the newsletter
     * @param newsletterTopicId
     *     the id of the topic - or null if no topic is to be assigned.
     */
    void mergePaperIntoWipNewsletter(final long paperId, final Integer newsletterTopicId);

    /**
     * Ensures the specified paper is associated with the (single) nesletter that is in status 'work in progress'.
     * It will be assigned without a topic.
     * <p>
     * If the paper had previously been assigned to the same newsletter with a different topic,
     * it will be reassigned with the new topic instead.
     *
     * @param paperId
     *     the id of the paper to assign to the newsletter
     */
    void mergePaperIntoWipNewsletter(final long paperId);

    /**
     * Removes the paper with the specified id from the current newsletter in status 'work in progress'.
     *
     * @param paperId
     *     the id of the paper to remove from the newsletter
     */
    boolean removePaperFromWipNewsletter(final long paperId);
}
