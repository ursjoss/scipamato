package ch.difty.scipamato.core.web.code;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.DefinitionListPage;

@MountPath("codes")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "WeakerAccess" })
public class CodeListPage extends DefinitionListPage<CodeDefinition, CodeFilter, CodeService, CodeDefinitionProvider> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private CodeService service;

    public CodeListPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected CodeFilter newFilter() {
        final CodeFilter filter = new CodeFilter();
        filter.setCodeClass(service.getCodeClass1(getLanguageCode()));
        return filter;
    }

    @Override
    protected CodeDefinitionProvider newProvider(final CodeFilter filter) {
        return new CodeDefinitionProvider(filter);
    }

    @Override
    protected Panel newFilterPanel(final String id) {
        return new CodeListFilterPanel(id, getProvider()) {

            @Override
            protected void doOnUpdate(final AjaxRequestTarget target) {
                modelChanged();
                target.add(getResultPanel());
            }

            @Override
            protected BootstrapAjaxButton doQueueNewCodeButton(final String id) {
                final BootstrapAjaxButton newButton = newResponsePageButton(id,
                    () -> new CodeEditPage(Model.of(service.newUnpersistedCodeDefinition()),
                        getPage().getPageReference()));
                newButton.add(new LoadingBehavior(new StringResourceModel(id + LOADING_RESOURCE_TAG, this, null)));
                return newButton;
            }
        };
    }

    @Override
    protected Panel newResultPanel(final String id) {
        final CodeListResultPanel panel = new CodeListResultPanel(id, getProvider());
        panel.setOutputMarkupId(true);
        return panel;
    }

}