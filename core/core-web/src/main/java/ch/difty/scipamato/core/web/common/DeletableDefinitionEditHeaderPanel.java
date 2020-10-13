package ch.difty.scipamato.core.web.common;

import static ch.difty.scipamato.common.web.WicketUtilsKt.LABEL_RESOURCE_TAG;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.DefinitionTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@SuppressWarnings({ "SameParameterValue", "SpellCheckingInspection" })
@Slf4j
public abstract class DeletableDefinitionEditHeaderPanel<E extends DefinitionEntity<ID, T>, T extends DefinitionTranslation, ID>
    extends DefinitionEditHeaderPanel<E, T, ID> {

    private static final long serialVersionUID = 1L;

    /**
     * @param id
     *     the panel wicket id
     * @param model
     *     model of type <code>T</code>. Must not be null.
     */
    protected DeletableDefinitionEditHeaderPanel(@NotNull final String id, @Nullable final IModel<E> model) {
        super(id, model);
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        queue(newDeleteButton("delete"));
    }

    private BootstrapButton newDeleteButton(final String id) {
        final BootstrapButton db = new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                super.onSubmit();
                try {
                    final E ntd = DeletableDefinitionEditHeaderPanel.this.getModelObject();
                    if (ntd.getNullSafeId() != null) {
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
                } catch (DataIntegrityViolationException dive) {
                    handleDataIntegrityViolationException(dive);
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
                    .setParameters(ole.getTableName(), DeletableDefinitionEditHeaderPanel.this
                        .getModelObject()
                        .getNullSafeId())
                    .getString();
                log.error(msg);
                error(msg);
            }

            @SuppressWarnings("unused")
            private void handleDataIntegrityViolationException(final DataIntegrityViolationException dive) {
                @SuppressWarnings("SpellCheckingInspection") final String msg = new StringResourceModel("delete.dataintegrityviolation.hint", this,
                    null)
                    .setParameters(DeletableDefinitionEditHeaderPanel.this
                        .getModelObject()
                        .getNullSafeId())
                    .getString();
                error(msg);
            }

            private void handleOtherException(final Exception oe) {
                final String msg = new StringResourceModel("delete.error.hint", this, null)
                    .setParameters(DeletableDefinitionEditHeaderPanel.this
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

    @Nullable
    protected abstract E doDelete(@NotNull final E ntd, @NotNull final ID recordId);
}
