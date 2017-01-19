package ch.difty.sipamato.web.jasper.summary_sp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.IteratorUtils;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.resources.jasper.PaperSummaryReportResourceReference;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * DataSource for the PaperSummaryReport.
 *
 * Can be instantiated in different ways, either by passing in
 *
 * <ul>
 * <li> a single {@link Paper}</li>
 * <li> a single {@link PaperSummary}</li>
 * <li> a collection of {@link PaperSummary} entities or</li>
 * <li> an instance of a {@link SortablePaperSlimProvider} and the {@link PaperService}</li>
 * </ul>
 * @author u.joss
 */
public class PaperSummaryDataSource extends JRConcreteResource<PdfResourceHandler> {

    private static final long serialVersionUID = 1L;

    private final Collection<PaperSummary> paperSummaries = new ArrayList<>();

    private SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider;
    private PaperService paperService;

    // TODO: retrieve labels dynamically, Define and retrieve headerPart and brand, timeService to get now

    /**
     * Instantiate by {@link Paper}
     * @param paper
     * @param userName
     */
    public PaperSummaryDataSource(final Paper paper, final String userName) {
        this(Arrays.asList(new PaperSummary(paper, "Kollektiv", "Methoden", "Resultat", "LUDOK-Zusammenfassung Nr.", "LUDOK", userName, LocalDateTime.now())));
    }

    /**
     * Instantiate by {@link PaperSummary}
     * @param paperSummary
     */
    public PaperSummaryDataSource(final PaperSummary paperSummary) {
        this(Arrays.asList(paperSummary));
    }

    /**
     * Instantiate by collection of {@link PaperSummary} entities
     * @param paperSummaries
     */
    public PaperSummaryDataSource(final Collection<PaperSummary> paperSummaries) {
        super(new PdfResourceHandler());
        setJasperReport(PaperSummaryReportResourceReference.get().getReport());
        setReportParameters(new HashMap<String, Object>());
        this.paperSummaries.clear();
        this.paperSummaries.addAll(paperSummaries);
    }

    /**
     * Instantiating by {@link SortablePaperSlimProvider} and {@link PaperService}
     * @param dataProvider
     * @param paperService
     */
    public PaperSummaryDataSource(final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider, final PaperService paperService) {
        super(new PdfResourceHandler());
        setJasperReport(PaperSummaryReportResourceReference.get().getReport());
        setReportParameters(new HashMap<String, Object>());
        this.paperSummaries.clear();
        this.dataProvider = dataProvider;
        this.paperService = paperService;
    }

    /** {@iheritDoc} */
    @Override
    public JRDataSource getReportDataSource() {
        if (dataProvider != null && paperService != null) {
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
            if (!ids.isEmpty()) {
                final List<Paper> papers = paperService.findByIds(ids);
                for (Paper p : papers) {
                    paperSummaries.add(new PaperSummary(p, "Kollektiv", "Methoden", "Resultat", "LUDOK-Zusammenfassung Nr.", "LUDOK", "userName", LocalDateTime.now()));
                }
            }
        }
    }

}
