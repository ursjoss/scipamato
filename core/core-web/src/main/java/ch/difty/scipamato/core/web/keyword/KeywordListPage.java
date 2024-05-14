package ch.difty.scipamato.core.web.keyword;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.DefinitionListPage;

@MountPath("keywords")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue" })
public class KeywordListPage
    extends DefinitionListPage<KeywordDefinition, KeywordFilter, KeywordService, KeywordDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    @SpringBean
    private KeywordService service;

    public KeywordListPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    @NotNull
    @Override
    protected KeywordFilter newFilter() {
        return new KeywordFilter();
    }

    @NotNull
    @Override
    protected KeywordDefinitionProvider newProvider(@Nullable final KeywordFilter filter) {
        return new KeywordDefinitionProvider(filter);
    }

    @NotNull
    @Override
    protected Panel newFilterPanel(@NotNull final String id) {
        return new KeywordListFilterPanel(id, getProvider()) {
            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @NotNull
            @Override
            protected BootstrapAjaxButton doQueueNewKeywordButton(@NotNull final String id) {
                return newResponsePageButton(id,
                    () -> new KeywordEditPage(Model.of(service.newUnpersistedKeywordDefinition()),
                        getPage().getPageReference()));
            }
        };
    }

    @NotNull
    @Override
    protected Panel newResultPanel(@NotNull final String id) {
        return new KeywordListResultPanel(id, getProvider());
    }
}
