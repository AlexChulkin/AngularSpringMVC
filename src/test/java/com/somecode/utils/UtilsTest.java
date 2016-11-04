/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.utils;

import com.somecode.dao.DaoTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.somecode.utils.Utils.getMessage;
import static org.junit.Assert.assertEquals;

/**
 * The fairly self-explanatory {@link Utils} test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@WebAppConfiguration
@ActiveProfiles("test")
public class UtilsTest {
    /**
     * The constants used in testing
     */
    private static final Long COMPT_ID = 1L;
    private static final Long DATA_COMPT_ID = 5L;
    private static final Long STATE_ID = 2L;
    private static final Long COMBO_DATA_ID = 4L;
    private static final Long PACKET_ID = 3L;
    private static final String COMPT_LABEL = "COMPT_LABEL_1";
    private static final boolean CHECKED = true;

    /**
     * The expected parametrized message source constants
     */
    private static final String LOAD_DATA_FOR_ALL_PACKETS_PATTERN = "packetAppService.loadDataForAllPackets";
    private static final String LOAD_DATA_FOR_ALL_PACKETS_FULL_MSG = "Load data for all packets";
    private static final String COMPT_UPDATE_NON_EXISTING_COMPT_PATTERN = "packetAppDao.comptUpdate.nonExistingCompt";
    private static final String COMPT_UPDATE_NON_EXISTING_COMPT_FULL_MSG = "The compt with id = 1 does not exist";
    private static final String COMPT_UPDATE_DATACOMPT_UPDATE_PATTERN
            = "packetAppDao.comptUpdate.dataComptUpdate.successReport";
    private static final String COMPT_UPDATE_DATACOMPT_UPDATE_FULL_MSG
            = "Compt#1 update: DataCompt updated widh id = 5";
    private final static String COMPT_TO_STRING_PATTERN = "compt.toString";
    private final static String COMPT_TO_STRING_FULL_MSG = "Component with id: 1 and label: COMPT_LABEL_1 and packet#3";
    private static final String DATA_COMPT_TO_STRING_PATTERN = "dataCompt.toString";
    private static final String DATA_COMPT_TO_STRING_FULL_MSG
            = "Data Component #5 and checked flag: true and component#1 and state#2 and combo data#4";

    @Test
    public void testGetMessageWithNoElements() {
        assertEquals(LOAD_DATA_FOR_ALL_PACKETS_FULL_MSG, getMessage(LOAD_DATA_FOR_ALL_PACKETS_PATTERN, null));
    }

    @Test
    public void testGetMessageWithOneElement() {
        assertEquals(COMPT_UPDATE_NON_EXISTING_COMPT_FULL_MSG,
                     getMessage(COMPT_UPDATE_NON_EXISTING_COMPT_PATTERN, new Object[]{COMPT_ID}));
    }

    @Test
    public void testGetMessageWithTwoElements() {
        assertEquals(COMPT_UPDATE_DATACOMPT_UPDATE_FULL_MSG,
                     getMessage(COMPT_UPDATE_DATACOMPT_UPDATE_PATTERN, new Object[]{COMPT_ID, DATA_COMPT_ID}));
    }

    @Test
    public void testGetMessageWithThreeElements() {
        assertEquals(COMPT_TO_STRING_FULL_MSG,
                getMessage(COMPT_TO_STRING_PATTERN, new Object[]{COMPT_ID, COMPT_LABEL, PACKET_ID}));

    }

    @Test
    public void testGetMessageWithFourOrMoreElements() {
        assertEquals(DATA_COMPT_TO_STRING_FULL_MSG,
                getMessage(DATA_COMPT_TO_STRING_PATTERN,
                           new Object[]{DATA_COMPT_ID, CHECKED, COMPT_ID, STATE_ID, COMBO_DATA_ID}));

    }
}
