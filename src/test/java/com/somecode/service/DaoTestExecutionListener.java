package com.somecode.service;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.util.fileloader.DataFileLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.Map;

public class DaoTestExecutionListener implements TestExecutionListener {

    private IDatabaseTester databaseTester;

    @Override
    public void afterTestClass(TestContext arg0) throws Exception {
    }

    @Override
    public void afterTestMethod(TestContext testCtx) throws Exception {
        ((HasCachedData) testCtx.getTestInstance()).clearCachedData();

        if (databaseTester != null) {
            databaseTester.onTearDown();
        }
    }

    @Override
    public void beforeTestClass(TestContext testCtx) throws Exception {
    }

    @Override
    public void beforeTestMethod(TestContext testCtx) throws Exception {
        DataSets dataSetAnnotation = testCtx.getTestMethod().getAnnotation(DataSets.class);

        if (dataSetAnnotation == null) {
            return;
        }

        String dataSetName = dataSetAnnotation.setUpDataSet();

        if (!dataSetName.equals("")) {
            databaseTester = getImplementingBeanFromContext(testCtx, IDatabaseTester.class);
            DataFileLoader xlsDataFileLoader = getImplementingBeanFromContext(testCtx, DataFileLoader.class);
            IDataSet dataSet = xlsDataFileLoader.load(dataSetName);

            databaseTester.setDataSet(dataSet);
            databaseTester.onSetup();
        }
    }

    private <T> T getImplementingBeanFromContext(TestContext testCtx, Class<T> tClass) throws Exception {
        Map<String, T> implementations = testCtx.getApplicationContext().getBeansOfType(tClass);
        for (T t : implementations.values()) {
            return t;
        }
        return null;
    }

    @Override
    public void prepareTestInstance(TestContext arg0) throws Exception {
    }
}
