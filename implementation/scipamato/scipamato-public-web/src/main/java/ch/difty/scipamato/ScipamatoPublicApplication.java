package ch.difty.scipamato;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;

@SpringBootApplication
public class ScipamatoPublicApplication extends WicketBootSecuredWebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(ScipamatoPublicApplication.class).run(args);
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new ScipamatoPublicSession(request);
    }

}
