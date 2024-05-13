package ch.difty.scipamato.core.web.code;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.DefinitionListPage;

@MountPath("codes")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue" })
public class CodeListPage extends DefinitionListPage<CodeDefinition, CodeFilter, CodeService, CodeDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    @SpringBean
    private CodeService service;

    public CodeListPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    @NotNull
    @Override
    protected CodeFilter newFilter() {
        final CodeFilter filter = new CodeFilter();
        filter.setCodeClass(service.getCodeClass1(getLanguageCode()));
        return filter;
    }

    @NotNull
    @Override
    protected CodeDefinitionProvider newProvider(@Nullable final CodeFilter filter) {
        return new CodeDefinitionProvider(filter);
    }

    @NotNull
    @Override
    protected Panel newFilterPanel(@NotNull final String id) {
        return new CodeListFilterPanel(id, getProvider()) {

            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Override
            protected void doOnUpdate(@NotNull final AjaxRequestTarget target) {
                modelChanged();
                target.add(getResultPanel());
            }

            @Override
            protected BootstrapAjaxButton doQueueNewCodeButton(@NotNull final String id) {
                return newResponsePageButton(id,
                    () -> new CodeEditPage(Model.of(service.newUnpersistedCodeDefinition()),
                        getPage().getPageReference()));
            }
        };
    }

    @NotNull
    @Override
    protected Panel newResultPanel(@NotNull final String id) {
        final CodeListResultPanel panel = new CodeListResultPanel(id, getProvider());
        panel.setOutputMarkupId(true);
        return panel;
    }
}
