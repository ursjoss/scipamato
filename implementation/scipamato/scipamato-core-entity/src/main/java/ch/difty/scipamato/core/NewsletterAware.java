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
     */
    void setNewsletterTopic(NewsletterTopic newsletterTopic);

    /**
     * Sets the newsletter headline
     *
     * @param headLine
     *     the headline of the paper in the newsletter
     */
    void setNewsletterHeadLine(String headLine);

    /**
     * @return the headline of the paper in the newsletter
     */
    String getNewsletterHeadLine();

}
