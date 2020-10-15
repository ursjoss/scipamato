package ch.difty.scipamato.publ.persistence.codeclass

import ch.difty.scipamato.common.persistence.codeclass.JooqCodeClassLikeService
import ch.difty.scipamato.publ.entity.CodeClass
import ch.difty.scipamato.publ.persistence.api.CodeClassService
import org.springframework.stereotype.Service

/**
 * jOOQ specific implementation of the [CodeClassService] interface.
 */
@Service
class JooqCodeClassService(
    codeClassRepository: CodeClassRepository,
) : JooqCodeClassLikeService<CodeClass, CodeClassRepository>(codeClassRepository), CodeClassService
