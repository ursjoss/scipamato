package ch.difty.scipamato.publ;

import static ch.difty.scipamato.common.web.AbstractPage.FOOTER_CONTAINER;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = "ch.difty.scipamato")
@Slf4j
public class ScipamatoPublicApplication extends WicketBootSecuredWebApplication {

    private final ScipamatoPublicProperties applicationProperties;

    public ScipamatoPublicApplication(@NotNull final ScipamatoPublicProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(ScipamatoPublicApplication.class)
            .run(args);
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new ScipamatoPublicSession(request);
    }

    @Override
    protected void init() {
        // temporarily disable CSS TODO consider activating
        getCspSettings().blocking().disabled();

        super.init();

        // enable putting JavaScript into Footer Container
        getHeaderResponseDecorators().add(r -> new JavaScriptFilteredIntoFooterHeaderResponse(r, FOOTER_CONTAINER));

        logSpecialConfiguration();
    }

    // package-private for testing purposes
    void logSpecialConfiguration() {
        if (applicationProperties.isCommercialFontPresent())
            log.info("CommercialFonts enabled.");

        if (applicationProperties.isResponsiveIframeSupportEnabled())
            log.info("Responsive iframes using PymJs enabled.");
    }
}
