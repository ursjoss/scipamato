package com.gmail.gcolaianni5.jris

import kotlin.reflect.KClass

/**
 * The enum defines all available Tags that may be used in the RIS format.
 *
 * A [description] outlines the purpose of the tag, the [maxLength] property
 * defines the maximum number of characters (for Strings) - a `null` value indicates
 * either no restriction or a non-String class (as specified in [kClass]).
 * The [requiredOrder] property defines the first level of sorting when writing RIS
 * files. The specification does not enforce any particular order except for `TY`
 * to be the first one and `ER` to be the last tag in a RIS record. All other tags
 * share the same value, resulting in either being sorted by tag name alphabetically
 * or by a custom sort order specified by the caller.
 *
 * The class was composed from information available on
 * [Wikipedia](https://en.wikipedia.org/wiki/RIS_(file_format)).
 */
@Suppress("unused", "SpellCheckingInspection")
enum class RisTag(
    val description: String,
    val maxLength: Int? = null,
    internal val kClass: KClass<*> = String::class,
    internal val requiredOrder: Int = 1000
) {
    TY("Type of reference", kClass = RisType::class, requiredOrder = 0), // must be first per record
    A1("First Author"),
    A2("Secondary Author", kClass = List::class),
    A3("Tertiary Author", kClass = List::class),
    A4("Subsidiary Author", kClass = List::class),
    AB("Abstract"),
    AD("Author Address"),
    AN("Accession Number"),
    AU("Author", kClass = List::class),
    AV("Location in Archives"),
    // This field maps to T2 for all reference types except for Whole Book and Unpublished Work references.
    BT("This field maps to T2 for all reference types except for Whole Book and Unpublished Work references."),
    C1("Custom 1"),
    C2("Custom 2"),
    C3("Custom 3"),
    C4("Custom 4"),
    C5("Custom 5"),
    C6("Custom 6"),
    C7("Custom 7"),
    C8("Custom 8"),
    CA("Caption"),
    CN("Call Number"),
    CP("This field can contain alphanumeric characters."),
    CT("Title of unpublished reference"),
    CY("Place Published"),
    DA("Date"),
    DB("Name of Database"),
    DO("DOI"),
    DP("Database Provider"),
    ED("Editor"),
    EP("End Page", kClass = Int::class),
    ET("Edition"),
    ID("Reference ID"),
    IS("Issue number"),
    J1("Periodical name: user abbreviation 1.", maxLength = 255),
    // (this field is used for the abbreviated title of a book or journal name, the latter mapped to T2)
    J2("Alternate Title"),
    // This is the periodical in which the article was (or is to be, in the case of in-press references) published.
    JA("Periodical name: standard abbreviation.", maxLength = 255),
    JF("Journal/Periodical name: full format.", maxLength = 255),
    JO("Journal/Periodical name: full format.", maxLength = 255),
    KW("Keywords", kClass = List::class),
    // URL addresses can be entered individually, one per tag or multiple addresses can be entered on one line using a semi-colon as a separator.
    L1("Link to PDF.", kClass = List::class),
    // URL addresses can be entered individually, one per tag or multiple addresses can be entered on one line using a semi-colon as a separator.
    L2("Link to Full-text.", kClass = List::class),
    L3("Related Records.", kClass = List::class),
    L4("Image(s).", kClass = List::class),
    LA("Language"),
    LB("Label"),
    LK("Website Link"),
    M1("Number", kClass = Long::class),
    M2("Miscellaneous 2."),
    M3("Type of Work"),
    N1("Notes"),
    //  This is a free text field and can contain alphanumeric characters. There is no practical length limit to this field.
    N2("Abstract."),
    NV("Number of Volumes"),
    OP("Original Publication"),
    PB("Publisher"),
    PP("Publishing Place"),
    PY("Publication year (YYYY)"),
    RI("Reviewed Item"),
    RN("Research Notes"),
    RP("Reprint Edition"),
    SE("Section"),
    SN("ISBN/ISSN"),
    SP("Start Page", kClass = Int::class),
    ST("Short Title"),
    T1("Primary Title"),
    T2("Secondary Title (journal title, if applicable)"),
    T3("Tertiary Title"),
    TA("Translated Author"),
    TI("Title"),
    TT("Translated Title"),
    U1("User definable 1."),
    U2("User definable 2."),
    U3("User definable 3."),
    U4("User definable 4."),
    U5("User definable 5."),
    UR("URL"),
    VL("Volume number"),
    VO("Published Standard number"),
    Y1("Primary Date"),
    Y2("Access Date"),
    ER("End of Reference", requiredOrder = Integer.MAX_VALUE) // must be last per record
}
