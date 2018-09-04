package ch.difty.scipamato.core.web.user;

import static ch.difty.scipamato.core.entity.User.UserFields.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePage;

@SuppressWarnings("WicketForgeJavaIdInspection")
@MountPath("user")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class UserEditPage extends BasePage<ChangePasswordUser> {

    private final Mode mode;

    @SpringBean
    private PasswordEncoder passwordEncoder;
    /*
    TODO User Liste für admins und aufruf managed. Roles managen
    */
    @SpringBean
    private UserService     userService;

    @SuppressWarnings("WeakerAccess")
    public UserEditPage(final PageParameters pp) {
        super(pp);
        StringValue modeValue = pp.get(CorePageParameters.MODE.getName());
        this.mode = Mode.valueOf(modeValue.toString());
        final StringValue userIdStringValue = pp.get(CorePageParameters.USER_ID.getName());
        final int userId = userIdStringValue.toInt(getActiveUser().getId());
        userService
            .findById(userId)
            .ifPresent(u -> UserEditPage.this.setDefaultModel(Model.of(new ChangePasswordUser(u))));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form<ChangePasswordUser> form = new Form<>("form", new CompoundPropertyModel<>(getModel()));
        queue(form);

        final TextField<String> userNameField = new TextField<>(USER_NAME.getName());
        userNameField.setEnabled(mode == Mode.MANAGE);
        queueFieldAndLabel(userNameField, new PropertyValidator<String>());

        final TextField<String> firstNameField = new TextField<>(FIRST_NAME.getName());
        firstNameField.setEnabled(mode != Mode.CHANGE_PASSWORD);
        queueFieldAndLabel(firstNameField, new PropertyValidator<String>());

        final TextField<String> lastNameField = new TextField<>(LAST_NAME.getName());
        lastNameField.setEnabled(mode != Mode.CHANGE_PASSWORD);
        queueFieldAndLabel(lastNameField, new PropertyValidator<String>());

        final EmailTextField emailField = new EmailTextField(EMAIL.getName());
        emailField.setEnabled(mode != Mode.CHANGE_PASSWORD);
        queueFieldAndLabel(emailField);

        final CheckBox enabledField = new CheckBox(ENABLED.getName());
        enabledField.setEnabled(mode == Mode.MANAGE);
        queueFieldAndLabel(enabledField);

        final Label rolesStringLabel = new Label("rolesStringLabel",
            new StringResourceModel("rolesString.label", this, null));
        rolesStringLabel.setVisible(mode != Mode.MANAGE);
        queue(rolesStringLabel);
        final Label rolesString = new Label("rolesString", getModelObject().getRolesString());
        rolesString.setVisible(mode != Mode.MANAGE);
        queue(rolesString);

        final PasswordTextField pwcField = new PasswordTextField("currentPassword");
        pwcField.setRequired(mustProvideCurrentPassword());
        pwcField.setVisible(mustProvideCurrentPassword());
        pwcField.add(new CurrentPasswordMatchesValidator(passwordEncoder, getModelObject().getPassword()));
        queueFieldAndLabel(pwcField);

        final PasswordTextField pw1Field = new PasswordTextField(PASSWORD.getName());
        pw1Field.setRequired(mustSetPasswords());
        pw1Field.setVisible(mustSetPasswords());
        queueFieldAndLabel(pw1Field);

        final PasswordTextField pw2Field = new PasswordTextField("password2");
        pw2Field.setRequired(mustSetPasswords());
        pw2Field.setVisible(mustSetPasswords());
        queueFieldAndLabel(pw2Field);

        form.add(new EqualPasswordInputValidator(pw1Field, pw2Field));

        queue(new BootstrapButton("submit", new StringResourceModel("submit.label"), Buttons.Type.Default) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                doOnSubmit();
            }
        });
    }

    private boolean mustProvideCurrentPassword() {
        return mode == Mode.CHANGE_PASSWORD;
    }

    private boolean mustSetPasswords() {
        return mode == Mode.CHANGE_PASSWORD || mode == Mode.MANAGE;
    }

    private void doOnSubmit() {
        try {
            final User unsaved = UserEditPage.this
                .getModelObject()
                .toUser();
            if (!mustSetPasswords())
                unsaved.setPassword(null);
            User user = userService.saveOrUpdate(unsaved);
            if (user != null) {
                UserEditPage.this.setModelObject(new ChangePasswordUser(user));
                switch (mode) {
                case CHANGE_PASSWORD:
                    info(new StringResourceModel("password.changed.hint", this,
                        UserEditPage.this.getModel()).getString());
                    break;
                default:
                    info(new StringResourceModel("save.successful.hint", this, null)
                        .setParameters(getNullSafeId(), user.getUserName())
                        .getString());
                }
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

    public enum Mode {
        EDIT,
        CHANGE_PASSWORD,
        MANAGE
    }
}
