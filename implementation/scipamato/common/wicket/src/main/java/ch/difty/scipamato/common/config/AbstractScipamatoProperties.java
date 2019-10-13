package ch.difty.scipamato.common.config;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common abstract base class for ScipamatoProperties in both the core and
 * public module.
 *
 * @param <SP>
 *     the concrete type of the ScipamatoProperties class (extending
 *     {@link ScipamatoBaseProperties}.
 * @author Urs Joss
 */
public abstract class AbstractScipamatoProperties<SP extends ScipamatoBaseProperties> implements ApplicationProperties {

    @NotNull
    private final SP              scipamatoProperties;
    @NotNull
    private final MavenProperties mavenProperties;

    protected AbstractScipamatoProperties(@NotNull final SP scipamatoProperties,
        @NotNull final MavenProperties mavenProperties) {
        this.scipamatoProperties = scipamatoProperties;
        this.mavenProperties = mavenProperties;
    }

    @NotNull
    protected SP getScipamatoProperties() {
        return scipamatoProperties;
    }

    @SuppressWarnings("WeakerAccess")
    @NotNull
    MavenProperties getMavenProperties() {
        return mavenProperties;
    }

    @Nullable
    @Override
    public String getBuildVersion() {
        return getMavenProperties().getVersion();
    }

    @NotNull
    @Override
    public String getDefaultLocalization() {
        return getScipamatoProperties().getDefaultLocalization();
    }

    @NotNull
    @Override
    public String getBrand() {
        return getScipamatoProperties().getBrand();
    }

    @NotNull
    @Override
    public String getTitleOrBrand() {
        final String pageTitle = getScipamatoProperties().getPageTitle();
        return Objects.requireNonNullElseGet(pageTitle, this::getBrand);
    }

    @NotNull
    @Override
    public String getPubmedBaseUrl() {
        return getScipamatoProperties().getPubmedBaseUrl();
    }

    @Nullable
    @Override
    public String getCmsUrlSearchPage() {
        return getScipamatoProperties().getCmsUrlSearchPage();
    }

    @Nullable
    @Override
    public Integer getRedirectFromPort() {
        return getScipamatoProperties().getRedirectFromPort();
    }

    @Override
    public int getMultiSelectBoxActionBoxWithMoreEntriesThan() {
        return getScipamatoProperties().getMultiSelectBoxActionBoxWithMoreEntriesThan();
    }
}
