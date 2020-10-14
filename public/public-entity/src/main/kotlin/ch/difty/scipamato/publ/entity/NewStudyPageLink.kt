package ch.difty.scipamato.publ.entity

import java.time.LocalDateTime

data class NewStudyPageLink(
    val langCode: String? = null,
    val sort: Int? = null,
    val title: String? = null,
    val url: String? = null,
    override val created: LocalDateTime? = null,
    override val lastModified: LocalDateTime? = null,
    override val version: Int = 0,
) : PublicDbEntity {

    companion object {
        private const val serialVersionUID = 1L
    }
}
