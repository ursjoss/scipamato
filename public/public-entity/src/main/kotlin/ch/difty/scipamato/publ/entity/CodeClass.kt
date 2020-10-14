package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.CodeClassLike
import java.time.LocalDateTime

data class CodeClass(
    val codeClassId: Int? = null,
    val langCode: String? = null,
    val name: String? = null,
    val description: String? = null,
    override val created: LocalDateTime? = null,
    override val lastModified: LocalDateTime? = null,
    override val version: Int = 0,
) : PublicDbEntity, CodeClassLike {
    companion object {
        private const val serialVersionUID = 1L
    }
}
