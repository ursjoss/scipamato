package ch.difty.scipamato.publ;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = "ch.difty.scipamato")
public class ScipamatoPublicApplication extends WicketBootSecuredWebApplication {

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
        super.init();

        // enable putting JavaScript into Footer Container
        setHeaderResponseDecorator(r -> new JavaScriptFilteredIntoFooterHeaderResponse(r, "footer-container"));
    }

}
