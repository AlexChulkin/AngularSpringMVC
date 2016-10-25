package com.somecode.service;

import com.somecode.dao.PacketAppDao;
import com.somecode.domain.*;
import com.somecode.helper.Helper;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
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
public class PacketAppServiceTest {

    private static final int TEST_LIST_SIZE = 2;
    private static final String test_loggedData = "loggedData";

    private static final PacketAppDao dao = mock(PacketAppDao.class);
    private static final PacketAppService service = mock(PacketAppService.class);
    private static final Logger logger = mock(Logger.class);

    private static final String TEST_LOAD_DATA_FOR_GIVEN_PACKET
            = (String) ReflectionTestUtils.getField(service, "LOAD_DATA_FOR_GIVEN_PACKET");
    private static final String TEST_LOAD_DATA_FOR_ALL_PACKETS
            = (String) ReflectionTestUtils.getField(service, "LOAD_DATA_FOR_ALL_PACKETS");

    @Before
    public void before() {
        ReflectionTestUtils.setField(service, "packetAppDao", dao);
        ReflectionTestUtils.setField(service, "logger", logger);
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
        T entity = instantiateEntity(entityClass);

        List<T> result = new ArrayList<>();
        IntStream.rangeClosed(1, TEST_LIST_SIZE)
                .boxed()
                .forEach(i ->
                        {
                            entity.setId((long) i);
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
    public void testLoadData() throws DatabaseException {
        testLoadDataWithParams(null, false, false, false, false);
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

        when(Helper.getMessage(packetId == null ? TEST_LOAD_DATA_FOR_ALL_PACKETS :
                TEST_LOAD_DATA_FOR_GIVEN_PACKET, anyVararg())).thenReturn(test_loggedData);
        PowerMockito.mockStatic(Helper.class);

        Data result = service.loadData(packetId);

        verify(logger, times(1)).debug(test_loggedData);
        PowerMockito.verifyStatic(times(1));

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
}
