package ch.difty.scipamato.core.web.common;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.ScipamatoSession;

@Component
public class CoreWebSessionFacade implements ScipamatoWebSessionFacade {

    @Override
    public String getLanguageCode() {
        return ScipamatoSession.get()
            .getLocale()
            .getLanguage();
    }

    @Override
    public ItemNavigator<Long> getPaperIdManager() {
        return ScipamatoSession.get()
            .getPaperIdManager();
    }

}
