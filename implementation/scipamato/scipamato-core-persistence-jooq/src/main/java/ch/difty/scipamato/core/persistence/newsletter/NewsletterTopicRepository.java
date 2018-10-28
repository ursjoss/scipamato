package ch.difty.scipamato.core.persistence.newsletter;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

public interface NewsletterTopicRepository {

    /**
     * Find the localized {@link NewsletterTopic}s
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of newsletterAssociation topics
     */
    List<NewsletterTopic> findAll(String languageCode);

    /**
     * Find the {@link NewsletterTopicDefinition} with the provided id
     *
     * @param id
     *     the id of the record
     * @return the {@link NewsletterTopicDefinition} or null if not found
     */
    NewsletterTopicDefinition findNewsletterTopicDefinitionById(int id);

    /**
     * Finds a page full of {@link NewsletterTopicDefinition} records matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of {@link NewsletterTopicDefinition}s as list
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
     * @return the main language code as string
     */
    String getMainLanguage();

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
    NewsletterTopicDefinition insert(NewsletterTopicDefinition entity);

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
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
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
    NewsletterTopicDefinition delete(Integer id, int version);

    /**
     * Finds the persisted sorted newsletter topics for the newsletter with given id.
     * There may be more topics assigned to the newsletter that have not yet been saved
     * in a sorted order. This method only retrieves those topics that have been assigned
     * an order.
     *
     * @param newsletterId
     *     the id of the newsletter for which to retrieve the sorted topics
     * @return the list of explicitly sorted topics
     */
    List<NewsletterNewsletterTopic> findPersistedSortedNewsletterTopicsForNewsletterWithId(int newsletterId);

    /**
     * Finds the sorted newsletter topics for the newsletter with given id for which the
     * sort order has not been persisted yet..
     *
     * @param newsletterId
     *     the id of the newsletter for which to retrieve the sorted topics
     * @return the list of not yet persisted sorted topics
     */
    List<NewsletterNewsletterTopic> findAllSortedNewsletterTopicsForNewsletterWithId(int newsletterId);

    /**
     * Removes newsletter topics from the sort list that were once defined for the newsletter but are not anymore.
     *
     * @param newsletterId
     *     the id of the newsletter to cleanse from obsolete topics
     */
    void removeObsoleteNewsletterTopicsFromSort(int newsletterId);

    /**
     * Saves the sort order to the database for the newsletter with given id.
     *
     * @param newsletterId
     *     id of the newsletter to store the sorted topics for
     * @param topics
     *     a list of topics to store
     */
    void saveSortedNewsletterTopics(int newsletterId, List<NewsletterNewsletterTopic> topics);
}
