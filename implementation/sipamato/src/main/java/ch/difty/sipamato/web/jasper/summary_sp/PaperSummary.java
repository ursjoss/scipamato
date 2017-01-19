package ch.difty.sipamato.web.jasper.summary_sp;

import java.io.Serializable;
import java.time.LocalDateTime;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.AssertAs;

/**
 * DTO to feed the PaperSummaryDataSource
 *
 * @author u.joss
 */
public class PaperSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
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

    private final String header;
    private final String brand;
    private final String createdBy;
    private final String year;

    /**
     * Instantiation with a {@link Paper} and additional fields
     *
     * @param p
     *      the paper
     * @param populationLabel
     *      localized label for the population field
     * @param methodsLabel
     *      localized label for the methods field
     * @param resultsLabel
     *      localized label for the result field
     * @param headerPart
     *      Static part of the header - will be supplemented with the id
     * @param brand
     *      Brand of the application
     * @param now
     *      current timestamp -> will be used to print the year
     */
    public PaperSummary(final Paper p, final String populationLabel, final String methodsLabel, final String resultsLabel, final String headerPart, final String brand, final LocalDateTime now) {
        this(p.getId(), p.getAuthors(), p.getTitle(), p.getLocation(), p.getGoals(), p.getPopulation(), p.getMethods(), p.getResult(), populationLabel, methodsLabel, resultsLabel, headerPart, brand,
                p.getCreatedByName(), now);
    }

    /**
     * Instantiation with all individual fields (those that are part of a {@link Paper} and all other from the other constructor.
     */
    public PaperSummary(final Long id, final String authors, final String title, final String location, final String goals, final String population, final String methods, final String result,
            final String populationLabel, final String methodsLabel, final String resultLabel, final String headerPart, final String brand, final String createdBy, final LocalDateTime now) {
        AssertAs.notNull(now, "now");
        this.id = id != null ? String.valueOf(id) : "";
        this.authors = na(authors);
        this.title = na(title);
        this.location = na(location);
        this.goals = na(goals);
        this.population = na(population);
        this.methods = na(methods);
        this.result = na(result);

        this.populationLabel = na(populationLabel);
        this.methodsLabel = na(methodsLabel);
        this.resultLabel = na(resultLabel);

        this.header = makeHeader(id, headerPart);
        this.brand = na(brand);
        this.createdBy = na(createdBy);
        this.year = String.valueOf(now.getYear());
    }

    private String makeHeader(final Long id, final String headerPart) {
        final StringBuilder sb = new StringBuilder();
        if (headerPart != null) {
            sb.append(headerPart);
        }
        if (id != null) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(id);
        }
        return sb.toString();
    }

    private String na(final String s) {
        return s != null ? s : "";
    }

    public String getId() {
        return id;
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

    public String getHeader() {
        return header;
    }

    public String getBrand() {
        return brand;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getYear() {
        return year;
    }
}