/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.util.fileloader.DataFileLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.transaction.AfterTransaction;

import java.util.Map;
import java.util.Optional;

/**
 * The intermediate superclass of the {@link PacketAppDaoTest}
 * that interweaves the After-Transaction method and execution listener into the test class.
 */

public abstract class AbstractDbunitTransactionalJUnit4SpringContextTests
        extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * DBUnit Tester
     */
    private IDatabaseTester databaseTester;

    /**
     * The name of file containing the expected data
     */
    private String afterDatasetFileName;

    /**
     * The XLS file loader
     */
    private DataFileLoader xlsDataFileLoader;

    /**
     * Method executing the after-transaction data comparison
     * (see the "after" parameter of the {@literal DataSets} annotation.
     */
    @AfterTransaction
    public void assertAfterTransaction() throws Exception {
        if (databaseTester == null) {
            return;
        }
        if (StringUtils.isEmpty(afterDatasetFileName)) {
            databaseTester.onTearDown();
            return;
        }

        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        IDataSet expectedDataSet = xlsDataFileLoader.load(afterDatasetFileName);
        Assertion.assertEquals(expectedDataSet, databaseDataSet);
        databaseTester.onTearDown();
    }

    /**
     * The test execution listener for the {@link PacketAppDaoTest}.
     */
    public static class DbunitTestExecutionListener extends AbstractTestExecutionListener {

        /**
         * The database tester
         */
        private IDatabaseTester databaseTester;
        /**
         * The data file loader
         */
        private DataFileLoader xlsDataFileLoader;

        /**
         * Runs after each test method.
         *
         * @param testCtx the test context
         */
        @Override
        public void afterTestMethod(TestContext testCtx) {
        }

        /**
         * Runs before each test method.
         * @param testContext the test context.
         */
        @Override
        public void beforeTestMethod(TestContext testContext) throws Exception {
            DataSets dataSetAnnotation = testContext.getTestMethod().getAnnotation(DataSets.class);

            if (!Optional.ofNullable(dataSetAnnotation).isPresent()) {
                return;
            }

            String beforeDatasetName = dataSetAnnotation.before();
            AbstractDbunitTransactionalJUnit4SpringContextTests testInstance
                    = (AbstractDbunitTransactionalJUnit4SpringContextTests) testContext.getTestInstance();
            testInstance.afterDatasetFileName = dataSetAnnotation.after();

            boolean afterDatasetEmpty = StringUtils.isEmpty(testInstance.afterDatasetFileName);
            boolean beforeDatasetEmpty = StringUtils.isEmpty(beforeDatasetName);

            if (!(afterDatasetEmpty && beforeDatasetEmpty)) {
                xlsDataFileLoader = getImplementingBeanFromContext(testContext, DataFileLoader.class);
                testInstance.xlsDataFileLoader = xlsDataFileLoader;
            }

            if (!beforeDatasetEmpty) {
                IDataSet dataSet = xlsDataFileLoader.load(beforeDatasetName);
                databaseTester = getImplementingBeanFromContext(testContext, IDatabaseTester.class);
                if (Optional.ofNullable(dataSet).isPresent()) {
                    databaseTester.setDataSet(dataSet);
                    databaseTester.onSetup();
                    testInstance.databaseTester = databaseTester;
                }
            }
        }

        /**
         * Gets the bean implementing the given class {@param <T>} from the context.
         *
         * @param testCtx the test context.
         * @param tClass the Class class.
         * @param <T> the bean class generic parameter.
         * @return the first bean of the given class.
         * @throws IllegalStateException if an error
         *         occurs while retrieving the application context of the test context.
         * @throws org.springframework.beans.BeansException if a bean of given class could not be created
         */
        private <T> T getImplementingBeanFromContext(TestContext testCtx, Class<T> tClass) throws Exception {
            Map<String, T> implementations = testCtx.getApplicationContext().getBeansOfType(tClass);
            for (T t : implementations.values()) {
                return t;
            }
            return null;
        }
    }
}
