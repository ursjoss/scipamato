package ch.difty.sipamato.web.jasper.literaturereview;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.web.jasper.JasperEntity;

/**
 * DTO to feed the PaperLiteratureReviewDataSource
 *
 * @author u.joss
 */
public class PaperLiteratureReview extends JasperEntity {

    // HARDCODED TODO - consider moving to settings
    private static final String PUBMED_BASE = "https://www.ncbi.nlm.nih.gov/pubmed/";

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String authors;
    private final String publicationYear;
    private final String title;
    private final String location;
    private final String pubmedLink;

    private final String caption;
    private final String brand;

    /**
     * Instantiation with a {@link Paper} and additional fields
     *
     * @param p
     *      the paper
     * @param caption
     *      localized caption
     * @param brand
     *      the application brand name
     */
    public PaperLiteratureReview(final Paper p, final String caption, final String brand) {
        this(AssertAs.notNull(p, "paper").getId(), p.getAuthors(), p.getPublicationYear(), p.getTitle(), p.getLocation(), p.getPmId(), caption, brand);
    }

    /**
     * Instantiation with all individual fields (those that are part of a {@link Paper} and all other from the other constructor.
     */
    public PaperLiteratureReview(final Long id, final String authors, final Integer publicationYear, final String title, final String location, final Integer pmId, final String caption,
            final String brand) {
        this.id = id != null ? String.valueOf(id) : "";
        this.authors = na(authors);
        this.publicationYear = publicationYear != null ? String.valueOf(publicationYear) : "";
        this.title = na(title);
        this.location = na(location);
        this.pubmedLink = makePubmedLink(pmId);

        this.caption = na(caption);
        this.brand = na(brand);
    }

    private String makePubmedLink(final Integer pmId) {
        if (pmId != null)
            return PUBMED_BASE + String.valueOf(pmId);
        else
            return "";
    }

    public String getId() {
        return id;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getPubmedLink() {
        return pubmedLink;
    }

    public String getCaption() {
        return caption;
    }

    public String getBrand() {
        return brand;
    }

}