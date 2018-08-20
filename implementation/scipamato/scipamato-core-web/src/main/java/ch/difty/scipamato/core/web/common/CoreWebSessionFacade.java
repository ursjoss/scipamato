package ch.difty.scipamato.core.web.common;

import java.util.Arrays;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.ScipamatoSession;

@Component
public class CoreWebSessionFacade implements ScipamatoWebSessionFacade {

    private static final long serialVersionUID = 1L;

    @Override
    public String getLanguageCode() {
        return ScipamatoSession
            .get()
            .getLocale()
            .getLanguage();
    }

    @Override
    public ItemNavigator<Long> getPaperIdManager() {
        return ScipamatoSession
            .get()
            .getPaperIdManager();
    }

    @Override
    public boolean hasAtLeastOneRoleOutOf(final String... roles) {
        final Roles authorizedRoles = ScipamatoSession
            .get()
            .getRoles();
        if (authorizedRoles != null)
            return Arrays
                .stream(roles)
                .anyMatch(authorizedRoles::hasRole);
        return false;
    }

}
