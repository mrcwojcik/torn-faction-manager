package pl.mrcwojcik.faction_torn.service;

import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.models.ReportDate;
import pl.mrcwojcik.faction_torn.models.entities.Member;
import pl.mrcwojcik.faction_torn.models.entities.Report;
import pl.mrcwojcik.faction_torn.repositories.MemberRepository;
import pl.mrcwojcik.faction_torn.repositories.ReportDateRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YellowFlagService {

    private MemberRepository memberRepository;
    private ReportDateRepository reportDateRepository;

    public YellowFlagService(MemberRepository memberRepository, ReportDateRepository reportDateRepository) {
        this.memberRepository = memberRepository;
        this.reportDateRepository = reportDateRepository;
    }

    public void doFlags(List<Report> reports){
        if (checkDates()){
            for (Report userReport : reports){
                if (userReport.getHits() < 6){
                    setFlag(userReport);
                }
//                Xan usage yellow flag part - temporary off
//                if (userReport.getHits() - (userReport.getXanUsed() * 10) <= -7){
//                    setFlag(userReport);
//                }

                checkYeetRecommendation(userReport);
            }

            saveReportDate(LocalDateTime.now());
        }
    }

    private void checkYeetRecommendation(Report userReport) {
        if (userReport.getMember().getYellowFlag() >= 3){
            userReport.getMember().setYeet(true);
        }
    }

    public boolean checkDates(){
        List<ReportDate> allReports = reportDateRepository.findAll();
        if (allReports.isEmpty()){
            return false;
        }

        if (LocalDateTime.now().getDayOfWeek() != DayOfWeek.TUESDAY){
            return false;
        }

        boolean actualDate = false;
        for (ReportDate reportDate : allReports){
            if (reportDate.getLocalDateTime().equals(LocalDateTime.now()) && reportDate.getLocalDateTime().equals(LocalDateTime.now().plusDays(1))){
                actualDate = true;
            }
        }
        return actualDate;
    }

    private void setFlag(Report userReport) {
        userReport.getMember().setYellowFlag(userReport.getMember().getYellowFlag() + 1);
    }

    private void saveReportDate(LocalDateTime now) {
        ReportDate reportDate = new ReportDate();
        reportDate.setLocalDateTime(now);
        reportDateRepository.save(reportDate);
    }

    public void cleanCards(){
        List<Member> memberList = memberRepository.findAll();
        for(Member m : memberList){
            m.setYellowFlag(0);
        }
    }

    public void resetCardForUser(){

    }

}
