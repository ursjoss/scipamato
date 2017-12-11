package ch.difty.scipamato.common.persistence.code;

import java.util.List;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;

/**
 * Generic interface for code like entities of type {@code T}.
 *
 * @author u.joss
 *
 * @param <T> codes of type {@code T}, extending {@link CodeLike}
 */
public interface CodeLikeRepository<T extends CodeLike> {

    /**
     * Find all codes of type {@code T} of the specified {@link CodeClassId} localized in language with the provided languageCode
     *
     * @param codeClassId
     * @param languageCode
     * @return a list of codes implementing {@code T}
     */
    List<T> findCodesOfClass(CodeClassId codeClassId, String languageCode);
}
