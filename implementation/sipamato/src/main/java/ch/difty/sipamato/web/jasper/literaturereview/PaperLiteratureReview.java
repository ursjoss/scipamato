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
     * Instantiation with a {@link Paper} and additional fields
     *
     * @param p
     *      the paper
     * @param caption
     *      localized caption
     * @param brand
     *      the application brand name
     */
    public PaperLiteratureReview(final Paper p, final String caption, final String brand, final String numberLabel) {
        this(AssertAs.notNull(p, "paper").getNumber(), p.getAuthors(), p.getPublicationYear(), p.getTitle(), p.getLocation(), p.getPmId(), caption, brand, numberLabel);
    }

    /**
     * Instantiation with all individual fields (those that are part of a {@link Paper} and all other from the other constructor.
     */
    public PaperLiteratureReview(final Long number, final String authors, final Integer publicationYear, final String title, final String location, final Integer pmId, final String caption,
            final String brand, final String numberLabel) {
        this.number = number != null ? String.valueOf(number) : "";
        this.authors = na(authors);
        this.publicationYear = publicationYear != null ? String.valueOf(publicationYear) : "";
        this.title = na(title);
        this.location = na(location);
        this.pubmedLink = makePubmedLink(pmId);

        this.caption = na(caption);
        this.brand = na(brand);
        this.numberLabel = na(numberLabel);
    }

    private String makePubmedLink(final Integer pmId) {
        if (pmId != null)
            return PUBMED_BASE + String.valueOf(pmId);
        else
            return "";
    }

    public String getNumber() {
        return number;
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

    public String getNumberLabel() {
        return numberLabel;
    }

}