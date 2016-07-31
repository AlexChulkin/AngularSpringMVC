package com.somecode.service;

import com.google.common.collect.Lists;
import com.somecode.dao.ComptRepository;
import com.somecode.domain.*;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestConfig.class})
@TestExecutionListeners({ServiceTestExecutionListener.class})
@ActiveProfiles("test")
public class ComptServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    ComptService comptService;

    @Autowired
    private ComptRepository comptRepository;

    @PersistenceContext
    private EntityManager em;

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_Compt.xls")
    @Test
    public void testGetCompts_positive() throws Exception {
        final long packetId = 1L;
        final String expectedLabel = "compt_label_1";
        final int expectedResultLength = 1;

        final List<ComptInfo> result = comptService.getCompts(packetId);

        assertEquals(expectedResultLength, result.size());
        assertEquals(expectedLabel, result.get(0).getLabel());
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_Compt.xls")
    @Test
    public void testGetCompts_negative() throws Exception {
        final long packetId = 2L;
        final int expectedResultSize = 0;
        final List<ComptInfo> result = comptService.getCompts(packetId);

        assertEquals(expectedResultSize, result.size());
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_Compt_SupplInfo.xls")
    @Test
    public void testGetComptsSupplInfo_positive() throws Exception {
        final long packetId = 1L;

        final Integer expectedNumOfStates = 3;
        final Integer expectedNumOfComboData = 3;
        final Integer expectedNumOfCompts = 1;
        final int expectedResultLength = calculateExpectedResultLength(expectedNumOfCompts, expectedNumOfStates,
                expectedNumOfComboData);

        findComboData(ComboDataGetTestCase.COMPTS_SUPPL_INFO, packetId, expectedResultLength,
                expectedNumOfCompts, expectedNumOfStates, expectedNumOfComboData);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_Compt_SupplInfo.xls")
    @Test
    public void testGetComptsSupplInfo_negative() throws Exception {
        final long packetId = 2L;
        final int expectedResultSize = 0;
        final List<ComptSupplInfo> result = comptService.getComptsSupplInfo(packetId);

        assertEquals(expectedResultSize, result.size());
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_ComboData.xls")
    @Test
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
                ? generateLabelsList(comboDataLabelPrefix, expectedResultLength)
                : (testCase == ComboDataGetTestCase.COMPTS_SUPPL_INFO)
                ? generateIteratedLabelsList(comboDataLabelPrefix, expectedNumOfCompts,
                expectedNumOfStates, expectedNumOfComboData)
                : new ArrayList<>();

        List<HasLabel> result = new ArrayList<>();

        if (testCase == ComboDataGetTestCase.COMPTS_SUPPL_INFO) {
            comptService.getComptsSupplInfo(packetId).forEach(result::add);
        } else if (testCase == ComboDataGetTestCase.COMBO_DATA_PROPERLY) {
            comptService.getDefaultComboData().forEach(result::add);
        }

        assertEquals((long) expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        assertEquals(expectedComboDataLabels.get(i), result.get(i).getLabel())
                );
    }

    private List<String> generateLabelsList(String labelPrefix, int numOfLabels) {
        final List<String> comboDataLabels = new ArrayList<>();
        IntStream.rangeClosed(1, numOfLabels)
                .boxed()
                .forEach(i -> comboDataLabels.add(labelPrefix + i.toString()));
        return comboDataLabels;
    }

    private List<String> generateIteratedLabelsList(final String labelPrefix,
                                                    final Integer numOfCompts,
                                                    final Integer numOfStates,
                                                    final Integer numOfComboData) {

        final List<String> comboDataLabels = new ArrayList<>();
        final int numOfIterations = numOfCompts * numOfStates;
        List<String> elementaryComboDataList = generateLabelsList(labelPrefix, numOfComboData);
        IntStream.rangeClosed(1, numOfIterations)
                .boxed()
                .forEach(i -> comboDataLabels.addAll(elementaryComboDataList));

        return comboDataLabels;
    }

    private int calculateExpectedResultLength(final int expectedNumOfCompts,
                                              final int expectedNumOfStates,
                                              final int expectedNumOfComboData) {

        return expectedNumOfCompts * expectedNumOfStates * expectedNumOfComboData;
    }

    @Test
    public void testGetComboData_negative() throws Exception {
        deleteFromTables("COMBO_DATA");

        final int expectedResultLength = 0;
        final List<ComboData> result = comptService.getDefaultComboData();

        assertEquals(expectedResultLength, result.size());
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_State.xls")
    @Test
    public void testGetStates_positive() throws Exception {
        final String expectedLabelsPrefix = "state_";
        final int expectedResultLength = 3;
        final List<String> expectedLabels = generateLabelsList(expectedLabelsPrefix, expectedResultLength);

        final List<State> result = comptService.getStates();

        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i -> assertEquals(expectedLabels.get(i), result.get(i).getLabel()));
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_State.xls")
    @Test
    public void testGetStates_negative() throws Exception {
        final int expectedResultLength = 0;
        final List<State> result = comptService.getStates();

        assertEquals(expectedResultLength, result.size());
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_PacketState.xls")
    @Test
    public void testGetPacketState_positive() throws Exception {
        final long packetId = 1L;

        final long expectedResult = 1L;

        final Long result = comptService.getPacketStateId(packetId);

        assertEquals((Long) expectedResult, result);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_PacketState.xls")
    @Test
    public void testGetPacketState_negative() throws Exception {
        final long packetId = 2L;

        final Long result = comptService.getPacketStateId(packetId);

        assertNull(result);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceImplTest_Update_Compts.xls")
    @Test
    public void testUpdateCompts_positive() throws Exception {
        final int numOfComptsToUpdate = 2;
        final String comboDataLabelPrefix = "combo_label_";
        final int numOfStates = 3;
        final int firstIdToUpdate = 1;


        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);

        final long[] idsForUpdate = generateIds(firstIdToUpdate, numOfComptsToUpdate);

        final List<ComptsParams> paramsListForUpdate
                = generateParamsList(OperationType.UPDATE, checkedValsForUpdate,
                idsForUpdate, numOfComptsToUpdate, null);

        final long[] expectedUpdatedIds = idsForUpdate;
        final int expectedResultSize = numOfComptsToUpdate;
        final String[][] expectedCheckedVals = checkedValsForUpdate;
        final int expectedNumOfStates = 3;
        final int expectedNumOfComboDataItemsPerState = 3;
        final int expectedNumOfComboDataItemsPerCompt = expectedNumOfStates * expectedNumOfComboDataItemsPerState;
        final int[][][] checkedComboDataArray =
                new int[numOfComptsToUpdate][expectedNumOfStates][expectedNumOfComboDataItemsPerState];
        final int numOfCheckedComboDataPerCompt = 1;

        comptService.updateCompts(paramsListForUpdate);
        em.flush();

        checkComptWithDependencies(OperationType.ADD, expectedResultSize, expectedNumOfComboDataItemsPerCompt,
                expectedCheckedVals, checkedComboDataArray, numOfCheckedComboDataPerCompt, expectedNumOfStates,
                expectedUpdatedIds, null);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceImplTest_Update_Compts.xls")
    @Test
    public void testUpdateCompts_negative() throws Exception {
        final int numOfComptsToUpdate = 1;
        final String comboDataLabelPrefix = "combo_label_";
        final int numOfStates = 3;
        final int firstIdToUpdate = 3;

        final int expectedResultSize = 0;
        final long expectedUpdatedComptId = 3L;

        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);

        final long[] idsForUpdate = generateIds(firstIdToUpdate, numOfComptsToUpdate);

        List<ComptsParams> paramsListForUpdate
                = generateParamsList(OperationType.UPDATE, checkedValsForUpdate,
                idsForUpdate, numOfComptsToUpdate, null);

        List<Long> result = comptService.updateCompts(paramsListForUpdate);
        em.flush();

        Compt expectedUpdatedCompt = em.find(Compt.class, expectedUpdatedComptId);

        assertEquals(expectedResultSize, result.size());
        assertNull(expectedUpdatedCompt);
    }

    private List<ComptsParams> generateParamsList(final OperationType operationType,
                                                  final String[][] checkedValsForAddOrUpdate,
                                                  final long[] idsForAddOrUpdate,
                                                  final int numOfComptsToAddOrUpdate,
                                                  final String[] labelsForAdding) {

        final List<ComptsParams> paramsList = new ArrayList<>();
        if (operationType == OperationType.UPDATE) {
            IntStream.range(0, numOfComptsToAddOrUpdate).boxed()
                    .forEach(i -> paramsList.add(new ComptsParams(Arrays.asList(checkedValsForAddOrUpdate[i]),
                            idsForAddOrUpdate[i])));
        } else if (operationType == OperationType.ADD) {
            IntStream.range(0, numOfComptsToAddOrUpdate).boxed()
                    .forEach(i ->
                            paramsList.add(new ComptsParams(Arrays.asList(checkedValsForAddOrUpdate[i]),
                                    labelsForAdding[i])));
        }


        return paramsList;
    }

    private long[] generateIds(final int firstId, final int numOfIds) {
        final long[] ids = {};
        IntStream.rangeClosed(firstId, numOfIds).boxed().forEach(i -> ids[i] = (long) i);

        return ids;
    }

    private String[][] generateMultipleLabelsLists(final String labelPrefix, final int numOfStates,
                                                   final int numOfCompts) {

        List<String> vals = generateLabelsList(labelPrefix, numOfStates);
        String[] valsArray = vals.toArray(new String[0]);
        List<String> invertedVals = Lists.reverse(vals);
        String[] invertedValsArray = invertedVals.toArray(new String[0]);
        final String[][] checkedVals = new String[numOfCompts][numOfStates];

        IntStream.range(0, numOfCompts)
                .filter(i -> i % 2 == 0)
                .boxed()
                .forEach(i -> checkedVals[i] = valsArray);

        IntStream.range(0, numOfCompts)
                .filter(i -> i % 2 != 0)
                .boxed()
                .forEach(i -> checkedVals[i] = invertedValsArray);

        return checkedVals;
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_PacketState.xls")
    @Test
    public void testUpdatePacketState_positive() throws Exception {
        long packetId = 1L;
        long newStateId = 2L;

        Long expectedModifiedPacketId = packetId;
        long expectedNewStateId = newStateId;

        Long result = comptService.updatePacketState(packetId, newStateId);
        em.flush();

        TypedQuery<Packet> packetTypedQuery = em.createNamedQuery("Packet.getPacketWithStateId", Packet.class);
        packetTypedQuery.setParameter("id", expectedModifiedPacketId);
        packetTypedQuery.setHint("javax.persistence.fetchgraph", em.getEntityGraph("acket.getGraphWithStateId"));
        Packet updatedPacket = packetTypedQuery.getSingleResult();

        assertEquals(expectedModifiedPacketId, result);
        assertNotNull(updatedPacket);
        assertNotNull(updatedPacket.getState());
        assertEquals(expectedNewStateId, updatedPacket.getState().getId());
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_PacketState.xls")
    @Test
    public void testUpdatePacketState_negative_nonExistingPacketId() throws Exception {
        long packetId = 2L;
        long newStateId = 1L;

        Long expectedUpdatedPacketId = packetId;

        Long result = comptService.updatePacketState(packetId, newStateId);
        em.flush();

        Packet expectedUpdatedPacket = em.find(Packet.class, expectedUpdatedPacketId);

        assertNull(expectedUpdatedPacket);
        assertNull(result);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_PacketState.xls")
    @Test
    public void testUpdatePacketState_negative_nonExistingStateId() throws Exception {
        long packetId = 1L;
        long newStateId = 4L;

        ascertainPacketNotChanged(packetId, newStateId, packetId);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceTest_PacketState.xls")
    @Test
    public void testUpdatePacketState_negative_notDifferentStateId() throws Exception {
        long packetId = 1L;
        long newStateId = 1L;

        ascertainPacketNotChanged(packetId, newStateId, packetId);
    }

    private void ascertainPacketNotChanged(long packetId, long newStateId, long expectedUpdatedPacketId) {
        Packet packetBeforeExpectedUpdate = em.find(Packet.class, expectedUpdatedPacketId);

        Long result = comptService.updatePacketState(packetId, newStateId);
        em.flush();

        Packet packetAfterExpectedUpdate = em.find(Packet.class, expectedUpdatedPacketId);

        assertEquals(packetAfterExpectedUpdate.getVersion(), packetBeforeExpectedUpdate.getVersion());
        assertNull(result);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceImplTest_Add_Compts.xls")
    @Test
    public void testAddCompts_positive() throws Exception {
        final int numOfComptsToAdd = 2;
        final long packetId = 1L;
        final String comboDataLabelPrefix = "combo_label_";
        final String comptLabelPrefix = "compt_label_";
        final int numOfStates = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAdd);
        final String[] labelsForAdding
                = generateLabelsList(comptLabelPrefix, numOfComptsToAdd).toArray(new String[0]);

        final int expectedResultSize = numOfComptsToAdd;
        final String[][] expectedCheckedVals = checkedValsForAdding;
        final int expectedNumOfStates = 3;
        final int expectedNumOfComboDataItemsPerState = 3;
        final int expectedNumOfComboDataItemsPerCompt = expectedNumOfStates * expectedNumOfComboDataItemsPerState;
        final int[][][] checkedComboDataArray = new int[numOfComptsToAdd][expectedNumOfStates][expectedNumOfComboDataItemsPerState];
        final int numOfCheckedComboDataPerCompt = 1;

        final List<ComptsParams> paramsListForAdding = generateParamsList(OperationType.ADD,
                checkedValsForAdding, null,
                numOfComptsToAdd, labelsForAdding);

        comptService.addCompts(packetId, paramsListForAdding);
        em.flush();

        checkComptWithDependencies(OperationType.ADD, expectedResultSize, expectedNumOfComboDataItemsPerCompt,
                expectedCheckedVals, checkedComboDataArray, numOfCheckedComboDataPerCompt, expectedNumOfStates,
                null, labelsForAdding);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceImplTest_Add_Compts.xls")
    @Test
    public void testAddCompts_negative() throws Exception {
        final int numOfComptsToAdd = 2;
        final long packetId = 2L;
        final String comboDataLabelPrefix = "combo_label_";
        final String comptLabelPrefix = "compt_label_";
        final int numOfStates = 3;
        final String[][] checkedValsForAdding
                = generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToAdd);
        final String[] labelsForAdding
                = generateLabelsList(comptLabelPrefix, numOfComptsToAdd).toArray(new String[0]);

        final List<ComptsParams> paramsListForAdding = new ArrayList<>();
        IntStream.range(0, numOfComptsToAdd).boxed()
                .forEach(i -> paramsListForAdding.add(new ComptsParams(Arrays.asList(checkedValsForAdding[i]),
                        labelsForAdding[i])));

        long expectedPacketId = packetId;
        long expectedResultSize = 0L;
        List<Long> result = comptService.addCompts(packetId, paramsListForAdding);
        em.flush();

        Packet expectedPacket = em.find(Packet.class, expectedPacketId);

        assertEquals(expectedResultSize, result.size());
        assertNull(expectedPacket);
    }

    private void checkComptWithDependencies(OperationType operationType, int expectedResultSize,
                                            int expectedNumOfComboDataItemsPerCompt,
                                            String[][] expectedCheckedVals, int[][][] checkedComboDataArray,
                                            int numOfCheckedComboDataPerCompt, int expectedNumOfStates,
                                            long[] expectedUpdatedIds, String[] labelsForAdding) {

        TypedQuery<Compt> comptTypedQuery = em.createNamedQuery("Compt.findAllWithDataCompts", Compt.class);
        comptTypedQuery.setHint("javax.persistence.fetchgraph", em.getEntityGraph("Compt.getGraphWithDataCompts"));
        Compt[] result = comptTypedQuery.getResultList().toArray(new Compt[0]);

        assertEquals(expectedResultSize, result.length);
        for (int i = 0; i < expectedResultSize; i++) {
            if (operationType == OperationType.ADD) {
                assertEquals(labelsForAdding[i], result[i].getPacket().getId());
            } else if (operationType == OperationType.UPDATE) {
                assertEquals(expectedUpdatedIds[i], result[i].getId());
            }
            for (int j = 0; j < expectedNumOfComboDataItemsPerCompt; j++) {
                int stateIndex = (int) result[i].getDataCompts().get(j).getState().getId();
                int comboDataIndex = (int) result[i].getDataCompts().get(j).getComboData().getId();
                boolean checked = result[i].getDataCompts().get(j).getChecked();
                String label = result[i].getDataCompts().get(j).getComboData().getLabel();

                assert (checked ^ (label.equals(expectedCheckedVals[i][stateIndex])));
                checkedComboDataArray[i][stateIndex][comboDataIndex] = checked ? 1 : 0;
            }
        }
        for (int i = 0; i < expectedResultSize; i++) {
            for (int j = 0; j < expectedNumOfStates; j++) {
                assertEquals(numOfCheckedComboDataPerCompt,
                        Arrays.stream(checkedComboDataArray[i][j]).boxed().filter(ch -> ch == 1).count());
            }
        }
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceImplTest_Remove_Compts.xls")
    @Test
    public void testRemoveCompts_positive() throws Exception {
        final int firstComptId = 1;
        final int numOfComptIds = 2;
        List<Long> comptIdsToRemove = Arrays.asList(ArrayUtils.toObject(generateIds(firstComptId, numOfComptIds)));

        List<Long> expectedResult = comptIdsToRemove;
        int expectedNumOfComptsAfterRemoval = 0;

        checkComptsAfterRemoval(RemoveComptsTestCase.NEGATIVE, comptIdsToRemove,
                expectedResult, null, expectedNumOfComptsAfterRemoval, null);
    }

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceImplTest_Remove_Compts.xls")
    @Test
    public void testRemoveCompts_negative() throws Exception {
        List<Long> comptIdsToRemove = Arrays.asList(3L);

        final int expectedResultSize = 0;
        final int expectedNumOfComptsAfterRemoval = 2;
        final int firstComptId = 1;

        List<Long> expectedComptIdsAfterRemoval =
                Arrays.asList(ArrayUtils.toObject(generateIds(firstComptId, expectedNumOfComptsAfterRemoval)));


        checkComptsAfterRemoval(RemoveComptsTestCase.NEGATIVE, comptIdsToRemove,
                null, expectedResultSize, expectedNumOfComptsAfterRemoval,
                expectedComptIdsAfterRemoval);
    }

    private void checkComptsAfterRemoval(RemoveComptsTestCase testCase, List<Long> comptIdsToRemove,
                                         List<Long> expectedResult, Integer expectedResultSize,
                                         int expectedNumOfComptsAfterRemoval,
                                         List<Long> expectedComptIdsAfterRemoval) {

        List<Long> result = comptService.removeCompts(comptIdsToRemove);

        List<Compt> comptsAfterRemoval = Lists.newArrayList(comptRepository.findAll());

        if (testCase == RemoveComptsTestCase.NEGATIVE) {
            assertEquals((long) expectedResultSize, result.size());
            assertEquals(comptsAfterRemoval
                    .stream()
                    .map(Compt::getId)
                    .allMatch(expectedComptIdsAfterRemoval::contains), true);
        } else if (testCase == RemoveComptsTestCase.POSITIVE) {
            assertEquals(expectedResult, result);
        }
        assertEquals(expectedNumOfComptsAfterRemoval, comptsAfterRemoval.size());
    }
}
