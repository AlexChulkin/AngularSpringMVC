package com.somecode.dao;

import com.google.common.collect.Lists;
import com.somecode.domain.*;
import com.somecode.utils.TestUtils;
import com.somecode.utils.Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@TestExecutionListeners({AbstractDbunitTransactionalJUnit4SpringContextTests.DbunitTestExecutionListener.class})
@ActiveProfiles("test")
public class PacketAppDaoTest extends AbstractDbunitTransactionalJUnit4SpringContextTests {
    private static final String DEFAULT_COMPT_LABEL_PREFIX = "compt_label_";
    private static final String DEFAULT_COMBO_LABEL_PREFIX = "combo_label_";
    private static final String DEFAULT_STATE_LABEL_PREFIX = "state_label_";
    private static final String ADMIN = "ADMIN";
    private static final String admin = "admin";
    private static final String USER = "USER";
    private static final String TEST_LOAD_COMPTS_FILENAME = "/com/somecode/dao/testLoadCompts.xls";
    private static final String TEST_LOAD_COMPTSSUPPLINFO_FILENAME =
            "/com/somecode/dao/testLoadComptsSupplInfo.xls";
    private static final String TEST_LOAD_STATES_FILENAME = "/com/somecode/dao/testLoadStates.xls";
    private static final String TEST_LOAD_COMBODATA_FILENAME = "/com/somecode/dao/testLoadComboData.xls";
    private static final String TEST_LOAD_PACKETS_FILENAME = "/com/somecode/dao/testLoadPackets.xls";
    private static final String TEST_UPDATE_COMPTS_BEFORE_FILENAME =
            "/com/somecode/dao/testUpdateCompts_before.xls";
    private static final String TEST_UPDATE_COMPTS_AFTER_FILENAME = "/com/somecode/dao/testUpdateCompts_after.xls";
    private static final String TEST_ADD_PACKETS_BEFORE_FILENAME = "/com/somecode/dao/testAddPackets_before.xls";
    private static final String TEST_ADD_PACKET_AFTER_POSITIVE_NULL_STATE_FILENAME
            = "/com/somecode/dao/testAddPacketWithNullState_after.xls";
    private static final String TEST_ADD_PACKET_AFTER_POSITIVE_NOT_EXISTENT_STATE_FILENAME
            = "/com/somecode/dao/testAddPacketWithNotExistentState_after.xls";
    private static final String TEST_ADD_PACKETS_AFTER_POSITIVE_FILENAME =
            "/com/somecode/dao/testAddPackets_after_positive.xls";
    private static final String TEST_ADD_PACKETS_AFTER_NEGATIVE_FILENAME =
            "/com/somecode/dao/testAddPackets_after_negative.xls";
    private static final String TEST_DELETE_PACKETS_BEFORE_FILENAME =
            "/com/somecode/dao/testDeletePackets_before.xls";
    private static final String TEST_DELETE_PACKETS_AFTER_POSITIVE_FILENAME =
            "/com/somecode/dao/testDeletePackets_after_positive.xls";
    private static final String TEST_DELETE_PACKETS_AFTER_NEGATIVE_FILENAME =
            "/com/somecode/dao/testDeletePackets_after_negative.xls";
    private static final String TEST_UPDATE_PACKETS_BEFORE_FILENAME =
            "/com/somecode/dao/testUpdatePackets_before.xls";
    private static final String TEST_UPDATE_PACKETS_AFTER_POSITIVE_FILENAME =
            "/com/somecode/dao/testUpdatePackets_after_positive.xls";
    private static final String TEST_UPDATE_PACKETS_AFTER_NEGATIVE_FILENAME =
            "/com/somecode/dao/testUpdatePackets_after_negative.xls";
    private static final String TEST_ADD_COMPTS_BEFORE_FILENAME =
            "/com/somecode/dao/testAddCompts_before.xls";
    private static final String TEST_ADD_COMPTS_AFTER_POSITIVE_FILENAME =
            "/com/somecode/dao/testAddCompts_after_positive.xls";
    private static final String TEST_ADD_COMPTS_AFTER_NEGATIVE_NONEXISTENTPACKETID_FILENAME =
            "/com/somecode/dao/testAddCompts_after_negative_nonExistentPacketId.xls";
    private static final String TEST_ADD_COMPTS_BEFORE_EQUAL_LABELS_FILENAME =
            "/com/somecode/dao/testAddCompts_before_equalLabels.xls";
    private static final String TEST_ADD_COMPTS_AFTER_NEGATIVE_EQUAL_LABELS_FILENAME =
            "/com/somecode/dao/testAddCompts_after_negative_equalLabels.xls";
    private static final String TEST_DELETE_COMPTS_BEFORE_FILENAME =
            "/com/somecode/dao/testDeleteCompts_before.xls";
    private static final String TEST_DELETE_COMPTS_AFTER_NEGATIVE_NONE_COMPT_IDS_EXIST_FILENAME =
            "/com/somecode/dao/testDeleteCompts_after_negative_noneOfTheComptIdsToDeleteExist.xls";
    private static final String TEST_DELETE_COMPTS_AFTER_NEGATIVE_SOME_COMPT_IDS_EXIST_FILENAME =
            "/com/somecode/dao/testDeleteCompts_after_negative_someOfTheComptIdsToDeleteExist.xls";
    private static final String TEST_DELETE_COMPTS_AFTER_POSITIVE_FILENAME =
            "/com/somecode/dao/testDeleteCompts_after_positive.xls";
    private static final String TEST_GET_USER_ROLE_FILENAME = "/com/somecode/dao/testGetUserRole.xls";
    private static final String ALL_COMPTS_FROM_GIVEN_PACKET_LOADED_MESSAGE
            = "ALL_COMPTS_FROM_GIVEN_PACKET_LOADED_MESSAGE";
    private static final String ALL_COMPTS_FROM_ALL_PACKETS_LOADED_MESSAGE
            = "ALL_COMPTS_FROM_ALL_PACKETS_LOADED_MESSAGE";
    private static final String ALL_COMPTSSUPPLINFO_LOADED = "ALL_COMPTSSUPPLINFO_LOADED";
    private static final String MAP_COMBODATA_LABELS_TO_INDICES_MESSAGE = "MAP_COMBODATA_LABELS_TO_INDICES_MESSAGE";
    private static final String MAP_COMBODATA_LABELS_TO_INDICES = "mapComboLabelsToIndices";
    private static final String ALL_COMBODATA = "allComboData";
    private static final String ALL_COMBODATA_LOADED_MESSAGE = "ALL_COMBODATA_LOADED_MESSAGE";
    private static final String COMBODATA_TABLE_IS_EMPTY = "COMBODATA_TABLE_IS_EMPTY";
    private static final String ALL_STATES = "allStates";
    private static final String ALL_STATES_LOADED_MESSAGE = "ALL_STATES_LOADED_MESSAGE";
    private static final String STATE_TABLE_IS_EMPTY = "STATE_TABLE_IS_EMPTY";
    private static final String EMPTY_STATE_TABLE_EXCEPTION_STACKTRACE = "emptyStateTableExceptionStackTrace";
    private static final String EMPTY_COMBODATA_TABLE_EXCEPTION_STACKTRACE = "emptyCombodataTableExceptionStackTrace";
    private static final String EMPTY_STATE_TABLE_EXCEPTION_MESSAGE = "emptyStateTableExceptionMessage";
    private static final String EMPTY_COMBODATA_TABLE_EXCEPTION_MESSAGE = "emptyCombodataTableExceptionMessage";
    private static final String ALL_PACKETS_LOADED_MESSAGE = "ALL_PACKETS_LOADED_MESSAGE";
    private static final String PACKET_NOT_LOADED = "PACKET_NOT_LOADED";
    private static final String NO_PACKETS_LOADED = "NO_PACKETS_LOADED";
    private static final String COMPT_UPDATE_DATACOMPT_UPDATE = "COMPT_UPDATE_DATACOMPT_UPDATE";
    private static final String COMPT_UPDATE_SUCCESS_REPORT = "COMPT_UPDATE_SUCCESS_REPORT";
    private static final String COMPT_UPDATE_NON_EXISTING_COMPT = "COMPT_UPDATE_NON_EXISTING_COMPT";
    private static final String PACKET_ADDING_ADD_COMPTS = "PACKET_ADDING_ADD_COMPTS";
    private static final String PACKET_ADDING_STATE_SET_SUCCESS_REPORT = "PACKET_ADDING_STATE_SET_SUCCESS_REPORT";
    private static final String PACKET_UPDATE_STATE_UPDATE_SUCCESS_REPORT = "PACKET_UPDATE_STATE_UPDATE_SUCCESS_REPORT";
    private static final String PACKET_ADDING_SUCCESS_REPORT = "PACKET_ADDING_SUCCESS_REPORT";
    private static final String PACKET_UPDATE_SUCCESS_REPORT = "PACKET_UPDATE_SUCCESS_REPORT";
    private static final String PACKETS_DELETE_SUCCESS_REPORT = "PACKETS_DELETE_SUCCESS_REPORT";
    private static final String PACKETS_DELETE_NON_EXISTING_IDS = "PACKETS_DELETE_NON_EXISTING_IDS";
    private static final String PACKET_UPDATE_ADD_COMPTS = "PACKET_UPDATE_ADD_COMPTS";
    private static final String PACKET_UPDATE_NOT_EXISTING_PACKET = "PACKET_UPDATE_NOT_EXISTING_PACKET";
    private static final String PACKET_UPDATE_STATE_UPDATE_NOT_DIFFERENT_NEW_STATE
            = "PACKET_UPDATE_STATE_UPDATE_NOT_DIFFERENT_NEW_STATE";
    private static final String PACKET_ADDING_STATE_SET_NOT_DIFFERENT_NEW_STATE
            = "PACKET_ADDING_STATE_SET_NOT_DIFFERENT_NEW_STATE";
    private static final String PACKET_UPDATE_STATE_UPDATE_NOT_EXISTING_STATE
            = "PACKET_UPDATE_STATE_UPDATE_NOT_EXISTING_STATE";
    private static final String PACKET_ADDING_STATE_SET_NOT_EXISTING_STATE
            = "PACKET_ADDING_STATE_SET_NOT_EXISTING_STATE";
    private static final String PACKET_ADDING_STATE_SET_NULL_STATE = "PACKET_ADDING_STATE_SET_NULL_STATE";
    private static final String COMPTS_DELETE_SUCCESS_REPORT = "COMPTS_DELETE_SUCCESS_REPORT";
    private static final String COMPTS_DELETE_NON_EXISTING_COMPTS = "COMPTS_DELETE_NON_EXISTING_COMPTS";
    private static final String USER_DATA_LOAD_ERROR = "USER_DATA_LOAD_ERROR";
    private static final String USER_DATA_LOAD_SUCCESS = "USER_DATA_LOAD_SUCCESS";

    @Autowired
    PacketAppDao packetAppDao;

    @PersistenceContext
    private EntityManager em;

    private TestUtils.TestAppender testAppender;

    @Before
    public void doBeforeEachTest() {
        testAppender = TestUtils.getTestAppender();
        Logger root = Logger.getRootLogger();
        root.addAppender(testAppender);
        root.setLevel(Level.INFO);
    }

    @DataSets(before = TEST_LOAD_COMPTS_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadCompts_onePacket_positive() throws Exception {
        final Long packetId = 1L;
        final String labelPrefix = DEFAULT_COMPT_LABEL_PREFIX;
        final int expectedResultLength = 2;
        final List<String> expectedComptsLabelsList = generateDiverseLabelsList(labelPrefix, expectedResultLength);
        final String testLoadDataForGivenPacket
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMPTS_FROM_GIVEN_PACKET_LOADED_MESSAGE);

        final List<ComptInfo> result = packetAppDao.loadCompts(packetId);

        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals(expectedComptsLabelsList.get(i), result.get(i).getLabel());
                            assertEquals(Long.valueOf(i + 1), result.get(i).getId());
                        }
                );

        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadDataForGivenPacket, new Object[]{packetId, result}),
                loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());

    }

    @DataSets(before = TEST_LOAD_COMPTS_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadCompts_onePacket_negative() throws Exception {
        final Long packetId = 3L;
        final int expectedResultSize = 0;
        final String testLoadDataForGivenPacket
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMPTS_FROM_GIVEN_PACKET_LOADED_MESSAGE);

        final List<ComptInfo> result = packetAppDao.loadCompts(packetId);

        assertEquals(expectedResultSize, result.size());
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadDataForGivenPacket, new Object[]{packetId, result}),
                loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_LOAD_COMPTS_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadCompts_allPackets_positive() throws Exception {
        final Long packetId = null;
        final String labelPrefix = DEFAULT_COMPT_LABEL_PREFIX;
        final int expectedResultLength = 4;
        final List<String> expectedComptsLabelsList = generateDiverseLabelsList(labelPrefix, expectedResultLength);
        final String testLoadDataForAllPackets
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMPTS_FROM_ALL_PACKETS_LOADED_MESSAGE);

        final List<ComptInfo> result = packetAppDao.loadCompts(packetId);

        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals(expectedComptsLabelsList.get(i), result.get(i).getLabel());
                            assertEquals(Long.valueOf(i + 1), result.get(i).getId());
                        }
                );
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadDataForAllPackets, new Object[]{result}),
                loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    @DirtiesContext
    public void testLoadCompts_allPackets_negative() throws Exception {
        final Long packetId = null;
        final int expectedResultLength = 0;
        final String testLoadDataForAllPackets
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMPTS_FROM_ALL_PACKETS_LOADED_MESSAGE);

        final List<ComptInfo> result = packetAppDao.loadCompts(packetId);

        assertEquals(expectedResultLength, result.size());
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadDataForAllPackets, new Object[]{result}),
                loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_LOAD_COMPTSSUPPLINFO_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadComptsSupplInfo_positive() throws Exception {
        final long packetId = 1L;

        final Integer expectedNumOfStates = 3;
        final Integer expectedNumOfComboData = 3;
        final Integer expectedNumOfCompts = 1;
        final Long expectedFirstNewComptId = 1L;
        final String expectedComboDatalLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final int expectedResultLength = calculateExpectedNumOfDataCompts(expectedNumOfCompts, expectedNumOfStates,
                expectedNumOfComboData);

        loadAndCheckComptsSupplInfo(expectedFirstNewComptId, packetId, expectedComboDatalLabelPrefix, expectedResultLength,
                expectedNumOfCompts, expectedNumOfStates, expectedNumOfComboData);
    }

    @DataSets(before = TEST_LOAD_COMPTSSUPPLINFO_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadComptsSupplInfo_negative() throws Exception {
        final long packetId = 2L;
        final int expectedResultSize = 0;
        final String testLoadDataForAllSupplInfo
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMPTSSUPPLINFO_LOADED);

        final List<ComptSupplInfo> result = packetAppDao.loadComptsSupplInfo(packetId);

        assertEquals(expectedResultSize, result.size());
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadDataForAllSupplInfo, new Object[]{result}),
                loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_LOAD_COMBODATA_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadComboData_positive() throws Exception {
        final int expectedNumOfComboData = 3;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;

        loadAndCheckComboData(expectedNumOfComboData, comboDataLabelPrefix);
    }

    @Test(expected = DatabaseException.class)
    @DirtiesContext
    public void testLoadComboData_negative() throws Exception {
        try {
            packetAppDao.loadAllComboData();
        } catch (DatabaseException exc) {
            throw testEmptyTableCase(exc);
        }
    }

    @DataSets(before = TEST_LOAD_STATES_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadAllStates_positive() throws Exception {
        final String labelsPrefix = DEFAULT_STATE_LABEL_PREFIX;
        final int expectedResultLength = 3;
        final List<String> expectedLabels = generateDiverseLabelsList(labelsPrefix, expectedResultLength);
        final String testAllStatesLoaded
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_STATES_LOADED_MESSAGE);

        final List<State> result = packetAppDao.loadAllStates();

        final List<String> testAllStates = (List<String>) ReflectionTestUtils.getField(packetAppDao, ALL_STATES);
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent statesLoggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testAllStatesLoaded, new Object[]{testAllStates}),
                statesLoggingEvent.getMessage());
        assertEquals(Level.INFO, statesLoggingEvent.getLevel());
        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i -> assertEquals(expectedLabels.get(i), result.get(i).getLabel()));
    }

    @Test(expected = DatabaseException.class)
    @DirtiesContext
    public void testLoadAllStates_negative() throws Exception {
        try {
            packetAppDao.loadAllStates();
        } catch (DatabaseException exc) {
            throw testEmptyTableCase(exc);
        }
    }

    @DataSets(before = TEST_LOAD_PACKETS_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadPackets_onePacket_positive() throws Exception {
        final Long packetId = 1L;
        final int expectedResultLength = 1;
        final String testAllPacketsLoaded = (String) ReflectionTestUtils.getField(packetAppDao, ALL_PACKETS_LOADED_MESSAGE);

        final List<PacketInfo> result = packetAppDao.loadPackets(packetId);

        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals((long) (i + 1), result.get(i).getStateId().longValue());
                            assertEquals((long) (i + 1), result.get(i).getId().longValue());
                        }
                );
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(
                Utils.getMessage(testAllPacketsLoaded,
                        new Object[]{result.stream().map(PacketInfo::getId).collect(Collectors.toList())}),
                loggingEvent.getMessage()
        );
        assertEquals(Level.INFO, loggingEvent.getLevel());

    }

    @DataSets(before = TEST_LOAD_PACKETS_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadPackets_onePacket_negative() throws Exception {
        final Long packetId = 3L;
        final int expectedResultSize = 0;
        final String testPacketNotFound = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_NOT_LOADED);

        final List<PacketInfo> result = packetAppDao.loadPackets(packetId);

        assertEquals(expectedResultSize, result.size());
        int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testPacketNotFound, new Object[]{packetId}),
                loggingEvent.getMessage()
        );
    }

    @DataSets(before = TEST_LOAD_PACKETS_FILENAME)
    @Test
    @DirtiesContext
    public void testLoadPackets_allPackets_positive() throws Exception {
        final Long packetId = null;
        final int expectedResultLength = 2;
        final String testAllPacketsLoadedMessage
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_PACKETS_LOADED_MESSAGE);

        final List<PacketInfo> result = packetAppDao.loadPackets(packetId);

        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals((long) (i + 1), result.get(i).getStateId().longValue());
                            assertEquals((long) (i + 1), result.get(i).getId().longValue());
                        }
                );
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(
                Utils.getMessage(testAllPacketsLoadedMessage,
                        new Object[]{result.stream().map(PacketInfo::getId).collect(Collectors.toList())}),
                loggingEvent.getMessage()
        );
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    @DirtiesContext
    public void testLoadPackets_allPackets_negative() throws Exception {
        final Long packetId = null;
        final int expectedResultLength = 0;
        final String testNoPacketsFound = (String) ReflectionTestUtils.getField(packetAppDao, NO_PACKETS_LOADED);

        final List<PacketInfo> result = packetAppDao.loadPackets(packetId);

        assertEquals(expectedResultLength, result.size());
        int testLogSize = testAppender.getLog().size();
        LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(
                Utils.getMessage(testNoPacketsFound, null),
                loggingEvent.getMessage()
        );
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_UPDATE_COMPTS_BEFORE_FILENAME, after = TEST_UPDATE_COMPTS_AFTER_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdateCompts_positive() throws Exception {
        final int numOfComptsToUpdate = 2;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final int numOfStates = 3;
        final int firstIdToUpdate = 1;
        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);
        final long[] idsForUpdate = generateIds(firstIdToUpdate, numOfComptsToUpdate);
        final List<ComptParams> paramsListForUpdate = generateComptParamsList(OperationType.UPDATE,
                checkedValsForUpdate, idsForUpdate, numOfComptsToUpdate, null);
        final String testComptUpdateDataComptUpdate
                = (String) ReflectionTestUtils.getField(packetAppDao, COMPT_UPDATE_DATACOMPT_UPDATE);
        final String testComptUpdateSuccessReport
                = (String) ReflectionTestUtils.getField(packetAppDao, COMPT_UPDATE_SUCCESS_REPORT);

        packetAppDao.updateCompts(paramsListForUpdate);
        em.flush();

        final int[] comptIds = {1, 1, 1, 1, 2, 2, 2, 2};
        final int[] dataComptIds = {4, 5, 7, 9, 10, 12, 13, 14};
        final int testLogSize = testAppender.getLog().size();
        final int arrayLength = comptIds.length;
        IntStream.range(0, arrayLength).boxed().forEach(i -> {
            assertEquals(
                    Utils.getMessage(testComptUpdateDataComptUpdate, new Object[]{comptIds[i], dataComptIds[i]}),
                    testAppender.getLog().get(testLogSize - 2 - (arrayLength - i - 1)).getMessage()
            );
            assertEquals(Level.INFO, testAppender.getLog().get(testLogSize - 2 - (arrayLength - i - 1)).getLevel());
        });
        Long packetId = 1L;
        List<Long> comptIdsForPacketIds = Arrays.asList(1L, 2L);
        LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(
                Utils.getMessage(testComptUpdateSuccessReport, new Object[]{packetId, comptIdsForPacketIds}),
                loggingEvent.getMessage()
        );
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_UPDATE_COMPTS_BEFORE_FILENAME, after = TEST_UPDATE_COMPTS_BEFORE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdateCompts_negative() throws Exception {
        final int numOfComptsToUpdate = 1;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final int numOfStates = 3;
        final int firstComptIdToUpdate = 3;

        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);

        final long[] idsForUpdate = generateIds(firstComptIdToUpdate, numOfComptsToUpdate);

        final List<ComptParams> paramsListForUpdate = generateComptParamsList(OperationType.UPDATE, checkedValsForUpdate,
                idsForUpdate, numOfComptsToUpdate, null);

        packetAppDao.updateCompts(paramsListForUpdate);
        em.flush();

        final String testComptUpdateNonExistingCompt
                = (String) ReflectionTestUtils.getField(packetAppDao, COMPT_UPDATE_NON_EXISTING_COMPT);

        int testLogSize = testAppender.getLog().size();
        LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(
                Utils.getMessage(testComptUpdateNonExistingCompt, new Object[]{firstComptIdToUpdate}),
                loggingEvent.getMessage()
        );
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_ADD_PACKETS_BEFORE_FILENAME, after = TEST_ADD_PACKETS_AFTER_POSITIVE_FILENAME)
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testAddPackets_positive() throws Exception {
        final int numOfComptsToAddInsideAddedPacket = 1;
        final Long expectedFirstNewComptId = 2L;
        final Long expectedPersistedNewOrUpdatedPacketId = 2L;
        final Long expectedNewOrUpdatedPacketIdBeforeBeingPersisted = null;
        final Long packetStateId = 2L;
        final Long secondPacketId = null;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final String comptLabelPrefix = DEFAULT_COMPT_LABEL_PREFIX;
        final int numOfStates = 3;
        final int numOfComboDatas = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAddInsideAddedPacket);
        final String[] labelsForAdding
                = generateDiverseLabelsList(comptLabelPrefix, numOfComptsToAddInsideAddedPacket).toArray(new String[0]);
        final List<ComptParams> comptParamsList = generateComptParamsList(OperationType.ADD,
                checkedValsForAdding, null, numOfComptsToAddInsideAddedPacket, labelsForAdding);
        final List<PacketParams> packetParamsList
                = generatePacketParamsList(expectedPersistedNewOrUpdatedPacketId, packetStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.ADD);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NONE, OperationType.ADD,
                expectedNewOrUpdatedPacketIdBeforeBeingPersisted, packetStateId, comptLabelPrefix,
                comboDataLabelPrefix, numOfComptsToAddInsideAddedPacket, numOfStates, numOfComboDatas,
                expectedFirstNewComptId, expectedPersistedNewOrUpdatedPacketId, secondPacketId);
    }

    @DataSets(before = TEST_ADD_PACKETS_BEFORE_FILENAME, after = TEST_ADD_PACKETS_AFTER_NEGATIVE_FILENAME)
    @Test(expected = DataIntegrityViolationException.class)
    @Rollback(false)
    @DirtiesContext
    public void testAddPackets_negative_comptsWithEqualLabels() throws Exception {
        final int numOfComptsToAddInsideAddedPacket = 2;
        final Long unusedNewPacketId = 2L;
        final Long expectedNewOrUpdatedPacketIdBeforeBeingPersisted = null;
        final Long expectedFirstNewComptId = null;
        final Long packetStateId = 2L;
        final Long secondPacketId = null;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final String comptLabelPrefix = DEFAULT_COMPT_LABEL_PREFIX;
        final int numOfStates = 3;
        final int numOfComboDatas = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAddInsideAddedPacket);
        final String[] labelsForAdding
                = generateEqualLabelsList(comptLabelPrefix, numOfComptsToAddInsideAddedPacket).toArray(new String[0]);
        final List<ComptParams> comptParamsList = generateComptParamsList(OperationType.ADD,
                checkedValsForAdding, null,
                numOfComptsToAddInsideAddedPacket, labelsForAdding);
        final List<PacketParams> packetParamsList
                = generatePacketParamsList(unusedNewPacketId, packetStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.ADD);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NONE, OperationType.ADD,
                expectedNewOrUpdatedPacketIdBeforeBeingPersisted, packetStateId, comptLabelPrefix, comboDataLabelPrefix,
                numOfComptsToAddInsideAddedPacket, numOfStates, numOfComboDatas, expectedFirstNewComptId,
                unusedNewPacketId, secondPacketId);
    }

    @DataSets(before = TEST_ADD_PACKETS_BEFORE_FILENAME,
            after = TEST_ADD_PACKET_AFTER_POSITIVE_NOT_EXISTENT_STATE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testAddPacketsSetPacketState_positive_NullStateId() throws Exception {
        final long packetId = 1L;
        final long expectedNewPacketId = 2L;
        final Long newStateId = null;
        final Long secondPacketId = null;
        final String comptLabelPrefix = null;
        final String comboDataLabelPrefix = null;
        final int numOfCompts = 0;
        final int numOfStates = 0;
        final int numOfComboDatas = 0;
        final long comptId = 0L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.ADD);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NULL_NEW_STATE_ID, OperationType.ADD, null, newStateId,
                comptLabelPrefix, comboDataLabelPrefix, numOfCompts, numOfStates, numOfComboDatas, comptId,
                expectedNewPacketId, secondPacketId);
    }

    @DataSets(before = TEST_ADD_PACKETS_BEFORE_FILENAME, after = TEST_ADD_PACKET_AFTER_POSITIVE_NULL_STATE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testAddPacketsSetPacketState_positive_nonExistingStateId() throws Exception {
        final Long expectedNewPacketIdBeforeBeingPersisted = null;
        final Long expectedPersistedNewPacketId = 2L;
        final Long newStateId = 4L;
        final Long secondPacketId = null;
        final String comptLabelPrefix = null;
        final String comboDataLabelPrefix = null;
        final int numOfCompts = 0;
        final int numOfComboDatas = 3;
        final int numOfStates = 0;
        final long comptId = 0L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList
                = generatePacketParamsList(expectedNewPacketIdBeforeBeingPersisted, newStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.ADD);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NOT_EXISTING_STATE_ID, OperationType.ADD,
                expectedNewPacketIdBeforeBeingPersisted, newStateId, comptLabelPrefix, comboDataLabelPrefix,
                numOfCompts, numOfStates, numOfComboDatas, comptId, expectedPersistedNewPacketId, secondPacketId);
    }

    @DataSets(before = TEST_DELETE_PACKETS_BEFORE_FILENAME, after = TEST_DELETE_PACKETS_AFTER_POSITIVE_FILENAME)
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testDeletePackets_positive() throws Exception {
        final long packetId = 2L;
        final List<Long> idsToDelete = Arrays.asList(packetId);
        final String testPacketsDeleteSuccessReport
                = (String) ReflectionTestUtils.getField(packetAppDao, PACKETS_DELETE_SUCCESS_REPORT);
        final int expectedLogSize = 1;

        List<Long> result = packetAppDao.deletePackets(idsToDelete);
        em.flush();

        assertEquals(idsToDelete, result);
        int testLogSize = testAppender.getLog().size();
        assertEquals(expectedLogSize, testLogSize);
        LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(
                Utils.getMessage(testPacketsDeleteSuccessReport, new Object[]{idsToDelete}),
                loggingEvent.getMessage()
        );
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_DELETE_PACKETS_BEFORE_FILENAME, after = TEST_DELETE_PACKETS_AFTER_NEGATIVE_FILENAME)
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testDeletePackets_negative() throws Exception {
        final long packetId = 3L;
        final List<Long> idsToDelete = Arrays.asList(packetId);
        final int expectedResultSize = 0;
        final int expectedLogSize = 1;
        final String testPacketsDeleteNonExistingIds
                = (String) ReflectionTestUtils.getField(packetAppDao, PACKETS_DELETE_NON_EXISTING_IDS);

        List<Long> result = packetAppDao.deletePackets(idsToDelete);
        em.flush();

        assertEquals(expectedResultSize, result.size());
        int testLogSize = testAppender.getLog().size();
        assertEquals(expectedLogSize, testLogSize);
        LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(
                Utils.getMessage(testPacketsDeleteNonExistingIds, new Object[]{idsToDelete}),
                loggingEvent.getMessage()
        );
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @DataSets(before = TEST_UPDATE_PACKETS_BEFORE_FILENAME, after = TEST_UPDATE_PACKETS_AFTER_POSITIVE_FILENAME)
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testUpdatePacketState_positive() throws Exception {
        final Long packetId = 1L;
        final Long newStateId = 2L;
        final Long secondPacketId = null;
        final Long expectedNewOrUpdatedPacketIdBeforeBeingPersisted = 1L;
        final Long expectedPersistedUpdatedPacketId = 1L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);
        final int numOfComptsToAddInsideAddedPacket = 0;
        final int numOfStates = 3;
        final int numOfComboDatas = 3;
        final String comboDataLabelPrefix = null;
        final String comptLabelPrefix = null;

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NONE, OperationType.UPDATE,
                expectedNewOrUpdatedPacketIdBeforeBeingPersisted, newStateId, comptLabelPrefix, comboDataLabelPrefix,
                numOfComptsToAddInsideAddedPacket, numOfStates, numOfComboDatas, null,
                expectedPersistedUpdatedPacketId, secondPacketId);
    }

    @DataSets(before = TEST_UPDATE_PACKETS_BEFORE_FILENAME, after = TEST_UPDATE_PACKETS_AFTER_NEGATIVE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdatePacketState_negative_nonExistingPacketId() throws Exception {
        final Long packetId = 2L;
        final Long newStateId = 1L;
        final Long secondPacketId = null;
        final String comptLabelPrefix = null;
        final String comboDataLabelPrefix = null;
        final int numOfCompts = 0;
        final int numOfStates = 0;
        final int numOfComboDatas = 0;
        final long comptId = 0L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NOT_EXISTING_THE_ONLY_PACKET_ID, OperationType.UPDATE, packetId,
                newStateId, comptLabelPrefix, comboDataLabelPrefix, numOfCompts, numOfStates, numOfComboDatas, comptId,
                packetId, secondPacketId);
    }

    @DataSets(before = TEST_UPDATE_PACKETS_BEFORE_FILENAME, after = TEST_UPDATE_PACKETS_AFTER_NEGATIVE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdatePacketState_negative_notDifferentNewStateId() throws Exception {
        final long packetId = 1L;
        final long newStateId = 1L;
        final Long secondPacketId = null;
        final String comptLabelPrefix = null;
        final String comboDataLabelPrefix = null;
        final int numOfCompts = 0;
        final int numOfStates = 0;
        final int numOfComboDatas = 0;
        final long comptId = 0L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NOT_DIFFERENT_NEW_STATE_ID, OperationType.UPDATE, packetId,
                newStateId, comptLabelPrefix, comboDataLabelPrefix, numOfCompts, numOfStates, numOfComboDatas, comptId,
                packetId, secondPacketId);
    }

    @DataSets(before = TEST_UPDATE_PACKETS_BEFORE_FILENAME, after = TEST_UPDATE_PACKETS_AFTER_NEGATIVE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdatePacketState_negative_nonExistingStateId() throws Exception {
        final Long packetId = 1L;
        final Long newStateId = 4L;
        final Long secondPacketId = null;
        final String comptLabelPrefix = null;
        final String comboDataLabelPrefix = null;
        final int numOfCompts = 0;
        final int numOfStates = 0;
        final int numOfComboDatas = 0;
        final long comptId = 0L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NOT_EXISTING_STATE_ID, OperationType.UPDATE, packetId,
                newStateId, comptLabelPrefix, comboDataLabelPrefix, numOfCompts, numOfStates, numOfComboDatas,
                comptId, packetId, secondPacketId);

    }

    @DataSets(before = TEST_UPDATE_PACKETS_BEFORE_FILENAME, after = TEST_UPDATE_PACKETS_AFTER_NEGATIVE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdatePacketState_negative_NullStateId() throws Exception {
        final long packetId = 1L;
        final Long newStateId = null;
        final Long secondPacketId = null;
        final String comptLabelPrefix = null;
        final String comboDataLabelPrefix = null;
        final int numOfCompts = 0;
        final int numOfStates = 0;
        final int numOfComboDatas = 0;
        final long comptId = 0L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NULL_NEW_STATE_ID, OperationType.UPDATE, packetId, newStateId,
                comptLabelPrefix, comboDataLabelPrefix, numOfCompts, numOfStates, numOfComboDatas, comptId, packetId,
                secondPacketId);
    }

    @DataSets(before = TEST_ADD_COMPTS_BEFORE_FILENAME, after = TEST_ADD_COMPTS_AFTER_POSITIVE_FILENAME)
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testUpdatePacketAddCompts_positive() throws Exception {
        final int numOfComptsToAdd = 2;
        final Long firstNewComptId = 2L;
        final Long packetId = 1L;
        final Long stateId = null;
        final Long secondPacketId = null;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final String comptLabelPrefix = DEFAULT_COMPT_LABEL_PREFIX;
        final int numOfStates = 3;
        final int numOfComboDatas = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAdd);
        final String[] labelsForAdding
                = generateDiverseLabelsList(comptLabelPrefix, numOfComptsToAdd).toArray(new String[0]);
        final List<ComptParams> paramsListForAdding = generateComptParamsList(OperationType.ADD,
                checkedValsForAdding, null, numOfComptsToAdd, labelsForAdding);
        final PacketParams packetParams = new PacketParams()
                .setId(packetId)
                .setStateId(stateId)
                .setNewComptParamsList(paramsListForAdding);
        List<PacketParams> packetParamsList = Arrays.asList(packetParams);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NONE, OperationType.UPDATE, packetId, stateId,
                comptLabelPrefix, comboDataLabelPrefix, numOfComptsToAdd, numOfStates, numOfComboDatas, firstNewComptId,
                packetId, secondPacketId);
    }

    @DataSets(before = TEST_ADD_COMPTS_BEFORE_FILENAME,
            after = TEST_ADD_COMPTS_AFTER_NEGATIVE_NONEXISTENTPACKETID_FILENAME)
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testUpdatePacketAddCompts_negative_nonExistentPacketId() throws Exception {
        final int numOfComptsToAddInsideUnproperlyUpdatedPacket = 2;
        final int numOfComptsToAddInsideProperlyUpdatedPacket = 0;
        final Long newPacketId = 2L;
        final Long updatedPacketId = 1L;
        final Long newPacketStateId = 1L;
        final Long updatedPacketNewStateId = 2L;
        final Long expectedFirstNewComptId = 2L;
        final int numOfComboDatas = 3;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final String comptLabelPrefix = DEFAULT_COMPT_LABEL_PREFIX;
        final int numOfStates = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix,
                numOfStates,
                numOfComptsToAddInsideUnproperlyUpdatedPacket);
        final String[] labelsForAdding
                = generateDiverseLabelsList(comptLabelPrefix,
                numOfComptsToAddInsideUnproperlyUpdatedPacket).toArray(new String[0]);
        final List<ComptParams> paramsListForAdding = new ArrayList<>();
        IntStream.range(0, numOfComptsToAddInsideUnproperlyUpdatedPacket).boxed()
                .forEach(i -> paramsListForAdding.add(new ComptParams()
                        .setVals(Arrays.asList(checkedValsForAdding[i]))
                        .setLabel(labelsForAdding[i]))
                );
        final PacketParams addPacketParams = new PacketParams()
                .setId(newPacketId)
                .setStateId(newPacketStateId)
                .setNewComptParamsList(paramsListForAdding);

        final PacketParams updatePacketParams = new PacketParams()
                .setId(updatedPacketId)
                .setStateId(updatedPacketNewStateId)
                .setNewComptParamsList(Collections.emptyList());

        List<PacketParams> packetParamsList = Arrays.asList(addPacketParams, updatePacketParams);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NOT_EXISTING_ONE_OF_PACKET_IDS, OperationType.UPDATE,
                updatedPacketId, updatedPacketNewStateId, comptLabelPrefix, comboDataLabelPrefix,
                numOfComptsToAddInsideProperlyUpdatedPacket, numOfStates, numOfComboDatas, expectedFirstNewComptId,
                updatedPacketId, newPacketId);
    }

    @DataSets(before = TEST_ADD_COMPTS_BEFORE_EQUAL_LABELS_FILENAME,
            after = TEST_ADD_COMPTS_AFTER_NEGATIVE_EQUAL_LABELS_FILENAME)
    @Test(expected = DataIntegrityViolationException.class)
    @Rollback(false)
    @DirtiesContext
    public void testUpdatePacketAddCompts_negative_comptsWithEqualLabels() throws Exception {
        final int numOfComptsToAdd = 2;
        final Long packetId = 1L;
        final Long stateId = null;
        final Long unusedNewPacketId = null;
        final Long secondPacketId = null;
        final Long expectedFirstNewComptId = 2L;
        final String comboDataLabelPrefix = DEFAULT_COMBO_LABEL_PREFIX;
        final String comptLabelPrefix = DEFAULT_COMPT_LABEL_PREFIX;
        final int numOfStates = 3;
        final int numOfComboDatas = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAdd);
        final String[] labelsForAdding
                = generateDiverseLabelsList(comptLabelPrefix, numOfComptsToAdd).toArray(new String[0]);

        final List<ComptParams> paramsListForAdding = new ArrayList<>();
        IntStream.range(0, numOfComptsToAdd).boxed()
                .forEach(i -> paramsListForAdding.add(new ComptParams()
                        .setVals(Arrays.asList(checkedValsForAdding[i]))
                        .setLabel(labelsForAdding[i]))
                );
        final PacketParams packetParams = new PacketParams()
                .setId(packetId)
                .setStateId(stateId)
                .setNewComptParamsList(paramsListForAdding);
        List<PacketParams> packetParamsList = Arrays.asList(packetParams);

        packetAppDao.addOrUpdatePackets(packetParamsList, OperationType.UPDATE);
        em.flush();

        testAddOrUpdatePackets(PacketDaoError.NONE, OperationType.ADD,
                packetId, stateId, comptLabelPrefix, comboDataLabelPrefix,
                numOfComptsToAdd, numOfStates, numOfComboDatas, expectedFirstNewComptId,
                unusedNewPacketId, secondPacketId);
    }

    @DataSets(before = TEST_DELETE_COMPTS_BEFORE_FILENAME, after = TEST_DELETE_COMPTS_AFTER_POSITIVE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testDeleteCompts_positive() throws Exception {
        final int firstComptId = 1;
        final int numOfComptIds = 2;
        final Set<Long> fakeIdsSet = Collections.emptySet();
        final List<Long> comptIdsToDelete
                = Arrays.asList(ArrayUtils.toObject(generateIds(firstComptId, numOfComptIds)));

        packetAppDao.deleteCompts(comptIdsToDelete);
        em.flush();

        testDeleteCompts(PacketDaoError.NONE, comptIdsToDelete, fakeIdsSet);
    }

    @DataSets(before = TEST_DELETE_COMPTS_BEFORE_FILENAME,
            after = TEST_DELETE_COMPTS_AFTER_NEGATIVE_NONE_COMPT_IDS_EXIST_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testDeleteCompts_negative_noneOfTheExistingComptIds() throws Exception {
        final int firstIdToRemove = 3;
        final int numOfComptsToRemove = 1;
        final Set<Long> fakeIdsSet = Collections.emptySet();
        final List<Long> comptIdsToDelete
                = Arrays.asList(ArrayUtils.toObject(generateIds(firstIdToRemove, numOfComptsToRemove)));

        packetAppDao.deleteCompts(comptIdsToDelete);
        em.flush();

        testDeleteCompts(PacketDaoError.NONE_OF_THE_COMPT_IDS_EXIST, comptIdsToDelete, fakeIdsSet);

    }

    @DataSets(before = TEST_DELETE_COMPTS_BEFORE_FILENAME,
            after = TEST_DELETE_COMPTS_AFTER_NEGATIVE_SOME_COMPT_IDS_EXIST_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testDeleteCompts_negative_someComptIdDoNotExist() throws Exception {
        final int firstIdToRemove = 2;
        final int numOfComptsToRemove = 2;
        final Set<Long> fakeIdsSet = new HashSet<>();
        fakeIdsSet.add(3L);
        final List<Long> comptIdsToDelete
                = Arrays.asList(ArrayUtils.toObject(generateIds(firstIdToRemove, numOfComptsToRemove)));

        packetAppDao.deleteCompts(comptIdsToDelete);
        em.flush();

        testDeleteCompts(PacketDaoError.SOME_OF_THE_COMPT_IDS_EXIST, comptIdsToDelete, fakeIdsSet);
    }

    @DataSets(before = TEST_GET_USER_ROLE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testGetUserRole_positive() throws Exception {
        final String username = ADMIN;
        final String password = ADMIN;
        final Role expectedRole = Role.ADMIN;

        assertEquals(expectedRole, packetAppDao.getUserRole(username, password));

        testGetUserRole(PacketDaoError.NONE, username);
    }

    @DataSets(before = TEST_GET_USER_ROLE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testGetUserRole_negative_invalidUsername() throws Exception {
        final String username = USER;
        final String password = ADMIN;

        assertNull(packetAppDao.getUserRole(username, password));

        testGetUserRole(PacketDaoError.AUTHENTICATION_ERROR, username);
    }

    @DataSets(before = TEST_GET_USER_ROLE_FILENAME)
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testGetUserRole_negative_invalidPassword() throws Exception {
        final String username = ADMIN;
        final String password = admin;

        assertNull(packetAppDao.getUserRole(username, password));

        testGetUserRole(PacketDaoError.AUTHENTICATION_ERROR, username);
    }

    private void loadAndCheckComboData(final Integer expectedResultLength, final String expectedComboDataLabelPrefix)
            throws Exception {
        final List<String> expectedComboDataLabels
                = generateDiverseLabelsList(expectedComboDataLabelPrefix, expectedResultLength);
        final String testMapCombodataLabelsToIndicesMessage
                = (String) ReflectionTestUtils.getField(packetAppDao, MAP_COMBODATA_LABELS_TO_INDICES_MESSAGE);
        final String testAllComboDataLoadedMessage
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMBODATA_LOADED_MESSAGE);

        final List<ComboData> result = packetAppDao.loadAllComboData();

        assertEquals((long) expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals(expectedComboDataLabels.get(i), result.get(i).getLabel());
                            assertEquals((long) (i + 1), result.get(i).getId().longValue());
                        }
                );
        final int testLogSize = testAppender.getLog().size();
        final Map<String, Integer> testMapCombodataLabelsToIndices
                = (Map<String, Integer>) ReflectionTestUtils.getField(packetAppDao, MAP_COMBODATA_LABELS_TO_INDICES);
        final List<String> testAllComboData
                = (List<String>) ReflectionTestUtils.getField(packetAppDao, ALL_COMBODATA);
        final LoggingEvent mapComboDataLoggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testMapCombodataLabelsToIndicesMessage,
                new Object[]{testMapCombodataLabelsToIndices}), mapComboDataLoggingEvent.getMessage());
        assertEquals(Level.INFO, mapComboDataLoggingEvent.getLevel());
        final LoggingEvent comboDataLoadingEvent = testAppender.getLog().get(testLogSize - 2);
        assertEquals(Utils.getMessage(testAllComboDataLoadedMessage,
                new Object[]{testAllComboData}), comboDataLoadingEvent.getMessage());
        assertEquals(Level.INFO, comboDataLoadingEvent.getLevel());
    }

    private void loadAndCheckComptsSupplInfo(final Long expectedFirstNewComptId, final Long packetId,
                                             final String expectedComboDataLabelPrefix,
                                             final Integer expectedResultLength, final Integer expectedNumOfCompts,
                                             final Integer expectedNumOfStates, final Integer expectedNumOfComboData)
            throws Exception {

        final List<String> expectedComboDataLabels
                = generateIteratedLabelsList(expectedComboDataLabelPrefix, expectedNumOfCompts, expectedNumOfStates,
                expectedNumOfComboData);
        final String testLoadDataForAllSupplInfo
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMPTSSUPPLINFO_LOADED);

        final List<ComptSupplInfo> result = packetAppDao.loadComptsSupplInfo(packetId);

        assertEquals((long) expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        {
                            assertEquals(expectedComboDataLabels.get(i), result.get(i).getLabel());
                            assertEquals(expectedFirstNewComptId, result.get(i).getComptId());
                            assertEquals((long) (i / 3 + 1), result.get(i).getStateId().longValue());
                            assertEquals((i % 3 == 0), result.get(i).isChecked());
                        }
                );
        final int testLogSize = testAppender.getLog().size();
        final LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testLoadDataForAllSupplInfo, new Object[]{result}),
                loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    private List<String> generateDiverseLabelsList(String labelPrefix, int numOfLabels) {
        final List<String> labels = new ArrayList<>();
        IntStream.rangeClosed(1, numOfLabels)
                .boxed()
                .forEach(i -> labels.add(labelPrefix + i.toString()));
        return labels;
    }

    private List<String> generateEqualLabelsList(String labelPrefix, int numOfLabels) {
        final List<String> labels = new ArrayList<>();
        IntStream.rangeClosed(1, numOfLabels)
                .forEach(i -> labels.add(labelPrefix + "1"));
        return labels;
    }

    private List<String> generateIteratedLabelsList(final String labelPrefix,
                                                    final Integer numOfCompts,
                                                    final Integer numOfStates,
                                                    final Integer numOfComboData) {

        final List<String> labels = new ArrayList<>();
        final int numOfIterations = numOfCompts * numOfStates;
        List<String> elementaryComboDataList = generateDiverseLabelsList(labelPrefix, numOfComboData);
        IntStream.rangeClosed(1, numOfIterations)
                .boxed()
                .forEach(i -> labels.addAll(elementaryComboDataList));

        return labels;
    }

    private int calculateExpectedNumOfDataCompts(final int expectedNumOfCompts,
                                                 final int expectedNumOfStates,
                                                 final int expectedNumOfComboData) {

        return expectedNumOfCompts * expectedNumOfStates * expectedNumOfComboData;
    }

    private List<PacketParams> generatePacketParamsList(final Long packetId,
                                                        final Long stateId,
                                                        final List<ComptParams> comptParamsList) {

        final List<PacketParams> paramsList = new ArrayList<>();
        PacketParams packetParams = new PacketParams()
                .setId(packetId)
                .setStateId(stateId)
                .setNewComptParamsList(comptParamsList);

        paramsList.add(packetParams);

        return paramsList;
    }

    private List<ComptParams> generateComptParamsList(final OperationType operationType,
                                                      final String[][] checkedValsForAddOrUpdate,
                                                      final long[] idsForAddOrUpdate,
                                                      final int numOfComptsToAddOrUpdate,
                                                      final String[] labelsForAdding) {

        final List<ComptParams> paramsList = new ArrayList<>();
        if (operationType == OperationType.UPDATE) {
            IntStream.range(0, numOfComptsToAddOrUpdate).boxed()
                    .forEach(i ->
                            paramsList.add(new ComptParams()
                                    .setVals(Arrays.asList(checkedValsForAddOrUpdate[i]))
                                    .setId(idsForAddOrUpdate[i]))
                    );
        } else if (operationType == OperationType.ADD) {
            IntStream.range(0, numOfComptsToAddOrUpdate).boxed()
                    .forEach(i ->
                            paramsList.add(new ComptParams()
                                    .setVals(Arrays.asList(checkedValsForAddOrUpdate[i]))
                                    .setLabel(labelsForAdding[i]))
                    );
        }

        return paramsList;
    }

    private long[] generateIds(final int startingId, final int numOfIds) {
        final long[] ids = new long[numOfIds];
        IntStream
                .range(startingId, startingId + numOfIds)
                .boxed()
                .forEach(i -> ids[i - startingId] = (long) i);

        return ids;
    }

    private String[][] generateMultipleLabelsLists(final String labelPrefix, final int numOfStates,
                                                   final int numOfCompts) {

        List<String> vals = generateDiverseLabelsList(labelPrefix, numOfStates);
        String[] valsArray = vals.toArray(new String[0]);
        List<String> invertedVals = Lists.reverse(vals);
        String[] invertedValsArray = invertedVals.toArray(new String[0]);
        final String[][] checkedVals = new String[numOfCompts][numOfStates];

        fillInTheCheckedVals(checkedVals, ((Integer i) -> i % 2 == 0), valsArray);
        fillInTheCheckedVals(checkedVals, ((Integer i) -> i % 2 != 0), invertedValsArray);

        return checkedVals;
    }

    private void fillInTheCheckedVals(final String[][] checkedVals, final Predicate<Integer> condition,
                                      final String[] vals) {
        IntStream.range(0, checkedVals.length)
                .boxed()
                .filter(condition::test)
                .forEach(i -> checkedVals[i] = vals);
    }

    private DatabaseException testEmptyTableCase(DatabaseException exc) {
        StackTraceElement[] testEmptyTableExceptionStackTrace = null;
        String testEmptyTableExceptionMessage = null;
        String testCombodataTableIsEmpty = null;
        if (exc.getCause().getClass().equals(EmptyComboDataTableException.class)) {
            testEmptyTableExceptionStackTrace = (StackTraceElement[]) ReflectionTestUtils.getField(packetAppDao,
                    EMPTY_COMBODATA_TABLE_EXCEPTION_STACKTRACE);
            testEmptyTableExceptionMessage = (String) ReflectionTestUtils.getField(packetAppDao,
                    EMPTY_COMBODATA_TABLE_EXCEPTION_MESSAGE);
            testCombodataTableIsEmpty = (String) ReflectionTestUtils.getField(packetAppDao, COMBODATA_TABLE_IS_EMPTY);
        } else if (exc.getCause().getClass().equals(EmptyStateTableException.class)) {
            testEmptyTableExceptionStackTrace = (StackTraceElement[]) ReflectionTestUtils.getField(packetAppDao,
                    EMPTY_STATE_TABLE_EXCEPTION_STACKTRACE);
            testEmptyTableExceptionMessage = (String) ReflectionTestUtils.getField(packetAppDao,
                    EMPTY_STATE_TABLE_EXCEPTION_MESSAGE);
            testCombodataTableIsEmpty = (String) ReflectionTestUtils.getField(packetAppDao, STATE_TABLE_IS_EMPTY);
        }
        int testLogSize = testAppender.getLog().size();
        LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - 1);
        assertEquals(Utils.getMessage(testCombodataTableIsEmpty, new Object[]{testEmptyTableExceptionStackTrace}),
                loggingEvent.getMessage());
        assertEquals(Level.ERROR, loggingEvent.getLevel());
        return exc;
    }

    private void testDeleteCompts(PacketDaoError error, List<Long> idsToDelete, Set<Long> fakeIds) {
        final int testLogSize = testAppender.getLog().size();
        int expectedNumOfLogEventsLeft = 1;

        if (error == PacketDaoError.SOME_OF_THE_COMPT_IDS_EXIST) {
            expectedNumOfLogEventsLeft = 2;
        }

        if (error == PacketDaoError.NONE_OF_THE_COMPT_IDS_EXIST) {
            testLoggingMessage(COMPTS_DELETE_NON_EXISTING_COMPTS, testLogSize, expectedNumOfLogEventsLeft, idsToDelete,
                    Level.ERROR);
            return;
        }

        final String testComptsDeleteSuccessReport
                = (String) ReflectionTestUtils.getField(packetAppDao, COMPTS_DELETE_SUCCESS_REPORT);
        final LoggingEvent successLoggingEvent
                = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
        Set<Long> idsToDeleteSet = new HashSet<>(idsToDelete);
        idsToDeleteSet.removeAll(fakeIds);
        assertEquals(Utils.getMessage(testComptsDeleteSuccessReport, new Object[]{idsToDeleteSet}),
                successLoggingEvent.getMessage());
        assertEquals(Level.INFO, successLoggingEvent.getLevel());

        if (error == PacketDaoError.SOME_OF_THE_COMPT_IDS_EXIST) {
            final String testSomeOfComptIdsExist
                    = (String) ReflectionTestUtils.getField(packetAppDao, COMPTS_DELETE_NON_EXISTING_COMPTS);
            final LoggingEvent someOfIdsExistLoggingEvent
                    = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
            assertEquals(Utils.getMessage(testSomeOfComptIdsExist, new Object[]{fakeIds}),
                    someOfIdsExistLoggingEvent.getMessage());
            assertEquals(Level.ERROR, someOfIdsExistLoggingEvent.getLevel());
        }
    }

    private void testGetUserRole(PacketDaoError error, String username) {
        final int testLogSize = testAppender.getLog().size();
        int expectedNumOfLogEventsLeft = 1;

        if (error == PacketDaoError.AUTHENTICATION_ERROR) {
            testLoggingMessage(USER_DATA_LOAD_ERROR, testLogSize, expectedNumOfLogEventsLeft, username, Level.ERROR);
            return;
        }

        testLoggingMessage(USER_DATA_LOAD_SUCCESS, testLogSize, expectedNumOfLogEventsLeft, username, Level.INFO);

    }

    private void testLoggingMessage(String errorMessage, int logSize, int expectedNumOfLogEventsLeft, Object errorParam,
                                    Level logLevel) {
        final String testError
                = (String) ReflectionTestUtils.getField(packetAppDao, errorMessage);
        final LoggingEvent testErrorLogEvent
                = testAppender.getLog().get(logSize - (expectedNumOfLogEventsLeft--));
        assertEquals(Utils.getMessage(testError, new Object[]{errorParam}),
                testErrorLogEvent.getMessage());
        assertEquals(logLevel, testErrorLogEvent.getLevel());
    }

    private void testAddOrUpdatePackets(PacketDaoError error, OperationType operationType,
                                        Long expectedNewOrUpdatedPacketIdBeforeBeingPersisted,
                                        Long packetStateId, String comptLabelPrefix, String comboDataLabelPrefix,
                                        int numOfComptsToAddInsideAddedPacket, int numOfStates, int numOfComboDatas,
                                        Long expectedFirstNewComptId, Long expectedPersistedNewOrUpdatedPacketId,
                                        Long secondPacketIdIfExists) {
        final int testLogSize = testAppender.getLog().size();

        int expectedNumOfLogEventsLeft = 6;
        EnumSet<PacketDaoError> fiveElsLogErrorsSet = buildFiveLogErrorsSet();
        EnumSet<PacketDaoError> sixElsLogErrorsSet = buildSixLogErrorsSet();
        if (fiveElsLogErrorsSet.contains(error) && operationType == OperationType.ADD) {
            expectedNumOfLogEventsLeft = 5;
        } else if (operationType == OperationType.UPDATE &&
                (error == PacketDaoError.NULL_NEW_STATE_ID || packetStateId == null)) {
            if (numOfComptsToAddInsideAddedPacket == 0) {
                expectedNumOfLogEventsLeft = 3;
            } else {
                expectedNumOfLogEventsLeft = 5;
            }
        } else if (!sixElsLogErrorsSet.contains(error)) {
            expectedNumOfLogEventsLeft = 4;
        } else if (operationType == OperationType.ADD && numOfComptsToAddInsideAddedPacket > 1
                || operationType == OperationType.UPDATE
                && numOfComptsToAddInsideAddedPacket == 0
                && error != PacketDaoError.NOT_EXISTING_ONE_OF_PACKET_IDS) {
            expectedNumOfLogEventsLeft = 5;
        }

        assertEquals(testLogSize, expectedNumOfLogEventsLeft);

        final String testAllStatesLoadedMessage
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_STATES_LOADED_MESSAGE);
        final List<String> testAllStates = (List<String>) ReflectionTestUtils.getField(packetAppDao, ALL_STATES);
        final LoggingEvent statesLoggingEvent = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
        assertEquals(Utils.getMessage(testAllStatesLoadedMessage, new Object[]{testAllStates}),
                statesLoggingEvent.getMessage());
        assertEquals(Level.INFO, statesLoggingEvent.getLevel());

        final String testAllComboDataLoadedMessage
                = (String) ReflectionTestUtils.getField(packetAppDao, ALL_COMBODATA_LOADED_MESSAGE);
        final List<String> testAllComboData
                = (List<String>) ReflectionTestUtils.getField(packetAppDao, ALL_COMBODATA);
        final LoggingEvent comboDataLoadingEvent
                = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
        assertEquals(Utils.getMessage(testAllComboDataLoadedMessage,
                new Object[]{testAllComboData}), comboDataLoadingEvent.getMessage());
        assertEquals(Level.INFO, comboDataLoadingEvent.getLevel());

        final Map<String, Integer> testMapCombodataLabelsToIndices
                = (Map<String, Integer>) ReflectionTestUtils.getField(packetAppDao, MAP_COMBODATA_LABELS_TO_INDICES);
        final String testMapCombodataLabelsToIndicesMessage
                = (String) ReflectionTestUtils.getField(packetAppDao, MAP_COMBODATA_LABELS_TO_INDICES_MESSAGE);
        final LoggingEvent mapComboDataLoggingEvent
                = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
        assertEquals(Utils.getMessage(testMapCombodataLabelsToIndicesMessage,
                new Object[]{testMapCombodataLabelsToIndices}), mapComboDataLoggingEvent.getMessage());
        assertEquals(Level.INFO, mapComboDataLoggingEvent.getLevel());

        if (operationType == OperationType.UPDATE && (error == PacketDaoError.NULL_NEW_STATE_ID
                || packetStateId == null) && (numOfComptsToAddInsideAddedPacket == 0)) {
            return;
        }

        if (error == PacketDaoError.NOT_EXISTING_THE_ONLY_PACKET_ID
                || error == PacketDaoError.NOT_EXISTING_ONE_OF_PACKET_IDS) {
            final String testPacketUpdateNotExistingPacket
                    = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_UPDATE_NOT_EXISTING_PACKET);
            LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
            assertEquals(
                    Utils.getMessage(testPacketUpdateNotExistingPacket,
                            new Object[]{secondPacketIdIfExists == null
                                    ? expectedPersistedNewOrUpdatedPacketId : secondPacketIdIfExists}),
                    loggingEvent.getMessage()
            );
            assertEquals(Level.ERROR, loggingEvent.getLevel());
            if (numOfComptsToAddInsideAddedPacket == 0) {
                return;
            }
        } else if (error == PacketDaoError.NOT_DIFFERENT_NEW_STATE_ID) {
            String testNotDifferentNewState = null;
            if (operationType == OperationType.UPDATE) {
                testNotDifferentNewState
                        = (String) ReflectionTestUtils.getField(packetAppDao,
                        PACKET_UPDATE_STATE_UPDATE_NOT_DIFFERENT_NEW_STATE
                );
            } else if (operationType == OperationType.ADD) {
                testNotDifferentNewState
                        = (String) ReflectionTestUtils.getField(packetAppDao,
                        PACKET_ADDING_STATE_SET_NOT_DIFFERENT_NEW_STATE
                );
            }
            LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
            assertEquals(
                    Utils.getMessage(testNotDifferentNewState, new Object[]{packetStateId}),
                    loggingEvent.getMessage()
            );
            assertEquals(Level.ERROR, loggingEvent.getLevel());
            return;
        } else if (error == PacketDaoError.NOT_EXISTING_STATE_ID) {
            LoggingEvent loggingEvent = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
            String testNotExistingNewState;
            if (operationType == OperationType.UPDATE) {
                testNotExistingNewState
                        = (String) ReflectionTestUtils.getField(packetAppDao,
                        PACKET_UPDATE_STATE_UPDATE_NOT_EXISTING_STATE
                );
                assertEquals(
                        Utils.getMessage(testNotExistingNewState, new Object[]{packetStateId}),
                        loggingEvent.getMessage()
                );
                assertEquals(Level.ERROR, loggingEvent.getLevel());
            } else if (operationType == OperationType.ADD) {
                State defaultPacketState = new State();
                defaultPacketState.setId(1L);
                defaultPacketState.setLabel(DEFAULT_STATE_LABEL_PREFIX + defaultPacketState.getId());
                testNotExistingNewState
                        = (String) ReflectionTestUtils.getField(packetAppDao,
                        PACKET_ADDING_STATE_SET_NOT_EXISTING_STATE
                );
                assertEquals(
                        Utils.getMessage(testNotExistingNewState,
                                new Object[]{packetStateId,
                                        defaultPacketState.toString()}),
                        loggingEvent.getMessage()
                );

                assertEquals(Level.INFO, loggingEvent.getLevel());

                testPersistPacketLoggingEvent(testLogSize,
                        expectedNumOfLogEventsLeft,
                        buildPacket(defaultPacketState.getId(),
                                expectedPersistedNewOrUpdatedPacketId,
                                Collections.emptyList(),
                                DEFAULT_STATE_LABEL_PREFIX)

                );
            }
            return;
        } else if (error == PacketDaoError.NULL_NEW_STATE_ID) {
            String testNullNewState;
            State defaultPacketState = new State();
            defaultPacketState.setId(1L);
            defaultPacketState.setLabel(DEFAULT_STATE_LABEL_PREFIX + defaultPacketState.getId());
            LoggingEvent nullStateLoggingEvent =
                    testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
            if (operationType == OperationType.ADD) {
                testNullNewState
                        = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_ADDING_STATE_SET_NULL_STATE);
                assertEquals(
                        Utils.getMessage(testNullNewState,
                                new Object[]{defaultPacketState}),
                        nullStateLoggingEvent.getMessage()
                );
                assertEquals(Level.INFO, nullStateLoggingEvent.getLevel());
                testPersistPacketLoggingEvent(testLogSize,
                        expectedNumOfLogEventsLeft,
                        buildPacket(defaultPacketState.getId(),
                                expectedPersistedNewOrUpdatedPacketId,
                                Collections.emptyList(),
                                DEFAULT_STATE_LABEL_PREFIX)
                );
            }
            if (numOfComptsToAddInsideAddedPacket == 0) {
                return;
            }
        }
        if (operationType != OperationType.UPDATE || packetStateId != null) {
            final LoggingEvent stateUpdateLoggingEvent
                    = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
            String testStateUpdateSuccessReport = null;
            if (operationType == OperationType.UPDATE) {
                testStateUpdateSuccessReport
                        = (String) ReflectionTestUtils.getField(packetAppDao,
                        PACKET_UPDATE_STATE_UPDATE_SUCCESS_REPORT);
            } else if (operationType == OperationType.ADD) {
                testStateUpdateSuccessReport
                        = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_ADDING_STATE_SET_SUCCESS_REPORT);
            }
            assertEquals(Utils.getMessage(testStateUpdateSuccessReport,
                    new Object[]{expectedNewOrUpdatedPacketIdBeforeBeingPersisted,
                            packetStateId}),
                    stateUpdateLoggingEvent.getMessage());
            assertEquals(Level.INFO, stateUpdateLoggingEvent.getLevel());
        }

        Packet packet = new Packet();
        List<Compt> unpersistedComptsList = new ArrayList<>();
        State state = new State();
        if (numOfComptsToAddInsideAddedPacket > 0) {
            final LoggingEvent addComptsLoggingEvent
                    = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
            String testPacketAddingOrUpdateAddCompts = null;
            if (operationType == OperationType.ADD) {
                testPacketAddingOrUpdateAddCompts
                        = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_ADDING_ADD_COMPTS);
            } else if (operationType == OperationType.UPDATE) {
                testPacketAddingOrUpdateAddCompts
                        = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_UPDATE_ADD_COMPTS);
            }

            if (numOfComptsToAddInsideAddedPacket == 1) {
                Compt compt = new Compt();
                state.setId(packetStateId);
                state.setLabel(DEFAULT_STATE_LABEL_PREFIX + packetStateId);
                packet.setState(state);
                compt.setLabel(comptLabelPrefix + 1);
                compt.setPacket(packet);
                unpersistedComptsList.add(compt);
            } else {
                state.setId(1L);
                state.setLabel(DEFAULT_STATE_LABEL_PREFIX + state.getId());
                packet.setState(state);
                packet.setId(1L);
                for (int i = 1; i <= numOfComptsToAddInsideAddedPacket; i++) {
                    Compt compt = new Compt();
                    compt.setLabel(comptLabelPrefix + i);
                    compt.setPacket(packet);
                    unpersistedComptsList.add(compt);
                }
            }
            List<ComboData> comboDatas = new ArrayList<>();
            for (int j = 1; j < (numOfComboDatas + 1); j++) {
                ComboData cd = new ComboData();
                cd.setId(Long.valueOf(j));
                cd.setLabel(comboDataLabelPrefix + j);
                cd.setVersion(0);
                comboDatas.add(cd);
            }
            for (int i = 0; i < numOfComptsToAddInsideAddedPacket * numOfStates * numOfComboDatas; i++) {
                DataCompt dc = new DataCompt();
                dc.setId(Long.valueOf(i + 1));
                dc.setComboData(comboDatas.get(i % 3));
                dc.setVersion(0);
                dc.setCompt(unpersistedComptsList.get(i / (numOfStates * 3)));
                dc.setChecked(i % 4 == 0);
            }
            Object[] messageArgs = null;
            if (operationType == OperationType.UPDATE) {
                messageArgs = new Object[]{expectedPersistedNewOrUpdatedPacketId, unpersistedComptsList};
            } else if (operationType == OperationType.ADD) {
                messageArgs = new Object[]{unpersistedComptsList};
            }
            assertEquals(
                    Utils.getMessage(testPacketAddingOrUpdateAddCompts, messageArgs),
                    addComptsLoggingEvent.getMessage()
            );
            assertEquals(Level.INFO, addComptsLoggingEvent.getLevel());
        } else {
            state.setId(packetStateId);
            state.setLabel(DEFAULT_STATE_LABEL_PREFIX + packetStateId);
            packet.setState(state);
        }

        if (operationType == OperationType.ADD && numOfComptsToAddInsideAddedPacket > 1) {
            return;
        }

        final LoggingEvent persistPacketLoggingEvent = testAppender.getLog()
                .get(testLogSize - (expectedNumOfLogEventsLeft--));
        List<Compt> packetComptsList = new ArrayList<>();

        if (expectedFirstNewComptId != null) {
            IntStream.range(1, expectedFirstNewComptId.intValue())
                    .boxed()
                    .forEach(id -> packetComptsList.add(buildCompt(id.longValue(), packet, comptLabelPrefix)));
            final int comptListSize = unpersistedComptsList.size();
            IntStream.range(0, comptListSize).boxed()
                    .forEach(i -> unpersistedComptsList.get(i).setId(expectedFirstNewComptId + i));
        }
        packetComptsList.addAll(unpersistedComptsList);
        packet.setId(expectedPersistedNewOrUpdatedPacketId);
        packet.setCompts(packetComptsList);
        String testPacketAddingOrUpdateSuccessReport = null;

        if (operationType == OperationType.ADD) {
            testPacketAddingOrUpdateSuccessReport
                    = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_ADDING_SUCCESS_REPORT);
        } else if (operationType == OperationType.UPDATE) {
            testPacketAddingOrUpdateSuccessReport
                    = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_UPDATE_SUCCESS_REPORT);
        }
        assertEquals(
                Utils.getMessage(testPacketAddingOrUpdateSuccessReport,
                        new Object[]{packet}),
                persistPacketLoggingEvent.getMessage()
        );
        assertEquals(Level.INFO, persistPacketLoggingEvent.getLevel());
    }

    private Compt buildCompt(Long id, Packet packet, String labelPrefix) {
        Compt compt = new Compt();
        compt.setId(id);
        compt.setLabel(labelPrefix + (id - 1));
        compt.setPacket(packet);
        return compt;
    }

    private Packet buildPacket(Long stateId, Long pktId, List<Compt> comptsList, String stateLabelPrefix) {
        State state = new State();
        state.setId(stateId);
        state.setLabel(stateLabelPrefix + stateId);
        Packet packet = new Packet();
        packet.setState(state);
        packet.setId(pktId);
        packet.setCompts(comptsList);
        return packet;
    }

    private void testPersistPacketLoggingEvent(int testLogSize, int expectedNumOfLogEventsLeft, Packet packet) {
        LoggingEvent persistPacketLoggingEvent
                = testAppender.getLog().get(testLogSize - (expectedNumOfLogEventsLeft--));
        String testPacketAddingOrUpdateSuccessReport
                = (String) ReflectionTestUtils.getField(packetAppDao, PACKET_ADDING_SUCCESS_REPORT);
        assertEquals(
                Utils.getMessage(testPacketAddingOrUpdateSuccessReport,
                        new Object[]{packet}),
                persistPacketLoggingEvent.getMessage()
        );
        assertEquals(Level.INFO, persistPacketLoggingEvent.getLevel());
    }

    private EnumSet<PacketDaoError> buildFiveLogErrorsSet() {
        EnumSet<PacketDaoError> fiveElsLogErrorsSet = EnumSet.noneOf(PacketDaoError.class);
        fiveElsLogErrorsSet.add(PacketDaoError.NULL_NEW_STATE_ID);
        fiveElsLogErrorsSet.add(PacketDaoError.NOT_EXISTING_STATE_ID);
        return fiveElsLogErrorsSet;
    }

    private EnumSet<PacketDaoError> buildSixLogErrorsSet() {
        EnumSet<PacketDaoError> fiveElsLogErrorsSet = EnumSet.noneOf(PacketDaoError.class);
        fiveElsLogErrorsSet.add(PacketDaoError.NONE);
        fiveElsLogErrorsSet.add(PacketDaoError.NOT_EXISTING_ONE_OF_PACKET_IDS);
        return fiveElsLogErrorsSet;
    }
}
