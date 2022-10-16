package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.common.entity.DefinitionEntity
import ch.difty.scipamato.common.entity.DefinitionTranslation
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import org.apache.wicket.PageReference
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.StringResourceModel
import org.springframework.dao.DuplicateKeyException

private val log = logger()

@Suppress("SameParameterValue")
abstract class DefinitionEditPage<E : DefinitionEntity<ID, T>, T : DefinitionTranslation, ID>(
    model: IModel<E>?,
    protected val callingPageRef: PageReference?,
) : BasePage<E>(model) {

    private lateinit var form: Form<E>

    protected fun getForm(): Form<E> {
        return form
    }

    override fun onInitialize() {
        super.onInitialize()
        queueForm("form")
    }

    private fun queueForm(id: String) {
        queue(newForm(id).also { form = it })
        queue(Label("headerLabel", StringResourceModel("header$LABEL_RESOURCE_TAG", this, null)))
        queue(newDefinitionHeaderPanel("headerPanel"))
        queue(Label("translationsLabel", StringResourceModel("translations$LABEL_RESOURCE_TAG", this, null)))
        queue(newDefinitionTranslationPanel("translationsPanel"))
    }

    @Suppress("TooGenericExceptionCaught")
    private fun newForm(id: String): Form<E> = object : Form<E>(id, CompoundPropertyModel(model)) {
        override fun onSubmit() {
            super.onSubmit()
            try {
                val persisted = persistModel()
                if (persisted != null) {
                    modelObject = persisted
                    info(StringResourceModel("save.successful.hint", this, null)
                        .setParameters(modelObject!!.nullSafeId, modelObject!!.translationsAsString).string)
                } else {
                    handleRepoException()
                }
            } catch (ole: OptimisticLockingException) {
                handleOptimisticLockingException(ole)
            } catch (dke: DuplicateKeyException) {
                handleDuplicateKeyException(dke)
            } catch (ex: Exception) {
                handleOtherException(ex)
            }
        }

        private fun handleRepoException() {
            error(StringResourceModel("save.unsuccessful.hint", this, null)
                .setParameters(modelObject!!.nullSafeId, "").string)
        }

        private fun handleOptimisticLockingException(ole: OptimisticLockingException) {
            val msg = StringResourceModel("save.optimisticlockexception.hint", this, null)
                .setParameters(ole.tableName, modelObject!!.nullSafeId).string
            log.error(msg)
            error(msg)
        }

        private fun handleOtherException(oe: Exception) {
            val msg = StringResourceModel("save.error.hint", this, null)
                .setParameters(modelObject!!.nullSafeId, oe.message)
                .string
            log.error(msg)
            error(msg)
        }
    }

    protected abstract fun persistModel(): E?
    protected abstract fun newDefinitionHeaderPanel(id: String): DefinitionEditHeaderPanel<E, T, ID>
    protected abstract fun newDefinitionTranslationPanel(id: String): DefinitionEditTranslationPanel<E, T>
    protected abstract fun handleDuplicateKeyException(dke: DuplicateKeyException)

    companion object {
        private const val serialVersionUID: Long = 1
    }
}
