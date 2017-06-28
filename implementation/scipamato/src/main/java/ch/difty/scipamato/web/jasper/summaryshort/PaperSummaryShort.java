package ch.difty.scipamato.web.jasper.summaryshort;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.JasperEntity;

/**
 * DTO to feed the PaperSummaryShortDataSource
 *
 * @author u.joss
 */
public class PaperSummaryShort extends JasperEntity {
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
     * Instantiation with a {@link Paper} and additional fields
     *
     * @param p
     *      the paper
     * @param populationLabel
     *      localized label for the population field
     * @param methodsLabel
     *      localized label for the methods field
     * @param resultLabel
     *      localized label for the result field
     * @param commentLabel
     *      localized label for the comment field
     * @param headerPart
     *      Static part of the header - will be supplemented with the id
     * @param brand
     *      Brand of the application
     */
    public PaperSummaryShort(final Paper p, final String populationLabel, final String methodsLabel, final String resultLabel, final String commentLabel, final String headerPart, final String brand) {
        this(p.getNumber(), p.getAuthors(), p.getTitle(), p.getLocation(), p.getGoals(), p.getPopulation(), p.getMethods(), p.getResult(), p.getComment(), populationLabel, methodsLabel, resultLabel,
                commentLabel, headerPart, brand, p.getCreatedByName());
    }

    /**
     * Instantiation with all individual fields (those that are part of a {@link Paper} and all other from the other constructor.
     */
    public PaperSummaryShort(final Long number, final String authors, final String title, final String location, final String goals, final String population, final String methods, final String result,
            final String comment, final String populationLabel, final String methodsLabel, final String resultLabel, final String commentLabel, final String headerPart, final String brand,
            final String createdBy) {
        this.number = number != null ? String.valueOf(number) : "";
        this.authors = na(authors);
        this.title = na(title);
        this.location = na(location);
        this.goals = na(goals);
        this.population = na(population);
        this.methods = na(methods);
        this.result = na(result);
        this.comment = na(comment);

        this.populationLabel = na(populationLabel, this.population);
        this.methodsLabel = na(methodsLabel, this.methods);
        this.resultLabel = na(resultLabel, this.result);
        this.commentLabel = na(commentLabel, this.comment);

        this.header = makeHeader(number, headerPart);
        this.brand = na(brand);
        this.createdBy = na(createdBy);
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