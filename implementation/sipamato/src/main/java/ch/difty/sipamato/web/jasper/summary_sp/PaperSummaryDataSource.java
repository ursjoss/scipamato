package ch.difty.sipamato.web.jasper.summary_sp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.web.resources.jasper.PaperSummaryReportResourceReference;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PaperSummaryDataSource extends JRConcreteResource<PdfResourceHandler> {

    private static final long serialVersionUID = 1L;

    private final Collection<PaperSummary> paperSummaries = new ArrayList<>();

    // TODO: retrieve labels dynamically, Define and retrieve headerPart and brand, timeService to get now
    public PaperSummaryDataSource(final Paper paper, final String userName) {
        this(Arrays.asList(new PaperSummary(paper, "Kollektiv", "Methoden", "Resultat", "LUDOK-Zusammenfassung Nr.", "LUDOK", userName, LocalDateTime.now())));
    }

    public PaperSummaryDataSource(final PaperSummary paperSummary) {
        this(Arrays.asList(paperSummary));
    }

    public PaperSummaryDataSource(final Collection<PaperSummary> paperSummaries) {
        super(new PdfResourceHandler());
        setJasperReport(PaperSummaryReportResourceReference.get().getReport());
        setReportParameters(new HashMap<String, Object>());
        this.paperSummaries.clear();
        this.paperSummaries.addAll(paperSummaries);
    }

    @Override
    public JRDataSource getReportDataSource() {
        return new JRBeanCollectionDataSource(paperSummaries);
    }

}
