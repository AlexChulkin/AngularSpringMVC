package com.somecode.controller;

import com.somecode.domain.Data;
import com.somecode.domain.Params;
import com.somecode.domain.PersistError;
import com.somecode.domain.RequestObj;
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
        return "/WEB-INF/jsp/home.jsp";
    }

    @RequestMapping(value = "/loadData", method = RequestMethod.POST)
    public
    @ResponseBody
    Data loadData(@RequestBody RequestObj requestObj) throws Exception {
        Long packetId = requestObj.getParams().getPacketId();
        LOGGER.info(packetId == null ? "Load All Data" : "Load Data for packetId: " + packetId);
        return comptService.loadData(packetId);
    }

    @RequestMapping(value = "/saveAllChangesToBase", method = RequestMethod.POST)
    public EnumSet<PersistError> saveAllChangesToBase(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Save All Changes to Base");
        Params params = requestObj.getParams();
        return comptService.saveAllChangesToBase(params.getComptIdsToDelete(),
                params.getPacketIdsToDelete(),
                params.getComptsToUpdateParamsList(),
                params.getPacketsToAddParamsList(),
                params.getPacketsToUpdateParamsList(),
                params.getPacketId());
    }
}

