package ch.difty.scipamato.core.web.user;

import static ch.difty.scipamato.core.entity.User.UserFields.*;

import java.util.Arrays;
import java.util.Optional;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePage;

/**
 * Loads the setting of the user with the id supplied in the page parameters.
 * If no valid integer is supplied in the page parameters, the settings of the
 * user in the current session is presented.
 * <p>
 * The page can be opened in different modes:
 * <ul>
 * <li><b>CREATE:</b> The admin can create a new user</li>
 * <li><b>MANAGE:</b> The admin edits other users accounts (including passwords)</li>
 * <li><b>EDIT:</b> The active user can edit parts of his/her profile but cannot change the password.</li>
 * <li><b>CHANGE_PASSWORD:</b> The (active) user can change the password but no other aspects of his/her profile</li>
 * </ul>
 */
@SuppressWarnings("WicketForgeJavaIdInspection")
@MountPath("user")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class UserEditPage extends BasePage<ChangePasswordUser> {

    private static final String AM_DATA_WIDTH = "data-width";

    private final Mode mode;

    @SpringBean
    private PasswordEncoder passwordEncoder;

    @SpringBean
    private UserService userService;

    @SuppressWarnings("WeakerAccess")
    public UserEditPage(final PageParameters pp) {
        super(pp);
        this.mode = getModeFrom(pp);
        assertManageOrCreateModeForAdminOnly();
        initDefaultModel(pp);
    }

    private Mode getModeFrom(final PageParameters pp) {
        final StringValue modeValue = pp.get(CorePageParameters.MODE.getName());
        return modeValue.isEmpty() ? Mode.EDIT : Mode.valueOf(modeValue.toString());
    }

    private void assertManageOrCreateModeForAdminOnly() {
        final User activeUser = getActiveUser();
        if (isInAdminMode() && !activeUser
            .getRoles()
            .contains(Role.ADMIN)) {
            final String msg = new StringResourceModel("page.access-forbidden", this, null)
                .setParameters(activeUser.getUserName(), this.getClass())
                .getString();
            log.warn(msg);
            throw new UnauthorizedInstantiationException(getPageClass());
        }
    }

    private boolean isInAdminMode() {
        return mode == Mode.MANAGE || mode == Mode.CREATE;
    }

    private boolean isInUserOwnedMode() {
        return !isInAdminMode();
    }

    private boolean isInPasswordResetMode() {
        return mode == Mode.CHANGE_PASSWORD;
    }

    private boolean canSetPasswords() {
        return isInAdminMode() || isInPasswordResetMode();
    }

    private void initDefaultModel(final PageParameters pp) {
        final StringValue userId = pp.get(CorePageParameters.USER_ID.getName());
        determineUserId(userId).ifPresentOrElse(this::loadUserWithIdFromDb,
            () -> setDefaultModel(Model.of(new ChangePasswordUser())));
    }

    /**
     * If the user id is supplied as page parameter, use its id (or that of the active user
     * if the parameter is not a numeric value). Otherwise use the id of the current user,
     * unless if in MANAGE Mode, then prepare for new user by returning an empty optional.
     */
    private Optional<Integer> determineUserId(final StringValue userIdStringValue) {
        final Integer activeUserId = getActiveUser().getId();
        switch (mode) {
        case CREATE:
            return Optional.empty();
        case MANAGE:
            return Optional.of(userIdStringValue.toInt(activeUserId));
        case EDIT:
        case CHANGE_PASSWORD:
        default:
            return Optional.of(activeUserId);
        }
    }

    private void loadUserWithIdFromDb(final int userId) {
        final boolean clearPassword = !isInPasswordResetMode();
        userService
            .findById(userId)
            .ifPresent(u -> setDefaultModel(Model.of(new ChangePasswordUser(u, clearPassword))));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form<ChangePasswordUser> form = new Form<>("form", new CompoundPropertyModel<>(getModel()));
        queue(form);

        final TextField<String> userNameField = new TextField<>(USER_NAME.getName());
        userNameField.setEnabled(isInAdminMode());
        queueFieldAndLabel(userNameField, new PropertyValidator<String>());

        final TextField<String> firstNameField = new TextField<>(FIRST_NAME.getName());
        firstNameField.setEnabled(!isInPasswordResetMode());
        queueFieldAndLabel(firstNameField, new PropertyValidator<String>());

        final TextField<String> lastNameField = new TextField<>(LAST_NAME.getName());
        lastNameField.setEnabled(!isInPasswordResetMode());
        queueFieldAndLabel(lastNameField, new PropertyValidator<String>());

        final EmailTextField emailField = new EmailTextField(EMAIL.getName());
        emailField.setEnabled(!isInPasswordResetMode());
        queueFieldAndLabel(emailField);

        final CheckBox enabledField = new CheckBox(ENABLED.getName());
        enabledField.setEnabled(isInAdminMode());
        queueFieldAndLabel(enabledField);

        final Label rolesLabel = new Label("roles" + LABEL_TAG,
            new StringResourceModel("roles" + LABEL_RESOURCE_TAG, this, null));
        rolesLabel.setVisible(isInAdminMode());
        queue(rolesLabel);

        final BootstrapMultiSelect<Role> rolesMultiSelect = new BootstrapMultiSelect<>("roles",
            new PropertyModel<>(getModel(), "roles"), Arrays.asList(Role.values()), new EnumChoiceRenderer<>(this));
        rolesMultiSelect.with(new BootstrapSelectConfig()
            .withMultiple(true)
            .withLiveSearch(true)
            .withLiveSearchStyle("startsWith"));
        rolesMultiSelect.add(new AttributeModifier(AM_DATA_WIDTH, "auto"));
        rolesMultiSelect.setVisible(isInAdminMode());
        queue(rolesMultiSelect);

        final Label rolesStringLabel = new Label("rolesStringLabel",
            new StringResourceModel("rolesString.label", this, null));
        rolesStringLabel.setVisible(isInUserOwnedMode());
        queue(rolesStringLabel);
        final Label rolesString = new Label("rolesString", getModelObject().getRolesString());
        rolesString.setVisible(isInUserOwnedMode());
        queue(rolesString);

        final PasswordTextField pwcField = new PasswordTextField("currentPassword");
        pwcField.setRequired(isInPasswordResetMode());
        pwcField.setVisible(isInPasswordResetMode());
        final String currentPassword = getModelObject().getPassword();
        if (isInPasswordResetMode() && currentPassword != null)
            pwcField.add(new CurrentPasswordMatchesValidator(passwordEncoder, currentPassword));
        queueFieldAndLabel(pwcField);

        final PasswordTextField pw1Field = new PasswordTextField(PASSWORD.getName());
        pw1Field.setRequired(isInPasswordResetMode());
        pw1Field.setVisible(canSetPasswords());
        queueFieldAndLabel(pw1Field);

        final PasswordTextField pw2Field = new PasswordTextField("password2");
        pw2Field.setRequired(isInPasswordResetMode());
        pw2Field.setVisible(canSetPasswords());
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

    private void doOnSubmit() {
        try {
            final User unsaved = getModelObject().toUser();
            if (!canSetPasswords())
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
        CREATE,
        MANAGE,
        EDIT,
        CHANGE_PASSWORD
    }
}
