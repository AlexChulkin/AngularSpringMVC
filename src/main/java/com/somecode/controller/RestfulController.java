package com.somecode.controller;

import com.somecode.domain.RequestObj;
import com.somecode.domain.State;
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
    List<Object[]> viewCompts(@RequestParam long packetId) throws Exception {
       LOGGER.info("viewCompts");
        return comptService.getCompts(packetId);
    }

    @RequestMapping(value = "/addCompt", method = RequestMethod.POST)
    public
    @ResponseBody
    void addCompt(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("addCompt");
        comptService.addCompt(requestObj.getParams().getComptLabel(),
                requestObj.getParams().getPacketId(),
                requestObj.getParams().getDefaultVals());

    }

    @RequestMapping(value = "/removeCompts", method = RequestMethod.POST)
    public void removeCompts(@RequestBody RequestObj requestObj) throws Exception {

        LOGGER.info("removeCompts");
        comptService.removeCompts(requestObj.getParams().getIdsToRemove());
    }

    @RequestMapping(value = "/updateCompt", method = RequestMethod.POST)
    public
    @ResponseBody
    void updateCompt(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("updateCompt");
        comptService.updateCompt(requestObj.getParams().getComptId(),
                requestObj.getParams().getDefaultVals());
    }

    @RequestMapping(value = "/comptsData", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Object[]> viewComptsData(@RequestParam long packetId) throws Exception {
        LOGGER.info("viewStaticData");
        return comptService.getComptsData(packetId);
    }

    @RequestMapping(value = "/states", method = RequestMethod.GET)
    public
    @ResponseBody
    List<State> viewStates() throws Exception {
        LOGGER.info("viewStates");
        return comptService.getStates();
    }

    @RequestMapping(value = "/packetState", method = RequestMethod.GET)
         public
         @ResponseBody
         Long viewPacketState(@RequestParam long packetId) throws Exception {
        LOGGER.info("viewPacketState");
        return comptService.getPacketState(packetId);
    }

}

