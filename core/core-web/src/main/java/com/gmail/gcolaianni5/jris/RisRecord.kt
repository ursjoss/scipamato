package com.gmail.gcolaianni5.jris

/**
 * A single RIS record. It contains all the allowed tag from RIS format.
 *
 * @author Gianluca Colaianni -- g.colaianni5@gmail.com
 * @version 1.0
 * @since 22 apr 2017
 */
@Suppress("ParameterListWrapping", "SpellCheckingInspection")
data class RisRecord(

    /** TY */
    var type: RisType? = null,

    /** A1 */
    val firstAuthors: MutableList<String> = mutableListOf(),

    /** A2 */
    val secondaryAuthors: MutableList<String> = mutableListOf(),

    /** A3 */
    val tertiaryAuthors: MutableList<String> = mutableListOf(),

    /** A4 */
    val subsidiaryAuthors: MutableList<String> = mutableListOf(),

    /** AU */
    var authors: MutableList<String> = mutableListOf(),

    /** AB */
    var abstr: String? = null,

    /** AD */
    var authorAddress: String? = null,

    /** AN */
    var accessionNumber: String? = null,

    /** AV */
    var archivesLocation: String? = null,

    /** BT */
    var bt: String? = null,

    /** C1 */
    var custom1: String? = null,

    /** C2 */
    var custom2: String? = null,

    /** C3 */
    var custom3: String? = null,

    /** C4 */
    var custom4: String? = null,

    /** C5 */
    var custom5: String? = null,

    /** C6 */
    var custom6: String? = null,

    /** C7 */
    var custom7: String? = null,

    /** C8 */
    var custom8: String? = null,

    /** CA */
    var caption: String? = null,

    /** CN */
    var callNumber: String? = null,

    /** CP */
    var cp: String? = null,

    /** CT */
    var unpublishedReferenceTitle: String? = null,

    /** CY */
    var placePublished: String? = null,

    /** DA */
    var date: String? = null,

    /** DB */
    var databaseName: String? = null,

    /** DO */
    var doi: String? = null,

    /** DP */
    var databaseProvider: String? = null,

    /** ED */
    var editor: String? = null,

    /** EP */
    var endPage: Int? = null,

    /** ED */
    var edition: String? = null,

    /** ID */
    var referenceId: String? = null,

    /** IS */
    var issue: String? = null,

    /**
     * J1. Max 255 characters.
     */
    var periodicalNameUserAbbrevation: String? = null,

    /**
     * J2. This field is used for the abbreviated title of a book or journal name, the latter mapped to T2.
     */
    var alternativeTitle: String? = null,

    /**
     * JA. This is the periodical in which the article was (or is to be, in the case of in-press references) published.
     *     This is an alphanumeric field of up to 255 characters.
     */
    var periodicalNameStandardAbbrevation: String? = null,

    /**
     * JF. Journal/Periodical name: full format. This is an alphanumeric field of up to 255 characters.
     */
    var periodicalNameFullFormatJF: String? = null,

    /**
     * JO. Journal/Periodical name: full format. This is an alphanumeric field of up to 255 characters.
     */
    var periodicalNameFullFormatJO: String? = null,

    /**
     * KW
     */
    val keywords: MutableList<String> = mutableListOf(),

    /**
     * L1. There is no practical limit to the length of this field. URL addresses can be entered individually,
     *     one per tag or multiple addresses can be entered on one line using a semi-colon as a separator.
     */
    val pdfLinks: MutableList<String> = mutableListOf(),

    /**
     * L2. Link to Full-text. There is no practical limit to the length of this field.
     *     URL addresses can be entered individually, one per tag or multiple addresses
     *     can be entered on one line using a semi-colon as a separator.
     */
    val fullTextLinks: MutableList<String> = mutableListOf(),

    /**
     * L3. Related Records. There is no practical limit to the length of this field.
     */
    val relatedRecords: MutableList<String> = mutableListOf(),

    /**
     * L4. Image(s). There is no practical limit to the length of this field.
     */
    val images: MutableList<String> = mutableListOf(),

    /** LA */
    var language: String? = null,

    /** LB */
    var label: String? = null,

    /** LK */
    var websiteLink: String? = null,

    /** M1 */
    var number: Long? = null,

    /**
     * M2. This is an alphanumeric field and there is no practical limit to the length of this field.
     */
    var miscellaneous2: String? = null,

    /** M3 */
    var typeOfWork: String? = null,

    /** N1 */
    var notes: String? = null,

    /**
     * N2. Abstract. This is a free text field and can contain alphanumeric characters.
     *     There is no practical length limit to this field.
     */
    var abstr2: String? = null,

    /**
     * NV.
     */
    var numberOfVolumes: String? = null,

    /** OP */
    var originalPublication: String? = null,

    /** PB */
    var publisher: String? = null,

    /** PP */
    var publishingPlace: String? = null,

    /**
     * PY. Publication year (YYYY/MM/DD).
     */
    var publicationYear: String? = null,

    /** RI */
    var reviewedItem: String? = null,

    /** RN */
    var researchNotes: String? = null,

    /** RP */
    var reprintEdition: String? = null,

    /** SE */
    var section: String? = null,

    /** SN */
    var isbnIssn: String? = null,

    /** SP - String? instead of Int? for DistillerSr format */
    var startPage: String? = null,

    /** ST */
    var shortTitle: String? = null,

    /** T1 */
    var primaryTitle: String? = null,

    /** T2 */
    var secondaryTitle: String? = null,

    /** T3 */
    var tertiaryTitle: String? = null,

    /** TA */
    var translatedAuthor: String? = null,

    /** TI */
    var title: String? = null,

    /** TT */
    var translatedTitle: String? = null,

    /**
     * U1. User definable 1. This is an alphanumeric field and there is no practical limit to the length of this field.
     */
    var userDefinable1: String? = null,

    /**
     * U. User definable 2. This is an alphanumeric field and there is no practical limit to the length of this field.
     */
    var userDefinable2: String? = null,

    /**
     * U3. User definable 3. This is an alphanumeric field and there is no practical limit to the length of this field.
     */
    var userDefinable3: String? = null,

    /**
     * U4. User definable 4. This is an alphanumeric field and there is no practical limit to the length of this field.
     */
    var userDefinable4: String? = null,

    /**
     * U5. User definable 5. This is an alphanumeric field and there is no practical limit to the length of this field.
     */
    var userDefinable5: String? = null,

    /** UR */
    var url: String? = null,

    /** VL */
    var volumeNumber: String? = null,

    /** VO */
    var publisherStandardNumber: String? = null,

    /** Y1 */
    var primaryDate: String? = null,

    /** Y2 */
    var accessDate: String? = null
)
