package ch.difty.scipamato.core.persistence;

import java.util.List;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;

/**
 * Service Interface as gateway to manage persisting {@link NewsletterTopic} instances.
 *
 * @author Urs Joss
 */
public interface NewsletterTopicService {

    /**
     * Find the localized {@link NewsletterTopic}s
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of newsletterAssociation topics
     */
    List<NewsletterTopic> findAll(String languageCode);

    /**
     * Finds a page full of {@link NewsletterTopicDefinition} records matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of entities of type {@code T} as list
     */
    List<NewsletterTopicDefinition> findPageOfNewsletterTopicDefinitions(NewsletterTopicFilter filter,
        PaginationContext paginationContext);

    /**
     * Counts the number of {@link NewsletterTopicDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type NewsletterTopicFilter
     * @return entity count
     */
    int countByFilter(NewsletterTopicFilter filter);

    /**
     * Creates and returns an unpersisted instance of a NewsletterTopicDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    NewsletterTopicDefinition newUnpersistedNewsletterTopicDefinition();
}
