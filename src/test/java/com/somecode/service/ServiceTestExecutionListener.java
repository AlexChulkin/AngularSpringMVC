package com.somecode.service;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.util.fileloader.DataFileLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.Optional;

public class ServiceTestExecutionListener implements TestExecutionListener {
    @PersistenceContext
    EntityManager em;
    private IDatabaseTester databaseTester;

    @Override
    public void afterTestClass(TestContext arg0) throws Exception {
    }

    @Override
    public void afterTestMethod(TestContext arg0) throws Exception {
        if (databaseTester != null) {
            databaseTester.onTearDown();
        }

        clearCaches();
    }

    private void clearCaches() {
        Optional.of(em)
                .ifPresent(EntityManager::clear);
        Optional.of(em)
                .map(EntityManager::getEntityManagerFactory)
                .map(EntityManagerFactory::getCache)
                .ifPresent(Cache::evictAll);
    }

    @Override
    public void beforeTestClass(TestContext arg0) throws Exception {
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
