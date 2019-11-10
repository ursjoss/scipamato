package ch.difty.scipamato.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * Attachment to a paper.
 * <p>
 * Note that typically, the paper repository will automatically load the
 * attachments with the paper, but not with the actual binary attachment
 * content. This will have to be loaded separately.
 * <p>
 * The repo will not save the attachments with the paper. They will have to be
 * saved separately too.
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "content" })
public class PaperAttachment extends IdScipamatoEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private static final long BYTES_PER_KB = 1024;

    private Long   paperId;
    private String name;
    private String contentType;
    private Long   size;

    public enum PaperAttachmentFields implements FieldEnumType {
        PAPER_ID("paperId"),
        NAME("name"),
        CONTENT("content"),
        CONTENT_TYPE("contentType"),
        SIZE("size"),
        SIZE_KB("sizeKiloBytes");

        private final String name;

        PaperAttachmentFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    // not persisted or loaded when loading through paper
    private byte[] content;

    public PaperAttachment() {
    }

    public PaperAttachment(@Nullable final Integer id, final Long paperId, final String name, final byte[] content,
        final String contentType, final Long size) {
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
    @Nullable
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

    @NotNull
    @Override
    public String toString() {
        return "PaperAttachment[paperId=" + paperId + ",name=" + name + ",id=" + getId() + "]";
    }
}
