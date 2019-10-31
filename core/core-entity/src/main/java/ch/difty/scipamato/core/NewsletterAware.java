package ch.difty.scipamato.core;

import org.jetbrains.annotations.Nullable;

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
    @Nullable
    Integer getNewsletterTopicId();

    /**
     * Sets the relevant fields of the NewsletterTopic.
     *
     * @param newsletterTopic
     *     the topic of the newsletter
     */
    void setNewsletterTopic(@Nullable NewsletterTopic newsletterTopic);

    /**
     * Sets the newsletter headline
     *
     * @param headline
     *     the headline of the paper in the newsletter
     */
    void setNewsletterHeadline(@Nullable String headline);

    /**
     * @return the headline of the paper in the newsletter
     */
    @Nullable
    String getNewsletterHeadline();

    /**
     * Sets the newsletter issue
     *
     * @param issue
     *     the issue of the paper in the newsletter
     */
    void setNewsletterIssue(@Nullable String issue);

    /**
     * @return the issue of the newsletter assigned to the paper
     */
    @Nullable
    String getNewsletterIssue();
}
