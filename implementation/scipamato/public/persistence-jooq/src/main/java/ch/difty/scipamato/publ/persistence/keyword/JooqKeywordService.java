package ch.difty.scipamato.publ.persistence.keyword;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.publ.entity.Keyword;
import ch.difty.scipamato.publ.persistence.api.KeywordService;

/**
 * jOOQ specific implementation of the {@link KeywordService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqKeywordService implements KeywordService {

    private final KeywordRepository repo;

    public JooqKeywordService(final KeywordRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Keyword> findKeywords(final String languageCode) {
        return repo.findKeywords(languageCode);
    }
}
