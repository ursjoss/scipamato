package ch.difty.scipamato.core.web.codeclass;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.common.DefinitionListPage;

@MountPath("codeclasses")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue" })
public class CodeClassListPage extends DefinitionListPage<CodeClassDefinition, CodeClassFilter, CodeClassService, CodeClassDefinitionProvider> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public CodeClassListPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    @NotNull
    @Override
    protected CodeClassFilter newFilter() {
        return new CodeClassFilter();
    }

    @NotNull
    @Override
    protected CodeClassDefinitionProvider newProvider(@Nullable final CodeClassFilter filter) {
        return new CodeClassDefinitionProvider(filter);
    }

    @NotNull
    @Override
    protected Panel newFilterPanel(@NotNull final String id) {
        return new CodeClassListFilterPanel(id, getProvider());
    }

    @NotNull
    @Override
    protected Panel newResultPanel(@NotNull final String id) {
        return new CodeClassListResultPanel(id, getProvider());
    }
}
