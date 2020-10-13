package ch.difty.scipamato.core.persistence.newsletter;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.persistence.JooqEntityService;
import ch.difty.scipamato.core.persistence.NewsletterService;
import ch.difty.scipamato.core.persistence.UserRepository;

@Service
class JooqNewsletterService extends JooqEntityService<Integer, Newsletter, NewsletterFilter, NewsletterRepository> implements NewsletterService {

    JooqNewsletterService(@NotNull final NewsletterRepository repo, @NotNull final UserRepository userRepo) {
        super(repo, userRepo);
    }

    @Override
    public boolean canCreateNewsletterInProgress() {
        return getRepository()
            .getNewsletterInStatusWorkInProgress()
            .isEmpty();
    }

    @Override
    @Transactional
    public void mergePaperIntoWipNewsletter(final long paperId) {
        mergePaperIntoWipNewsletter(paperId, null);
    }

    @Override
    @Transactional
    public void mergePaperIntoWipNewsletter(final long paperId, @Nullable final Integer newsletterTopicId) {
        final Optional<Newsletter> opt = getRepository().getNewsletterInStatusWorkInProgress();
        opt.ifPresent(newsletter -> getRepository().mergePaperIntoNewsletter(newsletter.getId(), paperId, newsletterTopicId, "en"));
    }

    @Override
    @Transactional
    public boolean removePaperFromWipNewsletter(final long paperId) {
        final Optional<Newsletter> opt = getRepository().getNewsletterInStatusWorkInProgress();
        return opt
            .filter(newsletter -> getRepository().removePaperFromNewsletter(newsletter.getId(), paperId) > 0)
            .isPresent();
    }

    @Nullable
    @Override
    public Newsletter saveOrUpdate(@NotNull final Newsletter entity) {
        if (entity
            .getPublicationStatus()
            .isInProgress()) {
            final Optional<Newsletter> wipOption = getRepository().getNewsletterInStatusWorkInProgress();
            if (wipOption.isPresent()) {
                final int savedWipId = wipOption
                    .get()
                    .getId();
                if ((entity.getId() == null) || (entity.getId() != savedWipId)) {
                    throw new IllegalArgumentException("newsletter.onlyOneInStatusWipAllowed");
                }
            }
        }
        return super.saveOrUpdate(entity);
    }
}
