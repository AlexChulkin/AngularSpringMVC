package com.luxoft.snp.controller;

import com.luxoft.snp.domain.Compt;
import com.luxoft.snp.domain.State;
import com.luxoft.snp.service.ComptService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    List<Compt> view(@RequestParam int packetId) throws Exception {
       LOGGER.info("viewCompts");
        return comptService.getComponents(packetId);
    }

    @RequestMapping(value = "/staticData", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Object[]> viewStaticData(@RequestParam int packetId) throws Exception {
        LOGGER.info("viewStaticData");
        return comptService.getStaticData(packetId);
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
         Integer viewPacketState(@RequestParam int packetId) throws Exception {
        LOGGER.info("viewPacketState");
        return comptService.getPacketState(packetId);
    }

}

