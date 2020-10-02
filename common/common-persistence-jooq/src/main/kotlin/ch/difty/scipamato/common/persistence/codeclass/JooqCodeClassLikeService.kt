package ch.difty.scipamato.common.persistence.codeclass

import ch.difty.scipamato.common.entity.CodeClassLike
import ch.difty.scipamato.common.persistence.CodeClassLikeService

/**
 * Generic implementation of the [CodeClassLikeRepository]. Can be used
 * for concrete implementations in the core or public modules for code classes.
 *
 * @param [T] type of the code classes, concrete implementations of [CodeClassLike]
 * @param [R] type of the code class repository, concrete implementations of
 * [CodeClassLikeRepository]
 * @author u.joss
</R></T> */
abstract class JooqCodeClassLikeService<T : CodeClassLike, R : CodeClassLikeRepository<T>>(
    val repo: R
) : CodeClassLikeService<T> {
    override fun find(languageCode: String): List<T> = repo.find(languageCode)
}
