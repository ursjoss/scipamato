package ch.difty.sipamato;

import org.apache.wicket.Page;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.giffing.wicket.spring.boot.starter.app.WicketBootWebApplication;
import com.giffing.wicket.spring.boot.starter.context.WicketSpringBootApplication;

@WicketSpringBootApplication
public class SipamatoApplication extends WicketBootWebApplication implements ApplicationContextAware {

    private ApplicationContext ctx;

    public static void main(String[] args) {
        SpringApplication.run(SipamatoApplication.class, args);
    }

    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this, ctx, true));
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return SipamatoHomePage.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

}
