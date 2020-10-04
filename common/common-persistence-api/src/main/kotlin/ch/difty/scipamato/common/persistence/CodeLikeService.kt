package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike

interface CodeLikeService<T : CodeLike> {

    /**
     * Find all codes of type [T] of the specified [CodeClassId] localized in language with the provided languageCode.
     *
     * @param [codeClassId] the id of the code class to find the codes for.
     * @param [languageCode] the language code, e.g. 'en' or 'de'.
     * @return a list of codes of type [T]
     */
    fun findCodesOfClass(codeClassId: CodeClassId, languageCode: String): List<T>
}
