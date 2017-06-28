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
    private final String goalsLabel;
    private final String goals;
    private final String populationPlaceLabel;
    private final String populationPlace;
    private final String methodsLabel;
    private final String methods;
    private final String resultEffectEstimateLabel;
    private final String resultEffectEstimate;
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
     * @param populationPlaceLabel
     *      localized label for the populationPlace field
     * @param methodsLabel
     *      localized label for the methods field
     * @param resultEffectEstimateLabel
     *      localized label for the resultEffectEstimate field
     * @param commentLabel
     *      localized label for the comment field
     * @param headerPart
     *      Static part of the header - will be supplemented with the id
     * @param brand
     *      Brand of the application
     */
    public PaperSummaryShort(final Paper p, final String goalsLabel, final String populationPlaceLabel, final String methodsLabel, final String resultEffectEstimateLabel, final String commentLabel,
            final String headerPart, final String brand) {
        this(p.getNumber(), p.getAuthors(), p.getTitle(), p.getLocation(), p.getGoals(), p.getPopulationPlace(), p.getMethods(), p.getResultEffectEstimate(), p.getComment(), goalsLabel,
                populationPlaceLabel, methodsLabel, resultEffectEstimateLabel, commentLabel, headerPart, brand, p.getCreatedByName());
    }

    /**
     * Instantiation with all individual fields (those that are part of a {@link Paper} and all other from the other constructor.
     */
    public PaperSummaryShort(final Long number, final String authors, final String title, final String location, final String goals, final String populationPlace, final String methods,
            final String resultEffectEstimate, final String comment, final String goalsLabel, final String populationPlaceLabel, final String methodsLabel, final String resultEffectEstimateLabel,
            final String commentLabel, final String headerPart, final String brand, final String createdBy) {
        this.number = number != null ? String.valueOf(number) : "";
        this.authors = na(authors);
        this.title = na(title);
        this.location = na(location);
        this.goals = na(goals);
        this.populationPlace = na(populationPlace);
        this.methods = na(methods);
        this.resultEffectEstimate = na(resultEffectEstimate);
        this.comment = na(comment);

        this.goalsLabel = na(goalsLabel, this.goals);
        this.populationPlaceLabel = na(populationPlaceLabel, this.populationPlace);
        this.methodsLabel = na(methodsLabel, this.methods);
        this.resultEffectEstimateLabel = na(resultEffectEstimateLabel, this.resultEffectEstimate);
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

    public String getGoalsLabel() {
        return goalsLabel;
    }

    public String getGoals() {
        return goals;
    }

    public String getPopulationPlaceLabel() {
        return populationPlaceLabel;
    }

    public String getPopulationPlace() {
        return populationPlace;
    }

    public String getMethodsLabel() {
        return methodsLabel;
    }

    public String getMethods() {
        return methods;
    }

    public String getResultEffectEstimateLabel() {
        return resultEffectEstimateLabel;
    }

    public String getResultEffectEstimate() {
        return resultEffectEstimate;
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