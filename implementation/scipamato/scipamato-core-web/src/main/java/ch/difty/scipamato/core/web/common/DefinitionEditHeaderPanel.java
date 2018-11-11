package ch.difty.scipamato.core.web.common;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.DefinitionTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@SuppressWarnings("SameParameterValue")
@Slf4j
public abstract class DefinitionEditHeaderPanel<E extends DefinitionEntity<ID, T>, T extends DefinitionTranslation, ID>
    extends BasePanel<E> {

    protected DefinitionEditHeaderPanel(final String id, final IModel<E> model) {
        super(id, model);
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        makeAndQueueFilterFields();
        makeAndQueueActionButtons();
    }

    protected abstract void makeAndQueueFilterFields();

    private void makeAndQueueActionButtons() {
        queue(newSubmitButton("submit"));
        if (canDelete())
            queue(newDeleteButton("delete"));
        queue(newBackButton("back"));
    }

    protected boolean canDelete() {
        return true;
    }

    private BootstrapButton newSubmitButton(final String id) {
        final BootstrapButton button = new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG),
            Buttons.Type.Primary);
        button.add(new LoadingBehavior(new StringResourceModel(id + LOADING_RESOURCE_TAG, this, null)));
        return button;
    }

    private BootstrapButton newDeleteButton(final String id) {
        final BootstrapButton db = new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG),
            Buttons.Type.Default) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                try {
                    final E ntd = DefinitionEditHeaderPanel.this.getModelObject();
                    if (ntd != null && ntd.getNullSafeId() != null) {
                        final ID recordId = ntd.getNullSafeId();
                        E deleted = doDelete(ntd, recordId);
                        if (deleted != null) {
                            setResponsePage(staticResponsePage());
                            info(new StringResourceModel("delete.successful.hint", this, null)
                                .setParameters(recordId, deleted.getTranslationsAsString())
                                .getString());
                        } else {
                            handleRepoError(recordId);
                        }
                    }
                } catch (OptimisticLockingException ole) {
                    handleOptimisticLockingException(ole);
                } catch (Exception oe) {
                    handleOtherException(oe);
                }
            }

            private void handleRepoError(final ID recordId) {
                error(new StringResourceModel("delete.unsuccessful.hint", this, null)
                    .setParameters(recordId, "")
                    .getString());
            }

            private void handleOptimisticLockingException(final OptimisticLockingException ole) {
                final String msg = new StringResourceModel("delete.optimisticlockexception.hint", this, null)
                    .setParameters(ole.getTableName(), DefinitionEditHeaderPanel.this
                        .getModelObject()
                        .getNullSafeId())
                    .getString();
                log.error(msg);
                error(msg);
            }

            private void handleOtherException(final Exception oe) {
                final String msg = new StringResourceModel("delete.error.hint", this, null)
                    .setParameters(DefinitionEditHeaderPanel.this
                        .getModelObject()
                        .getNullSafeId(), oe.getMessage())
                    .getString();
                log.error(msg);
                error(msg);
            }
        };
        db.setDefaultFormProcessing(false);
        db.add(new ConfirmationBehavior());
        return db;
    }

    protected abstract E doDelete(final E ntd, final ID recordId);

    private BootstrapButton newBackButton(final String id) {
        BootstrapButton back = new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG),
            Buttons.Type.Default) {
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

    protected abstract PageReference getCallingPageRef();

    protected abstract Class<? extends Page> staticResponsePage();

}
