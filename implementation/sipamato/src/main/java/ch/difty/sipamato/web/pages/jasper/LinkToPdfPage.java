package ch.difty.sipamato.web.pages.jasper;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.auth.Roles;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.web.jasper.summary_sp.PaperSummary;
import ch.difty.sipamato.web.jasper.summary_sp.PaperSummaryDataSource;
import ch.difty.sipamato.web.pages.BasePage;

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

        final Paper p1 = new Paper();
        p1.setId(8535l);
        p1.setAuthors("Shi L, Zanobetti A, Kloog I, Coull BA, Koutrakis P, Melly SJ, Schwartz JD.");
        p1.setTitle("Low-Concentration PM2.5 and Mortality: Estimating Acute and Chronic Effects in a Population-Based Study.");
        p1.setLocation("Environ Health Perspect. 2016; 124 (1): 46-52.");
        p1.setGoals("all the goals");
        p1.setPopulation("all the population");
        p1.setMethods("all the methods");
        p1.setResult("all the results");
        final Paper p2 = new Paper();
        p2.setId(8536l);
        p2.setAuthors("Shi L, Zanobetti A, Kloog I, Coull BA, Koutrakis P, Melly SJ, Schwartz JD XX.");
        p2.setTitle("Low-Concentration PM2.5 and Mortality: Estimating Acute and Chronic Effects in a Population-Based Study XX.");
        p2.setLocation("Environ Health Perspect. 2016; 124 (1): 46-52 XX.");
        p2.setGoals("all the goals XX");
        p2.setPopulation("all the population XX");
        p2.setMethods("all the methods XX");
        p2.setResult("all the results XX");
        PaperSummary ps1 = new PaperSummary(p1, "Kollektiv", "Methoden", "Resultat", "LUDOK-Zusammenfassung Nr.", "LUDOK", LocalDateTime.now());
        PaperSummary ps2 = new PaperSummary(p2, "Kollektiv", "Methoden", "Resultat", "LUDOK-Zusammenfassung Nr.", "LUDOK", LocalDateTime.now());
        add(new ResourceLink<Void>("linkToPdf", new PaperSummaryDataSource(Arrays.asList(ps1, ps2))));
    }

    /**
     * @see org.apache.wicket.Component#isVersioned()
     */
    @Override
    public boolean isVersioned() {
        return false;
    }
}
