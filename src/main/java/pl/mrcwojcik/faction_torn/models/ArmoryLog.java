package pl.mrcwojcik.faction_torn.models;

public class ArmoryLog {

    public String armoryId;
    public String armoryLog;
    public long timestamp;
    public boolean xanaxNews;
    public String member;

    public String getArmoryId() {
        return armoryId;
    }

    public void setArmoryId(String armoryId) {
        this.armoryId = armoryId;
    }

    public String getArmoryLog() {
        return armoryLog;
    }

    public void setArmoryLog(String armoryLog) {
        this.armoryLog = armoryLog;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isXanaxNews() {
        return xanaxNews;
    }

    public void setXanaxNews(boolean xanaxNews) {
        this.xanaxNews = xanaxNews;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
