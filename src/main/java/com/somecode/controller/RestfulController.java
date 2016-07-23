package com.somecode.controller;

import com.somecode.domain.*;
import com.somecode.service.ComptService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class RestfulController {

    private static final Logger LOGGER = Logger.getLogger(RestfulController.class);
    
    @Autowired
    private ComptService comptService;


    @RequestMapping({"/home", ""})
    public String home(){
        return "home";
    }


    @RequestMapping(value = "/compts", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptInfo> viewCompts(@RequestParam long packetId) throws Exception {
       LOGGER.info("viewCompts");
        return comptService.getCompts(packetId);
    }

    @RequestMapping(value = "/defaultComboData", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComboData> getDefaultComboData() throws Exception {
        LOGGER.info("getDefaultComboData");
        return comptService.getDefaultComboData();
    }

    @RequestMapping(value = "/addCompts", method = RequestMethod.POST)
    public
    @ResponseBody
    void addCompts(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("addCompts");
        comptService.addCompts(requestObj.getParams().getPacketId(),
                requestObj.getParams().getComptsParamsList());
    }

    @RequestMapping(value = "/removeCompts", method = RequestMethod.POST)
    public void removeCompts(@RequestBody RequestObj requestObj) throws Exception {

        LOGGER.info("removeCompts");
        comptService.removeCompts(requestObj.getParams().getIdsToRemove());
    }

    @RequestMapping(value = "/updateCompts", method = RequestMethod.POST)
    public
    @ResponseBody
    void updateCompt(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("updateCompts");
        comptService.updateCompts(requestObj.getParams().getComptsParamsList());
    }

    @RequestMapping(value = "/updatePacketsState", method = RequestMethod.POST)
    public
    @ResponseBody
    void updatePacket(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("updatePacketsState");

        comptService.updatePacketsState(requestObj.getParams().getPacketId(),
                requestObj.getParams().getNewStateId());
    }

    @RequestMapping(value = "/comptsSupplInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptSupplInfo> getComptsSupplInfo(@RequestParam long packetId) throws Exception {
        LOGGER.info("getComptsSupplInfo");
        return comptService.getComptsSupplInfo(packetId);
    }

    @RequestMapping(value = "/states", method = RequestMethod.GET)
    public
    @ResponseBody
    List<State> getStates() throws Exception {
        LOGGER.info("getState");
        return comptService.getStates();
    }

    @RequestMapping(value = "/packetsState", method = RequestMethod.GET)
         public
         @ResponseBody
         Long getPacketState(@RequestParam long packetId) throws Exception {
        LOGGER.info("getPacketState");
        return comptService.getPacketState(packetId);
    }
}

