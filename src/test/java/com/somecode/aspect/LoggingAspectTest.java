/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.aspect;

import com.google.gson.Gson;
import com.somecode.aspects.ClassType;
import com.somecode.aspects.LoggingAspect;
import com.somecode.controller.PacketAppService;
import com.somecode.controller.RestfulController;
import com.somecode.dao.AbstractDbunitTransactionalJUnit4SpringContextTests;
import com.somecode.dao.DaoTestConfig;
import com.somecode.dao.DataSets;
import com.somecode.dao.PacketAppDao;
import com.somecode.domain.DataParams;
import com.somecode.domain.OperationType;
import com.somecode.domain.RequestObj;
import com.somecode.domain.SecurityParams;
import com.somecode.utils.TestLogAppender;
import com.somecode.utils.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * The fairly self-explanatory Logging Aspect Test class.
 * Tests every separate method that is an object for the aspect logging.
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@WebAppConfiguration
@TestExecutionListeners({AbstractDbunitTransactionalJUnit4SpringContextTests.DbunitTestExecutionListener.class})
@ActiveProfiles("test")
public class LoggingAspectTest extends AbstractDbunitTransactionalJUnit4SpringContextTests {
    private static final String BEFORE_LOGGING = "BEFORE_LOGGING";
    private static final String AFTER_LOGGING = "AFTER_LOGGING";

    private static final String GET_USER_ROLE = "getUserRole";
    private static final String LOAD_DATA = "loadData";
    private static final String SAVE_ALL_CHANGES_TO_BASE = "saveAllChangesToBase";
    private static final String DELETE_COMPTS = "deleteCompts";
    private static final String DELETE_PACKETS = "deletePackets";
    private static final String DAO_PACKET = "com.somecode.dao.PacketAppDao";
    private static final String LOAD_ALL_COMBO_DATA = "loadAllComboData";
    private static final String LOAD_ALL_STATES = "loadAllStates";
    private static final String LOAD_COMPTS_SUPPL_INFO = "loadComptsSupplInfo";
    private static final String LOAD_COMPTS = "loadCompts";
    private static final String LOAD_PACKETS = "loadPackets";
    private static final String UPDATE_COMPTS = "updateCompts";
    private static final String ADD_OR_UPDATE_PACKETS = "addOrUpdatePackets";

    private static final String TEST_LOAD_STATES_FILENAME = "/com/somecode/dao/testLoadStates.xls";
    private static final String TEST_LOAD_COMBODATA_FILENAME = "/com/somecode/dao/testLoadComboData.xls";
    private static final String TEST_ADD_PACKETS_BEFORE_FILENAME = "/com/somecode/dao/testAddPackets_before.xls";

    private static final String SPACE = " ";
    private static final String DOT = ".";
    private static final String OPENING_BRACKET = "(";
    private static final String CLOSING_BRACKET = ")";

    private static final Long testId = 1L;

    private Gson GSON = new Gson();

    private Logger root = Logger.getRootLogger();
    private TestLogAppender testAppender;

    @Autowired
    private LoggingAspect loggingAspect;

    /**
     * The restful controller, service and dao all are injected, their proxies are built
     * in the {@link #setUpProxies()}.
     */
    @Autowired
    private RestfulController controller;
    private RestfulController proxyController;

    @Autowired
    private PacketAppService service;
    private PacketAppService proxyService;

    @Autowired
    private PacketAppDao dao;
    private PacketAppDao proxyDao;

    /**
     * Sets up the proxies with the logging aspect weaved.
     */
    @PostConstruct
    public void setUpProxies() {
        AspectJProxyFactory serviceFactory = new AspectJProxyFactory(service);
        serviceFactory.addAspect(loggingAspect);
        proxyService = serviceFactory.getProxy();

        AspectJProxyFactory controllerFactory = new AspectJProxyFactory(controller);
        controllerFactory.addAspect(loggingAspect);
        proxyController = controllerFactory.getProxy();

        AspectJProxyFactory daoFactory = new AspectJProxyFactory(dao);
        daoFactory.addAspect(loggingAspect);
        proxyDao = daoFactory.getProxy();
    }

    /**
     * Resets the log appender before each test.
     */
    @Before
    public void resetLogAppender() {
        testAppender = new TestLogAppender();
        root.addAppender(testAppender);
        root.setLevel(Level.DEBUG);
    }

    @Test
    public void testGetUserRoleInService() throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        proxyService.getUserRole(null, null);
        Method method = service.getClass().getDeclaredMethod(GET_USER_ROLE, String.class, String.class);
        String methodSignature = buildMethodSignature(method, false);
        testMethod(ClassType.SERVICE, methodSignature);
    }

    @Test
    public void testLoadDataInService() throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        proxyService.loadData(null);
        Method method = service.getClass().getDeclaredMethod(LOAD_DATA, Long.class);
        String methodSignature = buildMethodSignature(method, false);
        testMethod(ClassType.SERVICE, methodSignature);
    }

    @Test
    public void testSaveAllChangesToBaseInService() throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        proxyService.saveAllChangesToBase(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), null);
        Method method = service.getClass().getDeclaredMethod(SAVE_ALL_CHANGES_TO_BASE, List.class, List.class,
                List.class, List.class, List.class, Long.class);
        String methodSignature = buildMethodSignature(method, false);
        testMethod(ClassType.SERVICE, methodSignature);
    }

    @Test
    public void testGetUserRoleInController() throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        proxyController.getUserRole(buildRequestObjJson());
        Method method = controller.getClass().getDeclaredMethod(GET_USER_ROLE, String.class);
        String methodSignature = buildMethodSignature(method, false);
        testMethod(ClassType.CONTROLLER, methodSignature);
    }

    @Test
    public void testLoadDataInController() throws Exception {
        proxyController.loadData(buildRequestObjJson());
        Method method = controller.getClass().getDeclaredMethod(LOAD_DATA, String.class);
        String methodSignature = buildMethodSignature(method, false);
        testMethod(ClassType.CONTROLLER, methodSignature);
    }

    @Test
    public void testSaveAllChangesToBaseInController() throws Exception {
        proxyController.saveAllChangesToBase(buildRequestObjJson());
        Method method = controller.getClass().getDeclaredMethod(SAVE_ALL_CHANGES_TO_BASE, String.class);
        String methodSignature = buildMethodSignature(method, false);
        testMethod(ClassType.CONTROLLER, methodSignature);
    }

    @Test
    public void testDeleteComptsInDao() throws Exception {
        proxyDao.deleteCompts(Arrays.asList(testId));
        Method method = dao.getClass().getDeclaredMethod(DELETE_COMPTS, List.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @Test
    public void testDeletePacketsInDao() throws Exception {
        proxyDao.deletePackets(Arrays.asList(testId));
        Method method = dao.getClass().getDeclaredMethod(DELETE_PACKETS, List.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @DataSets(before = TEST_LOAD_COMBODATA_FILENAME)
    @DirtiesContext
    @Test
    public void testAllLoadComboDataInDao() throws Exception {
        proxyDao.loadAllComboData();
        Method method = dao.getClass().getDeclaredMethod(LOAD_ALL_COMBO_DATA);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @DataSets(before = TEST_LOAD_STATES_FILENAME)
    @DirtiesContext
    @Test
    public void testLoadAllStatesInDao() throws Exception {
        proxyDao.loadAllStates();
        Method method = dao.getClass().getDeclaredMethod(LOAD_ALL_STATES);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @Test
    public void testLoadComptsInDao() throws Exception {
        proxyDao.loadCompts(testId);
        Method method = dao.getClass().getDeclaredMethod(LOAD_COMPTS, Long.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @Test
    public void testLoadPacketsInDao() throws Exception {
        proxyDao.loadPackets(testId);
        Method method = dao.getClass().getDeclaredMethod(LOAD_PACKETS, Long.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @Test
    public void testLoadComptsSupplInfoInDao() throws Exception {
        proxyDao.loadComptsSupplInfo(testId);
        Method method = dao.getClass().getDeclaredMethod(LOAD_COMPTS_SUPPL_INFO, Long.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @DataSets(before = TEST_LOAD_COMBODATA_FILENAME)
    @DirtiesContext
    @Test
    public void testUpdateComptsInDao() throws Exception {
        proxyDao.updateCompts(Collections.emptyList());
        Method method = dao.getClass().getDeclaredMethod(UPDATE_COMPTS, List.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @Test
    public void testGetUserRoleInDao() throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        proxyDao.getUserRole(null, null);
        Method method = dao.getClass().getDeclaredMethod(GET_USER_ROLE, String.class, String.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    @Test
    @DataSets(before = TEST_ADD_PACKETS_BEFORE_FILENAME)
    @DirtiesContext
    public void testAddOrUpdatePackets() throws Exception {
        proxyDao.addOrUpdatePackets(Collections.emptyList(), OperationType.UPDATE);
        Method method = dao.getClass().getDeclaredMethod(ADD_OR_UPDATE_PACKETS, List.class, OperationType.class);
        String methodSignature = buildMethodSignature(method, true);
        testMethod(ClassType.DAO, methodSignature);
    }

    private String buildMethodSignature(Method method, boolean isDao) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getSimpleName());
        sb.append(SPACE);
        if(!isDao) {
            sb.append(method.getDeclaringClass().getName());
        } else {
            sb.append(DAO_PACKET);
        }
        sb.append(DOT);
        sb.append(method.getName());
        sb.append(OPENING_BRACKET);
        String parameterTypeNames = Arrays.asList(method.getParameterTypes()).stream().map(Class::getSimpleName)
                .collect(Collectors.joining(","));
        sb.append(parameterTypeNames);
        sb.append(CLOSING_BRACKET);
        return sb.toString();
    }

    private void testMethod (ClassType classType, String methodName) {
        final LoggingEvent loggingEventBefore = testAppender.getLog().get(0);
        final String testBeforeLogging = (String) ReflectionTestUtils.getField(loggingAspect, BEFORE_LOGGING);
        assertEquals(Utils.getMessage(testBeforeLogging, new Object[]{classType.toString(), methodName}),
                loggingEventBefore.getMessage());
        assertEquals(Level.DEBUG, loggingEventBefore.getLevel());
        final LoggingEvent loggingEventAfter = testAppender.getLog().get(testAppender.getLog().size() - 1);
        final String testAfterLogging = (String) ReflectionTestUtils.getField(loggingAspect, AFTER_LOGGING);
        assertEquals(Utils.getMessage(testAfterLogging, new Object[]{classType.toString(), methodName}),
                loggingEventAfter.getMessage());
        assertEquals(Level.DEBUG, loggingEventAfter.getLevel());
    }

    private String buildRequestObjJson() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.setUsername(SPACE);
        securityParams.setPassword(SPACE);

        DataParams dataParams = new DataParams();
        dataParams.setComptIdsToDelete(Arrays.asList(1L));
        dataParams.setComptsToUpdateParamsList(Collections.emptyList());
        dataParams.setPacketId(null);
        dataParams.setPacketIdsToDelete(Collections.emptyList());
        dataParams.setPacketsToAddParamsList(Collections.emptyList());
        dataParams.setPacketsToUpdateParamsList(Collections.emptyList());

        RequestObj requestObj = new RequestObj();
        requestObj.setDataParams(dataParams);
        requestObj.setSecurityParams(securityParams);

        return GSON.toJson(requestObj);
    }
}
