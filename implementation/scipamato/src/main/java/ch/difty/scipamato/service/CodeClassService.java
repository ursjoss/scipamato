package ch.difty.scipamato.service;

import java.io.Serializable;
import java.util.List;

import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.CodeClass;

/**
 * Service operating with {@link CodeClass}es.
 *
 * @author u.joss
 */
public interface CodeClassService extends Serializable {

    /**
     * Find all {@link CodeClass}es localized in language with the provided languageCode
     *
     * @param languageCode
     * @return a list of {@link Code}s
     */
    List<CodeClass> find(String languageCode);
}
