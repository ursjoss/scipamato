package ch.difty.scipamato.common.persistence.codeclass

import ch.difty.scipamato.common.entity.CodeClassLike

/**
 * Generic interface for code class like entities of type [T].
 *
 * @param [T] type of the code classes, extending [CodeClassLike]
 */
interface CodeClassLikeRepository<T : CodeClassLike?> {

    /**
     * Find the localized CodeClasses of type [T]
     *
     * @param [languageCode] the language code, e.g. 'en' or 'de'
     * @return a list of code classes implementing [CodeClassLike]
     */
    fun find(languageCode: String): List<T>
}
