package ch.difty.sipamato.service;

import java.util.Optional;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service methods.
 * 
 * @author u.joss
 *
 */
public interface PaperService extends EntityService<Paper, PaperFilter> {

    /**
     * Finds a {@link Paper} including the populated children as optional for a given id.
     * The languageCode determines the language.
     *
     * @param id
     * @param languageCode two digit language code, e.g. 'en'/'de'
     * @return optional paper
     */
    Optional<Paper> findWithChildrenById(Long id, String languageCode);
}
