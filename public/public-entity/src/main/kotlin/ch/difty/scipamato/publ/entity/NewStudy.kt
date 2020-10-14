package ch.difty.scipamato.publ.entity

import java.time.LocalDateTime

data class NewStudy(
    val sort: Int = 0,
    val number: Long = 0,
    val year: Int? = null,
    val authors: String? = null,
    val headline: String? = null,
    val description: String? = null,
    override val created: LocalDateTime? = null,
    override val lastModified: LocalDateTime? = null,
    override val version: Int = 0,
) : PublicDbEntity {

    val reference: String
        get() = "($authors; $year)"

    companion object {
        private const val serialVersionUID = 1L
    }
}
