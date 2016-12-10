package ch.difty.sipamato.persistance.jooq.user;

import java.io.Serializable;

import ch.difty.sipamato.entity.filter.SipamatoFilter;

public class UserFilter extends SipamatoFilter implements Serializable {

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
