package ch.difty.scipamato.common.web;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.navigator.LongNavigator;

@Component
@Primary
public class TestWebSessionFacade implements ScipamatoWebSessionFacade {

    @Override
    public String getLanguageCode() {
        return "de";
    }

    @Override
    public ItemNavigator<Long> getPaperIdManager() {
        return new LongNavigator();
    }

    @Override
    public boolean hasAtLeastOneRoleOutOf(final String... roles) {
        return true;
    }
}
