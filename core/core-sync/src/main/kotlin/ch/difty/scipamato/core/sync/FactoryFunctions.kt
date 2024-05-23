@file:Suppress("LongParameterList")

package ch.difty.scipamato.core.sync

import java.sql.Date
import java.sql.Timestamp

fun newPublicCode(
    code: String?,
    langCode: String?,
    codeClassId: Int?,
    name: String?,
    comment: String?,
    sort: Int?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
): PublicCode = ch.difty.scipamato.publ.db.tables.pojos.Code(
    code, langCode, codeClassId, name, comment, sort, version, created, lastModified, lastSynched
)

fun newPublicCodeClass(
    codeClassId: Int?,
    langCode: String?,
    name: String?,
    description: String?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
): PublicCodeClass = ch.difty.scipamato.publ.db.tables.pojos.CodeClass(
    codeClassId, langCode, name, description, version, created, lastModified, lastSynched
)

fun newPublicKeyword(
    id: Int?,
    keywordId: Int?,
    langCode: String?,
    name: String?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
    searchOverride: String?,
): PublicKeyword = ch.difty.scipamato.publ.db.tables.pojos.Keyword(
    id, keywordId, langCode, name, version, created, lastModified, lastSynched, searchOverride
)

fun newPublicLanguage(
    code: String?,
    lastSynched: Timestamp?,
    mainLanguage: Boolean?,
): PublicLanguage = ch.difty.scipamato.publ.db.tables.pojos.Language(
    code, lastSynched, mainLanguage
)

fun newPublicNewsletter(
    id: Int?,
    issue: String?,
    issueDate: Date?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
): PublicNewsletter = ch.difty.scipamato.publ.db.tables.pojos.Newsletter(
    id, issue, issueDate, version, created, lastModified, lastSynched
)

fun newPublicNewsletterTopic(
    id: Int?,
    langCode: String?,
    title: String?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
): PublicNewsletterTopic = ch.difty.scipamato.publ.db.tables.pojos.NewsletterTopic(
    id, langCode, title, version, created, lastModified, lastSynched
)

fun newPublicNewStudy(
    newsletterId: Int?,
    paperNumber: Long?,
    newsletterTopicId: Int?,
    sort: Int?,
    year: Int?,
    authors: String?,
    headline: String?,
    description: String?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
): PublicNewStudy = ch.difty.scipamato.publ.db.tables.pojos.NewStudy(
    newsletterId, paperNumber, newsletterTopicId, sort, year, authors, headline, description,
    version, created, lastModified, lastSynched
)

fun newPublicNewStudyTopic(
    newsletterId: Int?,
    newsletterTopicId: Int?,
    sort: Int?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
): PublicNewStudyTopic = ch.difty.scipamato.publ.db.tables.pojos.NewStudyTopic(
    newsletterId, newsletterTopicId, sort, version, created, lastModified, lastSynched
)

fun newPublicNewStudyPageLink(
    langCode: String?,
    sort: Int?,
    title: String?,
    url: String?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    lastSynched: Timestamp?,
): PublicNewStudyPageLink = ch.difty.scipamato.publ.db.tables.pojos.NewStudyPageLink(
    langCode, sort, title, url, version, created, lastModified, lastSynched
)

fun newPublicPaper(
    id: Long?,
    number: Long?,
    pmId: Int?,
    authors: String?,
    title: String?,
    location: String?,
    publicationYear: Int?,
    goals: String?,
    methods: String?,
    population: String?,
    result: String?,
    comment: String?,
    version: Int?,
    created: Timestamp?,
    lastModified: Timestamp?,
    codesPopulation: Array<Short>?,
    codesStudyDesign: Array<Short>?,
    codes: Array<String>?,
    lastSynched: Timestamp?,
): PublicPaper = ch.difty.scipamato.publ.db.tables.pojos.Paper(
    id, number, pmId, authors, title, location, publicationYear, goals, methods, population, result,
    comment, version, created, lastModified, codesPopulation, codesStudyDesign, codes, lastSynched
)
