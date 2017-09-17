package ch.difty.scipamato.web.jasper.summary;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.PaperSummaryCommon;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

/**
 * DTO to feed the PaperSummaryDataSource
 *
 * @author u.joss
 */
public class PaperSummary extends PaperSummaryCommon {
    private static final long serialVersionUID = 1L;

    private final String population;
    private final String result;

    private final String populationLabel;
    private final String resultLabel;

    /**
     * Instantiation with a {@link Paper} and the {@link ReportHeaderFields}
     *
     * @param p
     *      the paper with the relevant fields
     * @param rhf
     *      the reportHeaderFields with the localized field headers
     */
    public PaperSummary(final Paper p, final ReportHeaderFields rhf) {
        super(AssertAs.notNull(p, "p"), AssertAs.notNull(rhf, "rhf"));
        this.population = na(p.getPopulation());
        this.result = na(p.getResult());

        this.populationLabel = na2(rhf.getPopulationLabel(), population);
        this.resultLabel = na2(rhf.getResultLabel(), result);
    }

    public String getPopulationLabel() {
        return populationLabel;
    }

    public String getPopulation() {
        return population;
    }

    public String getResultLabel() {
        return resultLabel;
    }

    public String getResult() {
        return result;
    }

}