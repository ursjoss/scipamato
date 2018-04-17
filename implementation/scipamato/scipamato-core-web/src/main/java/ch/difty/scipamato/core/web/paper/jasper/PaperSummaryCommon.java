package ch.difty.scipamato.core.web.paper.jasper;

import ch.difty.scipamato.core.entity.Paper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Common base class for PaperSummary entities.
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class PaperSummaryCommon extends JasperEntity {

    private static final long serialVersionUID = 1L;

    private final String number;
    private final String authors;
    private final String title;
    private final String location;

    private final String goalsLabel;
    private final String goals;
    private final String methodsLabel;
    private final String methods;
    private final String commentLabel;
    private final String comment;

    private final String header;
    private final String brand;
    private final String createdBy;

    /**
     * Instantiation with a {@link Paper} and the {@link ReportHeaderFields}
     *
     * @param p
     *            the paper with the relevant fields
     * @param rhf
     *            the reportHeaderFields with the localized field headers
     */
    protected PaperSummaryCommon(final Paper p, final ReportHeaderFields rhf) {
        this(p.getNumber(), p.getAuthors(), p.getTitle(), p.getLocation(), p.getGoals(), p.getMethods(), p.getComment(),
                rhf.getGoalsLabel(), rhf.getMethodsLabel(), rhf.getCommentLabel(), rhf.getHeaderPart(), rhf.getBrand(),
                p.getCreatedByName());
    }

    // headerPart is expected not to be null (it can't in rhf)
    private PaperSummaryCommon(final Long number, final String authors, final String title, final String location,
            final String goals, final String methods, final String comment, final String goalsLabel,
            final String methodsLabel, final String commentLabel, final String headerPart, final String brand,
            final String createdBy) {

        this.number = number != null ? String.valueOf(number) : "";
        this.authors = na(authors);
        this.title = na(title);
        this.location = na(location);
        this.goals = na(goals);
        this.methods = na(methods);
        this.comment = na(comment);

        this.goalsLabel = na2(goalsLabel, goals);
        this.methodsLabel = na2(methodsLabel, methods);
        this.commentLabel = na2(commentLabel, comment);

        this.header = makeHeader(number, headerPart);
        this.brand = na(brand);
        this.createdBy = na(createdBy);
    }

    private String makeHeader(final Long number, final String headerPart) {
        final StringBuilder sb = new StringBuilder();
        sb.append(headerPart);
        if (number != null) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(number);
        }
        return sb.toString();
    }

}
