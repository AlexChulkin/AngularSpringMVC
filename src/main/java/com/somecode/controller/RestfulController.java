package com.somecode.controller;

import com.somecode.domain.ComptInfo;
import com.somecode.domain.ComptSupplInfo;
import com.somecode.domain.Data;
import com.somecode.domain.RequestObj;
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


    @RequestMapping(value = "/getComptsByPacketId", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptInfo> getComptsByPacketId(@RequestParam long packetId) throws Exception {
        LOGGER.info("Get Compts By Packet Id");
        return comptService.getComptsByPacketId(packetId);
    }

    @RequestMapping(value = "/getAllData", method = RequestMethod.GET)
    public
    @ResponseBody
    Data getAllData() throws Exception {
        LOGGER.info("Get All Compts");
        return comptService.getAllData();
    }

    @RequestMapping(value = "/deleteCompts", method = RequestMethod.POST)
    public void deleteCompts(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Delete Compts");
        comptService.deleteCompts(requestObj.getParams().getComptIds());
    }

    @RequestMapping(value = "/deletePackets", method = RequestMethod.POST)
    public void deletePackets(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Delete Packets");
        comptService.deletePackets(requestObj.getParams().getPacketIds());
    }

    @RequestMapping(value = "/getComptsSupplInfoByPacketId", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptSupplInfo> getComptsSupplInfoByPacketId(@RequestParam long packetId)
            throws Exception {
        LOGGER.info("Get Compts SupplInfo by Packet id");
        return comptService.getComptsSupplInfoByPacketId(packetId);
    }

    @RequestMapping(value = "/saveOrUpdatePackets", method = RequestMethod.POST)
    public void saveOrUpdatePackets(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Save or update packets");
        comptService.saveOrUpdatePackets(requestObj.getParams().getCreatePacketParamsList(),
                requestObj.getParams().getUpdatePacketParamsList());
    }
}

