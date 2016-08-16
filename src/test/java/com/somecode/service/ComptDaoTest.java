package com.somecode.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.somecode.dao.ComptDao;
import com.somecode.domain.*;
import org.apache.commons.lang3.ArrayUtils;
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

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@TestExecutionListeners({AbstractDbunitTransactionalJUnit4SpringContextTests.DbunitTestExecutionListener.class})
@ActiveProfiles("test")
public class ComptDaoTest extends AbstractDbunitTransactionalJUnit4SpringContextTests {

    @Autowired
    ComptDao comptDao;

    @PersistenceContext
    private EntityManager em;

    @Override
    protected void clearCachedData() {
        deleteFromTables("DATA_COMPT", "COMBO_DATA", "COMPT", "PACKET", "STATE");
        Optional.of(em)
                .ifPresent(EntityManager::clear);
        Optional.of(em)
                .map(EntityManager::getEntityManagerFactory)
                .map(EntityManagerFactory::getCache)
                .ifPresent(Cache::evictAll);
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Compt.xls")
    @Test
    @DirtiesContext
    public void testGetCompts_positive() throws Exception {
        final long packetId = 1L;
        final String expectedLabel = "compt_label_1";
        final int expectedResultLength = 1;

        final List<ComptInfo> result = comptDao.getComptsByPacketId(packetId);

        assertEquals(expectedResultLength, result.size());
        assertEquals(expectedLabel, result.get(0).getLabel());
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Compt.xls")
    @Test
    @DirtiesContext
    public void testGetCompts_negative() throws Exception {
        final long packetId = 2L;
        final int expectedResultSize = 0;
        final List<ComptInfo> result = comptDao.getComptsByPacketId(packetId);

        assertEquals(expectedResultSize, result.size());
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Compt_SupplInfo.xls")
    @Test
    @DirtiesContext
    public void testGetComptsSupplInfo_positive() throws Exception {
        final long packetId = 1L;

        final Integer expectedNumOfStates = 3;
        final Integer expectedNumOfComboData = 3;
        final Integer expectedNumOfCompts = 1;
        final int expectedResultLength = calculateExpectedNumOfDataCompts(expectedNumOfCompts, expectedNumOfStates,
                expectedNumOfComboData);

        findComboData(ComboDataGetTestCase.COMPTS_SUPPL_INFO, packetId, expectedResultLength,
                expectedNumOfCompts, expectedNumOfStates, expectedNumOfComboData);
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Compt_SupplInfo.xls")
    @Test
    @DirtiesContext
    public void testGetComptsSupplInfo_negative() throws Exception {
        final long packetId = 2L;
        final int expectedResultSize = 0;
        final List<ComptSupplInfo> result = comptDao.getComptsSupplInfoByPacketId(packetId);

        assertEquals(expectedResultSize, result.size());
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_ComboData.xls")
    @Test
    @DirtiesContext
    public void testGetComboData_positive() throws Exception {
        final int expectedNumOfComboData = 3;

        findComboData(ComboDataGetTestCase.COMBO_DATA_PROPERLY, null, expectedNumOfComboData, null, null, null);
    }

    private void findComboData(ComboDataGetTestCase testCase, final Long packetId,
                               final Integer expectedResultLength, final Integer expectedNumOfCompts,
                               final Integer expectedNumOfStates, final Integer expectedNumOfComboData)
            throws Exception {
        final String comboDataLabelPrefix = "combo_label_";

        final List<String> expectedComboDataLabels = (testCase == ComboDataGetTestCase.COMBO_DATA_PROPERLY)
                ? generateDiverseLabelsList(comboDataLabelPrefix, expectedResultLength)
                : (testCase == ComboDataGetTestCase.COMPTS_SUPPL_INFO)
                ? generateIteratedLabelsList(comboDataLabelPrefix, expectedNumOfCompts,
                expectedNumOfStates, expectedNumOfComboData)
                : new ArrayList<>();

        List<HasLabel> result = new ArrayList<>();

        if (testCase == ComboDataGetTestCase.COMPTS_SUPPL_INFO) {
            comptDao.getComptsSupplInfoByPacketId(packetId).forEach(result::add);
        } else if (testCase == ComboDataGetTestCase.COMBO_DATA_PROPERLY) {
            comptDao.getAllComboData().forEach(result::add);
        }

        assertEquals((long) expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        assertEquals(expectedComboDataLabels.get(i), result.get(i).getLabel())
                );
    }

    private List<String> generateDiverseLabelsList(String labelPrefix, int numOfLabels) {
        final List<String> comboDataLabels = new ArrayList<>();
        IntStream.rangeClosed(1, numOfLabels)
                .boxed()
                .forEach(i -> comboDataLabels.add(labelPrefix + i.toString()));
        return comboDataLabels;
    }

    private List<String> generateEqualLabelsList(String labelPrefix, int numOfLabels) {
        final List<String> comboDataLabels = new ArrayList<>();
        IntStream.rangeClosed(1, numOfLabels)
                .forEach(i -> comboDataLabels.add(labelPrefix + "1"));
        return comboDataLabels;
    }

    private List<String> generateIteratedLabelsList(final String labelPrefix,
                                                    final Integer numOfCompts,
                                                    final Integer numOfStates,
                                                    final Integer numOfComboData) {

        final List<String> comboDataLabels = new ArrayList<>();
        final int numOfIterations = numOfCompts * numOfStates;
        List<String> elementaryComboDataList = generateDiverseLabelsList(labelPrefix, numOfComboData);
        IntStream.rangeClosed(1, numOfIterations)
                .boxed()
                .forEach(i -> comboDataLabels.addAll(elementaryComboDataList));

        return comboDataLabels;
    }

    private int calculateExpectedNumOfDataCompts(final int expectedNumOfCompts,
                                                 final int expectedNumOfStates,
                                                 final int expectedNumOfComboData) {

        return expectedNumOfCompts * expectedNumOfStates * expectedNumOfComboData;
    }

    @Test
    @DirtiesContext
    public void testGetComboData_negative() throws Exception {
        final int expectedResultLength = 0;
        final List<ComboData> result = comptDao.getAllComboData();

        assertEquals(expectedResultLength, result.size());
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_State.xls")
    @Test
    @DirtiesContext
    public void testGetStates_positive() throws Exception {
        final String expectedLabelsPrefix = "state_label_";
        final int expectedResultLength = 3;
        final List<String> expectedLabels = generateDiverseLabelsList(expectedLabelsPrefix, expectedResultLength);

        final List<State> result = comptDao.getAllStates();

        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i -> assertEquals(expectedLabels.get(i), result.get(i).getLabel()));
    }

    @Test
    @DirtiesContext
    public void testGetAllStates_negative() throws Exception {
        final int expectedResultLength = 0;
        final List<State> result = comptDao.getAllStates();

        assertEquals(expectedResultLength, result.size());
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Get_PacketState.xls")
    @Test
    @DirtiesContext
    public void testGetPacketState_positive() throws Exception {
        final long packetId = 1L;

        final long expectedResult = 1L;

        final Long result = comptDao.getPacketStateId(packetId);

        assertEquals((Long) expectedResult, result);
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Get_PacketState.xls")
    @Test
    @DirtiesContext
    public void testGetPacketState_negative() throws Exception {
        final long packetId = 2L;

        final Long result = comptDao.getPacketStateId(packetId);

        assertNull(result);
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Update_Compts.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Update_Compts.xls")
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdateCompts_positive() throws Exception {
        final int numOfComptsToUpdate = 2;
        final String comboDataLabelPrefix = "combo_label_";
        final int numOfStates = 3;
        final int firstIdToUpdate = 1;

        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);

        final long[] idsForUpdate = generateIds(firstIdToUpdate, numOfComptsToUpdate);

        final List<ComptParams> paramsListForUpdate
                = generateComptParamsList(OperationType.UPDATE, checkedValsForUpdate,
                idsForUpdate, numOfComptsToUpdate, null);

        comptDao.updateCompts(paramsListForUpdate);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Update_Compts.xls",
            after = "/com/somecode/service/ComptDaoTest_Before_Update_Compts.xls")
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdateCompts_negative() throws Exception {
        final int numOfComptsToUpdate = 1;
        final String comboDataLabelPrefix = "combo_label_";
        final int numOfStates = 3;
        final int firstComptIdToUpdate = 3;

        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);

        final long[] idsForUpdate = generateIds(firstComptIdToUpdate, numOfComptsToUpdate);

        List<ComptParams> paramsListForUpdate
                = generateComptParamsList(OperationType.UPDATE, checkedValsForUpdate,
                idsForUpdate, numOfComptsToUpdate, null);

        comptDao.updateCompts(paramsListForUpdate);
        em.flush();
    }

    private List<PacketParams> generatePacketParamsList(final long packetId,
                                                        final long stateId,
                                                        final List<ComptParams> comptParamsList) {

        final List<PacketParams> paramsList = new ArrayList<>();
        PacketParams packetParams = new PacketParams()
                .setId(packetId)
                .setStateId(stateId)
                .setComptParamsList(comptParamsList);

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
                .filter(condition::apply)
                .forEach(i -> checkedVals[i] = vals);
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Add_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Add_Packets_Positive.xls")
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testAddPackets_positive() throws Exception {
        final int numOfComptsToAddInsideAddedPacket = 1;
        final long unusedAddedPacketId = 0L;
        final long packetStateId = 2L;
        final String comboDataLabelPrefix = "combo_label_";
        final String comptLabelPrefix = "compt_label_";
        final int numOfStates = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAddInsideAddedPacket);
        final String[] labelsForAdding
                = generateDiverseLabelsList(comptLabelPrefix, numOfComptsToAddInsideAddedPacket).toArray(new String[0]);
        final List<ComptParams> comptParamsList = generateComptParamsList(OperationType.ADD,
                checkedValsForAdding, null,
                numOfComptsToAddInsideAddedPacket, labelsForAdding);
        final List<PacketParams> packetParamsList
                = generatePacketParamsList(unusedAddedPacketId, packetStateId, comptParamsList);

        comptDao.addPackets(packetParamsList);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Add_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Add_Packets_Negative.xls")
    @Test(expected = DataIntegrityViolationException.class)
    @Rollback(false)
    @DirtiesContext
    public void testAddPackets_negative_equalLabels() throws Exception {
        final int numOfComptsToAddInsideAddedPacket = 2;
        final long unusedAddedPacketId = 2L;
        final long packetStateId = 2L;
        final String comboDataLabelPrefix = "combo_label_";
        final String comptLabelPrefix = "compt_label_";
        final int numOfStates = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAddInsideAddedPacket);
        final String[] labelsForAdding
                = generateEqualLabelsList(comptLabelPrefix, numOfComptsToAddInsideAddedPacket).toArray(new String[0]);
        final List<ComptParams> comptParamsList = generateComptParamsList(OperationType.ADD,
                checkedValsForAdding, null,
                numOfComptsToAddInsideAddedPacket, labelsForAdding);
        final List<PacketParams> packetParamsList
                = generatePacketParamsList(unusedAddedPacketId, packetStateId, comptParamsList);

        comptDao.addPackets(packetParamsList);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Delete_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Delete_Packets_Positive.xls")
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testDeletePackets_positive() throws Exception {
        final long packetId = 2L;
        final List<Long> idsToDelete = Arrays.asList(packetId);

        comptDao.deletePackets(idsToDelete);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Delete_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Delete_Packets_Negative.xls")
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testDeletePackets_negative() throws Exception {
        final long packetId = 3L;
        final List<Long> idsToDelete = Arrays.asList(packetId);

        comptDao.deletePackets(idsToDelete);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Update_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Update_Packets_Positive.xls")
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testUpdatePackets_positive() throws Exception {
        final long packetId = 1L;
        final long newStateId = 2L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        comptDao.updatePackets(packetParamsList);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Update_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Update_Packets_Negative_NonExistingPacketId.xls")
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdatePackets_negative_nonExistingPacketId() throws Exception {
        final long packetId = 2L;
        final long newStateId = 1L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        comptDao.updatePackets(packetParamsList);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Update_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Update_Packets_Negative_NotDifferentNewStateId.xls")
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdatePackets_negative_notDifferentNewStateId() throws Exception {
        final long packetId = 1L;
        final long newStateId = 1L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        comptDao.updatePackets(packetParamsList);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Update_Packets.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Update_Packets_Negative_NonExistingStateId.xls")
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testUpdatePackets_negative_nonExistingStateId() throws Exception {
        final long packetId = 1L;
        final long newStateId = 4L;
        final List<ComptParams> comptParamsList = Collections.emptyList();
        final List<PacketParams> packetParamsList = generatePacketParamsList(packetId, newStateId, comptParamsList);

        comptDao.updatePackets(packetParamsList);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Add_Compts.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Add_Compts_Positive.xls")
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testAddCompts_positive() throws Exception {
        final int numOfComptsToAdd = 2;
        final long packetId = 1L;
        final String comboDataLabelPrefix = "combo_label_";
        final String comptLabelPrefix = "compt_label_";
        final int numOfStates = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAdd);
        final String[] labelsForAdding
                = generateDiverseLabelsList(comptLabelPrefix, numOfComptsToAdd).toArray(new String[0]);

        final List<ComptParams> paramsListForAdding = generateComptParamsList(OperationType.ADD,
                checkedValsForAdding, null,
                numOfComptsToAdd, labelsForAdding);

        comptDao.addCompts(packetId, paramsListForAdding);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Add_Compts.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Add_Compts_Negative_NonExistent_PacketId.xls")
    @Test
    @Rollback(false)
    @DirtiesContext
    public void testAddCompts_negative_nonExistentPacketId() throws Exception {
        final int numOfComptsToAdd = 2;
        final long packetId = 2L;
        final String comboDataLabelPrefix = "combo_label_";
        final String comptLabelPrefix = "compt_label_";
        final int numOfStates = 3;
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

        comptDao.addCompts(packetId, paramsListForAdding);
        em.flush();
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Add_Compts.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Add_Compts_Negative_EqualLabels.xls")
    @Test(expected = DataIntegrityViolationException.class)
    @Rollback(false)
    @DirtiesContext
    public void testAddCompts_negative_equalLabels() throws Exception {
        final int numOfComptsToAdd = 2;
        final long packetId = 1L;
        final String comboDataLabelPrefix = "combo_label_";
        final String comptLabelPrefix = "compt_label_";
        final int numOfStates = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAdd);
        final String[] labelsForAdding
                = generateEqualLabelsList(comptLabelPrefix, numOfComptsToAdd).toArray(new String[0]);

        final List<ComptParams> paramsListForAdding = new ArrayList<>();
        IntStream.range(0, numOfComptsToAdd).boxed()
                .forEach(i -> paramsListForAdding.add(new ComptParams()
                        .setVals(Arrays.asList(checkedValsForAdding[i]))
                        .setLabel(labelsForAdding[i]))
                );

        comptDao.addCompts(packetId, paramsListForAdding);
        em.flush();
    }


    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Remove_Compts.xls",
            after = "/com/somecode/service/ComptDaoTest_After_Remove_Compts.xls")
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testRemoveCompts_positive() throws Exception {
        final int firstComptId = 1;
        final int numOfComptIds = 2;
        final List<Long> comptIdsToRemove
                = Arrays.asList(ArrayUtils.toObject(generateIds(firstComptId, numOfComptIds)));

        comptDao.deleteCompts(comptIdsToRemove);
    }

    @DataSets(before = "/com/somecode/service/ComptDaoTest_Before_Remove_Compts.xls",
            after = "/com/somecode/service/ComptDaoTest_Before_Remove_Compts.xls")
    @Rollback(false)
    @Test
    @DirtiesContext
    public void testRemoveCompts_negative() throws Exception {
        final int firstIdToRemove = 3;
        final int numOfComptsToRemove = 1;
        final List<Long> comptIdsToRemove
                = Arrays.asList(ArrayUtils.toObject(generateIds(firstIdToRemove, numOfComptsToRemove)));

        comptDao.deleteCompts(comptIdsToRemove);
    }
}
