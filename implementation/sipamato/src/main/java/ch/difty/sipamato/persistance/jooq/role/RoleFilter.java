package ch.difty.sipamato.persistance.jooq.role;

import java.io.Serializable;

import ch.difty.sipamato.entity.filter.SipamatoFilter;

public class RoleFilter extends SipamatoFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NAME_MASK = "nameMask";
    public static final String COMMENT_MASK = "nameMask";

    private String nameMask;
    private String commentMask;

    public String getNameMask() {
        return nameMask;
    }

    public void setNameMask(String nameMask) {
        this.nameMask = nameMask;
    }

    public String getCommentMask() {
        return commentMask;
    }

    public void setCommentMask(String commentMask) {
        this.commentMask = commentMask;
    }
}
