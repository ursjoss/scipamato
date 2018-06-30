package ch.difty.scipamato.core;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

/**
 * Accessor methods to the Newsletter fields for entities holding an instance.
 *
 * @author u.joss
 */
public interface NewsletterAware {

    /**
     * @return the id of the associated NewsletterTopic
     */
    Integer getNewsletterTopicId();

    /**
     * Sets the relevant fields of the NewsletterTopic.
     *
     * @param newsletterTopic
     *     the topic of the newsletter
     */
    void setNewsletterTopic(NewsletterTopic newsletterTopic);

    /**
     * Sets the newsletter headline
     *
     * @param headline
     *     the headline of the paper in the newsletter
     */
    void setNewsletterHeadline(String headline);

    /**
     * @return the headline of the paper in the newsletter
     */
    String getNewsletterHeadline();

}
