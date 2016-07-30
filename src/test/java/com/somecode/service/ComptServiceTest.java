package com.somecode.service;

import com.google.common.collect.Lists;
import com.somecode.dao.ComptRepository;
import com.somecode.domain.*;
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
        final String expectedLabel = "compt_label";
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

        final int expectedNumOfStates = 3;
        final int expectedNumOfComboData = 3;
        final int expectedNumOfCompts = 1;
        final int expectedResultLength = calculateExpectedResultLength(expectedNumOfCompts, expectedNumOfStates,
                expectedNumOfComboData);

        findComboData(ComboDataGetTestCase.COMPTS_SUPPL_INFO, packetId, expectedResultLength);
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

        final int expectedNumOfStates = 3;
        final int expectedNumOfComboData = 3;
        final int expectedNumOfCompts = 1;
        final int expectedResultLength = calculateExpectedResultLength(expectedNumOfCompts, expectedNumOfStates,
                expectedNumOfComboData);

        findComboData(ComboDataGetTestCase.COMBO_DATA_PROPERLY, null, expectedResultLength);
    }

    private void findComboData(ComboDataGetTestCase testCase, final Long packetId,
                               final int expectedResultLength) throws Exception {
        final String comboDataLabelPrefix = "combo_label_";

        final List<String> expectedComboDataLabels = generateLabelsList(comboDataLabelPrefix, expectedResultLength);

        List<HasLabel> result = new ArrayList<>();

        if (testCase == ComboDataGetTestCase.COMPTS_SUPPL_INFO) {
            comptService.getComptsSupplInfo(packetId).forEach(c -> result.add(c));
        } else if (testCase == ComboDataGetTestCase.COMBO_DATA_PROPERLY) {
            comptService.getDefaultComboData().forEach(c -> result.add(c));
        }

        assertEquals(expectedResultLength, result.size());
        IntStream.range(0, expectedResultLength)
                .boxed()
                .forEach(i ->
                        assertEquals(expectedComboDataLabels.get(i), result.get(i).getLabel())
                );
    }

    private List<String> generateLabelsList(String labelPrefix, int numOfLabels) {
        final List<String> expectedComboDataLabels = new ArrayList<>();
        IntStream.rangeClosed(1, numOfLabels)
                .boxed()
                .forEach(i -> expectedComboDataLabels.add(labelPrefix + i.toString()));
        return expectedComboDataLabels;
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

    @DataSets(setUpDataSet = "/com/somecode/service/ComptServiceImplTest.xls")
    @Test
    public void testUpdateCompts_positive() throws Exception {
        final int numOfComptsToUpdate = 2;
        final String comboDataLabelPrefix = "combo_label_";
        final int numOfStates = 3;

        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);

        final int firstIdToUpdate = 1;
        final long[] idsForUpdate = generateIds(firstIdToUpdate, numOfComptsToUpdate);

        final List<ComptsParams> paramsListForUpdate = new ArrayList<>();
        IntStream.range(0, numOfComptsToUpdate).boxed()
                .forEach(i -> paramsListForUpdate.add(new ComptsParams(Arrays.asList(checkedValsForUpdate[i]),
                        idsForUpdate[i])));


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

    @Test
    public void testUpdateCompts_negative() throws Exception {
        final int numOfComptsToUpdate = 1;
        final String comboDataLabelPrefix = "combo_label_";
        final int numOfStates = 3;

        final String[][] checkedValsForUpdate =
                generateMultipleLabelsLists(comboDataLabelPrefix, numOfStates, numOfComptsToUpdate);

        final int firstIdToUpdate = 3;
        final long[] idsForUpdate = generateIds(firstIdToUpdate, numOfComptsToUpdate);

        final List<ComptsParams> paramsListForUpdate = new ArrayList<>();
        IntStream.range(0, numOfComptsToUpdate).boxed()
                .forEach(i -> paramsListForUpdate.add(new ComptsParams(Arrays.asList(checkedValsForUpdate[i]),
                        idsForUpdate[i])));

        long expectedUpdatedComptId = idsForUpdate[0];
        int expectedResultSize = 0;

        List<Long> result = comptService.updateCompts(paramsListForUpdate);
        em.flush();

        assertEquals(expectedResultSize, result.size());
        Compt expectedUpdatedCompt = em.find(Compt.class, expectedUpdatedComptId);
        assertNull(expectedUpdatedCompt);
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

    @Test
    public void testUpdatePacketState_negative_nonExistingStateId() throws Exception {
        long packetId = 1L;
        long newStateId = 4L;

        ascertainPacketNotChanged(packetId, newStateId, packetId);
    }

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

    @Test
    public void testAddCompts_positive() throws Exception {
        final int numOfComptsToAdd = 2;
        final int packetId = 1;
        final String[][] checkedValsForAdding = {{"combo-value1", "combo-value1", "combo-value1"},
                {"combo-value2", "combo-value2", "combo-value2"}};
        final String[] labelsForAdding = {"label1", "label2"};

        final List<ComptsParams> paramsListForAdding = new ArrayList<>();
        IntStream.range(0, numOfComptsToAdd).boxed()
                .forEach(i -> paramsListForAdding.add(new ComptsParams(Arrays.asList(checkedValsForAdding[i]), labelsForAdding[i])));

        final int expectedResultSize = numOfComptsToAdd;
        final String[][] expectedCheckedVals = checkedValsForAdding;
        final int expectedNumOfStates = 3;
        final int expectedNumOfComboDataItemsPerState = 3;
        final int expectedNumOfComboDataItemsPerCompt = expectedNumOfStates * expectedNumOfComboDataItemsPerState;
        final int[][][] checkedComboDataArray = new int[numOfComptsToAdd][expectedNumOfStates][expectedNumOfComboDataItemsPerState];
        final int numOfCheckedComboDataPerCompt = 1;

        comptService.addCompts(packetId, paramsListForAdding);
        em.flush();

        checkComptWithDependencies(OperationType.ADD, expectedResultSize, expectedNumOfComboDataItemsPerCompt,
                expectedCheckedVals, checkedComboDataArray, numOfCheckedComboDataPerCompt, expectedNumOfStates,
                null, labelsForAdding);
    }

    @Test
    public void testAddCompts_negative() throws Exception {
        final int numOfComptsToAdd = 2;
        final long packetId = 2L;
        final String[][] checkedValsForAdding = {{"combo-value1", "combo-value1", "combo-value1"},
                {"combo-value2", "combo-value2", "combo-value2"}};
        final String[] labelsForAdding = {"label1", "label2"};

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

    @Test
    public void testRemoveCompts_positive() throws Exception {
        List<Long> comptIdsToRemove = Arrays.asList(1L, 2L);

        List<Long> expectedResult = comptIdsToRemove;
        int expectedNumOfComptsAfterRemoval = 0;

        checkComptsAfterRemoval(RemoveComptsTestCase.NEGATIVE, comptIdsToRemove,
                expectedResult, null, expectedNumOfComptsAfterRemoval, null);
    }

    @Test
    public void testRemoveCompts_negative() throws Exception {
        List<Long> comptIdsToRemove = Arrays.asList(3L);

        int expectedResultSize = 0;
        int expectedNumOfComptsAfterRemoval = 2;
        List<Long> expectedComptIdsAfterRemoval = Arrays.asList(1L, 2L);

        checkComptsAfterRemoval(RemoveComptsTestCase.NEGATIVE, comptIdsToRemove,
                null, expectedResultSize, expectedNumOfComptsAfterRemoval,
                expectedComptIdsAfterRemoval);
    }

    private void checkComptsAfterRemoval(RemoveComptsTestCase testCase, List<Long> comptIdsToRemove,
                                         List<Long> expectedResult, Integer expectedResultSize,
                                         int expectedNumOfComptsAfterRemoval, List<Long> expectedComptIdsAfterRemoval) {

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
