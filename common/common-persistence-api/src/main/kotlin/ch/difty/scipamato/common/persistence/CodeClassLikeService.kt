package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.CodeClassLike

/**
 * Generic service interface of code class like classes, i.e. for code classes in core vs public.
 *
 * @param [T] CodeClass like entities
 */
interface CodeClassLikeService<T : CodeClassLike> {

    /**
     * Find all code classes of Type [T] localized in language with the provided languageCode
     *
     * @param [languageCode] e.g. 'en' or 'de'
     * @return a list of code classes of Type [T]
     */
    fun find(languageCode: String): List<T>
}
