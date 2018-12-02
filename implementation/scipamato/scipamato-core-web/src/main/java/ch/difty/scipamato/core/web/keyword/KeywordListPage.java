package ch.difty.scipamato.core.web.keyword;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.DefinitionListPage;

@MountPath("keywords")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "WeakerAccess" })
public class KeywordListPage
    extends DefinitionListPage<KeywordDefinition, KeywordFilter, KeywordService, KeywordDefinitionProvider> {

    @SpringBean
    private KeywordService service;

    public KeywordListPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected KeywordFilter newFilter() {
        return new KeywordFilter();
    }

    @Override
    protected KeywordDefinitionProvider newProvider(final KeywordFilter filter) {
        return new KeywordDefinitionProvider(filter);
    }

    @Override
    protected Panel newFilterPanel(final String id) {
        return new KeywordListFilterPanel(id, getProvider()) {

            @Override
            protected BootstrapAjaxButton doQueueNewKeywordButton(final String id) {
                final BootstrapAjaxButton newButton = newResponsePageButton(id,
                    () -> new KeywordEditPage(Model.of(service.newUnpersistedKeywordDefinition()),
                        getPage().getPageReference()));
                newButton.add(new LoadingBehavior(new StringResourceModel(id + LOADING_RESOURCE_TAG, this, null)));
                return newButton;
            }
        };
    }

    @Override
    protected Panel newResultPanel(final String id) {
        return new KeywordListResultPanel(id, getProvider());
    }

}