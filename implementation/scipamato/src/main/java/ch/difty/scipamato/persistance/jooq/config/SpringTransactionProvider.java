/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package ch.difty.scipamato.persistance.jooq.config;

import static org.springframework.transaction.TransactionDefinition.*;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.tools.JooqLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import ch.difty.scipamato.lib.AssertAs;

/**
 * An example {@code TransactionProvider} implementing the {@link TransactionProvider} contract for use with Spring.
 *
 * @author Lukas Eder
 * @author Urs Joss
 */
public class SpringTransactionProvider implements TransactionProvider {

    private static final JooqLogger LOGGER = JooqLogger.getLogger(SpringTransactionProvider.class);

    private final DataSourceTransactionManager txMgr;

    @Autowired
    public SpringTransactionProvider(DataSourceTransactionManager txMgr) {
        this.txMgr = AssertAs.notNull(txMgr, "txMgr");
    }

    /** protected for test purposes */
    protected DataSourceTransactionManager getTxMgr() {
        return txMgr;
    }

    @Override
    public void begin(TransactionContext ctx) {
        LOGGER.info("Begin transaction");

        // This TransactionProvider behaves like jOOQ's DefaultTransactionProvider, which supports nested transactions using Savepoints
        TransactionStatus tx = getTxMgr().getTransaction(new DefaultTransactionDefinition(PROPAGATION_NESTED));
        ctx.transaction(new SpringTransaction(tx));
    }

    @Override
    public void commit(TransactionContext ctx) {
        LOGGER.info("commit transaction");

        getTxMgr().commit(((SpringTransaction) ctx.transaction()).tx);
    }

    @Override
    public void rollback(TransactionContext ctx) {
        LOGGER.info("rollback transaction");

        getTxMgr().rollback(((SpringTransaction) ctx.transaction()).tx);
    }
}
