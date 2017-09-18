package ch.difty.scipamato.entity.filter;

import java.io.Serializable;

public class UserFilter extends ScipamatoFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NAME_MASK = "nameMask";
    public static final String EMAIL_MASK = "emailMask";
    public static final String ENABLED = "enabled";

    private String nameMask;
    private String emailMask;
    private Boolean enabled;

    public String getNameMask() {
        return nameMask;
    }

    public void setNameMask(String nameMask) {
        this.nameMask = nameMask;
    }

    public String getEmailMask() {
        return emailMask;
    }

    public void setEmailMask(String emailMask) {
        this.emailMask = emailMask;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}
