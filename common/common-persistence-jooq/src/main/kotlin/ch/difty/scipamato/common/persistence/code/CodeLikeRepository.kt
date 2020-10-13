package ch.difty.scipamato.common.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike

/**
 * Generic interface for code like entities of type [T].
 *
 * @param [T] type of the codes, extending [CodeLike]
 */
interface CodeLikeRepository<T : CodeLike> {

    /**
     * Find all codes of type [T] of the specified [CodeClassId]
     * localized in language with the provided languageCode
     *
     * @param [codeClassId] the id of the code class for which to find the codes
     * @param [languageCode] the language code, e.g. 'en' or 'de'
     * @return a list of codes implementing [T]
     */
    fun findCodesOfClass(codeClassId: CodeClassId, languageCode: String): List<T>
}
