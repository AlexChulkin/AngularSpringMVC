package com.luxoft.snp.controller;

import com.luxoft.snp.domain.Compt;
import com.luxoft.snp.domain.State;

import com.luxoft.snp.service.ComptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class RestfulController {

    @Autowired
    private ComptService comptService;


    @RequestMapping("/home")
    public String home(){
        return "home";
    }



    @RequestMapping(value = "/insertDataIntoTables", method = RequestMethod.POST)
    public void insertData() throws Exception {
        System.out.println("/insertDataIntoTables");
        comptService.insertData();
    }

    @RequestMapping(value = "/removeData", method = RequestMethod.POST)
    public void removeData() throws Exception {
        System.out.println("/removeData");
        comptService.removeData();
    }

    @RequestMapping(value = "/compts", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Compt> view(@RequestParam int packetId) throws Exception {
        System.out.println("viewCompts");
        return comptService.getComponents(packetId);
    }

    @RequestMapping(value = "/staticData", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Object[]> viewStaticData(@RequestParam int packetId) throws Exception {
        System.out.println("viewStaticData");
        return comptService.getStaticData(packetId);
    }

    @RequestMapping(value = "/states", method = RequestMethod.GET)
    public
    @ResponseBody
    List<State> viewStates() throws Exception {
        System.out.println("viewStates");
        return comptService.getStates();
    }

    @RequestMapping(value = "/packetState", method = RequestMethod.GET)
         public
         @ResponseBody
         Integer viewPacketState(@RequestParam int packetId) throws Exception {
        System.out.println("viewPacketState");
        return comptService.getPacketState(packetId);
    }




}

