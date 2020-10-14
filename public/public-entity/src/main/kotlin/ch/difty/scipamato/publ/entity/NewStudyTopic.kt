package ch.difty.scipamato.publ.entity

import java.time.LocalDateTime

data class NewStudyTopic(
    val sort: Int = 0,
    val title: String? = null,
    val studies: List<NewStudy> = emptyList(),
    override val created: LocalDateTime? = null,
    override val lastModified: LocalDateTime? = null,
    override val version: Int = 0,
) : PublicDbEntity {
    companion object {
        private const val serialVersionUID = 1L
    }
}
