package ch.difty.scipamato.publ.persistence.code

import ch.difty.scipamato.common.persistence.code.JooqCodeLikeService
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.persistence.api.CodeService
import org.springframework.stereotype.Service

/**
 * jOOQ specific implementation of the [CodeService] interface.
 */
@Service
class JooqCodeService(codeRepository: CodeRepository) :
    JooqCodeLikeService<Code, CodeRepository>(codeRepository), CodeService
