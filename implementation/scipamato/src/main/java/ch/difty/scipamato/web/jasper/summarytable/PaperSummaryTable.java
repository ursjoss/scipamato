package ch.difty.scipamato.web.jasper.summarytable;

import java.util.List;
import java.util.stream.Collectors;

import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.web.jasper.JasperEntity;

/**
 * DTO to feed the PaperSummaryTableDataSource
 *
 * @author u.joss
 */
public class PaperSummaryTable extends JasperEntity {

    private static final String CODE_DELIMITER = ",";

    private static final long serialVersionUID = 1L;

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
     * Instantiation with a {@link Paper} and additional fields
     *
     * @param p
     *      the paper
     * @param includeResults
     *      true: the result field will be printed - false: it will not be printed
     * @param caption
     *      localized caption
     * @param brand
     *      the application brand name
     * @param numberLabel
     *      the localized label for the number field
     */
    public PaperSummaryTable(final Paper p, final boolean includeResults, final String caption, final String brand, final String numberLabel) {
        this(AssertAs.notNull(p, "paper").getNumber(), p.getFirstAuthor(), String.valueOf(p.getPublicationYear()), p.getCodesOf(CodeClassId.CC1), p.getCodesOf(CodeClassId.CC4),
                p.getCodesOf(CodeClassId.CC7), p.getGoals(), p.getTitle(), (includeResults ? p.getResult() : ""), caption, brand, numberLabel);
    }

    /**
     * Instantiation with all individual fields (those that are part of a {@link Paper} and all other from the other constructor.
     */
    public PaperSummaryTable(final Long number, final String firstAuthor, final String publicationYear, final List<Code> codesOfClass1, final List<Code> codesOfClass4, final List<Code> codesOfClass7,
            final String goals, final String title, final String result, final String caption, final String brand, final String numberLabel) {
        this.number = number != null ? String.valueOf(number) : "";
        this.firstAuthor = na(firstAuthor);
        this.publicationYear = na(publicationYear);
        this.codesOfClass1 = codesOfClass1.stream().map(Code::getCode).collect(Collectors.joining(CODE_DELIMITER));
        this.codesOfClass4 = codesOfClass4.stream().map(Code::getCode).collect(Collectors.joining(CODE_DELIMITER));
        this.codesOfClass7 = codesOfClass7.stream().map(Code::getCode).collect(Collectors.joining(CODE_DELIMITER));
        this.goals = na(goals);
        this.title = na(title);
        this.result = na(result);

        this.caption = na(caption);
        this.brand = na(brand);
        this.numberLabel = na(numberLabel);
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