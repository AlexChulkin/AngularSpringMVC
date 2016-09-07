package com.somecode.service;

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

/**
 * Created by alexc_000 on 2016-08-02.
 */

public abstract class AbstractDbunitTransactionalJUnit4SpringContextTests
        extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * Тестировщик DBUnit
     */
    private IDatabaseTester databaseTester;

    /**
     * Имя файла с ожидаемым набором данных
     */
    private String afterDatasetFileName;

    private DataFileLoader xlsDataFileLoader;

    /**
     * Метод, выполняющийся по окончании транзакции тестового метода: сверка данных
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


    static class DbunitTestExecutionListener extends AbstractTestExecutionListener {

        private IDatabaseTester databaseTester;
        private DataFileLoader xlsDataFileLoader;

        @Override
        public void afterTestMethod(TestContext testCtx) throws Exception {


        }

        @Override
        public void beforeTestMethod(TestContext testContext) throws Exception {
            DataSets dataSetAnnotation = testContext.getTestMethod().getAnnotation(DataSets.class);

            if (dataSetAnnotation == null) {
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
                databaseTester.setDataSet(dataSet);
                databaseTester.onSetup();
                testInstance.databaseTester = databaseTester;
            }
        }

        private <T> T getImplementingBeanFromContext(TestContext testCtx, Class<T> tClass) throws Exception {
            Map<String, T> implementations = testCtx.getApplicationContext().getBeansOfType(tClass);
            for (T t : implementations.values()) {
                return t;
            }
            return null;
        }
    }
}
