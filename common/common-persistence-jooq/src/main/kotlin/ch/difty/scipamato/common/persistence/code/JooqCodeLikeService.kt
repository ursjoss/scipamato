package ch.difty.scipamato.common.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike
import ch.difty.scipamato.common.persistence.CodeLikeService

/**
 * Generic implementation of the [CodeLikeService] interface.
 *
 * @param [T] type of the code like classes, extending [CodeLike]
 * @param [R] rype of the repository implementing [CodeLikeRepository]
 */
abstract class JooqCodeLikeService<T : CodeLike, R : CodeLikeRepository<T>>(
    val repo: R,
) : CodeLikeService<T> {

    override fun findCodesOfClass(codeClassId: CodeClassId, languageCode: String): List<T> =
        repo.findCodesOfClass(codeClassId, languageCode)
}
