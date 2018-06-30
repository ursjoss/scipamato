package ch.difty.scipamato.core.persistence.newsletter;

import java.util.List;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

public interface NewsletterTopicRepository {

    /**
     * Find the localized {@link NewsletterTopic}s
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of newsletterAssociation topics
     */
    List<NewsletterTopic> findAll(String languageCode);

}
