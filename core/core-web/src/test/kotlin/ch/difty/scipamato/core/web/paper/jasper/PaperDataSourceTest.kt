package ch.difty.scipamato.core.web.paper.jasper

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.web.WicketTest
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.design.JRDesignField
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach

abstract class PaperDataSourceTest : WicketTest() {

    @MockK
    protected lateinit var dataProviderMock: AbstractPaperSlimProvider<PaperFilter>

    @MockK
    protected lateinit var paperFilterMock: PaperFilter

    @MockK
    protected lateinit var searchOrderMock: SearchOrder

    @MockK(relaxed = true)
    protected lateinit var paperMock: Paper

    @MockK
    protected lateinit var pdfExporterConfigMock: ClusterablePdfExporterConfiguration

    @MockK
    protected lateinit var shortFieldConcatenatorMock: CoreShortFieldConcatenator

    @AfterEach
    fun tearDown() {
        confirmVerified(dataProviderMock, paperFilterMock, searchOrderMock, paperMock, shortFieldConcatenatorMock)
    }

    @Throws(JRException::class)
    protected fun assertFieldValue(fieldName: String?, value: String?, f: JRDesignField, jsds: JRDataSource) {
        f.name = fieldName
        jsds.getFieldValue(f) shouldBeEqualTo value
    }
}
