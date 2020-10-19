package ch.difty.scipamato.publ

import com.ninjasquad.springmockk.MockkBean
import org.jooq.DSLContext
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
abstract class AbstractIntegrationTest {

    @MockkBean
    lateinit var dslContext: DSLContext

    @MockkBean
    lateinit var userDetailsService: UserDetailsService
}
