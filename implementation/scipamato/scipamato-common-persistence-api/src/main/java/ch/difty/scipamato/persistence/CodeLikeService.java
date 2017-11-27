package ch.difty.scipamato.persistence;

import java.io.Serializable;
import java.util.List;

import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.CodeLike;

public interface CodeLikeService<T extends CodeLike> extends Serializable {

    /**
     * Find all codes of type {@code T} of the specified {@link CodeClassId} localized in language with the provided languageCode
     *
     * @param codeClassId
     * @param languageCode
     * @return a list of codes
     */
    List<T> findCodesOfClass(CodeClassId codeClassId, String languageCode);

}
