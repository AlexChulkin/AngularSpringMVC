package com.somecode.service;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by alexc_000 on 2016-10-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@ActiveProfiles("test")
public class PacketAppServiceTest {

    private static final int TEST_LIST_SIZE = 2;

    private static final String TEST_LOAD_DATA_FOR_SPECIFIC_PACKET = "LOAD_DATA_FOR_SPECIFIC_PACKET";
    private static final String TEST_LOAD_DATA_FOR_ALL_PACKETS = "LOAD_DATA_FOR_ALL_PACKETS";
    private static final String PERSIST_SPECIFIC_PACKET = "PERSIST_SPECIFIC_PACKET";
    private static final String PERSIST_ALL_PACKETS = "PERSIST_ALL_PACKETS";
    private static final String ADD_PACKETS_EXCEPTION_STACKTRACE = "addPacketsExceptionStackTrace";
    private static final String UPDATE_PACKETS_EXCEPTION_STACKTRACE = "updatePacketsExceptionStackTrace";
    private static final String UPDATE_COMPTS_EXCEPTION_STACKTRACE = "updateComptsExceptionStackTrace";
    private static final String ADD_PACKETS_EXCEPTION_REPORT = "ADD_PACKETS_EXCEPTION_REPORT";
    private static final String UPDATE_COMPTS_EXCEPTION_REPORT = "UPDATE_COMPTS_EXCEPTION_REPORT";
    private static final String UPDATE_PACKETS_EXCEPTION_REPORT = "UPDATE_PACKETS_EXCEPTION_REPORT";
    private static final String ADD_PACKETS_EXCEPTION_MESSAGE = "addPacketsExceptionMessage";
    private static final String UPDATE_COMPTS_EXCEPTION_MESSAGE = "updateComptsExceptionMessage";
    private static final String UPDATE_PACKETS_EXCEPTION_MESSAGE = "updatePacketsExceptionMessage";
    private static final String UPDATE_COMPTS = "UPDATE_COMPTS";
    private static final String UPDATE_PACKETS = "UPDATE_PACKETS";
    private static final String ADD_PACKETS = "ADD_PACKETS";

    private Logger root = Logger.getRootLogger();
    private TestLogAppender testAppender;

    private Long testPacketId = 1L;

    private int currentLogIndex;

    @Autowired
    private PacketAppService service;

    private PacketAppDao mockDao;

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
    public void testSaveAllChanges1() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChanges2() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, false, false, false, testPacketId);
    }

    @Test
    public void testSaveAllChanges3() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(true, false, false, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChanges4() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(true, false, false, false, false, false, false, testPacketId);
    }

    @Test
    public void testSaveAllChanges5() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, true, false, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChanges6() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, true, false, false, false, false, false, testPacketId);
    }

    @Test
    public void testSaveAllChanges7() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, false, null);
    }

    @Test
    public void testSaveAllChanges8() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, false, testPacketId);
    }

    @Test
    public void testSaveAllChanges9() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, true, null);
    }

    @Test
    public void testSaveAllChanges10() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, true, false, false, false, true, testPacketId);
    }

    @Test
    public void testSaveAllChanges11() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, false, true, null);
    }

    @Test
    public void testSaveAllChanges12() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, false, true, testPacketId);
    }

    @Test
    public void testSaveAllChanges13() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, false, null);
    }

    @Test
    public void testSaveAllChanges14() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, false, testPacketId);
    }

    @Test
    public void testSaveAllChanges15() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, true, null);
    }

    @Test
    public void testSaveAllChanges16() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, true, false, true, true, testPacketId);
    }

    @Test
    public void testSaveAllChanges17() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, false, null);
    }

    @Test
    public void testSaveAllChanges18() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, false, testPacketId);
    }

    @Test
    public void testSaveAllChanges19() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, false, null);
    }

    @Test
    public void testSaveAllChanges20() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, false, testPacketId);
    }

    @Test
    public void testSaveAllChanges21() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, true, null);
    }

    @Test
    public void testSaveAllChanges22() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, false, true, testPacketId);
    }

    @Test
    public void testSaveAllChanges23() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, true, null);
    }

    @Test
    public void testSaveAllChanges24() throws DatabaseException {
        testSaveAllChangesToBaseWithParams(false, false, false, false, true, true, true, testPacketId);
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
                = (String) ReflectionTestUtils.getField(service, packetId == null ? PERSIST_ALL_PACKETS
                                                                                  : PERSIST_SPECIFIC_PACKET);

        Map<String, Boolean> result = service.saveAllChangesToBase(test_comptIdsToDelete, test_packetIdsToDelete,
                test_comptsToUpdateParamsList, test_packetsToAddParamsList, test_packetsToUpdateParamsList, packetId);

        assertEquals(testLogSize, testAppender.getLog().size());

        final LoggingEvent loggingEvent = testAppender.getLog().get(currentLogIndex++);
        assertEquals(Utils.getMessage(testSaveAllChanges, packetId == null ? null : new Object[]{packetId}),
                     loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());


        if (!comptIdsToDeleteIsEmpty) {
            verify(mockDao, times(1)).deleteCompts(test_comptIdsToDelete);
        } else {
            verify(mockDao, never()).deleteCompts(any());
        }

        if (packetId == null && !packetIdsToDeleteIsEmpty) {
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
                = (String) ReflectionTestUtils.getField(service, packetId == null
                ? TEST_LOAD_DATA_FOR_ALL_PACKETS
                : TEST_LOAD_DATA_FOR_SPECIFIC_PACKET);
        Data result = service.loadData(packetId);

        final int testLogSize = 1;
        assertEquals(testLogSize, testAppender.getLog().size());

        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadData, packetId == null ? null : new Object[]{packetId}),
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

    private <T extends EntityProtoType> T instantiateEntity(Class<T> entityClass) {
        T entity;
        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            entity = null;
        }
        return entity;
    }

    private <T extends EntityProtoType> List<T> buildEntityList(Class<T> entityClass) {
        List<T> result = new ArrayList<>();
        IntStream.rangeClosed(1, TEST_LIST_SIZE)
                .boxed()
                .forEach(i ->
                        {
                            T entity = instantiateEntity(entityClass);
                            entity.setId((long) i);
                            result.add(entity);
                        });
        return result;
    }

    private <T extends EntityProtoType> void checkEntities(List<T> entities) {
        assertEquals(TEST_LIST_SIZE, entities.size());
        IntStream.range(0, TEST_LIST_SIZE)
                .boxed()
                .forEach(i -> assertEquals(Long.valueOf(i + 1), entities.get(i).getId()));
    }

    private <T extends SelfSettingEntityPrototype> T selfSettingInstantiateEntity(Class<T> entityClass) {
        T entity;
        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            entity = null;
        }
        return entity;
    }

    private <T extends SelfSettingEntityPrototype> List<T> buildSelfSettingEntityList(Class<T> entityClass) {
        List<T> result = new ArrayList<>();
        IntStream.rangeClosed(1, TEST_LIST_SIZE)
                .boxed()
                .forEach(i ->
                        {
                            T entity = selfSettingInstantiateEntity(entityClass);
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
