package ch.difty.scipamato.core.persistence.newsletter;

import java.util.*;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;

@Service
class JooqNewsletterTopicService implements NewsletterTopicService {

    private final NewsletterTopicRepository repo;

    public JooqNewsletterTopicService(@NotNull final NewsletterTopicRepository repo) {
        this.repo = repo;
    }

    @NotNull
    public NewsletterTopicRepository getRepo() {
        return repo;
    }

    @NotNull
    @Override
    public List<NewsletterTopic> findAll(@NotNull final String languageCode) {
        return getRepo().findAll(languageCode);
    }

    @NotNull
    @Override
    public List<NewsletterTopicDefinition> findPageOfNewsletterTopicDefinitions(
        @Nullable final NewsletterTopicFilter filter, @NotNull final PaginationContext paginationContext) {
        return getRepo().findPageOfNewsletterTopicDefinitions(filter, paginationContext);
    }

    @NotNull
    @Override
    public Iterator<NewsletterTopicDefinition> findPageOfEntityDefinitions(@Nullable final NewsletterTopicFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return findPageOfNewsletterTopicDefinitions(filter, paginationContext).iterator();
    }

    @Override
    public int countByFilter(@Nullable final NewsletterTopicFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @NotNull
    @Override
    public NewsletterTopicDefinition newUnpersistedNewsletterTopicDefinition() {
        return getRepo().newUnpersistedNewsletterTopicDefinition();
    }

    @Nullable
    @Override
    public NewsletterTopicDefinition insert(@NotNull final NewsletterTopicDefinition entity) {
        return getRepo().insert(entity);
    }

    @Nullable
    @Override
    public NewsletterTopicDefinition update(@NotNull final NewsletterTopicDefinition entity) {
        return getRepo().update(entity);
    }

    @Nullable
    @Override
    public NewsletterTopicDefinition saveOrUpdate(@NotNull final NewsletterTopicDefinition entity) {
        if (entity.getId() == null)
            return getRepo().insert(entity);
        else
            return getRepo().update(entity);
    }

    @Nullable
    @Override
    public NewsletterTopicDefinition delete(final int id, final int version) {
        return getRepo().delete(id, version);
    }

    @NotNull
    @Override
    public List<NewsletterNewsletterTopic> getSortedNewsletterTopicsForNewsletter(final int newsletterId) {
        repo.removeObsoleteNewsletterTopicsFromSort(newsletterId);

        final List<NewsletterNewsletterTopic> results = new ArrayList<>(
            repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId));
        final Set<Integer> persistedTopics = results
            .stream()
            .map(NewsletterNewsletterTopic::getNewsletterTopicId)
            .collect(Collectors.toSet());
        final OptionalInt maxSort = results
            .stream()
            .mapToInt(NewsletterNewsletterTopic::getSort)
            .max();
        int sort = maxSort.orElse(-1) + 1;
        for (final NewsletterNewsletterTopic nt : repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)) {
            if (!persistedTopics.contains(nt.getNewsletterTopicId())) {
                nt.setSort(sort++);
                results.add(nt);
            }
        }
        return results;
    }

    @Override
    public void saveSortedNewsletterTopics(final int newsletterId,
        @NotNull final List<NewsletterNewsletterTopic> topics) {
        if (!topics.isEmpty())
            repo.saveSortedNewsletterTopics(newsletterId, topics);
    }
}
