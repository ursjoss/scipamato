package ch.difty.scipamato.core.persistence.newsletter;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
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

}
