package ch.difty.scipamato.core.web.newsletter.topic;

import java.util.Collection;
import java.util.Iterator;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import lombok.extern.slf4j.Slf4j;
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
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.common.BasePage;

@MountPath("newsletter-topic/entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class NewsletterTopicEditPage extends BasePage<NewsletterTopicDefinition> {

    @SpringBean
    private NewsletterTopicService service;

    public NewsletterTopicEditPage(final IModel<NewsletterTopicDefinition> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queueForm("form");
    }

    private void queueForm(final String formId) {
        queue(newForm(formId));
        queue(newRefreshingView("translations"));
        queue(newSubmitButton("submit"));
        queue(newDeleteButton("delete"));

    }

    private Form<NewsletterTopicDefinition> newForm(final String id) {
        return new Form<NewsletterTopicDefinition>(id, new CompoundPropertyModel<>(getModel())) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    NewsletterTopicDefinition persisted = service.saveOrUpdate(getModelObject());
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

    private RefreshingView<NewsletterTopicTranslation> newRefreshingView(final String id) {
        RefreshingView<NewsletterTopicTranslation> translations = new RefreshingView<NewsletterTopicTranslation>(id) {
            @Override
            protected Iterator<IModel<NewsletterTopicTranslation>> getItemModels() {
                Collection<NewsletterTopicTranslation> translations = getModelObject()
                    .getTranslations()
                    .values();

                return new ModelIteratorAdapter<NewsletterTopicTranslation>(translations) {
                    @Override
                    protected IModel<NewsletterTopicTranslation> model(
                        final NewsletterTopicTranslation newsletterTopicTranslation) {
                        return new CompoundPropertyModel<>(newsletterTopicTranslation);
                    }
                };
            }

            @Override
            protected void populateItem(final Item<NewsletterTopicTranslation> item) {
                item.add(new Label("langCode"));
                item.add(new TextField<String>("title"));
            }
        };
        translations.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
        return translations;
    }

    private BootstrapButton newSubmitButton(String id) {
        return new BootstrapButton(id, new StringResourceModel(id + ".label"), Buttons.Type.Primary);
    }

    private BootstrapButton newDeleteButton(final String id) {
        final BootstrapButton db = new BootstrapButton(id, new StringResourceModel(id + ".label"),
            Buttons.Type.Default) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                try {
                    final NewsletterTopicDefinition ntd = NewsletterTopicEditPage.this.getModelObject();
                    if (ntd != null && ntd.getId() != null) {
                        int id = ntd.getId();
                        NewsletterTopicDefinition deleted = service.delete(id, ntd.getVersion());
                        if (deleted != null) {
                            setResponsePage(NewsletterTopicListPage.class);
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
                    if (ex
                        .getMessage()
                        .contains("is still referenced from table")) {
                        String msg = new StringResourceModel("delete.refintegrity.hint", this, null)
                            .setParameters(getNullSafeId())
                            .getString();
                        log.error(msg);
                        error(msg);
                    } else {
                        String msg = new StringResourceModel("delete.error.hint", this, null)
                            .setParameters(getNullSafeId(), ex.getMessage())
                            .getString();
                        log.error(msg);
                        error(msg);
                    }
                }
            }
        };
        db.setDefaultFormProcessing(false);
        db.add(new ConfirmationBehavior());
        return db;
    }
}
