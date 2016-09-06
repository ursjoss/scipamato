package ch.difty.sipamato.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;

import ch.difty.sipamato.web.pages.home.SipamatoHomePage;

@SpringBootApplication
public class SipamatoApplication extends WicketBootSecuredWebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SipamatoApplication.class).run(args);
    }

    @Override
    protected void init() {
        super.init();
        mountPackage("/", SipamatoHomePage.class);
    }

}
