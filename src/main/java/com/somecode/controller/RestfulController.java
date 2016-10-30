package com.somecode.controller;

import com.google.gson.Gson;
import com.somecode.domain.DataParams;
import com.somecode.domain.RequestObj;
import com.somecode.service.PacketAppService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.somecode.utils.Utils.getMessage;


@Controller
@PropertySource(value = "classpath:messages.properties")
@Log4j
public class RestfulController {
    private final static String HOME_MAPPING = "/home";
    private final static String HOME_MAPPING_FILE = "/resources/admin.jsp";
    private final static String USER_LOGIN_MAPPING = "/users/login";
    private final static String LOAD_DATA_MAPPING = "/loadData";
    private final static String SAVE_ALL_CHANGES_MAPPING = "/saveAllChangesToBase";
    private final static String LOAD_DATA_FOR_ALL_PACKETS = "restful.loadDataForAllPackets";
    private final static String LOAD_DATA_FOR_SPECIFIC_PACKET = "restful.loadDataForSpecificPacket";
    private final static String SAVE_ALL_CHANGES_TO_BASE_FOR_SPECIFIC_PACKET
            = "restful.saveAllChangesToBaseForSpecificPacket";
    private final static String SAVE_ALL_CHANGES_TO_BASE_FOR_ALL_PACKETS
            = "restful.saveAllChangesToBaseForAllPackets";

    private static Gson GSON = new Gson();

    @Autowired
    private PacketAppService packetAppService;

    @RequestMapping({HOME_MAPPING, ""})
    public String home(){
        return HOME_MAPPING_FILE;
    }

    @RequestMapping(value = USER_LOGIN_MAPPING, method = RequestMethod.POST)
    @ResponseBody
    public String getUserRole(@RequestBody String request) {
        RequestObj requestObj = GSON.fromJson(request, RequestObj.class);
        String result = GSON.toJson(packetAppService.getUserRole(requestObj.getSecurityParams().getUsername(),
                requestObj.getSecurityParams().getPassword()
        ));
        return result;
    }

    @RequestMapping(value = LOAD_DATA_MAPPING, method = RequestMethod.POST)
    @ResponseBody
    public String loadData(@RequestBody String json) throws Exception {
        RequestObj requestObj = GSON.fromJson(json, RequestObj.class);
        Long packetId = requestObj.getDataParams().getPacketId();
        log.debug(packetId == null
                ? getMessage(LOAD_DATA_FOR_ALL_PACKETS, null)
                : getMessage(LOAD_DATA_FOR_SPECIFIC_PACKET, new Object[]{packetId})
        );
        return GSON.toJson(packetAppService.loadData(packetId));
    }

    @RequestMapping(value = SAVE_ALL_CHANGES_MAPPING, method = RequestMethod.POST)
    public String saveAllChangesToBase(@RequestBody String json) throws Exception {
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

