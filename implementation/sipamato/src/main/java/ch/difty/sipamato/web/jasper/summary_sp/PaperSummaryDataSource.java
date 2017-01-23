package ch.difty.sipamato.web.jasper.summary_sp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.IteratorUtils;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.wicketstuff.jasperreports.JRConcreteResource;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.jasper.SipamatoPdfResourceHandler;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.resources.jasper.PaperSummaryReportResourceReference;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * DataSource for the PaperSummaryReport.
 *
 * Can be instantiated in different ways, either by passing in
 *
 * <ul>
 * <li> a single {@link Paper} + meta fields</li>
 * <li> a single {@link PaperSummary}</li>
 * <li> a collection of {@link PaperSummary} entities or</li>
 * <li> an instance of a {@link SortablePaperSlimProvider} and the {@link PaperService} + meta fields</li>
 * </ul>
 *
 * The meta fields are not contained within a paper instance and make up e.g. localized labels, the brand or part of the header.
 * @author u.joss
 */
public class PaperSummaryDataSource extends JRConcreteResource<SipamatoPdfResourceHandler> {

    private static final long serialVersionUID = 1L;

    private final Collection<PaperSummary> paperSummaries = new ArrayList<>();

    private SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider;
    private PaperService paperService;

    private String populationLabel;
    private String methodsLabel;
    private String resultLabel;
    private String headerPart;
    private String brand;

    /**
     * Build up the paper summary from a {@link Paper} and any additional information not contained within the paper
     * @param paper
     *      an instance of {@link Paper} - must not be null.
     * @param populationLabel
     *      localized label for the population field
     * @param methodsLabel
     *      localized label for the methods field
     * @param resultLabel
     *      localized label for the result field
     * @param headerPart
     *      Static part of the header - will be supplemented with the id
     * @param brand
     *      Brand of the application
     */
    public PaperSummaryDataSource(final Paper paper, final String populationLabel, final String methodsLabel, final String resultLabel, final String headerPart, final String brand,
            PdfExporterConfiguration config) {
        this(Arrays.asList(new PaperSummary(AssertAs.notNull(paper, "paper"), populationLabel, methodsLabel, resultLabel, headerPart, brand)), config);
    }

    /**
     * Uses as single {@link PaperSummary} transparently as data source
     * @param paperSummary an instance of {@link PaperSummary} - must not be null
     */
    public PaperSummaryDataSource(final PaperSummary paperSummary, PdfExporterConfiguration config) {
        this(Arrays.asList(AssertAs.notNull(paperSummary, "paperSummary")), config);
    }

    /**
     * Using a collection of {@link PaperSummary} instances as data source
     * @param paperSummaries collection of {@link PaperSummary} instances - must not be null
     */
    public PaperSummaryDataSource(final Collection<PaperSummary> paperSummaries, PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config));
        init();
        AssertAs.notNull(paperSummaries, "paperSummaries");
        this.paperSummaries.addAll(paperSummaries);
    }

    private void init() {
        setJasperReport(PaperSummaryReportResourceReference.get().getReport());
        setReportParameters(new HashMap<String, Object>());
        this.paperSummaries.clear();
    }

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the {@link PaperService} to retrieve the papers
     * based on the ids of the {@link PaperSlim}s that are used in the dataProvider.
     * @param dataProvider
     *      the {@link SortablePaperSlimProvider} - must not be null
     * @param paperService
     *      the {@link PaperService} - must not be null
     * @param populationLabel
     *      localized label for the population field
     * @param methodsLabel
     *      localized label for the methods field
     * @param resultLabel
     *      localized label for the result field
     * @param headerPart
     *      Static part of the header - will be supplemented with the id
     * @param brand
     *      Brand of the application
     */
    public PaperSummaryDataSource(final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider, final PaperService paperService, final String populationLabel, final String methodsLabel,
            final String resultLabel, final String headerPart, final String brand, PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config));
        init();
        this.dataProvider = AssertAs.notNull(dataProvider, "dataProvider");
        this.paperService = AssertAs.notNull(paperService, "paperService");
        this.populationLabel = populationLabel;
        this.methodsLabel = methodsLabel;
        this.resultLabel = resultLabel;
        this.headerPart = headerPart;
        this.brand = brand;
    }

    /** {@iheritDoc} */
    @Override
    public JRDataSource getReportDataSource() {
        if (dataProvider != null) {
            paperSummaries.clear();
            fetchSummariesFromDataProvider();
        }
        return new JRBeanCollectionDataSource(paperSummaries);
    }

    /**
     * This is admittedly a bit of a hack, as this will actually cause two service calls to the database:
     *
     * <ol>
     * <li> a call to the paperSlimService (within the dataprovider) to get thePaperSlims</li>
     * <li> a second call to the paperService to get the Papers from the paperSlim Ids</li>
     * </ol>
     *
     * We could refactor this to have PaperSlim have all the fields needed in PaperSummary and then
     * derive the PaperSummary from PaperSlim instead of from Paper. But that adds overhead in PaperSlim instead.
     *
     * TODO evaluate which way to go later
     */
    private void fetchSummariesFromDataProvider() {
        final long records = dataProvider.size();
        if (records > 0) {
            @SuppressWarnings("unchecked")
            final List<PaperSlim> paperSlims = IteratorUtils.toList(dataProvider.iterator(0, records));
            final List<Long> ids = paperSlims.stream().map(p -> p.getId()).collect(Collectors.toList());
            final List<Paper> papers = paperService.findByIds(ids);
            for (final Paper p : papers) {
                paperSummaries.add(new PaperSummary(p, populationLabel, methodsLabel, resultLabel, headerPart, brand));
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected byte[] getExporterData(JasperPrint print, JRAbstractExporter exporter) throws JRException {
        // prepare a stream to trap the exporter's output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        // execute the export and return the trapped result
        exporter.exportReport();

        return baos.toByteArray();
    }
}
