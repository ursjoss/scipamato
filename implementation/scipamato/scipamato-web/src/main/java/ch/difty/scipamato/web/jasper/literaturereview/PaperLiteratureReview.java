package ch.difty.scipamato.web.jasper.literaturereview;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.JasperEntity;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO to feed the PaperLiteratureReviewDataSource
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperLiteratureReview extends JasperEntity {

    // HARDCODED - consider moving to settings
    private static final String PUBMED_BASE = "https://www.ncbi.nlm.nih.gov/pubmed/";

    private static final long serialVersionUID = 1L;

    private final String number;
    private final String authors;
    private final String publicationYear;
    private final String title;
    private final String location;
    private final String pubmedLink;

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
    public PaperLiteratureReview(final Paper p, final ReportHeaderFields rhf) {
        AssertAs.notNull(p, "p");
        AssertAs.notNull(rhf, "rhf");

        final Long no = p.getNumber();
        this.number = no != null ? String.valueOf(no) : "";
        this.authors = na(p.getAuthors());
        this.publicationYear = p.getPublicationYear() != null ? String.valueOf(p.getPublicationYear()) : "";
        this.title = na(p.getTitle());
        this.location = na(p.getLocation());
        this.pubmedLink = makePubmedLink(p.getPmId());

        this.caption = na(rhf.getCaptionLabel());
        this.brand = na(rhf.getBrand());
        this.numberLabel = na(rhf.getNumberLabel());
    }

    private String makePubmedLink(final Integer pmId) {
        if (pmId != null)
            return PUBMED_BASE + String.valueOf(pmId);
        else
            return "";
    }

}