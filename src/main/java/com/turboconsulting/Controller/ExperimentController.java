package com.turboconsulting.Controller;

import com.turboconsulting.Entity.ConsentLevel;
import com.turboconsulting.Entity.Experiment;
import com.turboconsulting.Service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@SessionAttributes("aID")
public class ExperimentController {


    @Autowired
    private ConsentService consentService;

    @GetMapping("/visitors/experiments/experiment")
    public String experimentPage(Model m,
                                 @RequestParam("vID") int vID,
                                 @RequestParam("eID") int eID) {
        m.addAttribute("visitorExp", consentService.getVisitorExperiment(vID, eID));
        m.addAttribute("visitorName", consentService.getVisitor(vID).getName());
        m.addAttribute("vID", vID);
        m.addAttribute("eID", eID);
        return "experiment";
    }

    @PostMapping("/visitor/experiments/experiment/updateConsent")
    public ModelAndView updateConsent(@RequestParam("vID") int vID,
                                      @RequestParam("eID") int eID,
                                      @ModelAttribute("consentLevel") String c)  {
        boolean updateSuccessful = consentService.updateExperimentConsent(vID, ConsentLevel.fromString(c), eID);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/visitors/experiments?vID="+vID+"&update="+updateSuccessful);
        return mav;
    }
}
