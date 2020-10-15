package ch.difty.scipamato.publ.entity

data class NewStudyPageLink(
    val langCode: String? = null,
    val sort: Int? = null,
    val title: String? = null,
    val url: String? = null,
) : PublicDbEntity {

    companion object {
        private const val serialVersionUID = 1L
    }
}
