package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.CodeClassLike

data class CodeClass(
    val codeClassId: Int? = null,
    val langCode: String? = null,
    val name: String? = null,
    val description: String? = null,
) : PublicDbEntity, CodeClassLike {
    companion object {
        private const val serialVersionUID = 1L
    }
}
