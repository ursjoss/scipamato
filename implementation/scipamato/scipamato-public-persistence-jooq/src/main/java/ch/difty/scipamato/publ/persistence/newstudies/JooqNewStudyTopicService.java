package ch.difty.scipamato.publ.persistence.newstudies;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.AssertAs;
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

    public JooqNewStudyTopicService(final NewStudyRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<NewStudyTopic> findMostRecentNewStudyTopics(final String languageCode) {
        AssertAs.notNull(languageCode, "languageCode");
        return repo
            .findMostRecentNewsletterId()
            .map(id -> repo.findNewStudyTopicsForNewsletter(id, languageCode))
            .orElse(Collections.emptyList());
    }

    @Override
    public List<NewStudyTopic> findNewStudyTopicsForNewsletterIssue(final String issue, final String languageCode) {
        AssertAs.notNull(issue, "issue");
        AssertAs.notNull(languageCode, "languageCode");
        return repo
            .findIdOfNewsletterWithIssue(issue)
            .map(id -> repo.findNewStudyTopicsForNewsletter(id, languageCode))
            .orElse(Collections.emptyList());
    }

    @Override
    public List<Newsletter> findArchivedNewsletters(final String languageCode) {
        return repo.findArchivedNewsletters(languageCode);
    }

}
