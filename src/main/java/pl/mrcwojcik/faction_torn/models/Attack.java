package pl.mrcwojcik.faction_torn.models;

public class Attack implements Comparable<Attack>{

    private String attackId;
    private Integer memberId;
    private Integer attackStart;
    private Integer attackEnd;

    public String getAttackId() {
        return attackId;
    }

    public void setAttackId(String attackId) {
        this.attackId = attackId;
    }

    public Integer getAttackStart() {
        return attackStart;
    }

    public void setAttackStart(Integer attackStart) {
        this.attackStart = attackStart;
    }

    public Integer getAttackEnd() {
        return attackEnd;
    }

    public void setAttackEnd(Integer attackEnd) {
        this.attackEnd = attackEnd;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    @Override
    public int compareTo(Attack o) {
        if (getAttackStart() == null || o.getAttackStart() == null) {
            return 0;
        }
        return getAttackStart().compareTo(o.getAttackStart());
    }
}
