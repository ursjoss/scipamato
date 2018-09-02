package ch.difty.scipamato.core.web.user;

import static ch.difty.scipamato.core.entity.User.UserFields.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePage;

@MountPath("user")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class UserEditPage extends BasePage<User> {

    @SpringBean
    private UserService userService;

    public UserEditPage(final PageParameters pp) {
        super(pp);
        final StringValue userIdStringValue = pp.get(CorePageParameters.USER_ID.getName());
        final int userId = userIdStringValue.toInt(getActiveUser().getId());
        userService
            .findById(userId)
            .ifPresent(u -> UserEditPage.this.setDefaultModel(Model.of(u)));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form<User> form = new Form<>("form", new CompoundPropertyModel<>(getModel()));
        queue(form);

        queueFieldAndLabel(new TextField<String>(USER_NAME.getName()), new PropertyValidator<String>());
        queueFieldAndLabel(new TextField<String>(FIRST_NAME.getName()), new PropertyValidator<String>());
        queueFieldAndLabel(new TextField<String>(LAST_NAME.getName()), new PropertyValidator<String>());
        queueFieldAndLabel(new EmailTextField(EMAIL.getName()));

        final PasswordTextField pwField1 = new PasswordTextField(PASSWORD.getName());
        pwField1.setRequired(false);
        queueFieldAndLabel(pwField1);
        final PasswordTextField pwField2 = new PasswordTextField(PASSWORD2.getName());
        pwField2.setRequired(false);
        queueFieldAndLabel(pwField2);

        form.add(new EqualPasswordInputValidator(pwField1, pwField2));

        queue(new BootstrapButton("submit", new StringResourceModel("submit.label"), Buttons.Type.Default) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                doOnSubmit();
            }
        });
    }

    private void doOnSubmit() {
        try {
            User user = userService.saveOrUpdate(UserEditPage.this.getModelObject());
            if (user != null) {
                UserEditPage.this.setModelObject(user);
                info(new StringResourceModel("save.successful.hint", this, null)
                    .setParameters(getNullSafeId(), user.getUserName())
                    .getString());
            } else {
                error(new StringResourceModel("save.error.hint", this, null)
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

    private long getNullSafeId() {
        return getModelObject().getId() != null ? getModelObject().getId() : 0L;
    }

}
