package ch.difty.sipamato;

import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;

@SpringBootApplication
@EnableFeignClients
public class SipamatoApplication extends WicketBootSecuredWebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SipamatoApplication.class).run(args);
    }

    @Override
    protected void init() {
        super.init();

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

}
