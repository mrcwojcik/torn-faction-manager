package pl.mrcwojcik.faction_torn.service;

import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.models.Attack;
import pl.mrcwojcik.faction_torn.models.Chain;
import pl.mrcwojcik.faction_torn.models.ReportDate;
import pl.mrcwojcik.faction_torn.models.entities.Member;
import pl.mrcwojcik.faction_torn.models.entities.Report;
import pl.mrcwojcik.faction_torn.repositories.MemberRepository;
import pl.mrcwojcik.faction_torn.repositories.ReportDateRepository;
import pl.mrcwojcik.faction_torn.repositories.ReportRepository;
import pl.mrcwojcik.faction_torn.service.additionals.ChainAttacksBuilder;
import pl.mrcwojcik.faction_torn.service.additionals.XanaxUsage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReportsService {

    private ChainAttacksBuilder chainAttacksBuilder;
    private MemberRepository memberRepository;
    private XanaxUsage xanaxUsage;
    private ReportRepository reportRepository;
    private ReportDateRepository reportDateRepository;

    public ReportsService(ChainAttacksBuilder chainAttacksBuilder, MemberRepository memberRepository, XanaxUsage xanaxUsage, ReportRepository reportRepository) {
        this.chainAttacksBuilder = chainAttacksBuilder;
        this.memberRepository = memberRepository;
        this.xanaxUsage = xanaxUsage;
        this.reportRepository = reportRepository;
    }

    public List<Report> buildWeeklyReport(LocalDateTime startTime, LocalDateTime endTime){
        long from = startTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        long to = endTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        List<Chain> chains = chainAttacksBuilder.buildChainList(from);
        chainAttacksBuilder.buildAttackList(chains);
        List<Report> reportList = buildFirstPartOfReport(chains);
        getMembersWithoutAnyHits(reportList);
        xanaxUsage.getXanaxUsage(reportList, from, to);
        saveReportsToDb(reportList);
        Collections.sort(reportList);
        Collections.reverse(reportList);
        saveDateOfReport();
        return reportList;
    }

    private void saveDateOfReport() {
        ReportDate reportDate = new ReportDate();
        reportDate.setLocalDateTime(LocalDateTime.now());
        reportDateRepository.save(reportDate);
    }

    private void getMembersWithoutAnyHits(List<Report> reportList) {
        List<Member> memberList = memberRepository.findAll();
        for (Member m : memberList){
            if (!checkIfExistsById(m.getTornId(), reportList)){
                Report report = new Report();
                report.setMember(m);
                report.setHits(0);
                report.setXanUsed(0);
                reportList.add(report);
            }
        }
    }

    /** Save weekly report to db     */
    private void saveReportsToDb(List<Report> reportList) {
        for (Report r : reportList){
            reportRepository.save(r);
        }
    }

    private List<Report> buildFirstPartOfReport(List<Chain> chains) {
        List<Report> reportList = new ArrayList<>();
        for(Chain chain : chains){
            for (Attack attack : chain.getAttacks()){
                if(checkIfExistsById(attack.getMemberId(), reportList)){
                    Report report = getReportFromList(attack.getMemberId(), reportList);
                    report.setHits(report.getHits()+1);
                    reportRepository.save(report);
                } else {
                    Report report = new Report();
                    report.setMember(memberRepository.findByTornId(attack.getMemberId()));
                    report.setHits(1);
                    report.setXanUsed(0);
                    reportList.add(report);
                }
            }
        }

        return reportList;
    }

    private Report getReportFromList(int attacker_id, List<Report> reports) {
        for (Report r : reports){
            if (r.getMember().getTornId() == attacker_id){
                return r;
            }
        }

        return null;
    }

    private boolean checkIfExistsById(int attacker_id, List<Report> reports) {
        if (reports.isEmpty()){
            return false;
        }
        for (Report r : reports){
            if (r.getMember().getTornId() == attacker_id){
                return true;
            }
        }

        return false;
    }

    public List<Report> showLastReport() {
        return reportRepository.findByOrderByHitsDesc();
    }

    public void cleanLatestReportData() {
        List<Report> allReportData = reportRepository.findByOrderByHitsDesc();
        for (Report r : allReportData){
            reportRepository.delete(r);
        }
    }

    public ReportDate getLastReportDate(){
        ReportDate reportDate = reportDateRepository.findFirstByOrderByLocalDateTime();
        if (reportDate == null){
            return null;
        }
        return reportDate;
    }
}
