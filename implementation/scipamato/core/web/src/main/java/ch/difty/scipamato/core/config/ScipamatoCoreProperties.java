package ch.difty.scipamato.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.AbstractScipamatoProperties;
import ch.difty.scipamato.common.config.MavenProperties;
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

/**
 * This bean is used to evaluate all environment properties used in the
 * application in one place and serve those as bean to wherever they are used.
 *
 * @author u.joss
 * @see <a href=
 *     "https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/">https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/</a>
 */
@Component
public class ScipamatoCoreProperties extends AbstractScipamatoProperties<ScipamatoProperties>
    implements ApplicationCoreProperties {

    private static final long serialVersionUID = 1L;

    public ScipamatoCoreProperties(@NotNull final ScipamatoProperties scipamatoProperties,
        @NotNull final MavenProperties mavenProperties) {
        super(scipamatoProperties, mavenProperties);
    }

    @NotNull
    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return getScipamatoProperties().getAuthorParserStrategy();
    }

    @NotNull
    @Override
    public RisExporterStrategy getRisExporterStrategy() {
        return getScipamatoProperties().getRisExporterStrategy();
    }

    @Override
    public long getMinimumPaperNumberToBeRecycled() {
        return getScipamatoProperties().getPaperNumberMinimumToBeRecycled();
    }

    @Nullable
    @Override
    public String getPubmedApiKey() {
        return getScipamatoProperties().getPubmedApiKey();
    }
}
