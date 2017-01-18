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
    private final String user;
    private final String year;

    public PaperSummary(Long id, String authors, String title, String location, String goals, String population, String methods, String result, String populationLabel, String methodsLabel,
            String resultLabel, String headerPart, String brand, String user, LocalDateTime now) {
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
        this.user = na(user);
        this.year = String.valueOf(now.getYear());
    }

    private String makeHeader(Long id, String headerPart) {
        StringBuilder sb = new StringBuilder();
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

    private String na(String s) {
        return s != null ? s : "";
    }

    public PaperSummary(Paper p, String populationLabel, String methodsLabel, String resultsLabel, String headerPart, String brand, String user, LocalDateTime now) {
        this(p.getId(), p.getAuthors(), p.getTitle(), p.getLocation(), p.getGoals(), p.getPopulation(), p.getMethods(), p.getResult(), populationLabel, methodsLabel, resultsLabel, headerPart, brand,
                user, now);
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

    public String getUser() {
        return user;
    }

    public String getYear() {
        return year;
    }
}