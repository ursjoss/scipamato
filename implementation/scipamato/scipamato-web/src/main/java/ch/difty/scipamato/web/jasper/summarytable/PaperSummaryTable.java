package ch.difty.scipamato.web.jasper.summarytable;

import java.util.stream.Collectors;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.JasperEntity;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

/**
 * DTO to feed the PaperSummaryTableDataSource
 *
 * @author u.joss
 */
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
     *      the paper with the relevant fields
     * @param rhf
     *      the reportHeaderFields with the localized field headers
     */
    public PaperSummaryTable(final Paper p, final ReportHeaderFields rhf, final boolean includeResults) {
        AssertAs.notNull(p, "p");
        AssertAs.notNull(rhf, "rhf");

        final Long no = p.getNumber();
        this.number = no != null ? String.valueOf(no) : "";
        this.firstAuthor = na(p.getFirstAuthor());
        this.publicationYear = p.getPublicationYear() != null ? String.valueOf(p.getPublicationYear()) : "";
        this.codesOfClass1 = p.getCodesOf(CodeClassId.CC1).stream().map(Code::getCode).collect(Collectors.joining(CODE_DELIMITER));
        this.codesOfClass4 = p.getCodesOf(CodeClassId.CC4).stream().map(Code::getCode).collect(Collectors.joining(CODE_DELIMITER));
        this.codesOfClass7 = p.getCodesOf(CodeClassId.CC7).stream().map(Code::getCode).collect(Collectors.joining(CODE_DELIMITER));
        this.goals = na(p.getGoals());
        this.title = na(p.getTitle());
        this.result = includeResults ? na(p.getResult()) : "";

        this.caption = na(rhf.getCaptionLabel());
        this.brand = na(rhf.getBrand());
        this.numberLabel = na(rhf.getNumberLabel());
    }

    public String getNumber() {
        return number;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public String getCodesOfClass1() {
        return codesOfClass1;
    }

    public String getCodesOfClass4() {
        return codesOfClass4;
    }

    public String getCodesOfClass7() {
        return codesOfClass7;
    }

    public String getGoals() {
        return goals;
    }

    public String getTitle() {
        return title;
    }

    public String getResult() {
        return result;
    }

    public String getCaption() {
        return caption;
    }

    public String getBrand() {
        return brand;
    }

    public String getNumberLabel() {
        return numberLabel;
    }

}