package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.CodeLike

data class Code(
    val codeClassId: Int? = null,
    val code: String? = null,
    val langCode: String? = null,
    val name: String? = null,
    val comment: String? = null,
    val sort: Int = 0,
) : PublicDbEntity, CodeLike {

    val displayValue: String? get() = name

    companion object {
        private const val serialVersionUID = 1L
    }
}
