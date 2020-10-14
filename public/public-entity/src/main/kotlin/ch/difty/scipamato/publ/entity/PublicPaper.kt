package ch.difty.scipamato.publ.entity

import java.time.LocalDateTime

data class PublicPaper(
    val id: Long? = null,
    val number: Long? = null,
    val pmId: Int? = null,
    val authors: String? = null,
    val authorsAbbreviated: String? = null,
    val title: String? = null,
    val location: String? = null,
    val journal: String? = null,
    val publicationYear: Int? = null,
    val goals: String? = null,
    val methods: String? = null,
    val population: String? = null,
    val result: String? = null,
    val comment: String? = null,
    override val created: LocalDateTime? = null,
    override val lastModified: LocalDateTime? = null,
    override val version: Int = 0,
) : PublicDbEntity {
    companion object {
        private const val serialVersionUID = 1L
    }
}
