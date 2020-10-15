package ch.difty.scipamato.publ.entity

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
) : PublicDbEntity {
    companion object {
        private const val serialVersionUID = 1L
    }
}
