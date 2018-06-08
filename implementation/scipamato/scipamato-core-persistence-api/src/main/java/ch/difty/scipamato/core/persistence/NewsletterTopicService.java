package ch.difty.scipamato.core.persistence;

import java.util.List;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

/**
 * Service Interface as gateway to manage persisting {@link NewsletterTopic} instances.
 *
 * @author Urs Joss
 */
public interface NewsletterTopicService {

    /**
     * Find the localized {@link }NewsletterTopic}s
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of newsletterAssociation topics
     */
    List<NewsletterTopic> findAll(String languageCode);
}
