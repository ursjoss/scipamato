package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.entity.User.UserFields
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.persistence.UserService
import ch.difty.scipamato.core.web.CorePageParameters
import ch.difty.scipamato.core.web.common.BasePage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig
import org.apache.wicket.AttributeModifier
import org.apache.wicket.authorization.UnauthorizedInstantiationException
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.form.EmailTextField
import org.apache.wicket.markup.html.form.EnumChoiceRenderer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator
import org.apache.wicket.markup.html.form.validation.IFormValidator
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.util.string.StringValue
import org.springframework.security.crypto.password.PasswordEncoder
import org.wicketstuff.annotation.mount.MountPath

private val log = logger()

/**
 * Loads the setting of the user with the id supplied in the page parameters.
 * If no valid integer is supplied in the page parameters, the settings of the
 * user in the current session is presented.
 *
 * The page can be opened in different modes:
 *
 *  * **CREATE:** The admin can create a new user
 *  * **MANAGE:** The admin edits other users accounts (including passwords)
 *  * **EDIT:** The active user can edit parts of his/her profile but cannot change the password.
 *  * **CHANGE_PASSWORD:** The (active) user can change the password but no other aspects of his/her profile
 */
@MountPath("user")
@AuthorizeInstantiation(Roles.USER, Roles.ADMIN)
class UserEditPage(pp: PageParameters) : BasePage<ChangePasswordUser>(pp) {

    private val mode: Mode

    @SpringBean
    private lateinit var passwordEncoder: PasswordEncoder

    @SpringBean
    private lateinit var userService: UserService

    init {
        mode = getModeFrom(pp)
        assertManageOrCreateModeForAdminOnly()
        initDefaultModel(pp)
    }

    private fun getModeFrom(pp: PageParameters): Mode {
        val modeValue = pp[CorePageParameters.MODE.getName()]
        return if (modeValue.isEmpty) Mode.EDIT else Mode.valueOf(modeValue.toString())
    }

    private fun assertManageOrCreateModeForAdminOnly() {
        if (isInAdminMode && !activeUser.roles.contains(Role.ADMIN)) {
            val msg = StringResourceModel("page.access-forbidden", this, null)
                .setParameters(activeUser.userName, this.javaClass).string
            log.warn(msg)
            throw UnauthorizedInstantiationException(pageClass)
        }
    }

    private val isInAdminMode: Boolean
        get() = mode == Mode.MANAGE || mode == Mode.CREATE
    private val isInUserOwnedMode: Boolean
        get() = !isInAdminMode
    private val isInPasswordResetMode: Boolean
        get() = mode == Mode.CHANGE_PASSWORD

    private fun canSetPasswords(): Boolean = isInAdminMode || isInPasswordResetMode

    private fun initDefaultModel(pp: PageParameters) {
        val userId = pp[CorePageParameters.USER_ID.getName()]
        determineUserId(userId)
            ?.let { loadUserWithIdFromDb(it) }
            ?: run { defaultModel = Model.of(ChangePasswordUser()) }
    }

    /**
     * If the user id is supplied as page parameter, use its id (or that of the active user,
     * if the parameter is not a numeric value). Otherwise use the id of the current user,
     * unless if in CREATE Mode, then prepare for new user by returning null.
     */
    private fun determineUserId(userIdStringValue: StringValue): Int? {
        val activeUserId = activeUser.id!!
        return when (mode) {
            Mode.CREATE -> null
            Mode.MANAGE -> userIdStringValue.toInt(activeUserId)
            Mode.EDIT, Mode.CHANGE_PASSWORD -> activeUserId
        }
    }

    private fun loadUserWithIdFromDb(userId: Int) {
        userService.findById(userId)
            .ifPresent { u: User -> defaultModel = Model.of(ChangePasswordUser(u, !isInPasswordResetMode)) }
    }

    override fun onInitialize() {
        super.onInitialize()
        val form = Form("form", CompoundPropertyModel(model)).also { add(it) }
        TextField<String>(UserFields.USER_NAME.fieldName).apply { isEnabled = isInAdminMode }
            .also { queueFieldAndLabel(it, PropertyValidator<Any>()) }
        TextField<String>(UserFields.FIRST_NAME.fieldName).apply {
            isEnabled = !isInPasswordResetMode
        }.also { queueFieldAndLabel(it, PropertyValidator<Any>()) }
        TextField<String>(UserFields.LAST_NAME.fieldName).apply {
            isEnabled = !isInPasswordResetMode
        }.also { queueFieldAndLabel(it, PropertyValidator<Any>()) }
        EmailTextField(UserFields.EMAIL.fieldName).apply {
            isEnabled = !isInPasswordResetMode
        }.also { queueFieldAndLabel(it) }
        CheckBox(UserFields.ENABLED.fieldName).apply {
            isEnabled = isInAdminMode
        }.also { queueFieldAndLabel(it) }
        Label(ROLES + LABEL_TAG, StringResourceModel("$ROLES$LABEL_RESOURCE_TAG", this, null)).apply {
            isVisible = isInAdminMode
        }.also { queue(it) }
        BootstrapMultiSelect(ROLES, PropertyModel(model, ROLES), listOf(*Role.values()), EnumChoiceRenderer(this)).apply {
            with(BootstrapSelectConfig()
                .withMultiple(true)
                .withLiveSearch(true)
                .withLiveSearchStyle("startsWith"))
            add(AttributeModifier(AM_DATA_WIDTH, "auto"))
            isVisible = isInAdminMode
        }.also { queue(it) }
        Label("$ROLES_STRING$LABEL_TAG", StringResourceModel("$ROLES_STRING$LABEL_RESOURCE_TAG", this, null)).apply {
            isVisible = isInUserOwnedMode
        }.also { queue(it) }
        Label(ROLES_STRING, modelObject!!.rolesString).apply {
            isVisible = isInUserOwnedMode
        }.also { queue(it) }
        PasswordTextField("currentPassword").apply {
            isRequired = isInPasswordResetMode
            isVisible = isInPasswordResetMode
            val currentPassword = this@UserEditPage.modelObject.password
            if (isInPasswordResetMode && currentPassword != null)
                add(CurrentPasswordMatchesValidator(passwordEncoder, currentPassword))
        }.also { queueFieldAndLabel(it) }
        val pw1Field = PasswordTextField(UserFields.PASSWORD.fieldName).apply {
            isRequired = isInPasswordResetMode
            isVisible = canSetPasswords()
        }.also { queueFieldAndLabel(it) }
        val pw2Field = PasswordTextField("password2").apply {
            isRequired = isInPasswordResetMode
            isVisible = canSetPasswords()
        }.also { queueFieldAndLabel(it) }

        form.add(EqualPasswordInputValidator(pw1Field, pw2Field) as IFormValidator)

        queueSubmitButton("submit")

        object : BootstrapButton("delete", StringResourceModel("delete.label"),
            Buttons.Type.Default) {
            override fun onSubmit() {
                super.onSubmit()
                doOnDelete()
            }
        }.apply {
            add(ConfirmationBehavior())
        }.also {
            queue(it)
        }
    }

    @Suppress("SameParameterValue")
    private fun queueSubmitButton(id: String) {
        queue(object : BootstrapButton(id, StringResourceModel("submit.label"), Buttons.Type.Default) {
            override fun onSubmit() {
                super.onSubmit()
                doOnSubmit()
            }
        })
    }

    private fun doOnSubmit() {
        if (modelObject != null) {
            try {
                val unsaved = modelObject.toUser()
                if (!canSetPasswords()) unsaved.password = null
                val user = userService.saveOrUpdate(unsaved)
                if (user != null) {
                    this@UserEditPage.modelObject = ChangePasswordUser(user)
                    if (mode == Mode.CHANGE_PASSWORD) {
                        info(StringResourceModel("password.changed.hint", this, this@UserEditPage.model).string)
                    } else {
                        info(StringResourceModel("save.successful.hint", this, null)
                            .setParameters(modelObject.id, user.userName).string)
                    }
                } else {
                    error(StringResourceModel("save.error.hint", this, null)
                        .setParameters(modelObject.id, "").string)
                }
            } catch (ole: OptimisticLockingException) {
                val msg = StringResourceModel("save.optimisticlockexception.hint", this, null)
                    .setParameters(ole.tableName, modelObject.id).string
                log.error(msg)
                error(msg)
            } catch (ex: Exception) {
                val msg = StringResourceModel("save.error.hint", this, null)
                    .setParameters(modelObject.id, ex.message).string
                log.error(msg)
                error(msg)
            }
        }
    }

    private fun doOnDelete() {
        val user = modelObject!!.toUser()
        val userName = user.userName
        try {
            userService.remove(user)
            info(StringResourceModel("delete.successful.hint", this, null).setParameters(userName))
            setResponsePage(UserListPage::class.java)
        } catch (ex: Exception) {
            val msg = StringResourceModel("delete.error.hint", this, null)
                .setParameters(userName, ex.message).string
            log.error(msg)
            error(msg)
        }
    }

    enum class Mode {
        CREATE, MANAGE, EDIT, CHANGE_PASSWORD
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val AM_DATA_WIDTH = "data-width"
        private const val ROLES = "roles"
        private const val ROLES_STRING = "rolesString"
    }
}
