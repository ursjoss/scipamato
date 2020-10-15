package ch.difty.scipamato.publ.entity

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Newsletter(
    val id: Int = 0,
    val issue: String? = null,
    val issueDate: LocalDate? = null,
) : PublicDbEntity {

    fun getMonthName(langCode: String): String? =
        issueDate?.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag(langCode)))

    companion object {
        private const val serialVersionUID = 1L
    }
}
