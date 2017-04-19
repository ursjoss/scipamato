package ch.difty.sipamato.web.jasper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.wicketstuff.jasperreports.JRConcreteResource;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public abstract class JasperPaperDataSource<E extends JasperEntity> extends JRConcreteResource<SipamatoPdfResourceHandler> {

    private static final long serialVersionUID = 1L;

    private final Collection<E> jasperEntities = new ArrayList<>();
    private final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider;
    private final String baseName;

    public JasperPaperDataSource(SipamatoPdfResourceHandler handler, String baseName, Collection<E> jasperEntities) {
        super(handler);
        this.baseName = AssertAs.notNull(baseName, "baseName");
        this.jasperEntities.clear();
        this.jasperEntities.addAll(AssertAs.notNull(jasperEntities, "jasperEntities"));
        this.dataProvider = null;
        init();
    }

    public JasperPaperDataSource(SipamatoPdfResourceHandler handler, String baseName, AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider) {
        super(handler);
        this.baseName = AssertAs.notNull(baseName, "baseName");
        this.jasperEntities.clear();
        this.dataProvider = AssertAs.notNull(dataProvider, "dataProvider");
        init();
    }

    private void init() {
        setJasperReport(getReport());
        setReportParameters(new HashMap<String, Object>());
        setFileName(baseName + "." + getExtension());
    }

    protected abstract JasperReport getReport();

    /** {@iheritDoc} */
    @Override
    public JRDataSource getReportDataSource() {
        fetchEntitiesFromDataProvider();
        return new JRBeanCollectionDataSource(jasperEntities);
    }

    private void fetchEntitiesFromDataProvider() {
        if (dataProvider != null) {
            jasperEntities.clear();
            if (dataProvider.size() > 0)
                for (final Paper p : dataProvider.findAllPapersByFilter())
                    jasperEntities.add(makeEntity(p));
        }
    }

    /**
     * Implement to instantiate an entity <code>E</code> from the provided {@link Paper} and additional information
     * required to build it.
     * @param p the Paper
     * @return the entity
     */
    protected abstract E makeEntity(final Paper p);

    /**
     * Overriding in order to not use the deprecated and incompatible methods still used in JRResource (expoerter.setParameter)
     */
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
