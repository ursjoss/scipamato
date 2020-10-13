package ch.difty.scipamato.publ.entity.filter

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.entity.PopulationCode
import ch.difty.scipamato.publ.entity.StudyDesignCode

data class PublicPaperFilter(
    val number: Long? = null,
    val authorMask: String? = null,
    val titleMask: String? = null,
    val methodsMask: String? = null,
    val publicationYearFrom: Int? = null,
    val publicationYearUntil: Int? = null,
    val populationCodes: List<PopulationCode>? = null,
    val studyDesignCodes: List<StudyDesignCode>? = null,
    val codesOfClass1: List<Code>? = null,
    val codesOfClass2: List<Code>? = null,
    val codesOfClass3: List<Code>? = null,
    val codesOfClass4: List<Code>? = null,
    val codesOfClass5: List<Code>? = null,
    val codesOfClass6: List<Code>? = null,
    val codesOfClass7: List<Code>? = null,
    val codesOfClass8: List<Code>? = null,
    val keywords: List<Keyword>? = null,
) : ScipamatoFilter {
    companion object {
        private const val serialVersionUID = 1L
    }
}
