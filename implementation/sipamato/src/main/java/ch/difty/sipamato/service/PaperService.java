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
     * Finds a {@link Paper} including the assigned codes for a given id.
     * The languageCode determines the language of the codes/code classes.
     *
     * @param id
     * @param languageCode two digit language code, e.g. 'en'/'de'
     * @return optional paper
     */
    Optional<Paper> findCompleteById(Long id, String languageCode);
}
