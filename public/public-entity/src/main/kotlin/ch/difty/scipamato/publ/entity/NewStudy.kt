package ch.difty.scipamato.publ.entity

data class NewStudy(
    val sort: Int = 0,
    val number: Long = 0,
    val year: Int? = null,
    val authors: String? = null,
    val headline: String? = null,
    val description: String? = null,
) : PublicDbEntity {

    val reference: String
        get() = "($authors; $year)"

    companion object {
        private const val serialVersionUID = 1L
    }
}
