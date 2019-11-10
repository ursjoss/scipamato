package ch.difty.scipamato.publ.web.common;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.ScipamatoPublicSession;

@Component
public class PublicWebSessionFacade implements ScipamatoWebSessionFacade {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Override
    public String getLanguageCode() {
        return ScipamatoPublicSession
            .get()
            .getLocale()
            .getLanguage();
    }

    @NotNull
    @Override
    public ItemNavigator<Long> getPaperIdManager() {
        return ScipamatoPublicSession
            .get()
            .getPaperIdManager();
    }

    @Override
    public boolean hasAtLeastOneRoleOutOf(@NotNull final String... roles) {
        return false;
    }
}
