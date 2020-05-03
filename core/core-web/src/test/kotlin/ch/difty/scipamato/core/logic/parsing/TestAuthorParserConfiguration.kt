package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.core.logic.parsing.AuthorParserFactory.Companion.create
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
open class TestAuthorParserConfiguration {

    @Bean
    @Primary
    open fun authorParserFactory(): AuthorParserFactory = create(AuthorParserStrategy.PUBMED)

}
