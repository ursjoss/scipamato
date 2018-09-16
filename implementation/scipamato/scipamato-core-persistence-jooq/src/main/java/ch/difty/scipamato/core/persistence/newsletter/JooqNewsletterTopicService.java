package ch.difty.scipamato.core.persistence.newsletter;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.persistence.UserRepository;

@Service
@SuppressWarnings("WeakerAccess")
class JooqNewsletterTopicService implements NewsletterTopicService {

    private final NewsletterTopicRepository repo;
    private final UserRepository            userRepo;

    public JooqNewsletterTopicService(final NewsletterTopicRepository repo, final UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public NewsletterTopicRepository getRepo() {
        return repo;
    }

    public UserRepository getUserRepo() {
        return userRepo;
    }

    @Override
    public List<NewsletterTopic> findAll(final String languageCode) {
        return getRepo().findAll(languageCode);
    }

    @Override
    public List<NewsletterTopicDefinition> findPageOfNewsletterTopicDefinitions(final NewsletterTopicFilter filter,
        final PaginationContext paginationContext) {
        return getRepo().findPageOfNewsletterTopicDefinitions(filter, paginationContext);
    }

    @Override
    public int countByFilter(final NewsletterTopicFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @Override
    public NewsletterTopicDefinition newUnpersistedNewsletterTopicDefinition() {
        return getRepo().newUnpersistedNewsletterTopicDefinition();
    }

    @Override
    public NewsletterTopicDefinition insert(final NewsletterTopicDefinition entity) {
        return getRepo().insert(entity);
    }

    @Override
    public NewsletterTopicDefinition update(final NewsletterTopicDefinition entity) {
        return getRepo().update(entity);
    }

    @Override
    public NewsletterTopicDefinition saveOrUpdate(final NewsletterTopicDefinition entity) {
        AssertAs.notNull(entity, "entity");
        if (entity.getId() == null)
            return getRepo().insert(entity);
        else
            return getRepo().update(entity);
    }

    @Override
    public NewsletterTopicDefinition delete(final int id, final int version) {
        return getRepo().delete(id, version);
    }

    @Override
    public List<NewsletterNewsletterTopic> getSortedNewsletterTopicsForNewsletter(final int newsletterId) {
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
    public void saveSortedNewsletterTopics(final int newsletterId, final List<NewsletterNewsletterTopic> topics) {
        if (CollectionUtils.isNotEmpty(topics))
            repo.saveSortedNewsletterTopics(newsletterId, topics);
    }

}
