package ch.difty.scipamato.core.web.paper.jasper.literaturereview;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.web.paper.jasper.JasperEntity;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

/**
 * DTO to feed the PaperLiteratureReviewDataSource
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperLiteratureReview extends JasperEntity {

    private static final long serialVersionUID = 1L;

    private final String number;
    private final String authors;
    private final String publicationYear;
    private final String title;
    private final String goals;
    private final String location;
    private final String doi;
    private final String pubmedLink;

    private final String caption;
    private final String brand;
    private final String numberLabel;

    /**
     * Instantiation with a {@link Paper} and the {@link ReportHeaderFields}
     *
     * @param p
     *     the paper with the relevant fields
     * @param rhf
     *     the reportHeaderFields with the localized field headers
     */
    public PaperLiteratureReview(final Paper p, final ReportHeaderFields rhf) {
        AssertAs.INSTANCE.notNull(p, "p");
        AssertAs.INSTANCE.notNull(rhf, "rhf");
        AssertAs.INSTANCE.notNull(rhf.getPubmedBaseUrl(), "pubmedBaseUrl");

        final Long no = p.getNumber();
        this.number = no != null ? String.valueOf(no) : "";
        this.authors = na(p.getAuthors());
        this.publicationYear = p.getPublicationYear() != null ? String.valueOf(p.getPublicationYear()) : "";
        this.title = na(p.getTitle());
        this.location = na(p.getLocation());
        this.pubmedLink = makePubmedLink(rhf.getPubmedBaseUrl(), p.getPmId());
        this.goals = na(p.getGoals());
        this.caption = na(rhf.getCaptionLabel());
        this.doi = na(p.getDoi());
        this.brand = na(rhf.getBrand());
        this.numberLabel = na(rhf.getNumberLabel());
    }

    private String makePubmedLink(final String pubmedBaseUrl, final Integer pmId) {
        if (pmId != null)
            return pubmedBaseUrl + pmId;
        else
            return "";
    }

}