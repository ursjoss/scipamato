package ch.difty.sipamato.entity.projection;

import ch.difty.sipamato.entity.SipamatoEntity;

public class PaperSlim extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstAuthor;
    private int publicationYear;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public void setFirstAuthor(String firstAuthor) {
        this.firstAuthor = firstAuthor;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(firstAuthor).append(" (").append(publicationYear).append("): ");
        sb.append(title).append(".");
        return sb.toString();
    }

}
