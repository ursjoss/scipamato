package ch.difty.scipamato.core.persistence;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
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

    /**
     * Persist an unpersisted {@link NewsletterTopicDefinition} including its associated
     * NewsletterTopicTranslations to the database.
     *
     * @param entity
     *     - must not be null, id must be null
     * @return the persisted entity, including the generated id and default values - or
     *     {@code null} if it can't be added. Only the version field is populated out of
     *     all the audit fields.
     * @throws NullArgumentException
     *     if the entity is null.
     */
    NewsletterTopicDefinition add(NewsletterTopicDefinition entity);

    /**
     * Update an already persisted {@link NewsletterTopicDefinition} including its associated
     * NewsletterTopicTranslations in the database. Any associated translation will be updated
     * if it exists in the database (id != null) or will be added if it is new (id == null).
     *
     * @param entity
     *     - must not be null, id must not be null.
     * @return the updated persisted entity, including the generated id and default values - or
     *     {@code null} if it can't be added. Only the version field is populated out of
     *     all the audit fields.
     * @throws NullArgumentException
     *     if the entity is null.
     */
    NewsletterTopicDefinition update(NewsletterTopicDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id
     *     the database id - must not be null
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws NullArgumentException
     *     if the id is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    NewsletterTopicDefinition delete(int id, int version);

}
