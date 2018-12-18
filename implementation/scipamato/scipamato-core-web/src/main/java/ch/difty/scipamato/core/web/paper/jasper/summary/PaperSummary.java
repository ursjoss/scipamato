package ch.difty.scipamato.core.web.paper.jasper.summary;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.web.paper.jasper.PaperSummaryCommon;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator;

/**
 * DTO to feed the PaperSummaryDataSource
 *
 * @author u.joss
 */
@SuppressWarnings("WeakerAccess")
@Data
@EqualsAndHashCode(callSuper = true)
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
     *     the paper with the relevant fields
     * @param shortFieldConcatenator
     *     ShortfieldConcatenator
     * @param rhf
     *     the reportHeaderFields with the localized field headers
     */
    public PaperSummary(final Paper p, final CoreShortFieldConcatenator shortFieldConcatenator,
        final ReportHeaderFields rhf) {
        super(AssertAs.notNull(p, "p"), shortFieldConcatenator, AssertAs.notNull(rhf, "rhf"));
        this.population = na(shortFieldConcatenator.populationFrom(p));
        this.result = na(shortFieldConcatenator.resultFrom(p));

        this.populationLabel = na2(rhf.getPopulationLabel(), population);
        this.resultLabel = na2(rhf.getResultLabel(), result);
    }

}