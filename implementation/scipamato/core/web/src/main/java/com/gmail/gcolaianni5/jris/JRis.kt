package com.gmail.gcolaianni5.jris

import java.io.*
import kotlin.reflect.KClass

/** End record code */
private const val ER = "ER"

/** PDF link code */
private const val L1 = "L1"

/** Abstract code */
private const val AB = "AB"

private const val TAG_SEPARATOR = "  - "

private const val TAG_LENGTH = ER.length
private const val START_INDEX_VALUE = TAG_LENGTH + TAG_SEPARATOR.length

private val LINE_SEPARATOR = System.getProperty("line.separator")

private data class TagContext<T>(
        val tag: String,
        val kClass: KClass<*>,
        val getFrom: (record: RisRecord) -> T?,
        val setInto: (RisRecord, Any?) -> Unit,
        val sort: Int? = null
)

class JRisException(message: String) : Throwable(message)

/**
 * `RIS` format parsers and builder.
 * This is the core class of the module. It is capable to parse a `RIS` file from different source types
 * and to build a well formatted `RIS` format.
 *
 * @author Gianluca Colaianni -- g.colaianni5@gmail.com
 * @version 1.0
 * @since 22 apr 2017
 */
object JRis {

    private val risTagContexts: Set<TagContext<*>> = setOf(
            TagContext("TY", Type::class, { r: RisRecord -> r.type }, { r, v -> r.type = v as Type }, 0),
            TagContext<List<String>>("A1", List::class, { r: RisRecord -> r.firstAuthors }, { r, v -> r.firstAuthors.add(v as String) }),
            TagContext<List<String>>("A2", String::class, { r: RisRecord -> r.secondaryAuthors }, { r, v -> r.secondaryAuthors.add(v as String) }),
            TagContext<List<String>>("A3", String::class, { r: RisRecord -> r.tertiaryAuthors }, { r, v -> r.tertiaryAuthors.add(v as String) }),
            TagContext<List<String>>("A4", String::class, { r: RisRecord -> r.subsidiaryAuthors }, { r, v -> r.subsidiaryAuthors.add(v as String) }),
            TagContext("AA", String::class, { r: RisRecord -> r.authorAddress }, { r, v -> r.authorAddress = v as String? }),
            TagContext("AB", String::class, { r: RisRecord -> r.abstr }, { r, v -> r.abstr = v as String }, 14),
            TagContext("AN", String::class, { r: RisRecord -> r.accessionNumber }, { r, v -> r.accessionNumber = v as String? }),
            TagContext<List<String>>("AU", String::class, { r: RisRecord -> r.authors }, { r, v -> r.authors.add(v as String) }, 1),
            TagContext("AV", String::class, { r: RisRecord -> r.archivesLocation }, { r, v -> r.archivesLocation = v as String? }),
            TagContext("BT", String::class, { r: RisRecord -> r.bt }, { r, v -> r.bt = v as String? }),
            TagContext("C1", String::class, { r: RisRecord -> r.custom1 }, { r, v -> r.custom1 = v as String? }),
            TagContext("C2", String::class, { r: RisRecord -> r.custom2 }, { r, v -> r.custom2 = v as String? }),
            TagContext("C3", String::class, { r: RisRecord -> r.custom3 }, { r, v -> r.custom3 = v as String? }),
            TagContext("C4", String::class, { r: RisRecord -> r.custom4 }, { r, v -> r.custom4 = v as String? }),
            TagContext("C5", String::class, { r: RisRecord -> r.custom5 }, { r, v -> r.custom5 = v as String? }),
            TagContext("C6", String::class, { r: RisRecord -> r.custom6 }, { r, v -> r.custom6 = v as String? }),
            TagContext("C7", String::class, { r: RisRecord -> r.custom7 }, { r, v -> r.custom7 = v as String? }),
            TagContext("C8", String::class, { r: RisRecord -> r.custom8 }, { r, v -> r.custom8 = v as String? }),
            TagContext("CA", String::class, { r: RisRecord -> r.caption }, { r, v -> r.caption = v as String? }),
            TagContext("CN", String::class, { r: RisRecord -> r.callNumber }, { r, v -> r.callNumber = v as String? }),
            TagContext("CP", String::class, { r: RisRecord -> r.cp }, { r, v -> r.cp = v as String? }),
            TagContext("CT", String::class, { r: RisRecord -> r.unpublishedReferenceTitle }, { r, v -> r.unpublishedReferenceTitle = v as String? }),
            TagContext("CY", String::class, { r: RisRecord -> r.placePublished }, { r, v -> r.placePublished = v as String? }),
            TagContext("DA", String::class, { r: RisRecord -> r.date }, { r, v -> r.date = v as String? }),
            TagContext("DB", String::class, { r: RisRecord -> r.databaseName }, { r, v -> r.databaseName = v as String? }, 15),
            TagContext("DO", String::class, { r: RisRecord -> r.doi }, { r, v -> r.doi = v as String? }, 10),
            TagContext("DP", String::class, { r: RisRecord -> r.databaseProvider }, { r, v -> r.databaseProvider = v as String? }),
            TagContext("ED", String::class, { r: RisRecord -> r.editor }, { r, v -> r.editor = v as String? }),
            TagContext<Int>("EP", Int::class, { r: RisRecord -> r.endPage }, { r, v -> r.endPage = v as Int? }, 6),
            TagContext("ED", String::class, { r: RisRecord -> r.edition }, { r, v -> r.edition = v as String? }),
            TagContext("ID", String::class, { r: RisRecord -> r.referenceId }, { r, v -> r.referenceId = v as String? }, 9),
            TagContext("IS", String::class, { r: RisRecord -> r.issue }, { r, v -> r.issue = v as String? }, 8),
            TagContext("J1", String::class, { r: RisRecord -> r.periodicalNameUserAbbrevation }, { r, v -> r.periodicalNameUserAbbrevation = v as String? }),
            TagContext("J2", String::class, { r: RisRecord -> r.alternativeTitle }, { r, v -> r.alternativeTitle = v as String? }),
            TagContext("JA", String::class, { r: RisRecord -> r.periodicalNameStandardAbbrevation }, { r, v -> r.periodicalNameStandardAbbrevation = v as String? }),
            TagContext("JF", String::class, { r: RisRecord -> r.periodicalNameFullFormatJF }, { r, v -> r.periodicalNameFullFormatJF = v as String? }),
            TagContext("JO", String::class, { r: RisRecord -> r.periodicalNameFullFormatJO }, { r, v -> r.periodicalNameFullFormatJO = v as String? }, 4),
            TagContext<List<String>>("KW", String::class, { r: RisRecord -> r.keywords }, { r, v -> r.keywords.add(v as String) }),
            TagContext<List<String>>("L1", String::class, { r: RisRecord -> r.pdfLinks }, { r, v -> r.pdfLinks.add(v as String) }, 16),
            TagContext<List<String>>("L2", String::class, { r: RisRecord -> r.fullTextLinks }, { r, v -> r.fullTextLinks.add(v as String) }, 17),
            TagContext<List<String>>("L3", String::class, { r: RisRecord -> r.relatedRecords }, { r, v -> r.relatedRecords.add(v as String) }, 18),
            TagContext<List<String>>("L4", String::class, { r: RisRecord -> r.images }, { r, v -> r.images.add(v as String) }, 19),
            TagContext("LA", String::class, { r: RisRecord -> r.language }, { r, v -> r.language = v as String? }),
            TagContext("LB", String::class, { r: RisRecord -> r.label }, { r, v -> r.label = v as String? }),
            TagContext("LK", String::class, { r: RisRecord -> r.websiteLink }, { r, v -> r.websiteLink = v as String? }),
            TagContext<Long>("M1", Long::class, { r: RisRecord -> r.number }, { r, v -> r.number = v as Long? }, 11),
            TagContext("M2", String::class, { r: RisRecord -> r.miscellaneous2 }, { r, v -> r.miscellaneous2 = v as String? }, 12),
            TagContext("M3", String::class, { r: RisRecord -> r.typeOfWork }, { r, v -> r.typeOfWork = v as String? }, 13),
            TagContext("N1", String::class, { r: RisRecord -> r.notes }, { r, v -> r.notes = v as String? }),
            TagContext("N2", String::class, { r: RisRecord -> r.abstr2 }, { r, v -> r.abstr2 = v as String? }),
            TagContext("OP", String::class, { r: RisRecord -> r.originalPublication }, { r, v -> r.originalPublication = v as String? }),
            TagContext("PB", String::class, { r: RisRecord -> r.publisher }, { r, v -> r.publisher = v as String? }),
            TagContext("PP", String::class, { r: RisRecord -> r.publishingPlace }, { r, v -> r.publishingPlace = v as String? }),
            TagContext("PY", String::class, { r: RisRecord -> r.publicationYear }, { r, v -> r.publicationYear = v as String? }, 2),
            TagContext("R1", String::class, { r: RisRecord -> r.reviewedItem }, { r, v -> r.reviewedItem = v as String? }),
            TagContext("RN", String::class, { r: RisRecord -> r.researchNotes }, { r, v -> r.researchNotes = v as String? }),
            TagContext("RP", String::class, { r: RisRecord -> r.reprintEdition }, { r, v -> r.reprintEdition = v as String? }),
            TagContext("SE", String::class, { r: RisRecord -> r.section }, { r, v -> r.section = v as String? }),
            TagContext("SN", String::class, { r: RisRecord -> r.isbnIssn }, { r, v -> r.isbnIssn = v as String? }),
            TagContext<Int>("SP", Int::class, { r: RisRecord -> r.startPage }, { r, v -> r.startPage = v as Int? }, 5),
            TagContext("ST", String::class, { r: RisRecord -> r.shortTitle }, { r, v -> r.shortTitle = v as String? }),
            TagContext("T1", String::class, { r: RisRecord -> r.primaryTitle }, { r, v -> r.primaryTitle = v as String? }),
            TagContext("T2", String::class, { r: RisRecord -> r.secondaryTitle }, { r, v -> r.secondaryTitle = v as String? }),
            TagContext("T3", String::class, { r: RisRecord -> r.tertiaryTitle }, { r, v -> r.tertiaryTitle = v as String? }),
            TagContext("TA", String::class, { r: RisRecord -> r.translatedAuthor }, { r, v -> r.translatedAuthor = v as String? }),
            TagContext("TI", String::class, { r: RisRecord -> r.title }, { r, v -> r.title = v as String? }, 3),
            TagContext("U1", String::class, { r: RisRecord -> r.userDefinable1 }, { r, v -> r.userDefinable1 = v as String? }),
            TagContext("U2", String::class, { r: RisRecord -> r.userDefinable2 }, { r, v -> r.userDefinable2 = v as String? }),
            TagContext("U3", String::class, { r: RisRecord -> r.userDefinable3 }, { r, v -> r.userDefinable3 = v as String? }),
            TagContext("U4", String::class, { r: RisRecord -> r.userDefinable4 }, { r, v -> r.userDefinable4 = v as String? }),
            TagContext("U5", String::class, { r: RisRecord -> r.userDefinable5 }, { r, v -> r.userDefinable5 = v as String? }),
            TagContext("UR", String::class, { r: RisRecord -> r.url }, { r, v -> r.url = v as String? }),
            TagContext("VL", String::class, { r: RisRecord -> r.volumeNumber }, { r, v -> r.volumeNumber = v as String? }, 7),
            TagContext("VO", String::class, { r: RisRecord -> r.publisherStandardNumber }, { r, v -> r.publisherStandardNumber = v as String? }),
            TagContext("Y1", String::class, { r: RisRecord -> r.primaryDate }, { r, v -> r.primaryDate = v as String? }),
            TagContext("Y2", String::class, { r: RisRecord -> r.accessDate }, { r, v -> r.accessDate = v as String? })
    )

    private val tag2context: Map<String, TagContext<*>> = risTagContexts.map { it.tag to it }.toMap()

    //region:import

    /**
     * Accepts a sequence of Strings representing lines in a RIS file
     * to return a list of [RisRecord]s. Throws a [JRisException] if
     * the lines cannot be parsed appropriatly.
     */
    fun parse(sequence: Sequence<String>): List<RisRecord> {
        val records = mutableListOf<RisRecord>()

        var record: RisRecord? = null
        var previousTag: String? = null

        sequence.filterNotNull()
                .filter { line -> line.isNotEmpty() }
                .map { line -> line.trim { it <= ' ' } }
                .forEach { line ->
                    if (line.startsWith(ER)) {
                        record?.let { records += it }
                        record = RisRecord()
                    } else if (record != null) {
                        previousTag = line.parseInto(record!!, previousTag)
                    }
                }

        return records
    }

    private fun String.parseInto(record: RisRecord, previousTag: String?): String? {
        if (isEmpty() || length <= START_INDEX_VALUE + 1)
            return previousTag

        val tag = parseTag(previousTag)
        val context: TagContext<*> = tag.getContext()
        val value: Any? = parseValueAs(context.kClass)

        context.setInto(record, value)

        return tag
    }

    private fun String.parseTag(lastParsed: String?): String {
        val tag: String = substring(0, TAG_LENGTH)
        return if (tag in tag2context) tag else when (lastParsed) {
            AB -> AB
            else -> throw JRisException("Field with code $tag not allowed")
        }
    }

    private fun String.getContext(): TagContext<*> = tag2context[this] ?: tag2context.getValue(AB)

    private fun String.parseValueAs(kClass: KClass<*>): Any? {
        val rawValue: Any = substring(START_INDEX_VALUE).trim()
        val clazz = if (kClass != List::class) kClass else String::class
        return when (clazz) {
            Type::class -> Type.valueOf(rawValue as String)
            Integer::class -> Integer.valueOf(rawValue as String)
            else -> rawValue as String
        }
    }

    //endregion

    //region:export

    /**
     * Builds the content of a RIS file and returns it as String.
     * TODO use something more efficient for big files
     */
    fun build(records: List<RisRecord>): String {
        check(records.isNotEmpty()) { throw JRisException("Record list must not be empty.") }
        return records.asString()
    }

    private fun List<RisRecord>.asString(): String {
        val sb = StringBuilder()
        forEach { record ->
            if (sb.isNotEmpty()) sb.append("$LINE_SEPARATOR")
            risTagContexts.sortedWith(compareBy({
                it.sort ?: Int.MAX_VALUE
            }, { it.tag })).forEach { currentTagContext ->
                sb.collect(record, currentTagContext)
            }
            sb.append("$ER$TAG_SEPARATOR$LINE_SEPARATOR")
        }
        return sb.toString()
    }

    private fun StringBuilder.collect(risRecord: RisRecord, tagContext: TagContext<*>) {
        fun TagContext<*>.withValue(value: Any): String = "$tag$TAG_SEPARATOR$value$LINE_SEPARATOR"
        tagContext.getFrom(risRecord)?.let { recordValue ->
            when (recordValue) {
                is List<*> -> recordValue.filterNotNull().forEach { listValue ->
                    append(tagContext.withValue(listValue))
                }
                else -> append(tagContext.withValue(recordValue))
            }
        }
    }

    //endregion
}

//region:helperMethodsForImportingRis

@Throws(IOException::class, JRisException::class)
fun parse(reader: Reader): List<RisRecord> = JRis.parse(BufferedReader(reader).readLines().asSequence())

@Throws(IOException::class, JRisException::class)
fun parse(file: File): List<RisRecord> = parse(file.bufferedReader());

@Throws(IOException::class, JRisException::class)
fun parse(filePath: String): List<RisRecord> = parse(File(filePath).bufferedReader())

@Throws(IOException::class, JRisException::class)
fun parse(inputStream: InputStream): List<RisRecord> = parse(inputStream.bufferedReader())

//endregion


//region:helperMethodsForExportingRis

@Throws(IOException::class, JRisException::class)
fun build(records: List<RisRecord>, writer: Writer): Boolean { // TODO no return value
    writer.use {
        it.write(JRis.build(records))
    }
    return true
}

@Throws(IOException::class, JRisException::class)
fun build(records: List<RisRecord>, file: File): Boolean {
    FileWriter(file).use {
        return build(records, it)
    }
}

@Throws(IOException::class, JRisException::class)
fun build(records: List<RisRecord>, out: OutputStream): Boolean {
    OutputStreamWriter(out).use {
        return build(records, it)
    }
}

@Throws(IOException::class, JRisException::class)
fun build(records: List<RisRecord>, filePath: String): Boolean {
    FileOutputStream(filePath).use {
        return build(records, it)
    }
}
//endregion
