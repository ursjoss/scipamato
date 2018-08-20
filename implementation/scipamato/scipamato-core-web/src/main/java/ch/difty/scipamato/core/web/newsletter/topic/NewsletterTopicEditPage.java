package ch.difty.scipamato.core.web.newsletter.topic;

import java.util.Collection;
import java.util.Iterator;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
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

    private void queueForm(final String id) {
        queue(new Form<NewsletterTopicDefinition>(id, new CompoundPropertyModel<>(getModel())) {
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
        });
        RefreshingView<NewsletterTopicTranslation> translations = new RefreshingView<NewsletterTopicTranslation>(
            "translations") {
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
        queue(translations);
        queue(new BootstrapButton("submit", new StringResourceModel("submit.label"), Buttons.Type.Default));
    }

    private long getNullSafeId() {
        return getModelObject().getId() != null ? getModelObject().getId() : 0L;
    }
}
