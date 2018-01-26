package ch.difty.scipamato.core.web.paper.jasper.summarytable;

import java.util.stream.Collectors;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.web.paper.jasper.JasperEntity;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO to feed the PaperSummaryTableDataSource
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperSummaryTable extends JasperEntity {
    private static final long serialVersionUID = 1L;

    private static final String CODE_DELIMITER = ",";

    private final String number;
    private final String firstAuthor;
    private final String publicationYear;
    private final String codesOfClass1;
    private final String codesOfClass4;
    private final String codesOfClass7;
    private final String goals;
    private final String title;
    private final String result;

    private final String caption;
    private final String brand;
    private final String numberLabel;

    /**
     * Instantiation with a {@link Paper} and the {@link ReportHeaderFields}
     *
     * @param p
     *            the paper with the relevant fields
     * @param rhf
     *            the reportHeaderFields with the localized field headers
     */
    public PaperSummaryTable(final Paper p, final ReportHeaderFields rhf, final boolean includeResults) {
        AssertAs.notNull(p, "p");
        AssertAs.notNull(rhf, "rhf");

        final Long no = p.getNumber();
        this.number = no != null ? String.valueOf(no) : "";
        this.firstAuthor = na(p.getFirstAuthor());
        this.publicationYear = p.getPublicationYear() != null ? String.valueOf(p.getPublicationYear()) : "";
        this.codesOfClass1 = p.getCodesOf(CodeClassId.CC1)
            .stream()
            .map(Code::getCode)
            .collect(Collectors.joining(CODE_DELIMITER));
        this.codesOfClass4 = p.getCodesOf(CodeClassId.CC4)
            .stream()
            .map(Code::getCode)
            .collect(Collectors.joining(CODE_DELIMITER));
        this.codesOfClass7 = p.getCodesOf(CodeClassId.CC7)
            .stream()
            .map(Code::getCode)
            .collect(Collectors.joining(CODE_DELIMITER));
        this.goals = na(p.getGoals());
        this.title = na(p.getTitle());
        this.result = includeResults ? na(p.getResult()) : "";

        this.caption = na(rhf.getCaptionLabel());
        this.brand = na(rhf.getBrand());
        this.numberLabel = na(rhf.getNumberLabel());
    }

}