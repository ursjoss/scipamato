package ch.difty.scipamato.publ.entity

import java.time.LocalDateTime

data class Keyword(
    val id: Int = 0,
    val keywordId: Int = 0,
    val langCode: String? = null,
    val name: String? = null,
    val searchOverride: String? = null,
    override val created: LocalDateTime? = null,
    override val lastModified: LocalDateTime? = null,
    override val version: Int = 0,
) : PublicDbEntity {

    val displayValue: String? get() = name

    companion object {
        private const val serialVersionUID = 1L
    }
}
