package ch.difty.sipamato.web;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;

import ch.difty.sipamato.web.pages.home.SipamatoHomePage;

@SpringBootApplication
public class SipamatoApplication extends WicketBootSecuredWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SipamatoApplication.class, args);
    }

    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        mountPackage("/", SipamatoHomePage.class);
    }

}
