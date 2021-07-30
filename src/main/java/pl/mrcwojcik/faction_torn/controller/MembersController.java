package pl.mrcwojcik.faction_torn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.mrcwojcik.faction_torn.service.FactionService;
import pl.mrcwojcik.faction_torn.service.MembersService;
import pl.mrcwojcik.faction_torn.service.ReportsService;
import pl.mrcwojcik.faction_torn.service.YellowFlagService;

/** Old controller with help managed members and create results to themyleaf
 * @deprecated now it will be rewritten to Rest API */
@Controller
public class MembersController {

    private FactionService factionService;
    private MembersService membersService;
    private YellowFlagService yellowFlagService;
    private ReportsService reportsService;

    public MembersController(FactionService factionService, MembersService membersService, YellowFlagService yellowFlagService, ReportsService reportsService) {
        this.factionService = factionService;
        this.membersService = membersService;
        this.yellowFlagService = yellowFlagService;
        this.reportsService = reportsService;
    }

    @GetMapping(value = "/members")
    public String getFactionMembers(Model model) {
        model.addAttribute("faction", factionService.getFactionInfo());
        model.addAttribute("members", membersService.getMembersList());
        model.addAttribute("yeet", membersService.getYeetList());
//        model.addAttribute("reportDate", reportsService.getLastReportDate());
        return "faction";
    }

    @GetMapping(value = "/members/update")
    public String updateFactionMembers(Model model){
        membersService.updateMembersRespect();
        return "redirect:/members";
    }

    @GetMapping("/faction/info")
    public String updateFaction(){
        factionService.updateFaction();
        return "redirect:/members";
    }

    @GetMapping("/members/sort")
    public String sortMembersByRespect(Model model){
        model.addAttribute("members", membersService.sortMembersRespect());
        model.addAttribute("faction", factionService.getFactionInfo());
        model.addAttribute("yeet", membersService.getYeetList());
        return "faction";
    }

    @GetMapping("/members/cleanYellowCards")
    public String cleanYellowCardsForMembers(){
        yellowFlagService.cleanCards();
        return "redirect:/members";
    }

}
