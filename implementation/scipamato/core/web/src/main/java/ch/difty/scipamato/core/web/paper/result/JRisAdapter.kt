package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.core.entity.Paper
import com.gmail.gcolaianni5.jris.JRis
import com.gmail.gcolaianni5.jris.RisRecord
import com.gmail.gcolaianni5.jris.Type
import java.io.Serializable

/**
 * Adapter working as a kind of bridge between [Paper]s and the JRis world.
 */
class JRisAdapter(private val dbName: String, private val internalUrl: String?, private val publicUrl: String?) : Serializable {

    fun build(papers: List<Paper>): String {
        return JRis.build(papers.map { p ->

            val (periodical, volume, issue, startPage, endPage) = p.locationComponents()

            RisRecord(
                    type = Type.JOUR,
                    referenceId = p.pmId?.toString(),
                    primaryTitle = p.title,
                    authors = p.formattedAuthors().toMutableList(),
                    publicationYear = "${p.publicationYear}",
                    startPage = startPage,
                    endPage = endPage,
                    periodicalNameUserAbbrevation = periodical,
                    volumeNumber = volume,
                    issue = issue,
                    abstr = p.originalAbstract,
                    pdfLinks = publicUrl?.run { mutableListOf("${this}paper/number/${p.number}") } ?: mutableListOf(),
                    number = p.number.toLong(),
                    websiteLink = internalUrl?.run { "$this${p.pmId}" },
                    miscellaneous2 = p.goals.takeUnless { it?.trim()?.isBlank() ?: false },
                    doi = p.doi.takeUnless { it?.trim()?.isBlank() ?: false },
                    databaseName = dbName
            )
        }.toList())
    }

    private fun Paper.formattedAuthors(): List<String> {
        val formattedAuthors = authors
                .removeSuffix(AUTHOR_SUFFIX)
                .split(AUTHOR_DELIM)
                .map { it.trim() }
                .map(::toRisAuthor)
                .toList()
        check(formattedAuthors.isNotEmpty()) { throw IllegalStateException("paper must have at least one author") }
        return formattedAuthors
    }

    private fun toRisAuthor(scipamatoAuthor: String): String {
        val matchResult = authorRegex.matchEntire(scipamatoAuthor)
        matchResult?.let {
            val groups = matchResult.groupValues.drop(1).map { it.trim() }.filter { it.isNotEmpty() }
            val lastNames = groups.first()
            val firstAndStuff = groups.drop(1).joinToString("$AUTHOR_SUFFIX$AUTHOR_DELIM").plus(AUTHOR_SUFFIX)
            return "$lastNames,$firstAndStuff"
        }
        return "$scipamatoAuthor$AUTHOR_SUFFIX"
    }

    private fun Paper.locationComponents(): LocationComponents {
        val matchResult = locationRegex.matchEntire(location)
        matchResult?.let {
            val groups = it.groupValues
            check(groups.size == 6) { throw IllegalStateException("Unable to parse '$location' - should have been 5 parts") }
            return LocationComponents(
                    periodical = groups[1],
                    volume = groups[2],
                    issue = groups[3].takeUnless { it.trim().isBlank() },
                    startPage = groups[4].run { if (isNotEmpty()) toInt() else null },
                    endPage = groups[5].run { if (isNotEmpty()) toInt() else null }
            )
        }
        return LocationComponents(location)
    }

    private data class LocationComponents(
            val periodical: String,
            val volume: String? = null,
            val issue: String? = null,
            val startPage: Int? = null,
            val endPage: Int? = null
    )

    companion object {
        private const val AUTHOR_DELIM = ","
        private const val AUTHOR_SUFFIX = "."

        private const val RE_W = """\w\u00C0-\u024f"""
        private const val RW_WW = """[${RE_W}-']+""";

        private val authorRegex = """^((?:${RW_WW} ?)+) ([A-Z]+)(?: (Sr))?$""".toRegex()
        private val locationRegex = """^([^.]+)\. \d+; (\d+(?:-\d+)?)(?: \(((?:[\d-])+)\))?: (\d+)?(?:-(\d+))?(?:\.? ?e\d+)?\.$""".toRegex()
    }
}
