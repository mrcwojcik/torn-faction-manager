package pl.mrcwojcik.faction_torn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.mrcwojcik.faction_torn.models.entities.Report;
import pl.mrcwojcik.faction_torn.service.MembersService;
import pl.mrcwojcik.faction_torn.service.ReportsService;
import pl.mrcwojcik.faction_torn.service.YellowFlagService;

import java.time.LocalDateTime;
import java.util.List;

/** Old controller with create reports of chain effort and show it in themyleaf
 * @deprecated now it will be rewritten to Rest API */
@Controller
public class ReportsController {

    private ReportsService reportsService;
    private YellowFlagService yellowFlagService;
    private MembersService membersService;

    public ReportsController(ReportsService reportsService, YellowFlagService yellowFlagService, MembersService membersService) {
        this.reportsService = reportsService;
        this.yellowFlagService = yellowFlagService;
        this.membersService = membersService;
    }

    @GetMapping("/report")
    public String showLastWeeklyReport(Model model){
        model.addAttribute("reports", reportsService.showLastReport());
        model.addAttribute("yeet", membersService.getYeetList());
        return "report";
    }

    /** Building new reports with few steps
     * 1. Delete latest report data
     * 2. Set time range of building report (automatic weekly from last Monday to Monday
     * 3. Build report with time range
     * 4. Yellow flag evaluation */
    @GetMapping("/report/build")
    public String getWeeklyReport(Model model){
        reportsService.cleanLatestReportData();
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.from(endDate.minusDays(8));
        List<Report> reportList = reportsService.buildWeeklyReport(startDate, endDate);
        yellowFlagService.doFlags(reportList);
        model.addAttribute("yeet", membersService.getYeetList());
        model.addAttribute("reports", reportList);
        return "report";
    }

    @GetMapping("/cleanYellowCard")
    public String cleanCards(){
        yellowFlagService.cleanCards();
        return "redirect:/report";
    }

}
