package com.somecode.controller;

import com.somecode.domain.*;
import com.somecode.service.ComptService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.EnumSet;


@Controller
public class RestfulController {

    private static final Logger LOGGER = Logger.getLogger(RestfulController.class);
    
    @Autowired
    private ComptService comptService;


    @RequestMapping({"/home", ""})
    public String home(){
        return "/resources/admin.jsp";
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public
    @ResponseBody
    Role getUserRole(@RequestBody RequestObj requestObj) {
        return comptService.getUserRole(requestObj.getSecurityParams().getUsername(),
                requestObj.getSecurityParams().getPassword());
    }

    @RequestMapping(value = "/loadData", method = RequestMethod.POST)
    public
    @ResponseBody
    Data loadData(@RequestBody RequestObj requestObj) throws Exception {
        Long packetId = requestObj.getDataParams().getPacketId();
        LOGGER.info(packetId == null ? "Load All Data" : "Load Data for packetId: " + packetId);
        return comptService.loadData(packetId);
    }

    @RequestMapping(value = "/saveAllChangesToBase", method = RequestMethod.POST)
    public EnumSet<PersistError> saveAllChangesToBase(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Save All Changes to Base");
        DataParams params = requestObj.getDataParams();
        return comptService.saveAllChangesToBase(params.getComptIdsToDelete(),
                params.getPacketIdsToDelete(),
                params.getComptsToUpdateParamsList(),
                params.getPacketsToAddParamsList(),
                params.getPacketsToUpdateParamsList(),
                params.getPacketId());
    }
}

