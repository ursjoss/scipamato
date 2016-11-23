package ch.difty.sipamato.entity.projection;

import javax.validation.constraints.NotNull;

import ch.difty.sipamato.entity.SipamatoEntity;

public class PaperSlim extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull
    private String firstAuthor;
    @NotNull
    private Integer publicationYear;
    @NotNull
    private String title;

    public PaperSlim() {
    }

    public PaperSlim(Long id, String firstAuthor, Integer publicationYear, String title) {
        setId(id);
        setFirstAuthor(firstAuthor);
        setPublicationYear(publicationYear);
        setTitle(title);
    }

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

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
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
