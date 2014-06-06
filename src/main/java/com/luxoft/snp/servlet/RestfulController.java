package com.luxoft.snp.servlet;

import com.luxoft.snp.domain.Compt;
import com.luxoft.snp.domain.PseudoData;
import com.luxoft.snp.domain.State;
import com.luxoft.snp.service.ComptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestfulController class represents the portlet class responsible for
 * handling portlet requests.
 *
 * @author asarin
 */
@Controller
public class RestfulController {


//    private Logger logger = Logger.getLogger(RestfulController.class);
    private ComptService comptService;


    @RequestMapping(value = "/compt/view.action", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Compt> view(@RequestParam int packageId) throws Exception {
        System.out.println("view");
        return comptService.getComponents(packageId);
    }

    @RequestMapping(value = "/data/view.action", method = RequestMethod.GET)
    public
    @ResponseBody
    List<PseudoData> viewData(@RequestParam int packageId) throws Exception {
        System.out.println("viewData");
        return comptService.getStaticData(packageId);
    }

    @RequestMapping(value = "/states/view.action", method = RequestMethod.GET)
    public
    @ResponseBody
    List<State> viewStates() throws Exception {
        System.out.println("viewLabels");
        return comptService.getStates();
    }

    @RequestMapping(value = "/packetState/view.action", method = RequestMethod.GET)
    public
    @ResponseBody
    Integer viewPacketState(@RequestParam int packageId) throws Exception {
        System.out.println("viewPacketState");
        return comptService.getPacketState(packageId);
    }

    @ExceptionHandler({Exception.class})
    public String handleException() {
        return "errorPage";
    }

    @Autowired
    public void setComptService(ComptService comptService) {
        this.comptService = comptService;
    }
}

