package ch.difty.sipamato.persistance.jooq.codeclass;

import java.util.List;

import ch.difty.sipamato.entity.CodeClass;

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
