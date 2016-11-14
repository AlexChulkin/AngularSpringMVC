/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.controller;

import com.somecode.dao.DaoTestConfig;
import com.somecode.dao.PacketAppDao;
import com.somecode.domain.*;
import com.somecode.utils.TestLogAppender;
import com.somecode.utils.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * The fairly self-explanatory parameterized service test class.
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@ActiveProfiles("test")
public class PacketAppServiceTest {

    /**
     * The local constants
     */
    private static final int TEST_LIST_SIZE = 2;
    private static final Long TEST_PACKET_ID = 1L;

    /**
     * The service variables
     */
    private static final String ADD_PACKETS_EXCEPTION_STACKTRACE = "addPacketsExceptionStackTrace";
    private static final String UPDATE_PACKETS_EXCEPTION_STACKTRACE = "updatePacketsExceptionStackTrace";
    private static final String UPDATE_COMPTS_EXCEPTION_STACKTRACE = "updateComptsExceptionStackTrace";
    private static final String ADD_PACKETS_EXCEPTION_MESSAGE = "addPacketsExceptionMessage";
    private static final String UPDATE_COMPTS_EXCEPTION_MESSAGE = "updateComptsExceptionMessage";
    private static final String UPDATE_PACKETS_EXCEPTION_MESSAGE = "updatePacketsExceptionMessage";

    /**
     * The message source constants
     */
    private static final String TEST_LOAD_DATA_FOR_SPECIFIC_PACKET = "LOAD_DATA_FOR_SPECIFIC_PACKET";
    private static final String TEST_LOAD_DATA_FOR_ALL_PACKETS = "LOAD_DATA_FOR_ALL_PACKETS";
    private static final String PERSIST_SPECIFIC_PACKET = "PERSIST_SPECIFIC_PACKET";
    private static final String PERSIST_ALL_PACKETS = "PERSIST_ALL_PACKETS";
    private static final String ADD_PACKETS_EXCEPTION_REPORT = "ADD_PACKETS_EXCEPTION_REPORT";
    private static final String UPDATE_COMPTS_EXCEPTION_REPORT = "UPDATE_COMPTS_EXCEPTION_REPORT";
    private static final String UPDATE_PACKETS_EXCEPTION_REPORT = "UPDATE_PACKETS_EXCEPTION_REPORT";
    private static final String UPDATE_COMPTS = "UPDATE_COMPTS";
    private static final String UPDATE_PACKETS = "UPDATE_PACKETS";
    private static final String ADD_PACKETS = "ADD_PACKETS";

    /**
     * Root logger used for log testing
     */
    private Logger root = Logger.getRootLogger();
    /** Test Log appender logger used for log testing */
    private TestLogAppender testAppender;

    /** The current log index variable used for log testing */
    private int currentLogIndex;

    /** The injected service */
    @Autowired
    private PacketAppService service;

    /** The dao mock */
    private PacketAppDao mockDao;

    /**
     * Runs before each test, resets the current log index, log appender and dao mock in the initial state.
     */
    @Before
    public void beforeEachTest() {
        currentLogIndex = 0;
        mockDao = mock(PacketAppDao.class);
        ReflectionTestUtils.setField(service, "packetAppDao", mockDao);
        testAppender = new TestLogAppender();
        root.addAppender(testAppender);
        root.setLevel(Level.INFO);
    }

    @Test
    public void testGetUserRole() {
        String test_username = "username";
        String test_password = "password";
        Role test_role = Role.ADMIN;
        when(mockDao.getUserRole(test_username, test_password)).thenReturn(test_role);

        assertEquals(test_role, service.getUserRole(test_username, test_password));
        verify(mockDao, Mockito.times(1)).getUserRole(test_username, test_password);
    }

    @Test
    public void testLoadDataAllPacketsAllDataNotEmpty() throws DatabaseException {
        testLoadDataWithParams(null, false, false, false, false);
    }

    @Test
    public void testLoadDataOnePacketAllDataNotEmpty() throws DatabaseException {
        testLoadDataWithParams(1L, false, false, false, false);
    }

    @Test
    public void testLoadDataAllPacketsOnlyStatesEmpty() throws DatabaseException {
        testLoadDataWithParams(null, true, false, false, false);
    }

    @Test
    public void testLoadDataOnePacketOnlyStatesEmpty() throws DatabaseException {
        testLoadDataWithParams(1L, true, false, false, false);
    }

    @Test
    public void testLoadDataAllPacketsOnlyComboDatasEmpty() throws DatabaseException {
        testLoadDataWithParams(null, false, true, false, false);
    }

    @Test
    public void testLoadDataOnePacketOnlyComboDatasEmpty() throws DatabaseException {
        testLoadDataWithParams(1L, false, true, false, false);
    }

    @Test
    public void testLoadDataAllPacketsOnlyPacketsEmpty() throws DatabaseException {
        testLoadDataWithParams(null, false, false, true, false);
    }

    @Test
    public void testLoadDataOnePacketOnlyPacketsEmpty() throws DatabaseException {
        testLoadDataWithParams(1L, false, false, true, false);
    }

    @Test
    public void testLoadDataAllPacketsOnlyComptsEmpty() throws DatabaseException {
        testLoadDataWithParams(null, false, false, false, true);
    }

    @Test
    public void testLoadDataOnePacketOnlyComptsEmpty() throws DatabaseException {
        testLoadDataWithParams(1L, false, false, false, true);
    }

    @Test
    public void testLoadDataAllPacketsAllDataEmpty() throws DatabaseException {
        testLoadDataWithParams(null, true, true, true, true);
    }

    @Test
    public void testLoadDataOnePacketAllDataEmpty() throws DatabaseException {
        testLoadDataWithParams(1L, true, true, true, true);
    }

    @Test
    public void testSaveAllChangesNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChangesNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, false, false, false, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyComptsToDeleteNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(true, false, false, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChangesEmptyComptsToDeleteNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(true, false, false, false, false, false, false, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyPacketsToDeleteNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, true, false, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChangesEmptyPacketsToDeleteNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, true, false, false, false, false, false, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyComptsToUpdateNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChangesEmptyComptsToUpdateNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, false, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataComptsToUpdateNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, true, null);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataComptsToUpdateNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, true, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataPacketsToAddNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, false, true, null);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataPacketsToAddNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, false, true, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyStatesPacketsToAddNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, false, null);
    }

    @Test
    public void testSaveAllChangesEmptyStatesPacketsToAddNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, false, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataStatesPacketsToAddNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, true, null);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataStatesPacketsToAddNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, true, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyPacketsToUpdateNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, false, null);
    }

    @Test
    public void testSaveAllChangesEmptyPacketsToUpdateNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, false, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyStatesPacketsToUpdateNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, false, null);
    }

    @Test
    public void testSaveAllChangesEmptyStatesPacketsToUpdateNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, false, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataPacketsToUpdateNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, true, null);
    }

    @Test
    public void testSaveAllChangesEmptyComboDataPacketsToUpdateNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, true, TEST_PACKET_ID);
    }

    @Test
    public void testSaveAllChangesEmptyStatesComboDataPacketsToUpdateNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, true, null);
    }

    @Test
    public void testSaveAllChangesEmptyStatesComboDataPacketsToUpdateNotNullPacketId() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, true, TEST_PACKET_ID);
    }

    private void testSaveAllChangesToBaseWithParams(boolean comptIdsToDeleteIsEmpty,
                                                    boolean packetIdsToDeleteIsEmpty,
                                                    boolean comptsToUpdateParamsListIsEmpty,
                                                    boolean packetsToAddParamsListIsEmpty,
                                                    boolean packetsToUpdateParamsListIsEmpty,
                                                    boolean statesIsEmpty,
                                                    boolean comboDatasIsEmpty, Long packetId) throws DatabaseException {

        List<Long> test_comptIdsToDelete = comptIdsToDeleteIsEmpty ? Collections.emptyList() : generateIdsList();
        List<Long> test_packetIdsToDelete = packetIdsToDeleteIsEmpty ? Collections.emptyList() : generateIdsList();
        List<PacketParams> test_packetsToAddParamsList
                = packetsToAddParamsListIsEmpty
                ? Collections.emptyList()
                : buildSelfSettingEntityList(PacketParams.class);
        List<PacketParams> test_packetsToUpdateParamsList
                = packetsToUpdateParamsListIsEmpty
                ? Collections.emptyList()
                : buildSelfSettingEntityList(PacketParams.class);
        List<ComptParams> test_comptsToUpdateParamsList
                = comptsToUpdateParamsListIsEmpty
                ? Collections.emptyList()
                : buildSelfSettingEntityList(ComptParams.class);

        doReturn(Collections.emptyList()).when(mockDao).deleteCompts(anyListOf(Long.class));
        doReturn(Collections.emptyList()).when(mockDao).deletePackets(anyListOf(Long.class));

        if (statesIsEmpty || comboDatasIsEmpty) {
            doThrow(new DatabaseException()).when(mockDao).addOrUpdatePackets(anyListOf(PacketParams.class),
                    any(OperationType.class));
        } else {
            doNothing().when(mockDao).addOrUpdatePackets(anyListOf(PacketParams.class), any(OperationType.class));
        }

        if (comboDatasIsEmpty) {
            doThrow(new DatabaseException()).when(mockDao).updateCompts(anyListOf(ComptParams.class));
        } else {
            doReturn(Collections.emptyMap()).when(mockDao).updateCompts(anyListOf(ComptParams.class));
        }

        int testLogSize = 1;
        if (!comptsToUpdateParamsListIsEmpty && comboDatasIsEmpty) {
            testLogSize++;
        }
        if (comboDatasIsEmpty || statesIsEmpty) {
            if (!packetsToAddParamsListIsEmpty) {
                testLogSize++;
            }
            if (!packetsToUpdateParamsListIsEmpty) {
                testLogSize++;
            }
        }

        final String testSaveAllChanges
                = (String) ReflectionTestUtils.getField(service, Optional.ofNullable(packetId).isPresent()
                ? PERSIST_SPECIFIC_PACKET
                : PERSIST_ALL_PACKETS);

        Map<String, Boolean> result = service.saveAllChangesToBase(test_comptIdsToDelete, test_packetIdsToDelete,
                test_comptsToUpdateParamsList, test_packetsToAddParamsList, test_packetsToUpdateParamsList, packetId);

        assertEquals(testLogSize, testAppender.getLog().size());

        final LoggingEvent loggingEvent = testAppender.getLog().get(currentLogIndex++);
        assertEquals(Utils.getMessage(testSaveAllChanges, Optional.ofNullable(packetId).isPresent()
                        ? new Object[]{packetId}
                        : null),
                     loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());


        if (!comptIdsToDeleteIsEmpty) {
            verify(mockDao, times(1)).deleteCompts(test_comptIdsToDelete);
        } else {
            verify(mockDao, never()).deleteCompts(any());
        }

        if (!Optional.ofNullable(packetId).isPresent() && !packetIdsToDeleteIsEmpty) {
            verify(mockDao, times(1)).deletePackets(test_packetIdsToDelete);
        } else {
            verify(mockDao, never()).deletePackets(any());
        }

        if (!comptsToUpdateParamsListIsEmpty) {
            final String testUpdateCompts = (String) ReflectionTestUtils.getField(service, UPDATE_COMPTS);
            verify(mockDao, times(1)).updateCompts(test_comptsToUpdateParamsList);
            if (comboDatasIsEmpty) {
                assertTrue(result.containsKey(testUpdateCompts));
                assertTrue(result.get(testUpdateCompts));
                logErrorReport(UPDATE_COMPTS_EXCEPTION_REPORT, UPDATE_COMPTS_EXCEPTION_MESSAGE,
                               UPDATE_COMPTS_EXCEPTION_STACKTRACE);
            }
        }

        if (!packetsToAddParamsListIsEmpty) {
            final String testAddPackets = (String) ReflectionTestUtils.getField(service, ADD_PACKETS);
            verify(mockDao, times(1)).addOrUpdatePackets(test_packetsToAddParamsList, OperationType.ADD);
            if (comboDatasIsEmpty || statesIsEmpty) {
                assertTrue(result.containsKey(testAddPackets));
                assertTrue(result.get(testAddPackets));
                logErrorReport(ADD_PACKETS_EXCEPTION_REPORT, ADD_PACKETS_EXCEPTION_MESSAGE,
                        ADD_PACKETS_EXCEPTION_STACKTRACE);
            }
        }

        if (!packetsToUpdateParamsListIsEmpty) {
            final String testUpdatePackets = (String) ReflectionTestUtils.getField(service, UPDATE_PACKETS);
            verify(mockDao, times(1)).addOrUpdatePackets(test_packetsToUpdateParamsList, OperationType.UPDATE);
            if (comboDatasIsEmpty || statesIsEmpty) {
                assertTrue(result.containsKey(testUpdatePackets));
                assertTrue(result.get(testUpdatePackets));
                logErrorReport(UPDATE_PACKETS_EXCEPTION_REPORT, UPDATE_PACKETS_EXCEPTION_MESSAGE,
                        UPDATE_PACKETS_EXCEPTION_STACKTRACE);
            }
        }
    }

    private void logErrorReport (String exceptionReportVar, String exceptionMessageVar,
                                 String exceptionStackTraceVar) {
        final String exceptionReport
                = (String) ReflectionTestUtils.getField(service, exceptionReportVar);
        final String exceptionStackTrace
                = (String) ReflectionTestUtils.getField(service, exceptionStackTraceVar);
        final String exceptionMessage
                = (String) ReflectionTestUtils.getField(service, exceptionMessageVar);
        final LoggingEvent loggingEvent = testAppender.getLog().get(currentLogIndex++);
        assertEquals(Utils.getMessage(exceptionReport, new Object[]{exceptionMessage, exceptionStackTrace}),
                     loggingEvent.getMessage());
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    private void testLoadDataWithParams(Long packetId, boolean emptyStates, boolean emptyComboDatas,
                                        boolean emptyPackets, boolean emptyCompts) throws DatabaseException {
        List<ComboData> test_comboDatas = emptyComboDatas ? Collections.emptyList() : buildEntityList(ComboData.class);
        List<State> test_states = emptyStates ? Collections.emptyList() : buildEntityList(State.class);
        List<ComptInfo> test_comptsInfo = emptyCompts ? Collections.emptyList() : buildEntityList(ComptInfo.class);
        List<PacketInfo> test_packetsInfo = emptyPackets ? Collections.emptyList() : buildEntityList(PacketInfo.class);
        List<ComptSupplInfo> test_comptSupplInfo = (emptyComboDatas || emptyStates || emptyCompts)
                ? Collections.emptyList()
                : buildEntityList(ComptSupplInfo.class);

        if (emptyStates) {
            doThrow(new DatabaseException()).when(mockDao).loadAllStates();
        } else {
            doReturn(test_states).when(mockDao).loadAllStates();
        }

        if (emptyComboDatas) {
            doThrow(new DatabaseException()).when(mockDao).loadAllComboData();
        } else {
            doReturn(test_comboDatas).when(mockDao).loadAllComboData();
        }

        doReturn(test_comptsInfo).when(mockDao).loadCompts(packetId);
        doReturn(test_packetsInfo).when(mockDao).loadPackets(packetId);
        doReturn(test_comptSupplInfo).when(mockDao).loadComptsSupplInfo(packetId);

        final String testLoadData
                = (String) ReflectionTestUtils.getField(service,
                Optional.ofNullable(packetId).isPresent()
                        ? TEST_LOAD_DATA_FOR_SPECIFIC_PACKET
                        : TEST_LOAD_DATA_FOR_ALL_PACKETS);
        Data result = service.loadData(packetId);

        final int testLogSize = 1;
        assertEquals(testLogSize, testAppender.getLog().size());

        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadData,
                Optional.ofNullable(packetId).isPresent() ? new Object[]{packetId} : null),
                loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());

        if (!emptyComboDatas) {
            checkEntities(result.getComboData());
        }
        if (!emptyStates) {
            checkEntities(result.getStates());
        }
        if (!emptyCompts) {
            checkEntities(result.getCompts());
        }
        if (!emptyPackets) {
            checkEntities(result.getPackets());
        }
        if (!emptyComboDatas && !emptyStates && !emptyCompts) {
            checkEntities(result.getComptSupplInfo());
        }
    }

    /**
     * Returns the instance of the given implementation of the {@link EntityProtoType} class.
     *
     * @param entityClass the entity class that is to be instantiated.
     * @param <E>         the formal type parameter of the {@param entityClass}.
     * @return the instance of the given entity class.
     */
    private <E extends EntityProtoType> E instantiateEntity(Class<E> entityClass) {
        E entity;
        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException x) {
            entity = null;
        }
        return entity;
    }

    /**
     * Returns the list of the elements of the given implementation type of the {@link EntityProtoType}.
     *
     * @param entityClass the class of the list elements.
     * @param <E> the formal type parameter of the {@param entityClass}.
     * @return the list of the elements of the given type.
     */
    private <E extends EntityProtoType> List<E> buildEntityList(Class<E> entityClass) {
        List<E> result = new ArrayList<>();
        IntStream.rangeClosed(1, TEST_LIST_SIZE)
                .boxed()
                .forEach(i ->
                        {
                            E entity = instantiateEntity(entityClass);
                            entity.setId((long) i);
                            result.add(entity);
                        });
        return result;
    }

    /**
     * Checks the length of the given list to be equal to the {@link #TEST_LIST_SIZE} and the ids of the elements 
     * of the given list to be sequentially equal to the values 1..{@link #TEST_LIST_SIZE}
     *
     * @param entities the list that is to be checked.
     * @param <E> the formal type parameter of the {@param entities}.
     */
    private <E extends EntityProtoType> void checkEntities(List<E> entities) {
        assertEquals(TEST_LIST_SIZE, entities.size());
        IntStream.range(0, TEST_LIST_SIZE)
                .boxed()
                .forEach(i -> assertEquals(Long.valueOf(i + 1), entities.get(i).getId()));
    }

    /**
     * Returns the instance of the given implementation of the {@link SelfSettingEntityPrototype} class.
     *
     * @param entityClass the entity class that is to be instantiated.
     * @param <E>         the formal type parameter of the {@param entityClass}.
     * @return the entity of the given type or null if {@link InstantiationException} or {@link IllegalAccessException}
     * is thrown.
     */
    private <E extends SelfSettingEntityPrototype> E instantiateSelfSettingEntity(Class<E> entityClass) {
        E entity;
        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException x) {
            entity = null;
        }
        return entity;
    }

    /**
     * Returns the list of the elements of the given implementation type of the {@link SelfSettingEntityPrototype}.
     *
     * @param entityClass the class of the list elements.
     * @param <E> the formal type parameter of the {@param entityClass}.
     * @return the list of the elements of the given type.
     */
    private <E extends SelfSettingEntityPrototype> List<E> buildSelfSettingEntityList(Class<E> entityClass) {
        List<E> result = new ArrayList<>();
        IntStream.rangeClosed(1, TEST_LIST_SIZE)
                .boxed()
                .forEach(i ->
                {
                    E entity = instantiateSelfSettingEntity(entityClass);
                            entity.setId((long) i);
                            result.add(entity);
                        });
        return result;
    }

    private List<Long> generateIdsList() {
        List<Long> result = new ArrayList<>();
        IntStream.rangeClosed(1, TEST_LIST_SIZE).boxed().forEach(i -> result.add(Long.valueOf(i)));
        return result;
    }
}
