package ch.difty.scipamato.core.web.codeclass;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.common.DefinitionListPage;

@MountPath("codeclasses")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue" })
public class CodeClassListPage
    extends DefinitionListPage<CodeClassDefinition, CodeClassFilter, CodeClassService, CodeClassDefinitionProvider> {

    private static final long serialVersionUID = 1L;

    public CodeClassListPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected CodeClassFilter newFilter() {
        return new CodeClassFilter();
    }

    @Override
    protected CodeClassDefinitionProvider newProvider(final CodeClassFilter filter) {
        return new CodeClassDefinitionProvider(filter);
    }

    @Override
    protected Panel newFilterPanel(final String id) {
        return new CodeClassListFilterPanel(id, getProvider());
    }

    @Override
    protected Panel newResultPanel(final String id) {
        return new CodeClassListResultPanel(id, getProvider());
    }

}