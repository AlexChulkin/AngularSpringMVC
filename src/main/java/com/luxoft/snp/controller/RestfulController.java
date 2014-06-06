package com.luxoft.snp.controller;

import com.luxoft.snp.domain.Compt;
import com.luxoft.snp.service.ComptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @RequestMapping("/save")
    public @ResponseBody String save(){

        comptService.saveCompt();

        return "test vahe alex";

    }
    @RequestMapping("/getAll")
    public @ResponseBody List<Compt> getAll(){

        List<Compt> allCompts = comptService.getAllCompts();
        return allCompts;
    }

    public ComptService getComptService() {
        return comptService;
    }

    public void setComptService(ComptService comptService) {
        this.comptService = comptService;
    }



}

