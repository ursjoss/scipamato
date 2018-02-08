package ch.difty.scipamato.publ.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "scipamato")
@Getter
@Setter
public class ScipamatoProperties {

    /**
     * Brand name of the application. Appears e.g. in the Navbar.
     */
    private String brand = "SciPaMaTo-Public";

    /**
     * Default localization. Normally the browser locale is used.
     */
    private String defaultLocalization = "en";

    /**
     * The base url used to access the Pubmed API.
     */
    private String pubmedBaseUrl = "https://www.ncbi.nlm.nih.gov/pubmed/";

    /**
     * Port from where an unsecured http connection is forwarded to the secured
     * https port (@literal server.port}. Only has an effect if https is configured.
     */
    private Integer redirectFromPort;

    /**
     * Indicates whether the commercial font MetaOT is present and can be used.
     * <p>
     * The location where SciPaMaTo expectes the font is:
     *
     * <pre>
     * src/main/resources/ch/difty/scipamato/publ/web/resources/MetaOT/
     * </pre>
     */
    private boolean commercialFontPresent;
}
