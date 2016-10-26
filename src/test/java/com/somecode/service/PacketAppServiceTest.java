package com.somecode.service;

import com.somecode.dao.DaoTestConfig;
import com.somecode.dao.PacketAppDao;
import com.somecode.domain.*;
import com.somecode.utils.TestUtils;
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
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by alexc_000 on 2016-10-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@ActiveProfiles("test")
public class PacketAppServiceTest {

    private static final int TEST_LIST_SIZE = 2;

    private static final String TEST_LOAD_DATA_FOR_GIVEN_PACKET = "LOAD_DATA_FOR_GIVEN_PACKET";
    private static final String TEST_LOAD_DATA_FOR_ALL_PACKETS = "LOAD_DATA_FOR_ALL_PACKETS";
    private static final String PERSIST_GIVEN_PACKET = "PERSIST_GIVEN_PACKET";
    private static final String PERSIST_ALL_PACKETS = "PERSIST_ALL_PACKETS";
    private static final String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";
    private static final String ADD_PACKETS_EXCEPTION_STACKTRACE = "addPacketsExceptionStackTrace";
    private static final String UPDATE_PACKETS_EXCEPTION_STACKTRACE = "updatePacketsExceptionStackTrace";
    private static final String UPDATE_COMPTS_EXCEPTION_STACKTRACE = "updateComptsExceptionStackTrace";
    private static final String ADD_PACKETS_EXCEPTION_MESSAGE = "addPacketsExceptionMessage";
    private static final String UPDATE_COMPTS_EXCEPTION_MESSAGE = "updateComptsExceptionMessage";
    private static final String UPDATE_PACKETS_EXCEPTION_MESSAGE = "updatePacketsExceptionMessage"

    private TestUtils.TestAppender testAppender;

    @Autowired
    private PacketAppService service;

    private PacketAppDao dao = mock(PacketAppDao.class);

    @Before
    public void beforeEachTest() {
        ReflectionTestUtils.setField(service, "packetAppDao", dao);
        testAppender = TestUtils.getTestAppender();
        Logger root = Logger.getRootLogger();
        root.addAppender(testAppender);
        root.setLevel(Level.DEBUG);
    }

    @Test
    public void testGetUserRole() {
        String test_username = "username";
        String test_password = "password";
        Role test_role = Role.ADMIN;
        when(dao.getUserRole(test_username, test_password)).thenReturn(test_role);

        assertEquals(test_role, service.getUserRole(test_username, test_password));
        verify(dao, Mockito.times(1)).getUserRole(test_username, test_password);
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

    private void testLoadDataWithParams(Long packetId, boolean emptyStates, boolean emptyComboDatas,
                                        boolean emptyPackets, boolean emptyCompts) throws DatabaseException {
        List<ComboData> test_comboDatas = emptyComboDatas ? Collections.emptyList() : buildEntityList(ComboData.class);
        List<State> test_states = emptyStates ? Collections.emptyList() : buildEntityList(State.class);
        List<ComptInfo> test_comptsInfo = emptyCompts ? Collections.emptyList() : buildEntityList(ComptInfo.class);
        List<PacketInfo> test_packetsInfo = emptyPackets ? Collections.emptyList() : buildEntityList(PacketInfo.class);
        List<ComptSupplInfo> test_comptSupplInfo = (emptyComboDatas || emptyStates || emptyCompts)
                ? Collections.emptyList() : buildEntityList(ComptSupplInfo.class);

        if (emptyStates) {
            doThrow(new DatabaseException()).when(dao).loadAllStates();
        } else {
            doReturn(test_states).when(dao).loadAllStates();
        }

        if (emptyComboDatas) {
            doThrow(new DatabaseException()).when(dao).loadAllComboData();
        } else {
            doReturn(test_comboDatas).when(dao).loadAllComboData();
        }

        doReturn(test_comptsInfo).when(dao).loadCompts(packetId);
        doReturn(test_packetsInfo).when(dao).loadPackets(packetId);
        doReturn(test_comptSupplInfo).when(dao).loadComptsSupplInfo(packetId);

        final String testLoadData
                = (String) ReflectionTestUtils.getField(service, packetId == null ? TEST_LOAD_DATA_FOR_ALL_PACKETS
                : TEST_LOAD_DATA_FOR_GIVEN_PACKET);
        Data result = service.loadData(packetId);

        final int testLogSize = 1;
        assertEquals(testLogSize, testAppender.getLog().size());

        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadData,
                packetId == null ? null : new Object[]{packetId}),
                loggingEvent.getMessage());
        assertEquals(Level.DEBUG, loggingEvent.getLevel());

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
                        }
                );
        return result;
    }

    private <T extends EntityProtoType> void checkEntities(List<T> entities) {
        assertEquals(TEST_LIST_SIZE, entities.size());
        IntStream.range(0, TEST_LIST_SIZE)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals(Long.valueOf(i + 1), entities.get(i).getId());
                        }
                );
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
                        }
                );
        return result;
    }

    private <T extends SelfSettingEntityPrototype> void checkSelfSettingEntities(List<T> entities) {
        assertEquals(TEST_LIST_SIZE, entities.size());
        IntStream.range(0, TEST_LIST_SIZE)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals(Long.valueOf(i + 1), entities.get(i).getId());
                        }
                );
    }

    private List<Long> generateIdsList() {
        List<Long> result = new ArrayList<>();
        IntStream.rangeClosed(1, TEST_LIST_SIZE).boxed().forEach(i -> result.add(Long.valueOf(i)));
        return result;
    }


    private void testSaveAllChangesToBaseWithParams(boolean comptIdsToDeleteIsEmpty, boolean packetIdsToDeleteIsEmpty,
                                                    boolean comptsToUpdateParamsListIsEmpty,
                                                    boolean packetsToAddParamsListIsEmpty,
                                                    boolean packetsToUpdateParamsListIsEmpty, boolean statesIsEmpty,
                                                    boolean comboDatasIsEmpty, boolean comptsToUpdateExceptionIsThrown,
                                                    boolean packetsToAddExceptionIsThrown,
                                                    boolean packetsToUpdateExceptioIsThrown, Long packetId)
            throws DatabaseException {

        List<ComboData> test_comboDatas = comboDatasIsEmpty ? Collections.emptyList()
                : buildEntityList(ComboData.class);
        List<State> test_states = statesIsEmpty ? Collections.emptyList() : buildEntityList(State.class);

        List<Long> test_comptIdsToDelete = comptIdsToDeleteIsEmpty ? Collections.emptyList() : generateIdsList();
        List<Long> test_packetIdsToDelete = packetIdsToDeleteIsEmpty ? Collections.emptyList() : generateIdsList();
        List<PacketParams> test_packetsToAddParamsList
                = packetsToAddParamsListIsEmpty ? Collections.emptyList()
                : buildSelfSettingEntityList(PacketParams.class);
        List<PacketParams> test_packetsToUpdateParamsList
                = packetsToUpdateParamsListIsEmpty ? Collections.emptyList()
                : buildSelfSettingEntityList(PacketParams.class);
        List<ComptParams> test_comptsToUpdateParamsList
                = comptsToUpdateParamsListIsEmpty ? Collections.emptyList()
                : buildSelfSettingEntityList(ComptParams.class);

        doNothing().when(dao).deleteCompts(anyListOf(Long.class));
        doNothing().when(dao).deletePackets(anyListOf(Long.class));
        doNothing().when(dao).updateCompts(anyListOf(ComptParams.class));
        doNothing().when(dao).addOrUpdatePackets(anyListOf(PacketParams.class), any(OperationType.class));


        if (statesIsEmpty) {
            doThrow(new DatabaseException()).when(dao).loadAllStates();
        } else {
            doReturn(test_states).when(dao).loadAllStates();
        }

        if (comboDatasIsEmpty) {
            doThrow(new DatabaseException()).when(dao).loadAllComboData();
        } else {
            doReturn(test_comboDatas).when(dao).loadAllComboData();
        }

        final String testSaveAllChanges
                = (String) ReflectionTestUtils.getField(service, packetId == null ? PERSIST_ALL_PACKETS
                : PERSIST_GIVEN_PACKET);

        Data result = service.loadData(packetId);

        final int testLogSize = 1;
        assertEquals(testLogSize, testAppender.getLog().size());

        if (!comptIdsToDeleteIsEmpty) {
            verify(dao, times(1)).deleteCompts(test_comptIdsToDelete);
        } else {
            verify(dao, never()).deleteCompts(any());
        }

        if (packetId == null && !packetIdsToDeleteIsEmpty) {
            verify(dao, times(1)).deletePackets(test_packetIdsToDelete);
        } else {
            verify(dao, never()).deletePackets(any());
        }

        if (!comptsToUpdateParamsListIsEmpty) {
            if (!comptsToUpdateExceptionIsThrown) {
                verify(dao, times(1)).updateCompts(test_comptsToUpdateParamsList);
            } else {
                verify(dao, never()).updateCompts(test_comptsToUpdateParamsList);
                final String testComptsUpdateExceptionReport
                        = (String) ReflectionTestUtils.getField(service, EXCEPTION_MESSAGE);
                final String testComptsUpdateExceptionStackTrace
                        = (String) ReflectionTestUtils.getField(service, UPDATE_COMPTS_EXCEPTION_STACKTRACE);
                final String testComptsUpdateExceptionMessage
                        = (String) ReflectionTestUtils.getField(service, UPDATE_COMPTS_EXCEPTION_MESSAGE);
                final LoggingEvent comptsUpdateErrorLoggingEvent = testAppender.getLog().get(testLogSize - 1);
                assertEquals(Utils.getMessage(testComptsUpdateExceptionReport,
                        new Object[]{testComptsUpdateExceptionMessage, testComptsUpdateExceptionMessage}),
                        comptsUpdateErrorLoggingEvent.getMessage());
                assertEquals(Level.ERROR, comptsUpdateErrorLoggingEvent.getLevel());
            }
        }
    }

}
