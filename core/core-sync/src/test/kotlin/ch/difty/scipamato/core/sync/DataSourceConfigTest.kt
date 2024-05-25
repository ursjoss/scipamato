package ch.difty.scipamato.core.sync

import ch.difty.scipamato.core.sync.launcher.UnsynchronizedEntitiesWarner
import io.mockk.mockk
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBeNull
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultExecuteListenerProvider
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.jooq.ExceptionTranslatorExecuteListener
import org.springframework.boot.autoconfigure.jooq.JooqProperties

internal class DataSourceConfigTest {

    private val jooqProperties = JooqProperties().apply {
        sqlDialect = SQLDialect.POSTGRES
    }

    private val config = DataSourceConfig(jooqProperties)

    @Test
    fun listenerProviderShouldBeExceptionTranslatorExecuteListener() {
        val listenerProvider = config.executeListenerProvider()
        listenerProvider shouldBeInstanceOf DefaultExecuteListenerProvider::class
        val translator = listenerProvider.provide()
        translator shouldBeInstanceOf ExceptionTranslatorExecuteListener::class
    }

    @Test
    fun coreConfiguration() {
        val config = config.coreConfiguration()
        val connectionProvider = config.connectionProvider()
        connectionProvider shouldBeInstanceOf DataSourceConnectionProvider::class
    }

    @Test
    fun publicConfiguration() {
        val config = config.publicConfiguration()
        val connectionProvider = config.connectionProvider()
        connectionProvider shouldBeInstanceOf DataSourceConnectionProvider::class
    }

    @Test
    fun batchConfiguration() {
        val config = config.batchConfiguration()
        val connectionProvider = config.connectionProvider()
        connectionProvider shouldBeInstanceOf DataSourceConnectionProvider::class
    }

    @Test
    fun dslContext() {
        val context = config.dslContext()
        context.shouldNotBeNull()
    }

    @Test
    fun publicDslContext() {
        val context = config.publicDslContext()
        context.shouldNotBeNull()
    }

    @Test
    fun batchDslContext() {
        val context = config.batchDslContext()
        context.shouldNotBeNull()
    }

    @Test
    fun unsynchronizedEntitiesWarner() {
        val dslContext = mockk<DSLContext>()
        val warner = config.unsynchronizedEntitiesWarner(dslContext)
        warner shouldBeInstanceOf UnsynchronizedEntitiesWarner::class
    }
}
