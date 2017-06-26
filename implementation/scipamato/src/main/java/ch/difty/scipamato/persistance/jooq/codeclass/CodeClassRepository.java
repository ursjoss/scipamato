package ch.difty.scipamato.persistance.jooq.codeclass;

import java.util.List;

import ch.difty.scipamato.entity.CodeClass;

/**
 * Provides access to the localized {@link CodeClass}es.
 *
 * @author u.joss
 */
public interface CodeClassRepository {

    /**
     * Find the localized {@link CodeClass}es
     *
     * @param languageCode
     * @return a list of {@link CodeClass}es
     */
    List<CodeClass> find(String languageCode);

}
