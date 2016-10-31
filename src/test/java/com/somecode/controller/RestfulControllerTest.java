package com.somecode.controller;

import com.google.gson.Gson;
import com.somecode.dao.DaoTestConfig;
import com.somecode.domain.*;
import com.somecode.service.PacketAppService;
import com.somecode.utils.TestUtils;
import com.somecode.utils.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by alexc_000 on 2016-10-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@WebAppConfiguration
@ActiveProfiles("test")
public class RestfulControllerTest {

    private static final String SAVE_ALL_CHANGES_MAPPING = "SAVE_ALL_CHANGES_MAPPING";
    private static final String SAVE_ALL_CHANGES_TO_BASE_FOR_ALL_PACKETS = "SAVE_ALL_CHANGES_TO_BASE_FOR_ALL_PACKETS";
    private static final String SAVE_ALL_CHANGES_TO_BASE_FOR_SPECIFIC_PACKET
            = "SAVE_ALL_CHANGES_TO_BASE_FOR_SPECIFIC_PACKET";
    private static final String LOAD_DATA_FOR_ALL_PACKETS = "LOAD_DATA_FOR_ALL_PACKETS";
    private static final String LOAD_DATA_FOR_SPECIFIC_PACKET = "LOAD_DATA_FOR_SPECIFIC_PACKET";
    private static final String LOAD_DATA_MAPPING = "LOAD_DATA_MAPPING";
    private static final String ADD_PACKETS = "ADD_PACKETS";
    private static final String USER_LOGIN_MAPPING = "USER_LOGIN_MAPPING";
    private static final String HOME_MAPPING = "HOME_MAPPING";
    private static final String HOME_MAPPING_FILE = "HOME_MAPPING_FILE";
    private static final String USER_USERNAME = "USER_USERNAME";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String ADMIN_USERNAME = "ADMIN_USERNAME";
    private static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    private static final String PACKET_APP_SERVICE = "packetAppService";

    private Gson GSON = new Gson();

    @Autowired
    private RestfulController controller;

    private PacketAppService serviceMock;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private Logger root = Logger.getRootLogger();
    private TestUtils.TestAppender testAppender;

    @Before
    public void beforeEach() {
        mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .build();

        serviceMock = mock(PacketAppService.class);
        ReflectionTestUtils.setField(controller, PACKET_APP_SERVICE, serviceMock);

        testAppender = TestUtils.getTestAppender();
        root.addAppender(testAppender);
        root.setLevel(Level.DEBUG);
    }

    @Test
    public void testHomeMethodForEmptyHomeMapping () throws Exception {
        testHomeMethod(true);
    }

    @Test
    public void testHomeMethodForNotEmptyHomeMapping () throws Exception {
        testHomeMethod(false);
    }

    @Test
    public void testSaveAllChangesToBaseForAllPackets() throws Exception {
        testSaveAllChangesToBase(null);
    }

    @Test
    public void testSaveAllChangesToBaseForSpecificPacket() throws Exception {
        testSaveAllChangesToBase(1L);
    }

    @Test
    public void testLoadAllChangesFromBaseForAllPackets() throws Exception {
        testLoadAllChangesFromBase(null);
    }

    @Test
    public void testLoadAllChangesFromBaseForSpecificPacket() throws Exception {
        testLoadAllChangesFromBase(1L);
    }

    @Test
    public void testGetUserRole_forSpecificUsernameAndPassword() throws Exception {
        String adminTestUserName = ADMIN_USERNAME;
        String adminTestPassword = ADMIN_PASSWORD;

        Role adminServiceMockReturnRole = Role.ADMIN;

        getUserRoleTestSetUp(adminTestUserName, adminTestPassword, adminServiceMockReturnRole);

        testControllerGetUserRoleMethod(adminTestUserName, adminTestPassword, adminServiceMockReturnRole);

    }

    @Test
    public void testGetUserRole_forAnyUsernameAndPassword() throws Exception {
        Role userServiceMockReturnRole = Role.USER;

        String userTestUserName = USER_USERNAME;
        String userTestPassword = USER_PASSWORD;

        getUserRoleTestSetUp(null, null, userServiceMockReturnRole);

        testControllerGetUserRoleMethod(userTestUserName, userTestPassword, userServiceMockReturnRole);
    }

    private void testHomeMethod(boolean emptyHomeMapping) throws Exception {
        MockHttpServletRequestBuilder request;
        if (emptyHomeMapping) {
            request = MockMvcRequestBuilders.get("");
        } else {
            final String requestURI = (String) ReflectionTestUtils.getField(controller, HOME_MAPPING);
            request = MockMvcRequestBuilders.get(requestURI);
        }
        ResultActions result = mockMvc.perform(request);
        final String view = (String) ReflectionTestUtils.getField(controller, HOME_MAPPING_FILE);
        result.andExpect(MockMvcResultMatchers.view().name(view));
    }

    private void getUserRoleTestSetUp(String testUserName, String testPassword,  Role serviceMockReturnRole) {
        doReturn(serviceMockReturnRole).when(serviceMock).getUserRole(testUserName != null ? testUserName : anyString(),
                                                                      testPassword != null ? testPassword : anyString());
    }

    private void testControllerGetUserRoleMethod(String testUserName, String testPassword, Role serviceMockReturnRole)
                                                                                                       throws Exception{
        SecurityParams securityParams = new SecurityParams();
        securityParams.setUsername(testUserName);
        securityParams.setPassword(testPassword);

        RequestObj requestObj = new RequestObj();
        requestObj.setSecurityParams(securityParams);

        String requestObjJson = GSON.toJson(requestObj);

        final String requestURI = (String) ReflectionTestUtils.getField(controller, USER_LOGIN_MAPPING);
        MvcResult mvcResult  = mockMvc.perform(post(requestURI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestObjJson))
                .andExpect(status().isOk())
                .andReturn();

        String specialResponseObjJson = GSON.toJson(serviceMockReturnRole);
        assertEquals(specialResponseObjJson, mvcResult.getResponse().getContentAsString());
        verify(serviceMock, times(1)).getUserRole(anyString(), anyString());

        String result = controller.getUserRole(requestObjJson);
        Role fromJsonControllerReturnVal = GSON.fromJson(result, Role.class);
        assertEquals(serviceMockReturnRole, fromJsonControllerReturnVal);
    }

    private void testSaveAllChangesToBase(Long testPacketId) throws Exception {
        List<Long> testComptIdsToDelete = new ArrayList<>();
        IntStream.rangeClosed(3, 3).boxed().forEach(id -> testComptIdsToDelete.add(id.longValue()));

        List<Long> testPacketIdsToDelete = new ArrayList<>();
        if (testPacketId == null) {
            IntStream.rangeClosed(1, 2).boxed().forEach(id -> testPacketIdsToDelete.add(id.longValue()));
        }

        List<ComptParams> testComptsToUpdateParamsList = new ArrayList<>();
        IntStream.rangeClosed(1, 2).boxed().forEach(id ->
                testComptsToUpdateParamsList.add(new ComptParams().setId(Long.valueOf(id))));

        List<PacketParams> testPacketsToAddParamsList = new ArrayList<>();
        if (testPacketId == null) {
            IntStream.rangeClosed(3, 4).boxed().forEach(id ->
                    testPacketsToAddParamsList.add(new PacketParams().setId(Long.valueOf(id))));
        }

        List<PacketParams> testPacketsToUpdateParamsList = new ArrayList<>();
        int upperPacketToUpdateId = 1;
        if (testPacketId == null) {
            upperPacketToUpdateId = 2;
        }
        IntStream.rangeClosed(1, upperPacketToUpdateId).boxed().forEach(id ->
                testPacketsToUpdateParamsList.add(new PacketParams().setId(Long.valueOf(id))));

        Map<String, Boolean> resultMap = new HashMap<>();
        String serviceMockMethodForNotNullPacketId = ADD_PACKETS;
        if (testPacketId != null) {
            resultMap.put(serviceMockMethodForNotNullPacketId, true);
        }
        doReturn(resultMap).when(serviceMock).saveAllChangesToBase(anyListOf(Long.class),
                anyListOf(Long.class), anyListOf(ComptParams.class), anyListOf(PacketParams.class),
                anyListOf(PacketParams.class), any(Long.class));

        DataParams dataParams = new DataParams();
        dataParams.setComptIdsToDelete(testComptIdsToDelete);
        dataParams.setComptsToUpdateParamsList(testComptsToUpdateParamsList);
        dataParams.setPacketId(testPacketId);
        dataParams.setPacketIdsToDelete(testPacketIdsToDelete);
        dataParams.setPacketsToAddParamsList(testPacketsToAddParamsList);
        dataParams.setPacketsToUpdateParamsList(testPacketsToUpdateParamsList);
        RequestObj requestObj = new RequestObj();
        requestObj.setDataParams(dataParams);

        String requestObjJson = GSON.toJson(requestObj);
        final String requestURI = (String) ReflectionTestUtils.getField(controller, SAVE_ALL_CHANGES_MAPPING);
        MvcResult mvcResult
                = mockMvc.perform(post(requestURI).contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(requestObjJson)).andExpect(status().isOk()).andReturn();
        
        String responseObjJson = GSON.toJson(Collections.emptyMap());
        assertEquals(responseObjJson.substring(1, responseObjJson.length()-1),
                     mvcResult.getResponse().getContentAsString()
        );

        verify(serviceMock, times(1)).saveAllChangesToBase(anyListOf(Long.TYPE), anyListOf(Long.TYPE),
                anyListOf(ComptParams.class), anyListOf(PacketParams.class), anyListOf(PacketParams.class), anyLong());

        int testLogSize = 1;

        assertEquals(testLogSize, testAppender.getLog().size());

        final LoggingEvent loggingEvent = testAppender.getLog().get(0);
        final String testSaveAllChangesToBase
                = (String) ReflectionTestUtils.getField(controller, testPacketId == null
                                                                        ? SAVE_ALL_CHANGES_TO_BASE_FOR_ALL_PACKETS
                                                                        : SAVE_ALL_CHANGES_TO_BASE_FOR_SPECIFIC_PACKET);
        assertEquals(Utils.getMessage(testSaveAllChangesToBase,
                                      new Object[] {testPacketId}), loggingEvent.getMessage());
        assertEquals(Level.DEBUG, loggingEvent.getLevel());

        String result = controller.saveAllChangesToBase(requestObjJson);
        Map<String, Boolean> fromJsonData = GSON.fromJson(result, Map.class);
        assertEquals(fromJsonData.size(), resultMap.size());
        if (testPacketId != null) {
            assertTrue(fromJsonData.get(serviceMockMethodForNotNullPacketId));
        }
    }

    private void testLoadAllChangesFromBase(Long testPacketId) throws Exception {
        List<State> testStates = new ArrayList<>();
        for (long id = 1L; id < 4L; id++) {
            State state = new State();
            state.setId(id);
            testStates.add(state);
        }

        List<ComboData> testComboDatas = new ArrayList<>();
        for (long id = 1L; id<4L; id++) {
            ComboData cd = new ComboData();
            cd.setId(id);
            testComboDatas.add(cd);
        }
        
        List<PacketInfo> testPackets = new ArrayList<>();
        PacketInfo packet = new PacketInfo();
        packet.setId(1L);
        testPackets.add(packet);

        List<ComptInfo> testCompts = new ArrayList<>();
        if (testPacketId == null){
            for (long id = 1L; id < 3L; id++) {
                ComptInfo compt = new ComptInfo();
                compt.setId(id);
                testCompts.add(compt);
            }
        }
        
        List<ComptSupplInfo> testComptSupplInfos = new ArrayList<>();
        if (testPacketId == null) {
            for (long id = 1L; id < 19L; id++) {
                ComptSupplInfo comptSupplInfo = new ComptSupplInfo();
                comptSupplInfo.setId(id);
                testComptSupplInfos.add(comptSupplInfo);
            }
        }
        
        Data data = new Data();
        data.setComboData(testComboDatas).setStates(testStates).setPackets(testPackets).setCompts(testCompts)
                .setComptSupplInfo(testComptSupplInfos);

        doReturn(data).when(serviceMock).loadData(any(Long.class));

        DataParams dataParams = new DataParams();
        if(testPacketId != null) {
            dataParams.setPacketId(testPacketId);
        }
        RequestObj requestObj = new RequestObj();
        requestObj.setDataParams(dataParams);
        String requestObjJson = GSON.toJson(requestObj);
        final String requestURI = (String) ReflectionTestUtils.getField(controller, LOAD_DATA_MAPPING);
        mockMvc.perform(post(requestURI).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestObjJson))
                .andExpect(status().isOk());
        
        verify(serviceMock, times(1)).loadData(anyLong());

        int testLogSize = 1;

        assertEquals(testLogSize, testAppender.getLog().size());

        final LoggingEvent loggingEvent = testAppender.getLog().get(0);
        final String testSaveAllChangesToBase
                = (String) ReflectionTestUtils.getField(controller, testPacketId == null
                                                                                ? LOAD_DATA_FOR_ALL_PACKETS
                                                                                : LOAD_DATA_FOR_SPECIFIC_PACKET);
        assertEquals(Utils.getMessage(testSaveAllChangesToBase,
                new Object[] {testPacketId}), loggingEvent.getMessage());
        assertEquals(Level.DEBUG, loggingEvent.getLevel());

        String result = controller.loadData(requestObjJson);
        Data fromJsonData = GSON.fromJson(result, Data.class);
        assertEquals(fromJsonData.getComboData().size(), testComboDatas.size());
        assertEquals(fromJsonData.getStates().size(), testStates.size());
        assertEquals(fromJsonData.getCompts().size(), testCompts.size());
        assertEquals(fromJsonData.getPackets().size(), testPackets.size());
        assertEquals(fromJsonData.getComptSupplInfo().size(), testComptSupplInfos.size());
    }
}
