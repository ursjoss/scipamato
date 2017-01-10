package ch.difty.sipamato.web.pages.jasper;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import ch.difty.sipamato.auth.Roles;
import ch.difty.sipamato.web.jasper.WebappDataSource;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.resources.jasper.SampleReportResourceReference;
import ch.difty.sipamato.web.resources.jasper.StudySummaryReportResourceReference;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * Simple Jasper reports example with PDF output and a jasper reports panel..
 * 
 * @author Eelco Hillenius
 */
@MountPath("/pdfl")
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class LinkToPdfPage extends BasePage<Void> {
    private static final long serialVersionUID = 1L;

    public LinkToPdfPage(final PageParameters parameters) {
        super(parameters);
    }

    /**
     * Constructor.
     * @throws ResourceStreamNotFoundException 
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("BaseDir", new File(context.getRealPath("/reports")));

        final File reportFile1 = new File(context.getRealPath("/reports/WebappReport.jasper"));
        add(new ResourceLink<Void>("linkToPdf1",
                new JRConcreteResource<PdfResourceHandler>(reportFile1, new PdfResourceHandler()).setReportParameters(parameters).setReportDataSource(new WebappDataSource())));
        final File reportFile2 = new File(context.getRealPath("/reports/study_summary_A4.jasper"));
        add(new ResourceLink<Void>("linkToPdf2",
                new JRConcreteResource<PdfResourceHandler>(reportFile2, new PdfResourceHandler()).setReportParameters(parameters).setReportDataSource(new WebappDataSource())));

        JRResource r = new JRConcreteResource<PdfResourceHandler>(new PdfResourceHandler()) {
            private static final long serialVersionUID = 1L;

            @Override
            public JRDataSource getReportDataSource() {
                Collection<BeanWithList> coll = new ArrayList<BeanWithList>();
                coll.add(new BeanWithList(Arrays.asList("London", "Paris"), 1));
                coll.add(new BeanWithList(Arrays.asList("London", "Madrid", "Moscow"), 2));
                coll.add(new BeanWithList(Arrays.asList("Rome"), 3));
                return new JasperBeanCollectionDataSource(coll);
            }

        };
        r.setJasperReport(SampleReportResourceReference.get().getReport());
        r.setReportParameters(parameters);
        add(new ResourceLink<Void>("linkToPdf3", r));

        JRResource r4 = new JRConcreteResource<PdfResourceHandler>(new PdfResourceHandler()) {
            private static final long serialVersionUID = 1L;

            @Override
            public JRDataSource getReportDataSource() {
                LocalDateTime now = LocalDateTime.now();
                Collection<JasperPaper> coll = new ArrayList<JasperPaper>();
                coll.add(new JasperPaper(8535l, "Shi L, Zanobetti A, Kloog I, Coull BA, Koutrakis P, Melly SJ, Schwartz JD.",
                        "Low-Concentration PM2.5 and Mortality: Estimating Acute and Chronic Effects in a Population-Based Study.", "Environ Health Perspect. 2016; 124 (1): 46-52.", "all the goals",
                        "Kollektiv", "all the population", "Methoden", "all the methods", "Resultat", "all the results", "LUDOK-Zusammenfassung Nr.", "LUDOK", getActiveUser().getUserName(), now));
                return new JasperBeanCollectionDataSource(coll);
            }

        };
        r4.setJasperReport(StudySummaryReportResourceReference.get().getReport());
        r4.setReportParameters(parameters);
        add(new ResourceLink<Void>("linkToPdf4", r4));

        try {
            //            SampleReportResourceReference sampleReportRef2 = SampleReportResourceReference.get();
            //            Map<String, Object> params = new HashMap<String, Object>();
            //            JasperPrint jasperPrint = JasperFillManager.fillReport(sampleReportRef2.getReport(), params, getDataSource());
            //            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            //            add(new ResourceLink<Void>("linkToPdf3", ))
            //            JasperExportManager.exportReportToPdfFile(jasperPrint, "/tmp/sample.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * @see org.apache.wicket.Component#isVersioned()
     */
    @Override
    public boolean isVersioned() {
        return false;
    }

    public static class BeanWithList implements Serializable {

        private static final long serialVersionUID = 1L;

        private List<String> cities;
        private Integer id;

        public BeanWithList(List<String> cities, Integer id) {
            this.cities = new ArrayList<String>(cities);
            this.id = id;
        }

        public List<String> getCities() {
            return cities;
        }

        public Integer getId() {
            return id;
        }
    }

    public static class JasperPaper implements Serializable {
        private static final long serialVersionUID = 1L;

        private final long id;
        private final String authors;
        private final String title;
        private final String location;
        private final String goals;
        private final String populationLabel;
        private final String population;
        private final String methodsLabel;
        private final String methods;
        private final String resultsLabel;
        private final String results;
        

        private final String header;
        private final String brand;
        private final String user;
        private final String year;

        public JasperPaper(long id, String authors, String title, String location, String goals, String populationLabel, String population, String methodsLabel, String methods, String resultsLabel, String results, String headerPart, String brand, String user,
                LocalDateTime now) {
            this.id = id;
            this.authors = authors;
            this.title = title;
            this.location = location;
            this.goals = goals;
            this.populationLabel = populationLabel;
            this.population = population;
            this.methodsLabel = methodsLabel;
            this.methods = methods;
            this.resultsLabel = resultsLabel;
            this.results = results;

            this.header = new StringBuilder().append(headerPart).append(" ").append(String.valueOf(id)).toString();
            this.brand = brand;
            this.user = user;
            this.year = String.valueOf(now.getYear());
        }

        public long getId() {
            return id;
        }

        public String getAuthors() {
            return authors;
        }

        public String getTitle() {
            return title;
        }

        public String getLocation() {
            return location;
        }

        public String getGoals() {
            return goals;
        }

        public String getPopulationLabel() {
            return populationLabel;
        }
        public String getPopulation() {
            return population;
        }
        public String getMethodsLabel() {
            return methodsLabel;
        }

        public String getMethods() {
            return methods;
        }
        public String getResultsLabel() {
            return resultsLabel;
        }

        public String getResults() {
            return results;
        }

        public String getHeader() {
            return header;
        }

        public String getBrand() {
            return brand;
        }

        public String getUser() {
            return user;
        }

        public String getYear() {
            return year;
        }
    }
}
