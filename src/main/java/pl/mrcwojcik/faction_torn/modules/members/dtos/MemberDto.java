package pl.mrcwojcik.faction_torn.modules.members.dtos;

import java.util.Arrays;
import java.util.List;

public class MemberDto {

    private Integer tornId;
    private String username;
    private Integer rpForFaction;
    private Integer daysInFaction;
    private Integer level;
    private boolean yeet;
    private long lastAction;
    private String roles;
    private Integer distanceToMerit;
    private Integer closestMerit;

    public Integer getTornId() {
        return tornId;
    }

    public void setTornId(Integer tornId) {
        this.tornId = tornId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRpForFaction() {
        return rpForFaction;
    }

    public void setRpForFaction(Integer rpForFaction) {
        this.rpForFaction = rpForFaction;
    }

    public Integer getDaysInFaction() {
        return daysInFaction;
    }

    public void setDaysInFaction(Integer daysInFaction) {
        this.daysInFaction = daysInFaction;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isYeet() {
        return yeet;
    }

    public void setYeet(boolean yeet) {
        this.yeet = yeet;
    }

    public long getLastAction() {
        return lastAction;
    }

    public void setLastAction(long lastAction) {
        this.lastAction = lastAction;
    }

    public Integer getDistanceToMerit() {
        return distanceToMerit;
    }

    public void setDistanceToMerit() {
        if (this.rpForFaction == null){
            this.distanceToMerit = 0;
        } else {
            this.distanceToMerit = this.closestMerit - this.rpForFaction;
        }
    }

    public Integer getClosestMerit() {
        return closestMerit;
    }

    public void setClosestMerit() {
        List<Integer> respectMerits = Arrays.asList(100, 500, 1000, 2500, 5000, 10000, 25000, 50000, 75000, 100000);
        this.closestMerit = findClosest(this.rpForFaction, respectMerits);
    }

    public Integer findClosest(Integer rpForFaction, List<Integer> respectMerits){
        int closest = respectMerits.get(0);
        if (rpForFaction == null){
            return 0;
        }
        if (rpForFaction < closest){
            return closest;
        }
        for (int i = 1; i <= respectMerits.size()-1; i++){
            if (rpForFaction < respectMerits.get(i) && rpForFaction > respectMerits.get(i - 1)){
                closest = respectMerits.get(i);
            }
        }

        return closest;
    }
}
