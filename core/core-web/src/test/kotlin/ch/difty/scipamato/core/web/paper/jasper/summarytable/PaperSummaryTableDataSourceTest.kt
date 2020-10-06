@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.web.paper.jasper.summarytable

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import io.mockk.every
import io.mockk.verify
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.design.JRDesignField
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal class PaperSummaryTableDataSourceTest : PaperDataSourceTest() {

    private lateinit var ds: PaperSummaryTableDataSource

    private val codesOfCodeClass1: MutableList<Code> = ArrayList()
    private val codesOfCodeClass4: MutableList<Code> = ArrayList()
    private val codesOfCodeClass7: MutableList<Code> = ArrayList()

    private val rhf = newReportHeaderFields()

    private fun newReportHeaderFields() = ReportHeaderFields(
        headerPart = "",
        brand = BRAND,
        numberLabel = NUMBER_LABEL,
        captionLabel = CAPTION,
    )

    public override fun setUpHook() {
        codesOfCodeClass1.add(Code("1F", "Code1F", "", false, CodeClassId.CC1.id, "CC1", "CC1D", 1))
        codesOfCodeClass4.add(Code("4A", "Code4A", "", false, CodeClassId.CC4.id, "CC4", "CC4D", 1))
        codesOfCodeClass4.add(Code("4C", "Code4C", "", false, CodeClassId.CC4.id, "CC4", "CC4D", 3))
        codesOfCodeClass7.add(Code("7B", "Code7B", "", false, CodeClassId.CC7.id, "CC7", "CC7D", 2))
        every { paperMock.number } returns NUMBER
        every { paperMock.firstAuthor } returns FIRST_AUTHOR
        every { paperMock.publicationYear } returns PUBLICATION_YEAR
        every { paperMock.goals } returns GOALS
        every { paperMock.title } returns TITLE
        every { paperMock.result } returns RESULT
        every { paperMock.getCodesOf(CodeClassId.CC1) } returns codesOfCodeClass1
        every { paperMock.getCodesOf(CodeClassId.CC4) } returns codesOfCodeClass4
        every { paperMock.getCodesOf(CodeClassId.CC7) } returns codesOfCodeClass7
    }

    @Suppress("SameParameterValue")
    @Throws(JRException::class)
    private fun assertDataSource(fileName: String) {
        ds.connectionProvider.shouldBeNull()
        ds.contentDisposition.toString() shouldBeEqualTo "ATTACHMENT"
        ds.contentType shouldBeEqualTo "application/pdf"
        ds.extension shouldBeEqualTo "pdf"
        ds.jasperReport shouldBeInstanceOf JasperReport::class
        ds.reportParameters.shouldBeEmpty()
        ds.fileName shouldBeEqualTo fileName
        val jsds = ds.reportDataSource
        val f = JRDesignField()
        jsds.next().shouldBeTrue()
        assertFieldValue("number", NUMBER.toString(), f, jsds)
        assertFieldValue("firstAuthor", FIRST_AUTHOR, f, jsds)
        assertFieldValue("publicationYear", PUBLICATION_YEAR.toString(), f, jsds)
        assertFieldValue("goals", GOALS, f, jsds)
        assertFieldValue("title", TITLE, f, jsds)
        assertFieldValue("result", RESULT, f, jsds)
        assertFieldValue("codesOfClass1", CODES_OF_CC1, f, jsds)
        assertFieldValue("codesOfClass4", CODES_OF_CC4, f, jsds)
        assertFieldValue("codesOfClass7", CODES_OF_CC7, f, jsds)
        assertFieldValue("caption", CAPTION, f, jsds)
        assertFieldValue("brand", BRAND, f, jsds)
        jsds.next().shouldBeFalse()
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() {
        every { dataProviderMock.size() } returns 1L
        every { dataProviderMock.findAllPapersByFilter() } returns listOf(paperMock)
        ds = PaperSummaryTableDataSource(dataProviderMock, rhf, pdfExporterConfigMock)
        assertDataSource(FILE_NAME)
        verify { dataProviderMock.size() }
        verify { dataProviderMock.findAllPapersByFilter() }
        verify { paperMock.number }
        verify { paperMock.firstAuthor }
        verify(exactly = 2) { paperMock.publicationYear }
        verify { paperMock.goals }
        verify { paperMock.title }
        verify { paperMock.result }
        verify { paperMock.getCodesOf(CodeClassId.CC1) }
        verify { paperMock.getCodesOf(CodeClassId.CC4) }
        verify { paperMock.getCodesOf(CodeClassId.CC7) }
    }

    @Test
    @Throws(JRException::class)
    fun instantiatingWithProvider_withEmptyProvider_returnsNoRecord() {
        every { dataProviderMock.size() } returns 0L
        ds = PaperSummaryTableDataSource(dataProviderMock, rhf, pdfExporterConfigMock)
        ds.reportDataSource.next().shouldBeFalse()
        verify { dataProviderMock.size() }
    }

    companion object {
        private const val NUMBER = 100L
        private const val FIRST_AUTHOR = "firstAuthor"
        private const val PUBLICATION_YEAR = 2017
        private const val GOALS = "goals"
        private const val TITLE = "title"
        private const val RESULT = "result"
        private const val CODES_OF_CC1 = "1F"
        private const val CODES_OF_CC4 = "4A,4C"
        private const val CODES_OF_CC7 = "7B"
        private const val CAPTION = "caption"
        private const val BRAND = "brand"
        private const val NUMBER_LABEL = "nl"
        private const val FILE_NAME = "paper_summary_table.pdf"
    }
}
