package ch.difty.scipamato.publ.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;

@SpringBootApplication
@ComponentScan(basePackages = "ch.difty.scipamato")
public class TestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(TestApplication.class)
            .run(args);
    }

    @Bean
    public ApplicationPublicProperties applicationProperties() {
        return new ApplicationPublicProperties() {

            @Override
            public String getBuildVersion() {
                return "vxy";
            }

            @Override
            public String getDefaultLocalization() {
                return "de";
            }

            @Override
            public String getBrand() {
                return "scipamato";
            }

            @Override
            public String getTitleOrBrand() {
                return getBrand();
            }

            @Override
            public String getPubmedBaseUrl() {
                return "http://pubmed/";
            }

            @Override
            public Integer getRedirectFromPort() {
                return 8081;
            }

            @Override
            public int getMultiSelectBoxActionBoxWithMoreEntriesThan() {
                return 4;
            }

            @Override
            public boolean isCommercialFontPresent() {
                return false;
            }

            @Override
            public boolean isLessUsedOverCss() {
                return false;
            }

            @Override
            public boolean isNavbarVisibleByDefault() {
                return false;
            }

            @Override
            public String getCmsUrlSearchPage() {
                return null;
            }

            @Override
            public String getCmsUrlNewStudyPage() {
                return null;
            }

            @Override
            public int getAuthorsAbbreviatedMaxLength() {
                return 50;
            }

            @Override
            public boolean isResponsiveIframeSupportEnabled() {
                return false;
            }

            @Override
            public String getManagementUserName() {
                return "admin";
            }

            @Override
            public String getManagementUserPassword() {
                return "admin";
            }

            @Override
            public int getNumberOfPreviousNewslettersInArchive() {
                return 14;
            }
        };
    }
}
