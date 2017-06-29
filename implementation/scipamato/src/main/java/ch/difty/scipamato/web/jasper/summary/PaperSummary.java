package ch.difty.scipamato.web.jasper.summary;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.web.jasper.JasperEntity;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

/**
 * DTO to feed the PaperSummaryDataSource
 *
 * @author u.joss
 */
public class PaperSummary extends JasperEntity {
    private static final long serialVersionUID = 1L;

    private final String number;
    private final String authors;
    private final String title;
    private final String location;
    private final String goals;
    private final String populationLabel;
    private final String population;
    private final String methodsLabel;
    private final String methods;
    private final String resultLabel;
    private final String result;
    private final String comment;
    private final String commentLabel;

    private final String header;
    private final String brand;
    private final String createdBy;

    /**
     * Instantiation with a {@link Paper} and the {@link ReportHeaderFields}
     *
     * @param p
     *      the paper with the relevant fields
     * @param rhf
     *      the reportHeaderFields with the localized field headers
     */
    public PaperSummary(final Paper p, final ReportHeaderFields rhf) {
        AssertAs.notNull(p, "p");
        AssertAs.notNull(rhf, "rhf");

        final Long no = p.getNumber();
        this.number = no != null ? String.valueOf(no) : "";
        this.authors = na(p.getAuthors());
        this.title = na(p.getTitle());
        this.location = na(p.getLocation());
        this.goals = na(p.getGoals());
        this.population = na(p.getPopulation());
        this.methods = na(p.getMethods());
        this.result = na(p.getResult());
        this.comment = na(p.getComment());

        this.populationLabel = na2(rhf.getPopulationLabel(), population);
        this.methodsLabel = na2(rhf.getMethodsLabel(), methods);
        this.resultLabel = na2(rhf.getResultLabel(), result);
        this.commentLabel = na2(rhf.getCommentLabel(), comment);

        this.header = makeHeader(no, rhf.getHeaderPart());
        this.brand = na(rhf.getBrand());
        this.createdBy = na(p.getCreatedByName());
    }

    private String makeHeader(final Long number, final String headerPart) {
        final StringBuilder sb = new StringBuilder();
        if (headerPart != null) {
            sb.append(headerPart);
        }
        if (number != null) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(number);
        }
        return sb.toString();
    }

    public String getNumber() {
        return number;
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

    public String getResultLabel() {
        return resultLabel;
    }

    public String getResult() {
        return result;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentLabel() {
        return commentLabel;
    }

    public String getHeader() {
        return header;
    }

    public String getBrand() {
        return brand;
    }

    public String getCreatedBy() {
        return createdBy;
    }

}