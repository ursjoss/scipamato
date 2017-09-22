package ch.difty.scipamato.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Attachment to a paper.<p>
 *
 * Note that typically, the paper repository will automatically load the attachments with the paper, but
 * not with the actual binary attachment content. This will have to be loaded separately.<p>
 *
 * The repo will not save the attachments with the paper. They will have to be saved separately too.
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "content" })
public class PaperAttachment extends IdScipamatoEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private static final long BYTES_PER_KB = 1024;

    public static final String PAPER_ID = "paperId";
    public static final String NAME = "name";
    public static final String CONTENT = "content";
    public static final String CONTENT_TYPE = "contentType";
    public static final String SIZE = "size";
    public static final String SIZE_KB = "sizeKiloBytes";

    private Long paperId;
    private String name;
    private String contentType;
    private Long size;

    // not persisted or loaded when loading through paper
    private byte[] content;

    public PaperAttachment() {
    }

    public PaperAttachment(final Integer id, final Long paperId, final String name, final byte[] content, final String contentType, final Long size) {
        setId(id);
        setPaperId(paperId);
        setName(name);
        setContent(content);
        setContentType(contentType);
        setSize(size);
    }

    /**
     * @return the size in bytes
     */
    public Long getSize() {
        return size;
    }

    /**
     * @return the size in kilo bytes (rounded up)
     */
    public Long getSizeKiloBytes() {
        if (size == null)
            return null;
        if ((size % BYTES_PER_KB) == 0)
            return size / BYTES_PER_KB;
        return size / BYTES_PER_KB + 1;
    }

    @Override
    public String getDisplayValue() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PaperAttachment[paperId=");
        builder.append(paperId);
        builder.append(",name=");
        builder.append(name);
        builder.append(",id=");
        builder.append(getId());
        builder.append("]");
        return builder.toString();
    }

}
