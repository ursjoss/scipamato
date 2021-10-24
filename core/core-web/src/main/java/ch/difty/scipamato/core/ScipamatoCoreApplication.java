package ch.difty.scipamato.core;

import static ch.difty.scipamato.common.web.AbstractPage.FOOTER_CONTAINER;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, R2dbcAutoConfiguration.class })
@EnableFeignClients
@EnableCaching
@ComponentScan(basePackages = "ch.difty.scipamato")
public class ScipamatoCoreApplication extends WicketBootSecuredWebApplication {

    public static void main(@NotNull String[] args) {
        new SpringApplicationBuilder()
            .sources(ScipamatoCoreApplication.class)
            .run(args);
    }

    @Override
    protected void init() {
        // TODO consider making it CSP compliant
        getCspSettings().blocking().disabled();

        super.init();

        // enable putting JavaScript into Footer Container
        getHeaderResponseDecorators().add(r -> new JavaScriptFilteredIntoFooterHeaderResponse(r, FOOTER_CONTAINER));

        registerJasperJrxmlFilesWithPackageResourceGuard();
    }

    // Allow access to jrxml jasper report definition files
    private void registerJasperJrxmlFilesWithPackageResourceGuard() {
        final IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
        if (packageResourceGuard instanceof SecurePackageResourceGuard) {
            final SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
            guard.addPattern("+*.jrxml");
        }
    }

    @NotNull
    @Override
    public Session newSession(@NotNull Request request, @NotNull Response response) {
        return new ScipamatoSession(request);
    }
}
