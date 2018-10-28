package ch.difty.scipamato.core.web.keyword;

import java.util.Collection;
import java.util.Iterator;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.common.BasePage;

@MountPath("keyword-topic/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
public class KeywordEditPage extends BasePage<KeywordDefinition> {

    @SpringBean
    private KeywordService service;

    private Form<KeywordDefinition> form;

    private final PageReference callingPageRef;

    KeywordEditPage(final IModel<KeywordDefinition> model, final PageReference callingPageRef) {
        super(model);
        this.callingPageRef = callingPageRef;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queueForm("form");
    }

    private void queueForm(final String formId) {
        queue(form = newForm(formId));
        queue(new Label("keywordLabel", new StringResourceModel("keyword" + LABEL_RESOURCE_TAG, this, null)));
        queueFieldAndLabel(new TextField<String>(Keyword.KeywordFields.SEARCH_OVERRIDE.getName()));
        queue(new Label("translationsLabel", new StringResourceModel("translations" + LABEL_RESOURCE_TAG, this, null)));
        queue(newRefreshingView("translations"));
        queue(newBackButton("back"));
        queue(newSubmitButton("submit"));
        queue(newDeleteButton("delete"));

    }

    private Form<KeywordDefinition> newForm(final String id) {
        return new Form<>(id, new CompoundPropertyModel<>(getModel())) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    KeywordDefinition persisted = service.saveOrUpdate(getModelObject());
                    if (persisted != null) {
                        setModelObject(persisted);
                        info(new StringResourceModel("save.successful.hint", this, null)
                            .setParameters(getNullSafeId(), getModelObject().getTranslationsAsString())
                            .getString());
                    } else {
                        error(new StringResourceModel("save.unsuccessful.hint", this, null)
                            .setParameters(getNullSafeId(), "")
                            .getString());
                    }
                } catch (OptimisticLockingException ole) {
                    final String msg = new StringResourceModel("save.optimisticlockexception.hint", this, null)
                        .setParameters(ole.getTableName(), getNullSafeId())
                        .getString();
                    log.error(msg);
                    error(msg);
                } catch (Exception ex) {
                    String msg = new StringResourceModel("save.error.hint", this, null)
                        .setParameters(getNullSafeId(), ex.getMessage())
                        .getString();
                    log.error(msg);
                    error(msg);
                }
            }
        };
    }

    private long getNullSafeId() {
        return getModelObject().getId() != null ? getModelObject().getId() : 0L;
    }

    private RefreshingView<KeywordTranslation> newRefreshingView(final String id) {
        final RefreshingView<KeywordTranslation> translations = new RefreshingView<>(id) {
            @Override
            protected Iterator<IModel<KeywordTranslation>> getItemModels() {
                final Collection<KeywordTranslation> translations = getModelObject()
                    .getTranslations()
                    .values();
                return new ModelIteratorAdapter<>(translations) {
                    @Override
                    protected IModel<KeywordTranslation> model(final KeywordTranslation keywordTopicTranslation) {
                        return new CompoundPropertyModel<>(keywordTopicTranslation);
                    }
                };
            }

            @Override
            protected void populateItem(final Item<KeywordTranslation> item) {
                item.add(new Label("langCode"));
                item.add(new TextField<String>("name"));
                item.add(newAddLink("addTranslation", item));
                item.add(newRemoveLink("removeTranslation", item));
            }

            private AjaxLink<Void> newAddLink(final String id, final Item<KeywordTranslation> item) {
                final AjaxLink<Void> newLink = new AjaxLink<>(id) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        final KeywordTranslation currentKt = item.getModelObject();
                        final String langCode = currentKt.getLangCode();
                        final KeywordTranslation newKt = new KeywordTranslation(null, langCode, null, 1);
                        KeywordEditPage.this
                            .getModelObject()
                            .getTranslations()
                            .put(langCode, newKt);
                        target.add(form);
                    }
                };
                newLink.add(new ButtonBehavior());
                newLink.setBody(new StringResourceModel("button." + id + LABEL_RESOURCE_TAG));
                return newLink;
            }

            private AjaxLink<Void> newRemoveLink(final String id, final Item<KeywordTranslation> item) {
                final AjaxLink<Void> newLink = new AjaxLink<>(id) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        final KeywordTranslation currentKt = item.getModelObject();
                        final String langCode = currentKt.getLangCode();
                        final Iterator<KeywordTranslation> it = KeywordEditPage.this
                            .getModelObject()
                            .getTranslations()
                            .get(langCode)
                            .iterator();
                        while (it.hasNext()) {
                            final KeywordTranslation kt = it.next();
                            if (currentKt.equals(kt)) {
                                it.remove();
                                break;
                            }
                        }
                        target.add(form);
                    }
                };
                newLink.add(new ButtonBehavior());
                newLink.setBody(new StringResourceModel("button." + id + LABEL_RESOURCE_TAG));
                newLink.add(new ConfirmationBehavior());
                return newLink;
            }
        };
        translations.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
        return translations;
    }

    private BootstrapButton newBackButton(final String id) {
        BootstrapButton back = new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG),
            Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                if (callingPageRef != null)
                    setResponsePage(callingPageRef.getPage());
                else
                    setResponsePage(KeywordListPage.class);
            }
        };
        back.setDefaultFormProcessing(false);
        return back;
    }

    private BootstrapButton newSubmitButton(String id) {
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
                    final KeywordDefinition ntd = KeywordEditPage.this.getModelObject();
                    if (ntd != null && ntd.getId() != null) {
                        int id = ntd.getId();
                        KeywordDefinition deleted = service.delete(id, ntd.getVersion());
                        if (deleted != null) {
                            setResponsePage(KeywordListPage.class);
                            info(new StringResourceModel("delete.successful.hint", this, null)
                                .setParameters(id, deleted.getTranslationsAsString())
                                .getString());
                        } else {
                            error(new StringResourceModel("delete.unsuccessful.hint", this, null)
                                .setParameters(id, "")
                                .getString());
                        }
                    }
                } catch (OptimisticLockingException ole) {
                    final String msg = new StringResourceModel("delete.optimisticlockexception.hint", this, null)
                        .setParameters(ole.getTableName(), getNullSafeId())
                        .getString();
                    log.error(msg);
                    error(msg);
                } catch (Exception ex) {
                    String msg = new StringResourceModel("delete.error.hint", this, null)
                        .setParameters(getNullSafeId(), ex.getMessage())
                        .getString();
                    log.error(msg);
                    error(msg);
                }
            }
        };
        db.setDefaultFormProcessing(false);
        db.add(new ConfirmationBehavior());
        return db;
    }
}
