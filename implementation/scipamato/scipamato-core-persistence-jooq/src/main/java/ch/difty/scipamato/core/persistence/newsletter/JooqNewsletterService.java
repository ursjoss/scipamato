package ch.difty.scipamato.core.persistence.newsletter;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.persistence.JooqEntityService;
import ch.difty.scipamato.core.persistence.NewsletterService;
import ch.difty.scipamato.core.persistence.UserRepository;

@Service
class JooqNewsletterService extends JooqEntityService<Integer, Newsletter, NewsletterFilter, NewsletterRepository>
    implements NewsletterService {

    JooqNewsletterService(final NewsletterRepository repo, final UserRepository userRepo) {
        super(repo, userRepo);
    }

    @Override
    public boolean canCreateNewsletterInProgress() {
        return !getRepository()
            .getNewsletterInStatusWorkInProgress()
            .isPresent();
    }

    @Override
    @Transactional(readOnly = false)
    public void mergePaperIntoWipNewsletter(final long paperId) {
        mergePaperIntoWipNewsletter(paperId, null);
    }

    @Override
    @Transactional(readOnly = false)
    public void mergePaperIntoWipNewsletter(final long paperId, final Integer newsletterTopicId) {
        final Optional<Newsletter> opt = getRepository().getNewsletterInStatusWorkInProgress();
        if (opt.isPresent())
            getRepository().mergePaperIntoNewsletter(opt
                .get()
                .getId(), paperId, newsletterTopicId, "en");
    }

    @Override
    @Transactional(readOnly = false)
    public boolean removePaperFromWipNewsletter(final long paperId) {
        final Optional<Newsletter> opt = getRepository().getNewsletterInStatusWorkInProgress();
        if (!opt.isPresent())
            return false;
        else
            return getRepository().removePaperFromNewsletter(opt
                .get()
                .getId(), paperId) > 0;
    }

}
