package ch.difty.scipamato.core.persistence;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;

/**
 * Service Interface as gateway to manage persisting {@link NewsletterTopic} instances.
 *
 * @author Urs Joss
 */
public interface NewsletterTopicService
    extends DefinitionProviderService<NewsletterTopicDefinition, NewsletterTopicFilter> {

    /**
     * Find the localized {@link NewsletterTopic}s
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of newsletterAssociation topics
     */
    @NotNull
    List<NewsletterTopic> findAll(@NotNull String languageCode);

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
    @NotNull
    List<NewsletterTopicDefinition> findPageOfNewsletterTopicDefinitions(@Nullable NewsletterTopicFilter filter,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of {@link NewsletterTopicDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type NewsletterTopicFilter
     * @return entity count
     */
    int countByFilter(@Nullable NewsletterTopicFilter filter);

    /**
     * Creates and returns an unpersisted instance of a NewsletterTopicDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    @NotNull
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
     */
    @Nullable
    NewsletterTopicDefinition insert(@NotNull NewsletterTopicDefinition entity);

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
     */
    @Nullable
    NewsletterTopicDefinition update(@NotNull NewsletterTopicDefinition entity);

    /**
     * Persists the provided entity. Depending on the absence or presence of an ID, the
     * entity will be added to the database or updated.
     *
     * @param entity
     *     the {@link NewsletterTopicDefinition} to be persisted
     * @return the persisted entity.
     */
    @Nullable
    NewsletterTopicDefinition saveOrUpdate(@NotNull NewsletterTopicDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id
     *     the database id - must not be null
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @Nullable
    NewsletterTopicDefinition delete(int id, int version);

    /**
     * Fetches all newsletter topics associated with the current newsletter and places them
     * into an order - either as explicitly saved in db or sorted alphabetically by title.
     *
     * @param newsletterId
     *     the id of the newsletter
     * @return a sorted list of {@link NewsletterNewsletterTopic}s.
     */
    @NotNull
    List<NewsletterNewsletterTopic> getSortedNewsletterTopicsForNewsletter(int newsletterId);

    /**
     * Saves the sort order to the database for the newsletter with given id.
     *
     * @param newsletterId
     *     id of the newsletter to store the sorted topics for
     * @param topics
     *     a list of topics to store
     */
    void saveSortedNewsletterTopics(int newsletterId, @NotNull List<NewsletterNewsletterTopic> topics);
}
