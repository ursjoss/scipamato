package ch.difty.scipamato.core.web.code;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.DefinitionProvider;

public class CodeDefinitionProvider extends DefinitionProvider<CodeDefinition, CodeFilter, CodeService> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private CodeService service;

    CodeDefinitionProvider() {
        this(null);
    }

    CodeDefinitionProvider(@Nullable final CodeFilter filter) {
        super(filter);
        setSort(CodeDefinition.CodeDefinitionFields.SORT.getFieldName(), SortOrder.ASCENDING);
    }

    @NotNull
    @Override
    protected CodeService getService() {
        return service;
    }

    @NotNull
    @Override
    protected CodeFilter newFilter() {
        return new CodeFilter();
    }
}
