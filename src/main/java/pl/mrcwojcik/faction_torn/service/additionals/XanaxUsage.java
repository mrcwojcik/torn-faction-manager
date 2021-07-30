package pl.mrcwojcik.faction_torn.service.additionals;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.mrcwojcik.faction_torn.models.ArmoryLog;
import pl.mrcwojcik.faction_torn.models.entities.Report;
import pl.mrcwojcik.faction_torn.repositories.MemberRepository;
import pl.mrcwojcik.faction_torn.utils.RequestToApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class XanaxUsage {

    private RequestToApi requestToApi;
    private MemberRepository memberRepository;

    public XanaxUsage(RequestToApi requestToApi, MemberRepository memberRepository) {
        this.requestToApi = requestToApi;
        this.memberRepository = memberRepository;
    }

    public void getXanaxUsage(List<Report> reportList, long from, long to){
        List<ArmoryLog> armoryLogs = getNews(from, to);
        fillReportWithXanaxLogs(armoryLogs, reportList);
    }

    private void fillReportWithXanaxLogs(List<ArmoryLog> armoryLogs, List<Report> reportList) {
        for (ArmoryLog armoryLog : armoryLogs){
            if (armoryLog.isXanaxNews()){
                if (checkIfExistsByUsername(armoryLog.getMember(), reportList)){
                    Report report = getReportForMember(armoryLog.member, reportList);
                    report.setXanUsed(report.getXanUsed() + 1);
                }
            }
        }
    }

    private boolean checkIfExistsByUsername(String member, List<Report> reports) {
        if (reports.isEmpty()){
            return false;
        }

        for (Report r : reports){
            if (r.getMember().getUsername().equals(member)){
                return true;
            }
        }

        return false;
    }

    private Report getReportForMember(String member, List<Report> reports) {
        for (Report r : reports){
            if (r.getMember().getUsername().equals(member)){
                return r;
            }
        }

        return null;
    }

    /** Ogarniam wszystkie logi */
    public List<ArmoryLog> getNews(long from, long to){
        try {
            List<ArmoryLog> armoryLogs = new ArrayList<>();
            if (armoryLogs.isEmpty()){
                List<ArmoryLog> tmpArmoryLogList = getArmoryLogList(from, to);
                armoryLogs.addAll(tmpArmoryLogList);
            }

            while (armoryLogs.size()%1000 == 0){
                TimeUnit.SECONDS.sleep(30);
                ArmoryLog lastArmoryLog = armoryLogs.stream()
                        .max(Comparator.comparing(al -> al.getTimestamp()))
                        .get();
                List<ArmoryLog> tmpArmoryLogList = getArmoryLogList(lastArmoryLog.getTimestamp(), to);
                armoryLogs.addAll(tmpArmoryLogList);
            }

            return armoryLogs;
        } catch (InterruptedException e){
            e.printStackTrace();
            return null;
        }
    }

    /** Pobieranie listy WSZYSTKICH logów z armoryNews*/
    private List<ArmoryLog> getArmoryLogList(long from, long to) {
        List<ArmoryLog> armoryLogList = new ArrayList<>();
        JSONObject armory = requestToApi.getXanaxNews(from, to);
        JSONObject armoryNews = armory.getJSONObject("armorynews");
        armoryNews.keySet().forEach(logKey -> {
            JSONObject singleLog = armoryNews.getJSONObject(logKey);
            ArmoryLog armoryLog = buildArmoryLog(logKey, singleLog);
            armoryLogList.add(armoryLog);
        });

        return armoryLogList;
    }

    /** Budowanie pojedynczego logu */
    private ArmoryLog buildArmoryLog(String logKey, JSONObject singleLog){
        ArmoryLog armoryLog = new ArmoryLog();
        armoryLog.setArmoryId(logKey);
        armoryLog.setArmoryLog(singleLog.getString("news"));
        armoryLog.setTimestamp(singleLog.getLong("timestamp"));
        armoryLog.setMember(getMemberFromLog(singleLog.getString("news")));
        if (singleLog.getString("news").contains("Xanax") && singleLog.getString("news").contains("used")){
            armoryLog.setXanaxNews(true);
        } else {
            armoryLog.setXanaxNews(false);
        }

        return armoryLog;
    }

    private String getMemberFromLog(String news) {
        Pattern p = Pattern.compile(".*\\> *(.*) *\\</a>.*");
        Matcher m = p.matcher(news);
        m.find();
        return m.group(1);
    }
}
