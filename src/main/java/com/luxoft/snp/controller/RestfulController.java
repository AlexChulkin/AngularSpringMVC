package com.luxoft.snp.controller;

import com.luxoft.snp.service.ComptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * RestfulController class represents the portlet class responsible for
 * handling portlet requests.
 *
 * @author asarin
 */
@Controller
public class RestfulController {

    @Autowired
    private ComptService comptService;

    @RequestMapping("/ttt")
    public @ResponseBody String test(){

        comptService.saveCompt();

        return "test vahe alex";

    }

    public ComptService getComptService() {
        return comptService;
    }

    public void setComptService(ComptService comptService) {
        this.comptService = comptService;
    }



}

