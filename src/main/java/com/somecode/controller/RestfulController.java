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


    @RequestMapping(value = "/getComptsByPacketId", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptInfo> getComptsByPacketId(@RequestParam long packetId) throws Exception {
        LOGGER.info("Get Compts By Packet Id");
        return comptService.getComptsByPacketId(packetId);
    }

    @RequestMapping(value = "/getAllCompts", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptInfo> getAllCompts() throws Exception {
        LOGGER.info("Get All Compts");
        return comptService.getAllCompts();
    }

    @RequestMapping(value = "/getAllComboData", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComboData> getDefaultComboData() throws Exception {
        LOGGER.info("Get All Combo Data");
        return comptService.getAllComboData();
    }

    @RequestMapping(value = "/addCompts", method = RequestMethod.POST)
    public
    @ResponseBody
    void addCompts(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Add Compts");
        comptService.addCompts(requestObj.getParams().getPacketId(),
                requestObj.getParams().getComptsParamsList());
    }

    @RequestMapping(value = "/removeCompts", method = RequestMethod.POST)
    public void removeCompts(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Remove Compts");
        comptService.removeCompts(requestObj.getParams().getIdsToRemove());
    }

    @RequestMapping(value = "/updateCompts", method = RequestMethod.POST)
    public
    @ResponseBody
    void updateCompts(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Update Compts");
        comptService.updateCompts(requestObj.getParams().getComptsParamsList());
    }

    @RequestMapping(value = "/updatePacketState", method = RequestMethod.POST)
    public
    @ResponseBody
    void updatePacket(@RequestBody RequestObj requestObj) throws Exception {
        LOGGER.info("Update Packet State");
        comptService.updatePacketState(requestObj.getParams().getPacketId(),
                requestObj.getParams().getNewStateId());
    }

    @RequestMapping(value = "/getAllComptsSupplInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptSupplInfo> getAllComptsSupplInfo() throws Exception {
        LOGGER.info("Get All Compts SupplInfo");
        return comptService.getAllComptsSupplInfo();
    }

    @RequestMapping(value = "/getComptsSupplInfoByPacketId", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ComptSupplInfo> getComptsSupplInfoByPacketId(@RequestParam long packetId)
            throws Exception {
        LOGGER.info("get Compts SupplInfo by Packet id");
        return comptService.getComptsSupplInfoByPacketId(packetId);
    }

    @RequestMapping(value = "/getAllStates", method = RequestMethod.GET)
    public
    @ResponseBody
    List<State> getStates() throws Exception {
        LOGGER.info("Get All States");
        return comptService.getAllStates();
    }

    @RequestMapping(value = "/getAllPackets", method = RequestMethod.GET)
    public
    @ResponseBody
    List<PacketInfo> getPackets() throws Exception {
        LOGGER.info("Get All Packets");
        return comptService.getAllPackets();
    }

    @RequestMapping(value = "/getPacketStateId", method = RequestMethod.GET)
    public
    @ResponseBody
    Long getPacketStateId(@RequestParam long packetId) throws Exception {
        LOGGER.info("Get Packet State Id");
        return comptService.getPacketStateId(packetId);
    }
}

