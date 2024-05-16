package ch.difty.scipamato.core.web.common;

import static ch.difty.scipamato.common.web.WicketUtilsKt.LABEL_RESOURCE_TAG;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.DefinitionTranslation;

@SuppressWarnings("SameParameterValue")
@Slf4j
public abstract class DefinitionEditHeaderPanel<E extends DefinitionEntity<ID, T>, T extends DefinitionTranslation, ID> extends BasePanel<E> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    protected DefinitionEditHeaderPanel(@NotNull final String id, @Nullable final IModel<E> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        makeAndQueueFilterFields();
        makeAndQueueActionButtons();
    }

    protected abstract void makeAndQueueFilterFields();

    private void makeAndQueueActionButtons() {
        queue(newSubmitButton("submit"));
        queue(newBackButton("back"));
    }

    private BootstrapButton newSubmitButton(@NotNull final String id) {
        return new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG), Buttons.Type.Primary);
    }

    private BootstrapButton newBackButton(@NotNull final String id) {
        final BootstrapButton back = new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG), Buttons.Type.Default) {
            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                if (getCallingPageRef() != null)
                    setResponsePage(getCallingPageRef().getPage());
                else
                    setResponsePage(staticResponsePage());
            }
        };
        back.setDefaultFormProcessing(false);
        return back;
    }

    @Nullable
    protected abstract PageReference getCallingPageRef();

    @NotNull
    protected abstract Class<? extends Page> staticResponsePage();
}
