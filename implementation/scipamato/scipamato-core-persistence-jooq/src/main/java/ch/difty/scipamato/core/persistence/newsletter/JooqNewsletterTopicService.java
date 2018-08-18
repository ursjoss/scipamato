package ch.difty.scipamato.core.persistence.newsletter;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.persistence.UserRepository;

@Service
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
    public NewsletterTopicDefinition add(final NewsletterTopicDefinition entity) {
        return getRepo().add(entity);
    }

    @Override
    public NewsletterTopicDefinition update(final NewsletterTopicDefinition entity) {
        return getRepo().update(entity);
    }

    @Override
    public NewsletterTopicDefinition delete(final int id, final int version) {
        return getRepo().delete(id, version);
    }
}
