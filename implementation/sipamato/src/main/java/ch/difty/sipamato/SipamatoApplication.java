package ch.difty.sipamato;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.springframework.boot.SpringApplication;

import com.giffing.wicket.spring.boot.starter.app.WicketBootWebApplication;
import com.giffing.wicket.spring.boot.starter.context.WicketSpringBootApplication;

@WicketSpringBootApplication
public class SipamatoApplication extends WicketBootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SipamatoApplication.class, args);
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return SipamatoHomePage.class;
    }
}
