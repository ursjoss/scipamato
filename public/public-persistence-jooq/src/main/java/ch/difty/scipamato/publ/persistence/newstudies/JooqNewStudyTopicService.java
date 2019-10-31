package ch.difty.scipamato.publ.persistence.newstudies;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.publ.entity.NewStudyPageLink;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService;

/**
 * Currently only stubbed. Implement from table
 *
 * @author Urs Joss
 */
@Service
public class JooqNewStudyTopicService implements NewStudyTopicService {

    private final NewStudyRepository repo;

    public JooqNewStudyTopicService(@NotNull final NewStudyRepository repo) {
        this.repo = repo;
    }

    @NotNull
    @Override
    public List<NewStudyTopic> findMostRecentNewStudyTopics(@NotNull final String languageCode) {
        return repo
            .findMostRecentNewsletterId()
            .map(id -> repo.findNewStudyTopicsForNewsletter(id, languageCode))
            .orElse(Collections.emptyList());
    }

    @NotNull
    @Override
    public List<NewStudyTopic> findNewStudyTopicsForNewsletterIssue(@NotNull final String issue,
        @NotNull final String languageCode) {
        return repo
            .findIdOfNewsletterWithIssue(issue)
            .map(id -> repo.findNewStudyTopicsForNewsletter(id, languageCode))
            .orElse(Collections.emptyList());
    }

    @NotNull
    @Override
    public List<Newsletter> findArchivedNewsletters(final int newsletterCount, @NotNull final String languageCode) {
        return repo.findArchivedNewsletters(newsletterCount, languageCode);
    }

    @NotNull
    @Override
    public List<NewStudyPageLink> findNewStudyPageLinks(@NotNull final String languageCode) {
        return repo.findNewStudyPageLinks(languageCode);
    }
}
