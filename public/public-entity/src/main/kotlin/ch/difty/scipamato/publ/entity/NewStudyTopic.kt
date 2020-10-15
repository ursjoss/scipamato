package ch.difty.scipamato.publ.entity

data class NewStudyTopic(
    val sort: Int = 0,
    val title: String? = null,
    val studies: List<NewStudy> = emptyList(),
) : PublicDbEntity {
    companion object {
        private const val serialVersionUID = 1L
    }
}
