package ch.difty.scipamato.core;

import org.jetbrains.annotations.Nullable;

/**
 * Accessor methods to the attachment fields for searching attachment specific stuff.
 *
 * @author u.joss
 */
public interface AttachmentAware {

    @Nullable
    Boolean getHasAttachments();

    void setHasAttachments(@Nullable Boolean isWithAttachments);

    @Nullable
    String getAttachmentNameMask();

    void setAttachmentNameMask(@Nullable String attachmentNameMask);
}
