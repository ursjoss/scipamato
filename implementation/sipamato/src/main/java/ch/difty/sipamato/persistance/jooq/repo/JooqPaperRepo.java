package ch.difty.sipamato.persistance.jooq.repo;

import static ch.difty.sipamato.db.h2.Tables.PAPER;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.persistance.repository.PaperRepository;

/**
 * @author Lukas Eder
 */
@Service
@Profile("DB_JOOQ")
public class JooqPaperRepo implements PaperRepository {

    @Autowired
    DSLContext dsl;

    @Override
    @Transactional
    public void create(int id, String authors, String firstAuthor, boolean firstAuthorOverridden, String title, String location, String goals) {

        // This method has a "bug". It creates the same book twice. The second insert
        // should lead to a constraint violation, which should roll back the whole transaction
        for (int i = 0; i < 2; i++)
            // @formatter:off
			dsl.insertInto(PAPER)
			    .set(PAPER.ID, id)
			    .set(PAPER.AUTHORS, authors)
			    .set(PAPER.FIRST_AUTHOR, firstAuthor)
			    .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, firstAuthorOverridden)
			    .set(PAPER.TITLE, title)
			    .set(PAPER.LOCATION, location)
			    .set(PAPER.GOALS, goals).execute();
            // @formatter:on
    }
}
