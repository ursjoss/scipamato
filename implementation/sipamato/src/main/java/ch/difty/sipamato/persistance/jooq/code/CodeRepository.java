package ch.difty.sipamato.persistance.jooq.code;

import java.util.List;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;

/**
 * Provides access to the localized {@link Code}s.
 *
 * @author u.joss
 */
public interface CodeRepository {

    /**
     * Find all codes of the specified {@link CodeClassId} localized in language with the provided languageCode
     *
     * @param codeClassId
     * @param languageCode
     * @return a list of {@link Code}s
     */
    List<Code> findCodesOfClass(CodeClassId codeClassId, String languageCode);

}
