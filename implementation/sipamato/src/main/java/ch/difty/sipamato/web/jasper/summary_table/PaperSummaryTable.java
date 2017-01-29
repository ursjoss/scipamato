package ch.difty.sipamato.web.jasper.summary_table;

import java.util.List;
import java.util.stream.Collectors;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.web.jasper.JasperEntity;

/**
 * DTO to feed the PaperSummaryTableDataSource
 *
 * @author u.joss
 */
public class PaperSummaryTable extends JasperEntity {

    private static final String CODE_DELIMITER = ",";

    private static final long serialVersionUID = 1L;

    private final String id;
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

    /**
     * Instantiation with a {@link Paper} and additional fields
     *
     * @param p
     *      the paper
     * @param caption
     *      localized caption
     */
    public PaperSummaryTable(final Paper p, final boolean includeResults, final String caption, final String brand) {
        this(AssertAs.notNull(p, "paper").getId(), p.getFirstAuthor(), String.valueOf(p.getPublicationYear()), p.getCodesOf(CodeClassId.CC1), p.getCodesOf(CodeClassId.CC4),
                p.getCodesOf(CodeClassId.CC7), p.getGoals(), p.getTitle(), (includeResults ? p.getResult() : ""), caption, brand);
    }

    /**
     * Instantiation with all individual fields (those that are part of a {@link Paper} and all other from the other constructor.
     */
    public PaperSummaryTable(final Long id, final String firstAuthor, final String publicationYear, final List<Code> codesOfClass1, final List<Code> codesOfClass4, final List<Code> codesOfClass7,
            final String goals, final String title, final String result, final String caption, final String brand) {
        this.id = id != null ? String.valueOf(id) : "";
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
    }

    public String getId() {
        return id;
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

}