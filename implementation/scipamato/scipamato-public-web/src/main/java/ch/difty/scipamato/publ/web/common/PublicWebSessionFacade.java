package ch.difty.scipamato.publ.web.common;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.ScipamatoPublicSession;

@Component
public class PublicWebSessionFacade implements ScipamatoWebSessionFacade {

    @Override
    public String getLanguageCode() {
        return ScipamatoPublicSession.get()
            .getLocale()
            .getLanguage();
    }

    @Override
    public ItemNavigator<Long> getPaperIdManager() {
        return ScipamatoPublicSession.get()
            .getPaperIdManager();
    }

}
