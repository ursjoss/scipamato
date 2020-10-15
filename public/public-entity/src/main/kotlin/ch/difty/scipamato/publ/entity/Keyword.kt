package ch.difty.scipamato.publ.entity

data class Keyword(
    val id: Int = 0,
    val keywordId: Int = 0,
    val langCode: String? = null,
    val name: String? = null,
    val searchOverride: String? = null,
) : PublicDbEntity {

    val displayValue: String? get() = name

    companion object {
        private const val serialVersionUID = 1L
    }
}
