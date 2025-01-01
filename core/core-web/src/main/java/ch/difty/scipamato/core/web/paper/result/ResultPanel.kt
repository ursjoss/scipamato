package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.Companion.byId
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.common.web.TITLE_RESOURCE_TAG
import ch.difty.scipamato.common.web.component.SerializableConsumer
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn
import ch.difty.scipamato.common.web.component.table.column.LinkIconColumn
import ch.difty.scipamato.core.entity.IdScipamatoEntity
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.Paper.PaperFields
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory
import ch.difty.scipamato.core.persistence.NewsletterService
import ch.difty.scipamato.core.persistence.PaperService
import ch.difty.scipamato.core.web.behavior.AjaxCsvDownload
import ch.difty.scipamato.core.web.behavior.AjaxDownload
import ch.difty.scipamato.core.web.behavior.AjaxTextDownload
import ch.difty.scipamato.core.web.common.BasePanel
import ch.difty.scipamato.core.web.fixed
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent
import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent
import ch.difty.scipamato.core.web.paper.csv.ReviewCsvAdapter
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfExporterConfiguration
import ch.difty.scipamato.core.web.paper.jasper.literaturereview.PaperLiteratureReviewDataSource
import ch.difty.scipamato.core.web.paper.jasper.literaturereview.PaperLiteratureReviewPlusDataSource
import ch.difty.scipamato.core.web.paper.jasper.referenceabstract.PaperReferenceAbstractDataSource
import ch.difty.scipamato.core.web.paper.jasper.review.PaperReviewDataSource
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource
import ch.difty.scipamato.core.web.paper.jasper.summarytable.PaperSummaryTableDataSource
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6IconType
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6IconTypeBuilder
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.event.Broadcast
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.link.ResourceLink
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.spring.injection.annot.SpringBean
import java.io.IOException
import java.io.ObjectInputStream

/**
 * The result panel shows the results of searches (by filter or by search order)
 * which are provided by the instantiating page through the data provider
 * holding the filter specification.
 */
@Suppress("SameParameterValue", "TooManyFunctions")
abstract class ResultPanel protected constructor(
    id: String,
    private val dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter>,
    override val mode: Mode,
) : BasePanel<Unit>(id) {

    @SpringBean
    private lateinit var paperService: PaperService

    @SpringBean
    private lateinit var newsletterService: NewsletterService

    @SpringBean
    private lateinit var shortFieldConcatenator: CoreShortFieldConcatenator

    @SpringBean
    private lateinit var risAdapterFactory: RisAdapterFactory

    private lateinit var results: DataTable<PaperSlim, String>
    private lateinit var risDownload: AjaxDownload
    private lateinit var csvDownload: AjaxDownload

    override fun onInitialize() {
        super.onInitialize()
        makeAndQueueTable("table")
        addExportRisAjax()
        addExportReviewCsvAjax()
        addOrReplaceExportLinks()
    }

    private fun addOrReplaceExportLinks() {
        addOrReplaceExportLink("exportRisLink") @JvmSerializableLambda { risDownload.initiate(it) }
        addOrReplacePdfSummaryLink("summaryLink")
        addOrReplacePdfSummaryShortLink("summaryShortLink")
        addOrReplacePdfReviewLink("reviewLink")
        addOrReplaceExportLink("reviewCsvLink") @JvmSerializableLambda { csvDownload.initiate(it) }
        addOrReplacePdfLiteratureReviewLink("literatureReviewLink", false)
        addOrReplacePdfLiteratureReviewLink("literatureReviewPlusLink", true)
        addOrReplacePdfSummaryTableLink("summaryTableLink")
        addOrReplacePdfReferenceAbstractLink("referenceAbstractLink")
    }

    private fun makeAndQueueTable(id: String) {
        results = object : BootstrapDefaultDataTable<PaperSlim, String>(
            id,
            makeTableColumns(),
            dataProvider,
            dataProvider.rowsPerPage.toLong()
        ) {
            private val serialVersionUID: Long = 1L

            override fun onAfterRender() {
                super.onAfterRender()
                paperIdManager.initialize(this@ResultPanel.dataProvider.findAllPaperIdsByFilter())
            }
        }.apply {
            outputMarkupId = true
            add(TableBehavior().striped().hover())
        }.also {
            queue(it)
        }
    }

    private fun makeTableColumns(): List<IColumn<PaperSlim, String>> =
        mutableListOf<IColumn<PaperSlim, String>>().apply {
            add(makePropertyColumn(PaperFields.NUMBER.fieldName))
            add(makePropertyColumn(PaperFields.FIRST_AUTHOR.fieldName))
            add(makePropertyColumn(PaperFields.PUBL_YEAR.fieldName))
            add(makeClickableColumn(PaperFields.TITLE.fieldName, this@ResultPanel::onTitleClick))
            if (mode !== Mode.VIEW && isOfferingSearchComposition) add(makeExcludeLinkIconColumn("exclude"))
            if (mode !== Mode.VIEW) add(makeNewsletterLinkIconColumn("newsletter"))
            add(makePropertyColumn(IdScipamatoEntity.IdScipamatoEntityFields.ID.fieldName))
        }

    /**
     * Determines if the result panel is embedded into a page that offers composing complex searches.
     * If so, the table offers an icon column to exclude papers from searches. Otherwise, it will not.
     */
    protected abstract val isOfferingSearchComposition: Boolean

    private fun onTitleClick(m: IModel<PaperSlim>) {
        m.getObject()?.id?.let { paperIdManager.setFocusToItem(it) }
        setResponsePage(getResponsePage(m, localization, paperService, dataProvider))
    }

    private fun getResponsePage(
        m: IModel<PaperSlim>,
        languageCode: String,
        paperService: PaperService,
        dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter>,
    ): PaperEntryPage = PaperEntryPage(Model.of(
        paperService.findByNumber(m.getObject()?.number ?: 0, languageCode)
            .orElse(Paper())), page.pageReference, dataProvider.searchOrderId, dataProvider.isShowExcluded, Model.of(0))

    private fun makePropertyColumn(propExpression: String) = PropertyColumn<PaperSlim, String>(
        StringResourceModel("$COLUMN_HEADER$propExpression", this@ResultPanel, null), propExpression, propExpression
    )

    private fun makeClickableColumn(propExpression: String, consumer: SerializableConsumer<IModel<PaperSlim>>) =
        ClickablePropertyColumn(
            displayModel = StringResourceModel("$COLUMN_HEADER$propExpression", this@ResultPanel, null),
            property = propExpression,
            action = consumer,
            sort = propExpression,
        )

    private fun makeExcludeLinkIconColumn(id: String): IColumn<PaperSlim, String> =
        object : LinkIconColumn<PaperSlim>(
            StringResourceModel("$COLUMN_HEADER$id", this@ResultPanel, null)
        ) {
            private val serialVersionUID: Long = 1L

            override fun createIconModel(rowModel: IModel<PaperSlim>): IModel<String> {
                val circle = FontAwesome6IconTypeBuilder.FontAwesome6Regular.circle_check.fixed()
                val ban = FontAwesome6IconTypeBuilder.FontAwesome6Solid.ban.fixed()
                return Model.of(if (dataProvider.isShowExcluded) circle.cssClassName() else ban.cssClassName())
            }

            override fun createTitleModel(rowModel: IModel<PaperSlim>): IModel<String> =
                StringResourceModel(
                    if (dataProvider.isShowExcluded) "column.title.reinclude"
                    else "column.title.exclude", this@ResultPanel, null
                )

            override fun onClickPerformed(
                target: AjaxRequestTarget,
                rowModel: IModel<PaperSlim>,
                link: AjaxLink<Void>,
            ) {
                val excludedId = rowModel.getObject().id
                target.add(results)
                excludedId?.let {
                    send(page, Broadcast.BREADTH, SearchOrderChangeEvent(target).withExcludedPaperId(it))
                }
            }
        }

    /**
     * Icon indicating whether the paper
     *
     *  * can be added to the current newsletter
     *  * has been added to the current newsletter
     *  * had been added previously to a newsletter that had been closed already
     *
     * and providing the option of changing the association between paper and newsletter:
     *
     *  * If the paper had been added to a newsletter, the association can only be removed if
     * the newsletter in question is still in status work in progress.
     *  * >Otherwise the association is read only.
     */
    private fun makeNewsletterLinkIconColumn(id: String): IColumn<PaperSlim, String> {
        val plusSquare = FontAwesome6IconTypeBuilder.FontAwesome6Solid.square_plus.fixed()
        val envelopeOpen = FontAwesome6IconTypeBuilder.FontAwesome6Regular.envelope_open.fixed()
        val envelope = FontAwesome6IconTypeBuilder.FontAwesome6Regular.envelope.fixed()
        return newLinkIconColumn(id, plusSquare, envelopeOpen, envelope)
    }

    private fun newLinkIconColumn(
        id: String, plusSquare: FontAwesome6IconType,
        envelopeOpen: FontAwesome6IconType, envelope: FontAwesome6IconType,
    ): LinkIconColumn<PaperSlim> = object :
        LinkIconColumn<PaperSlim>(StringResourceModel("$COLUMN_HEADER$id", this@ResultPanel, null)) {
        private val serialVersionUID: Long = 1L
        override fun createIconModel(rowModel: IModel<PaperSlim>) = Model.of(newLinkIcon(rowModel.getObject()))
        private fun newLinkIcon(paper: PaperSlim): String =
            if (hasNoNewsletter(paper))
                if (isThereOneNewsletterInStatusWip) plusSquare.cssClassName() else ""
            else if (hasNewsletterWip(paper)) envelopeOpen.cssClassName()
            else envelope.cssClassName()

        private fun hasNoNewsletter(paper: PaperSlim): Boolean = paper.newsletterAssociation == null
        private val isThereOneNewsletterInStatusWip: Boolean
            get() = !newsletterService.canCreateNewsletterInProgress()

        private fun hasNewsletterWip(paper: PaperSlim): Boolean =
            byId(paper
                .newsletterAssociation
                .publicationStatusId)
                .isInProgress

        override fun createTitleModel(rowModel: IModel<PaperSlim>): IModel<String>? {
            val paper = rowModel.getObject()
            return if (hasNoNewsletter(paper)) {
                if (isThereOneNewsletterInStatusWip)
                    StringResourceModel("column.title.newsletter.add", this@ResultPanel, null)
                else Model.of("")
            } else if (hasNewsletterWip(paper)) {
                StringResourceModel("column.title.newsletter.remove", this@ResultPanel, null)
            } else {
                StringResourceModel(
                    "column.title.newsletter.closed", this@ResultPanel, Model.of(paper.newsletterAssociation)
                )
            }
        }

        override fun onClickPerformed(
            target: AjaxRequestTarget, rowModel: IModel<PaperSlim>,
            link: AjaxLink<Void>,
        ) {
            val paper = rowModel.getObject()
            if (hasNoNewsletter(paper)) {
                if (isThereOneNewsletterInStatusWip)
                    newsletterService.mergePaperIntoWipNewsletter(paper.id!!)
                else
                    warn(StringResourceModel("newsletter.noneInProgress", this@ResultPanel, null).string)
            } else if (hasNewsletterWip(paper)) {
                newsletterService.removePaperFromWipNewsletter(paper.id!!)
            } else {
                warn(
                    StringResourceModel(
                        "newsletter.readonly",
                        this@ResultPanel,
                        Model.of(paper.newsletterAssociation)
                    ).string
                )
            }
            target.add(results)
            send(page, Broadcast.BREADTH, NewsletterChangeEvent(target))
        }
    }

    private fun addOrReplacePdfSummaryLink(id: String) {
        val brand = properties.brand
        val headerPart = "$brand-${StringResourceModel("headerPart.summary", this, null).string}"
        val pdfCaption = "$brand- ${StringResourceModel("paper_summary.titlePart", this, null).string}"
        val rhf = commonReportHeaderFieldsBuildPart(
            headerPart = headerPart,
            brand = brand,
        ).copy(
            populationLabel = getLabelResourceFor(PaperFields.POPULATION.fieldName),
            resultLabel = getLabelResourceFor(PaperFields.RESULT.fieldName),
        )
        val config = ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(activeUser)
            .withCreator(brand)
            .withCompression()
            .build()
        addOrReplace(newJasperResourceLink(
            id,
            "summary",
            PaperSummaryDataSource(dataProvider, rhf, shortFieldConcatenator, config)
        ))
    }

    private fun commonReportHeaderFieldsBuildPart(brand: String, headerPart: String) =
        ReportHeaderFields(
            headerPart = headerPart,
            brand = brand,
            goalsLabel = getLabelResourceFor(PaperFields.GOALS.fieldName),
            methodsLabel = getLabelResourceFor(PaperFields.METHODS.fieldName),
            methodOutcomeLabel = getLabelResourceFor(PaperFields.METHOD_OUTCOME.fieldName),
            resultMeasuredOutcomeLabel = getLabelResourceFor(PaperFields.RESULT_MEASURED_OUTCOME.fieldName),
            methodStudyDesignLabel = getLabelResourceFor(PaperFields.METHOD_STUDY_DESIGN.fieldName),
            populationPlaceLabel = getLabelResourceFor(PaperFields.POPULATION_PLACE.fieldName),
            populationParticipantsLabel = getLabelResourceFor(PaperFields.POPULATION_PARTICIPANTS.fieldName),
            populationDurationLabel = getLabelResourceFor(PaperFields.POPULATION_DURATION.fieldName),
            exposurePollutantLabel = getLabelResourceFor(PaperFields.EXPOSURE_POLLUTANT.fieldName),
            exposureAssessmentLabel = getLabelResourceFor(PaperFields.EXPOSURE_ASSESSMENT.fieldName),
            resultExposureRangeLabel = getLabelResourceFor(PaperFields.RESULT_EXPOSURE_RANGE.fieldName),
            methodStatisticsLabel = getLabelResourceFor(PaperFields.METHOD_STATISTICS.fieldName),
            methodConfoundersLabel = getLabelResourceFor(PaperFields.METHOD_CONFOUNDERS.fieldName),
            resultEffectEstimateLabel = getLabelResourceFor(PaperFields.RESULT_EFFECT_ESTIMATE.fieldName),
            conclusionLabel = getLabelResourceFor(PaperFields.CONCLUSION.fieldName),
            commentLabel = getLabelResourceFor(PaperFields.COMMENT.fieldName)
        )

    private fun addOrReplacePdfSummaryShortLink(id: String) {
        val brand = properties.brand
        val headerPart = "$brand-${StringResourceModel("headerPart.summaryShort", this, null).string}"
        val pdfCaption = "$brand- ${StringResourceModel("paper_summary.titlePart", this, null).string}"
        val rhf = commonReportHeaderFieldsBuildPart(brand, headerPart)
        val config = ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(activeUser)
            .withCreator(brand)
            .withCompression()
            .build()
        addOrReplace(newJasperResourceLink(id, "summary-short", PaperSummaryShortDataSource(dataProvider, rhf, config)))
    }

    private fun addOrReplacePdfReviewLink(id: String) {
        val brand = properties.brand
        val pdfCaption = "$brand- ${StringResourceModel("paper_review.titlePart", this, null).string}"
        val rhf = reviewReportHeaderFields(brand)
        val config = ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(activeUser)
            .withCreator(brand)
            .withCompression()
            .build()
        addOrReplace(newJasperResourceLink(id, "review", PaperReviewDataSource(dataProvider, rhf, config)))
    }

    private fun reviewReportHeaderFields(brand: String) = ReportHeaderFields(
        headerPart = "",
        brand = brand,
        numberLabel = getLabelResourceFor(PaperFields.NUMBER.fieldName),
        authorYearLabel = getLabelResourceFor("authorYear"),
        populationPlaceLabel = getShortLabelResourceFor(PaperFields.POPULATION_PLACE.fieldName),
        methodOutcomeLabel = getShortLabelResourceFor(PaperFields.METHOD_OUTCOME.fieldName),
        exposurePollutantLabel = getLabelResourceFor(PaperFields.EXPOSURE_POLLUTANT.fieldName),
        methodStudyDesignLabel = getShortLabelResourceFor(PaperFields.METHOD_STUDY_DESIGN.fieldName),
        populationDurationLabel = getShortLabelResourceFor(PaperFields.POPULATION_DURATION.fieldName),
        populationParticipantsLabel = getShortLabelResourceFor(PaperFields.POPULATION_PARTICIPANTS.fieldName),
        exposureAssessmentLabel = getShortLabelResourceFor(PaperFields.EXPOSURE_ASSESSMENT.fieldName),
        resultExposureRangeLabel = getShortLabelResourceFor(PaperFields.RESULT_EXPOSURE_RANGE.fieldName),
        methodConfoundersLabel = getLabelResourceFor(PaperFields.METHOD_CONFOUNDERS.fieldName),
        resultEffectEstimateLabel = getShortLabelResourceFor(PaperFields.RESULT_EFFECT_ESTIMATE.fieldName),
        conclusionLabel = getShortLabelResourceFor(PaperFields.CONCLUSION.fieldName),
        commentLabel = getLabelResourceFor(PaperFields.COMMENT.fieldName),
        internLabel = getLabelResourceFor(PaperFields.INTERN.fieldName),
        goalsLabel = getLabelResourceFor(PaperFields.GOALS.fieldName),
        populationLabel = getLabelResourceFor(PaperFields.POPULATION.fieldName),
        methodsLabel = getLabelResourceFor(PaperFields.METHODS.fieldName),
        resultLabel = getLabelResourceFor(PaperFields.RESULT.fieldName),
    )

    private fun addOrReplacePdfLiteratureReviewLink(id: String, plus: Boolean) {
        val brand = properties.brand
        val pdfCaption = StringResourceModel("paper_literature_review.caption", this, null)
            .setParameters(brand).string
        val url = properties.pubmedBaseUrl
        val rhf = ReportHeaderFields(
            headerPart = "",
            brand = brand,
            numberLabel = getLabelResourceFor(PaperFields.NUMBER.fieldName),
            captionLabel = pdfCaption,
            pubmedBaseUrl = url
        )
        val config = ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(activeUser)
            .withCreator(brand)
            .withCompression()
            .build()
        if (plus)
            addOrReplace(
                newJasperResourceLink(
                    id,
                    "literature_review_plus",
                    PaperLiteratureReviewPlusDataSource(dataProvider, rhf, config)
                )
            )
        else
            addOrReplace(
                newJasperResourceLink(
                    id,
                    "literature_review",
                    PaperLiteratureReviewDataSource(dataProvider, rhf, config)
                )
            )
    }

    private fun addOrReplacePdfSummaryTableLink(id: String) {
        addOrReplace(newPdfSummaryTable(id, "summary_table"))
    }


    private fun newPdfSummaryTable(id: String, resourceKeyPart: String): ResourceLink<Unit> {
        val pdfCaption = StringResourceModel("paper_summary_table.titlePart", this, null).string
        val brand = properties.brand
        val rhf = ReportHeaderFields(
            headerPart = "",
            brand = brand,
            numberLabel = getLabelResourceFor(PaperFields.NUMBER.fieldName),
            captionLabel = pdfCaption,
        )
        val config = ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(activeUser)
            .withCreator(brand)
            .withCompression()
            .build()
        return newJasperResourceLink(id, resourceKeyPart, PaperSummaryTableDataSource(dataProvider, rhf, config))
    }

    private fun addOrReplacePdfReferenceAbstractLink(id: String) {
        addOrReplace(newPdfReferenceAbstract(id, "reference_abstract"))
    }


    private fun newPdfReferenceAbstract(id: String, resourceKeyPart: String): ResourceLink<Unit> {
        val brand = properties.brand
        val pdfCaption = StringResourceModel("paper_reference_abstract.caption", this, null)
            .setParameters(brand).string
        val url = properties.pubmedBaseUrl
        val rhf = ReportHeaderFields(
            headerPart = "",
            brand = brand,
            numberLabel = getLabelResourceFor(PaperFields.NUMBER.fieldName),
            captionLabel = pdfCaption,
            pubmedBaseUrl = url
        )
        val config = ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(activeUser)
            .withCreator(brand)
            .withCompression()
            .build()
        return newJasperResourceLink(id, resourceKeyPart, PaperReferenceAbstractDataSource(dataProvider, rhf, config))
    }

    private fun newJasperResourceLink(id: String, resourceKeyPart: String, resource: JasperPaperDataSource<*>) =
        ResourceLink<Unit>(id, resource).apply {
            outputMarkupId = true
            body = StringResourceModel("$LINK_RESOURCE_PREFIX$resourceKeyPart$LABEL_RESOURCE_TAG")
            add(AttributeModifier(
                TITLE_ATTR,
                StringResourceModel(
                    "$LINK_RESOURCE_PREFIX$resourceKeyPart$TITLE_RESOURCE_TAG", this@ResultPanel, null
                ).string
            ))
        }

    // Deserialization of the panel without recreating the jasper links renders them invalid
    // see https://github.com/ursjoss/scipamato/issues/2
    @Suppress("UnusedPrivateMember")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(s: ObjectInputStream) {
        s.defaultReadObject()
        addOrReplaceExportLinks()
    }

    private fun addExportRisAjax() {
        val url = properties.pubmedBaseUrl
        val brand = properties.brand
        val baseUrl = properties.cmsUrlSearchPage
        val risAdapter = risAdapterFactory.createRisAdapter(brand, url, baseUrl)
        risDownload = object : AjaxTextDownload(true) {
            private val serialVersionUID: Long = 1L
            override fun onRequest() {
                content = risAdapter.build(dataProvider.findAllPapersByFilter())
                fileName = "export.ris"
                super.onRequest()
            }
        }.also {
            add(it)
        }
    }

    private fun addOrReplaceExportLink(id: String, initiate: (AjaxRequestTarget) -> Unit) {
        val titleResourceKey = LINK_RESOURCE_PREFIX + id + TITLE_RESOURCE_TAG
        val reviewLink: AjaxLink<Unit> = object : AjaxLink<Unit>(id) {
            private val serialVersionUID: Long = 1L
            override fun onClick(target: AjaxRequestTarget) {
                initiate(target)
            }
        }
        reviewLink.add(AttributeModifier(TITLE_ATTR, StringResourceModel(titleResourceKey, this, null).string))
        addOrReplace(reviewLink)
    }

    private fun addExportReviewCsvAjax() {
        val csvBuilder = ReviewCsvAdapter(reviewReportHeaderFields(""))
        csvDownload = object : AjaxCsvDownload(true) {
            private val serialVersionUID: Long = 1L
            override fun onRequest() {
                content = csvBuilder.build(dataProvider.findAllPapersByFilter())
                fileName = ReviewCsvAdapter.FILE_NAME
                super.onRequest()
            }
        }.also {
            add(it)
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val COLUMN_HEADER = "column.header."
        private const val TITLE_ATTR = "title"
        private const val LINK_RESOURCE_PREFIX = "link."
    }
}
