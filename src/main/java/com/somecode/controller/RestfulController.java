/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.controller;

import com.google.gson.Gson;
import com.somecode.domain.DataParams;
import com.somecode.domain.RequestObj;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.somecode.utils.Utils.getMessage;

/**
 * The Restful Controller mapping the Angular UI http requests to the Java backend.
 * @version 1.0
 */

@Controller
@PropertySource(value = "classpath:messages.properties")
@Log4j
public class RestfulController {
    /**
     * Mapping values
     */
    private final static String HOME_MAPPING = "/home";
    private final static String HOME_MAPPING_FILE = "/resources/admin.jsp";
    private final static String USER_LOGIN_MAPPING = "/users/login";
    private final static String LOAD_DATA_MAPPING = "/loadData";
    private final static String SAVE_ALL_CHANGES_MAPPING = "/saveAllChangesToBase";

    /**
     * Resources messages
     */
    private final static String LOAD_DATA_FOR_ALL_PACKETS = "restful.loadDataForAllPackets";
    private final static String LOAD_DATA_FOR_SPECIFIC_PACKET = "restful.loadDataForSpecificPacket";
    private final static String SAVE_ALL_CHANGES_TO_BASE_FOR_SPECIFIC_PACKET
            = "restful.saveAllChangesToBaseForSpecificPacket";
    private final static String SAVE_ALL_CHANGES_TO_BASE_FOR_ALL_PACKETS
            = "restful.saveAllChangesToBaseForAllPackets";

    /**
     * GSON instance for JSON transformations
     */
    private static Gson GSON = new Gson();

    /** Injected service */
    @Autowired
    private PacketAppService packetAppService;

    /**
     * The home mapping method.
     *
     * @return the home mapping filename.
     */
    @RequestMapping({HOME_MAPPING, ""})
    public String home(){
        return HOME_MAPPING_FILE;
    }

    /**
     * The get User Role mapping method.
     *
     * @param json the JSON transformation of the http front-end request.
     * @return the JSON transformation of the service's
     *         {@link com.somecode.controller.PacketAppService#getUserRole(String, String)} method.
     */
    @RequestMapping(value = USER_LOGIN_MAPPING, method = RequestMethod.POST)
    @ResponseBody
    public String getUserRole(@RequestBody String json) {
        RequestObj requestObj = GSON.fromJson(json, RequestObj.class);
        return GSON.toJson(packetAppService.getUserRole(requestObj.getSecurityParams().getUsername(),
                requestObj.getSecurityParams().getPassword()));
    }

    /**
     * Loads and returns the json transformation of the bulk data needed for the front-end.
     *
     * @param json the JSON transformation of the http front-end request.
     * @return the JSON bulk data transformation.
     */
    @RequestMapping(value = LOAD_DATA_MAPPING, method = RequestMethod.POST)
    @ResponseBody
    public String loadData(@RequestBody String json) {
        RequestObj requestObj = GSON.fromJson(json, RequestObj.class);
        Long packetId = requestObj.getDataParams().getPacketId();
        log.debug(packetId == null
                ? getMessage(LOAD_DATA_FOR_ALL_PACKETS, null)
                : getMessage(LOAD_DATA_FOR_SPECIFIC_PACKET, new Object[]{packetId})
        );
        return GSON.toJson(packetAppService.loadData(packetId));
    }

    /**
     * Saves all changes from the front-end and returns the json transformation of the saving error report.
     *
     * @param json the JSON transformation of the http front-end request.
     * @return the json transformation of the saving error report
     */
    @RequestMapping(value = SAVE_ALL_CHANGES_MAPPING, method = RequestMethod.POST)
    public String saveAllChangesToBase(@RequestBody String json) {
        RequestObj requestObj = GSON.fromJson(json, RequestObj.class);
        DataParams params = requestObj.getDataParams();
        Long packetId = params.getPacketId();
        if (packetId != null) {
            log.debug(getMessage(SAVE_ALL_CHANGES_TO_BASE_FOR_SPECIFIC_PACKET, new Object[] {packetId}));
        } else {
            log.debug(getMessage(SAVE_ALL_CHANGES_TO_BASE_FOR_ALL_PACKETS, null));
        }
        return GSON.toJson(packetAppService.saveAllChangesToBase(
                params.getComptIdsToDelete(),
                params.getPacketIdsToDelete(),
                params.getComptsToUpdateParamsList(),
                params.getPacketsToAddParamsList(),
                params.getPacketsToUpdateParamsList(),
                params.getPacketId()
        ));
    }
}

