package ch.difty.scipamato.common.web.autoconfiguration;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;

import de.agilecoders.wicket.core.Bootstrap;

@ApplicationInitExtension
@ConditionalOnProperty(prefix = "wicket.external.agilecoders.bootstrap", value = "enabled", matchIfMissing = true)
@ConditionalOnClass(BootstrapConfig.class)
public class BootstrapConfig implements WicketApplicationInitConfiguration {

    public BootstrapConfig() {
    }

    @Override
    public void init(WebApplication webApplication) {
        Bootstrap.install(webApplication);
    }

}
